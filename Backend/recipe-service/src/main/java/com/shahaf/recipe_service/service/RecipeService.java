package com.shahaf.recipe_service.service;


import com.shahaf.recipe_service.dto.RecipeCreationDto;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.enums.Category;
import com.shahaf.recipe_service.exceptions.ErrorOccurredException;
import com.shahaf.recipe_service.exceptions.ResourceNotFound;
import com.shahaf.recipe_service.mapper.RecipeMapper;
import com.shahaf.recipe_service.repository.RecipesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.shahaf.recipe_service.constants.ApplicationConstants.SEARCH_SERVICE_URL;

@Service
public class RecipeService {
    @Autowired
    FavoriteRecipeService favoriteRecipeService;
    @Autowired
    RecipesRepository recipesRepository;
    @Autowired
    RecipeMapper recipeMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    public List<Recipe> getRecipes(Integer numOfRecipes, Category category, Boolean random) {
        logger.info("Fetching recipes. Number of recipes: {}, category: {}, is randomize: {}", numOfRecipes, category, random);
        if (category != null) {
            return getRecipesByCategory(numOfRecipes, category, random);
        }
        return getRecipes(numOfRecipes, random);
    }

    private List<Recipe> getRecipes(Integer numOfRecipes, Boolean random) {
        List<Recipe> recipes;

        // if recipes should be randomize-getting all recipes, and then shuffling
        if (Boolean.TRUE.equals(random)) {
            recipes = getAllRecipes();
            Collections.shuffle(recipes);
            if (numOfRecipes != null && (recipes.size() > numOfRecipes)) {
                recipes = recipes.subList(0, numOfRecipes);
            }
        } else {
            if (numOfRecipes != null) {
                recipes = getNumberOfRecipes(numOfRecipes);
            } else {
                recipes = getAllRecipes();
            }
        }

        if (recipes == null) {
            return Collections.emptyList();
        }
        return recipes;
    }

    private List<Recipe> getRecipesByCategory(Integer numOfRecipes, Category category, Boolean random) {
        List<Recipe> recipesByCategory;

        // if recipes should be randomize-getting all recipes by category, and then shuffling
        if (Boolean.TRUE.equals(random)) {
            recipesByCategory = getAllRecipesByCategory(category);
            if (recipesByCategory == null) {
                return Collections.emptyList();
            }
            Collections.shuffle(recipesByCategory);
            if (numOfRecipes != null && recipesByCategory.size() > numOfRecipes) {
                recipesByCategory = recipesByCategory.subList(0, numOfRecipes);
            }
        } else {
            if (numOfRecipes != null) {
                recipesByCategory = getNumberOfRecipesByCategory(numOfRecipes, category);
            } else {
                recipesByCategory = getAllRecipesByCategory(category);
            }
        }

        return Objects.requireNonNullElse(recipesByCategory, Collections.emptyList());
    }

    private List<Recipe> getAllRecipes() {
        logger.info("Fetching all recipes by descending recipe ID order");
        return recipesRepository.findRecipesByOrderByIdDesc();
    }

    private List<Recipe> getAllRecipesByCategory(Category category) {
        logger.info("Fetching all recipes by category: {}.", category);
        Optional<List<Recipe>> queryResult = recipesRepository.getRecipesByCategory(category);
        return queryResult.orElse(null);
    }

    private List<Recipe> getNumberOfRecipes(Integer numOfRecipes) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        logger.info("Fetching number of recipes: {}.", numOfRecipes);
        return recipesRepository.findNRecipesByOrderByIdDesc(pageable);
    }

    private List<Recipe> getNumberOfRecipesByCategory(Integer numOfRecipes, Category category) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        logger.info("Fetching number of recipes: {} by category: {}.", numOfRecipes, category);
        Optional<List<Recipe>> queryResult = recipesRepository.getNRecipesByCategory(category, pageable);
        return queryResult.orElse(null);
    }

    public Recipe getRecipeById(Long recipeId) {
        logger.info("Fetching recipe {} from repository.", recipeId);
        Optional<Recipe> recipe = recipesRepository.findById(recipeId);
        if (!recipe.isPresent()) {
            String errorMessage = "Recipe not found. ID: " + recipeId;
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
        return recipe.get();
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) {
        Long recipeId = null;
        try {
            logger.info("Attempting to add a new recipe. Recipe name: {}", recipeCreationDto.getName());
            Recipe recipe = saveRecipe(recipeCreationDto);
            recipeId = recipe.getId();
            try {
                addRecipeToElasticsearchService(recipe);
            } catch (ErrorOccurredException e) {
                handleSaveRecipeRollback(recipeId);
            }
            logger.info("Recipe added successfully. Recipe ID: {}", recipeId);
            return recipe;
        } catch (Exception e) {
            logger.error("Failed to add recipe. Recipe name {}.", recipeCreationDto.getName());
            if (recipeId != null) {
                handleSaveRecipeRollback(recipeId);
            }
            throw new ErrorOccurredException(e.getMessage());
        }
    }

    private void addRecipeToElasticsearchService(Recipe recipe) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Recipe> requestEntity = new HttpEntity<>(recipe, headers);

        ResponseEntity<String> response = restTemplate.exchange(SEARCH_SERVICE_URL, HttpMethod.POST, requestEntity, String.class);
        logger.info("Sending http request to search service in url: {} to add recipe {}", SEARCH_SERVICE_URL, recipe.getId());
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode != HttpStatus.CREATED) {
            throw new ErrorOccurredException("Recipe not added in Elastic");
        }
    }

    private Recipe saveRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        logger.info("Saving recipe to relational database. Recipe name: {}", recipeCreationDto.getName());
        Recipe recipeCreation = recipeMapper.recipeDtoToRecipe(recipeCreationDto);
        try {
            return recipesRepository.save(recipeCreation);
        } catch (Exception e) {
            String errorMessage = "Failed to add recipe to relational database. Recipe name: " + recipeCreationDto.getName();
            logger.error(errorMessage);
            throw new ErrorOccurredException(errorMessage);
        }
    }

    private void handleSaveRecipeRollback(Long recipeId) {
        logger.info("Rolling back and deleting recipe. Recipe ID: {}.", recipeId);
        recipesRepository.deleteById(recipeId);
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            logger.info("Deleting recipe. Recipe ID: {}, Title: {}", recipeId, recipe.getName());
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
            String deleteRecipeUrl = SEARCH_SERVICE_URL + "/" + recipeId;
            deleteFromElastic(deleteRecipeUrl);
        }
    }

    public void deleteAllRecipes() {
        logger.info("Deleting all recipes from all DB.");
        favoriteRecipeService.deleteAllFavorites();
        recipesRepository.deleteAll();
        String deleteAllRecipesUrl = SEARCH_SERVICE_URL + "/all";
        deleteFromElastic(deleteAllRecipesUrl);
    }

    private void deleteFromElastic(String url) {
        logger.info("Sending http request to search service in url: {}", url);
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            throw new ErrorOccurredException(e.getMessage());
        }
    }
}
