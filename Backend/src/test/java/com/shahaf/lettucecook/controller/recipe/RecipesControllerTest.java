package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.recipe.IngredientDto;
import com.shahaf.lettucecook.dto.recipe.InstructionDto;
import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.recipe.Ingredient;
import com.shahaf.lettucecook.entity.recipe.Instruction;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecipesRepository recipesRepository;
    @MockBean
    private FavoriteRecipeRepository favoriteRecipeRepository;

    private static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTY5MTQxNTY2NSwiZXhwIjoxNjkzNTYzMTQ4fQ.ycuIn9fMTjcneKOhGjZjA_yv7jotqqh00YgMOS-ROUg";

    @Test
    void getAllRecipes() throws Exception {
        // setting up the saved recipes list
        List<Recipe> mockRecipeList = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName("recipe 1");
        mockRecipeList.add(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("recipe 2");
        mockRecipeList.add(recipe2);

        when(recipesRepository.findAll()).thenReturn(mockRecipeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("recipe 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("recipe 2"));

        // Verify that the repository method was called once
        verify(recipesRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById() throws Exception {
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        mockRecipe.setName("recipe");
        mockRecipe.setIngredients(List.of(new Ingredient(null, "ingredient", "unit", 1F)));
        mockRecipe.setInstructions(List.of(new Instruction(null, "instruction")));
        mockRecipe.setVegan(true);

        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("recipe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[0].name").value("ingredient"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions[0].description").value("instruction"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vegan").value(true));

        verify(recipesRepository, times(1)).findById(1L);
    }

    @Test
    void getRecipeByIdRecipeNotExists() throws Exception {
        when(recipesRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found with id: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
        verify(recipesRepository, times(1)).findById(1L);
    }

    @Test
    void addRecipe() throws Exception {
        RecipeCreationDto recipeToAdd = createRecipeCreationDto();
        // configuring the behavior of the save method on the mocked repository
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        when(recipesRepository.save(any())).thenReturn(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipe added successfully. New recipe id: 1")));
        // Verify that the repository method was called once
        verify(recipesRepository, times(1)).save(any());
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
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        mockRecipe.setName("mock recipe");

        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));
        doNothing().when(recipesRepository).delete(mockRecipe);
        doNothing().when(favoriteRecipeRepository).deleteAllFavoritesByRecipe(mockRecipe.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/1")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(recipesRepository, times(1)).findById(1L);
        verify(recipesRepository, times(1)).deleteById(mockRecipe.getId());
        verify(favoriteRecipeRepository, times(1)).deleteAllFavoritesByRecipe(mockRecipe.getId());
    }

    @Test
    void deleteRecipeByIdRecipeNotExists() throws Exception {
        when(recipesRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/1")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found with id: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
        verify(recipesRepository, times(1)).findById(1L);
    }
}