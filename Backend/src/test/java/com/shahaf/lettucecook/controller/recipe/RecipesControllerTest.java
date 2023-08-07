package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.recipe.IngredientDto;
import com.shahaf.lettucecook.dto.recipe.InstructionDto;
import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.recipe.Ingredient;
import com.shahaf.lettucecook.entity.recipe.Instruction;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecipesRepository recipesRepository;

    private static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpYXQiOjE2OTEzNTk5MjAsImV4cCI6MTY5MzUwNzQwNH0.idX07t3feLJO0zHJdnokqMFM474nccCS8zpn5m8nTbY";
    private Long recipe1Id;

    private void saveRecipesToRepository() {
        Recipe recipe1 = new Recipe();
        recipe1.setName("recipe 1");
        recipe1.setIngredients(List.of(new Ingredient(null, "ingredient1", "uni1", 1F)));
        recipe1.setInstructions(List.of(new Instruction(null, "instruction1")));
        recipe1.setVegan(true);
        Recipe savedRecipe1 = recipesRepository.save(recipe1);
        recipe1Id = savedRecipe1.getId();

        Recipe recipe2 = new Recipe();
        recipe2.setName("recipe 2");
        recipe2.setIngredients(List.of(new Ingredient(null, "ingredient2", "uni2", 2F)));
        recipe2.setInstructions(List.of(new Instruction(null, "instruction2")));
        recipesRepository.save(recipe2);
    }

    @Test
    void getAllRecipes() throws Exception {
        saveRecipesToRepository();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Recipe> recipeList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(2, recipeList.size());
        assertEquals(recipe1Id, recipeList.get(0).getId());
        assertEquals("recipe 1", recipeList.get(0).getName());
        assertNotNull(recipeList.get(1).getId());
    }

    @Test
    void getRecipeById() throws Exception {
        saveRecipesToRepository();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/" + recipe1Id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Recipe recipe = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(recipe1Id, recipe.getId());
        assertEquals("recipe 1", recipe.getName());
        assertEquals("ingredient1", recipe.getIngredients().get(0).getName());
        assertEquals(true, recipe.getVegan());
    }

    @Test
    void getRecipeByIdRecipeNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found with id: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
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
        assertEquals(1, recipesRepository.findAll().size()); // only the newly added recipe should be in database
    }

    @Test
    void addRecipeWithoutFields() throws Exception {
        RecipeCreationDto recipeToAdd = new RecipeCreationDto();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> errorMessage = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals("must not be blank", errorMessage.get("name"));
        assertEquals("must not be empty", errorMessage.get("instructions"));
        assertEquals("must not be empty", errorMessage.get("ingredients"));
    }

    private RecipeCreationDto createRecipeCreationDto() {
        RecipeCreationDto newRecipeDto = new RecipeCreationDto();
        newRecipeDto.setName("chocolate covered strawberries");
        newRecipeDto.setVegetarian(true);
        newRecipeDto.setVegan(false);
        newRecipeDto.setGlutenFree(true);
        newRecipeDto.setDairyFree(false);
        newRecipeDto.setIngredients(List.of(
                new IngredientDto("chocolate", "cup", 2F),
                new IngredientDto("strawberries", "pieces", 10F)));
        newRecipeDto.setInstructions(List.of(
                new InstructionDto("melt the chocolate"),
                new InstructionDto("dip strawberries into chocolate")));
        newRecipeDto.setPictureUrl("https://hips.hearstapps.com/del.h-cdn.co/assets/18/06/1600x1600/square-1518114769-delish-chocolate-covered-strawberries.jpg?resize=1200:*");
        return newRecipeDto;
    }

    @Test
    void deleteRecipe() throws Exception {
        saveRecipesToRepository();
        assertEquals(2, recipesRepository.findAll().size()); // verify that the repository has 2 recipes added at setup

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/" + recipe1Id)
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(1, recipesRepository.findAll().size());
        assertFalse(recipesRepository.findById(recipe1Id).isPresent());
    }

    @Test
    void deleteRecipeByIdRecipeNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/1")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found with id: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @AfterEach
    @Transactional
    @Rollback  // Rollback the transaction to clean up the database after each test
    public void tearDown() {
        recipesRepository.deleteAll();
    }
}