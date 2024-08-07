package com.shahaf.recipe_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.recipe_service.dto.IngredientDto;
import com.shahaf.recipe_service.dto.InstructionDto;
import com.shahaf.recipe_service.dto.RecipeCreationDto;
import com.shahaf.recipe_service.entity.Ingredient;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.enums.Category;
import com.shahaf.recipe_service.enums.Unit;
import com.shahaf.recipe_service.repository.RecipesRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.shahaf.recipe_service.constants.ApplicationConstants.SEARCH_SERVICE_URL;
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
    private RestTemplate restTemplate;

    @MockBean
    private RecipesRepository recipesRepository;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTY5MTQxNTY2NSwiZXhwIjoxNjkzNTYzMTQ4fQ.ycuIn9fMTjcneKOhGjZjA_yv7jotqqh00YgMOS-ROUg";


    @Test
    void getAllRecipes() throws Exception {
        // setting up the saved recipes list
        List<Recipe> mockRecipeList = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId(212L);
        recipe1.setName("recipe 212");
        recipe1.setCategories(List.of(Category.DESSERT, Category.SNACK));
        mockRecipeList.add(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setId(121L);
        recipe2.setName("recipe 121");
        recipe2.setCategories(List.of(Category.DINNER, Category.LUNCH));
        mockRecipeList.add(recipe2);

        when(recipesRepository.findRecipesByOrderByIdDesc()).thenReturn(mockRecipeList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get-recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe1 = recipesListResponse.get(0);
        Recipe responseRecipe2 = recipesListResponse.get(1);
        assertEquals(212, responseRecipe1.getId());
        assertEquals("recipe 212", responseRecipe1.getName());
        assertEquals(121, responseRecipe2.getId());
        assertEquals("recipe 121", responseRecipe2.getName());
        assertEquals(2, recipesListResponse.size());

        verify(recipesRepository, times(1)).findRecipesByOrderByIdDesc();
    }

    @Test
    void getRecipesByCriteria() throws Exception {
        List<Recipe> mockRecipeList = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setId(212L);
        recipe.setName("recipe 212");
        recipe.setCategories(List.of(Category.DINNER, Category.SNACK));
        mockRecipeList.add(recipe);

        when(recipesRepository.getRecipesByCategory(Category.DINNER)).thenReturn(Optional.of(mockRecipeList));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get-recipes?numOfRecipes=4&category=dinner&random=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe = recipesListResponse.get(0);
        assertEquals(212, responseRecipe.getId());
        assertEquals("recipe 212", responseRecipe.getName());
        assertEquals(1, recipesListResponse.size());

        verify(recipesRepository, times(1)).getRecipesByCategory(Category.DINNER);
    }

    @Test
    void getRecipeByIdRecipeNotExists() throws Exception {
        when(recipesRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found. ID: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
        verify(recipesRepository, times(1)).findById(1L);
    }

    @Test
    void addRecipe() throws Exception {
        RecipeCreationDto recipeToAdd = createRecipeCreationDto();
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setIngredients(List.of(new Ingredient(1L, "ingredient", Unit.PIECE, 1F)));

        when(recipesRepository.save(any())).thenReturn(savedRecipe);

        // mock http call to search service
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Recipe> requestEntity = new HttpEntity<>(savedRecipe, headers);

        when(restTemplate.exchange(SEARCH_SERVICE_URL, HttpMethod.POST, requestEntity, String.class))
                .thenReturn(new ResponseEntity<>("Recipe added successfully to Elasticsearch", HttpStatus.CREATED));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe added successfully. New recipe id: 1")));

        verify(recipesRepository, times(1)).save(any());
    }

    @Test
    void addRecipeWithoutFields() throws Exception {
        RecipeCreationDto recipeToAdd = new RecipeCreationDto();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Map<String, String> errorsResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals("must not be blank", errorsResponse.get("name"));
        assertEquals("must not be empty", errorsResponse.get("instructions"));
        assertEquals("must not be empty", errorsResponse.get("ingredients"));
    }

    @Test
    void addRecipeFailedSavingInElastic() throws Exception {
        RecipeCreationDto recipeToAdd = createRecipeCreationDto();
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setName("chocolate covered strawberries");
        savedRecipe.setIngredients(List.of(new Ingredient(1L, "ingredient", Unit.PIECE, 1F)));

        when(recipesRepository.save(any())).thenReturn(savedRecipe);

        // mock http call to search service
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Recipe> requestEntity = new HttpEntity<>(savedRecipe, headers);

        when(restTemplate.exchange(SEARCH_SERVICE_URL, HttpMethod.POST, requestEntity, String.class))
                .thenReturn(new ResponseEntity<>("Adding recipe failed due to issue adding recipe to Elastic.", HttpStatus.UNPROCESSABLE_ENTITY));

        doNothing().when(recipesRepository).deleteById(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Adding recipe failed due to issue adding recipe to Elastic.", content);

        verify(recipesRepository, times(1)).save(any());
        verify(recipesRepository, times(1)).deleteById(1L);
    }

    private RecipeCreationDto createRecipeCreationDto() {
        RecipeCreationDto newRecipeDto = new RecipeCreationDto();
        newRecipeDto.setName("chocolate covered strawberries");
        newRecipeDto.setSummary("strawberries covered by chocolate. perfect for snack or dessert");
        newRecipeDto.setVegetarian(true);
        newRecipeDto.setVegan(false);
        newRecipeDto.setGlutenFree(true);
        newRecipeDto.setDairyFree(false);
        newRecipeDto.setMakingTime(10);
        newRecipeDto.setServings(2);
        newRecipeDto.setCategories(List.of(Category.DESSERT, Category.SNACK));
        newRecipeDto.setPictureUrl("https://hips.hearstapps.com/del.h-cdn.co/assets/18/06/1600x1600/square-1518114769-delish-chocolate-covered-strawberries.jpg?resize=1200:*");
        newRecipeDto.setIngredients(List.of(
                new IngredientDto("chocolate", Unit.CUP, 2F),
                new IngredientDto("strawberries", Unit.GRAM, 10F)));
        newRecipeDto.setInstructions(List.of(
                new InstructionDto(1, "melt the chocolate"),
                new InstructionDto(2, "dip strawberries into chocolate")));
        return newRecipeDto;
    }

    @Test
    void deleteRecipe() throws Exception {
        Long id = 1L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(id);

        when(recipesRepository.findById(id)).thenReturn(Optional.of(mockRecipe));
        doNothing().when(recipesRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(recipesRepository, times(1)).findById(id);
        verify(recipesRepository, times(1)).deleteById(id);

    }

    @Test
    void deleteRecipeByIdRecipeNotExists() throws Exception {
        when(recipesRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found. ID: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
        verify(recipesRepository, times(1)).findById(1L);
    }
}