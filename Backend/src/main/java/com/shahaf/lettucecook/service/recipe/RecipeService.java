package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import com.shahaf.lettucecook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    @Autowired
    private RecipesRepository recipesRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeMapper recipeMapper;

    public List<RecipeUserDto> getRecipes(Integer numOfRecipes, Category category, Boolean random) {
        User user = userService.getUserFromToken();

        if (numOfRecipes != null && category != null) {
            return getNumberOfRecipesByCategory(user, numOfRecipes, category, random);
        }

        if (numOfRecipes != null) {
            return getNumberOfRecipes(user, numOfRecipes, random);
        }

        if (category != null) {
            return getAllRecipesByCategory(user, category, random);
        }
        return getAllRecipes(user, random);
    }


    private List<RecipeUserDto> getAllRecipes(User user, Boolean random) {
        List<RecipeUserDto> allRecipes = recipesRepository.findAll().stream()
                .map(recipe -> mapRecipeToRecipeUserDto(recipe, user))
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(random)) {
            Collections.shuffle(allRecipes);
        }
        return allRecipes;
    }

    private List<RecipeUserDto> getAllRecipesByCategory(User user, Category category, Boolean random) {
        Optional<List<Recipe>> queryResult = recipesRepository.getRecipesByCategory(category);
        if (!queryResult.isPresent()) {
            return Collections.emptyList();
        }
        List<RecipeUserDto> allRecipesByCategory = queryResult.get().stream()
                .map(recipe -> mapRecipeToRecipeUserDto(recipe, user))
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(random)) {
            Collections.shuffle(allRecipesByCategory);
        }
        return allRecipesByCategory;

    }

    private List<RecipeUserDto> getNumberOfRecipes(User user, Integer numOfRecipes, Boolean random) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        Page<Recipe> queryResult = recipesRepository.findAll(pageable);

        List<RecipeUserDto> numberOfRecipes = queryResult.stream()
                .map(recipe -> mapRecipeToRecipeUserDto(recipe, user))
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(random)) {
            Collections.shuffle(numberOfRecipes);
        }
        return numberOfRecipes;
    }

    private List<RecipeUserDto> getNumberOfRecipesByCategory(User user, Integer numOfRecipes, Category category, Boolean random) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        Optional<List<Recipe>> queryResult = recipesRepository.getNRecipesByCategory(category, pageable);

        if (!queryResult.isPresent()) {
            return Collections.emptyList();
        }
        List<RecipeUserDto> numberOfRecipesByCategory = queryResult.get().stream()
                .map(recipe -> mapRecipeToRecipeUserDto(recipe, user))
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(random)) {
            Collections.shuffle(numberOfRecipesByCategory);
        }
        return numberOfRecipesByCategory;
    }

    private RecipeUserDto mapRecipeToRecipeUserDto(Recipe recipe, User user) {
        boolean isFavorite = (user != null) && favoriteRecipeService.isFavorite(recipe.getId(), user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public RecipeUserDto getRecipeById(Long recipeId) {
        User user = userService.getUserFromToken();
        Recipe recipe = recipeGlobalService.getRecipeById(recipeId);
        boolean isFavorite = (user != null) && favoriteRecipeService.isFavorite(recipeId, user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        Recipe recipeCreation = recipeMapper.recipeDtoToRecipe(recipeCreationDto);
        return recipesRepository.save(recipeCreation);
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeGlobalService.getRecipeById(recipeId);
        if (recipe != null) {
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
        }
    }

    public void deleteAllRecipes() {
        favoriteRecipeService.deleteAllFavorites();
        recipesRepository.deleteAll();
    }
}
