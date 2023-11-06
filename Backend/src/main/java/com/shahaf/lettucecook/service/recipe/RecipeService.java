package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import com.shahaf.lettucecook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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

    public List<RecipeUserDto> getAll() {
        User user = userService.getUserFromToken();

        return recipesRepository.findAll().stream()
                .map(recipe -> mapRecipeToRecipeUserDto(recipe, user))
                .collect(Collectors.toList());
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
