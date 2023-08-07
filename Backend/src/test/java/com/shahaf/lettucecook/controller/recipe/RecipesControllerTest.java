package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.recipe.IngredientDto;
import com.shahaf.lettucecook.dto.recipe.InstructionDto;
import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpYXQiOjE2OTEzNTk5MjAsImV4cCI6MTY5MzUwNzQwNH0.idX07t3feLJO0zHJdnokqMFM474nccCS8zpn5m8nTbY";

    @Test
    void getAllRecipes() {
    }

    @Test
    void getRecipeById() {
    }

    @Test
    void addRecipe() throws Exception {
        RecipeCreationDto recipeToAdd = createRecipeCreationDto();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipe added successfully.")));
    }

    private RecipeCreationDto createRecipeCreationDto() {
        RecipeCreationDto newRecipeDto = new RecipeCreationDto();
        newRecipeDto.setName("chocolate covered strawberries");
        newRecipeDto.setVegetarian(true);
        newRecipeDto.setVegan(false);
        newRecipeDto.setGlutenFree(true);
        newRecipeDto.setDairyFree(false);
        List<IngredientDto> ingredients = new ArrayList<>();
        ingredients.add(new IngredientDto("chocolate", "cup", 2F));
        ingredients.add(new IngredientDto("strawberries", "pieces", 10F));
        newRecipeDto.setIngredients(ingredients);
        List<InstructionDto> instructions = new ArrayList<>();
        instructions.add(new InstructionDto("melt the chocolate"));
        instructions.add(new InstructionDto("dip strawberries into chocolate"));
        newRecipeDto.setInstructions(instructions);
        newRecipeDto.setPictureUrl("https://hips.hearstapps.com/del.h-cdn.co/assets/18/06/1600x1600/square-1518114769-delish-chocolate-covered-strawberries.jpg?resize=1200:*");
        return newRecipeDto;
    }

    @Test
    void deleteRecipe() {
    }
}