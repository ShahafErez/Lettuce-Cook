package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.recipe.IngredientDto;
import com.shahaf.lettucecook.dto.recipe.InstructionDto;
import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Ingredient;
import com.shahaf.lettucecook.entity.recipe.Instruction;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.enums.recipe.Unit;
import com.shahaf.lettucecook.enums.users.Role;
import com.shahaf.lettucecook.reposetory.UserRepository;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import com.shahaf.lettucecook.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private RecipesRepository recipesRepository;
    @MockBean
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @MockBean
    private UserRepository userRepository;

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTY5MTQxNTY2NSwiZXhwIjoxNjkzNTYzMTQ4fQ.ycuIn9fMTjcneKOhGjZjA_yv7jotqqh00YgMOS-ROUg";
    private static User mockUser;

    @BeforeEach
    void setup() {
        // mocking jwt authentication functions
        String userEmail = "testUser@gmail.com";
        mockUser = new User(1L, "testUser", userEmail, "abc", Role.USER);

        when(jwtService.extractEmail(JWT_TOKEN)).thenReturn(userEmail);
        when(jwtService.isTokenValid(JWT_TOKEN, mockUser)).thenReturn(true);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(mockUser);
    }

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals(1, recipesListResponse.get(0).getId());
        assertEquals("recipe 1", recipesListResponse.get(0).getName());
        assertEquals(2, recipesListResponse.get(1).getId());
        assertEquals("recipe 2", recipesListResponse.get(1).getName());

        verify(recipesRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById() throws Exception {
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        mockRecipe.setName("recipe");
        mockRecipe.setIngredients(List.of(new Ingredient(null, "ingredient", Unit.CUP, 1F)));
        mockRecipe.setInstructions(List.of(new Instruction(null, "instruction")));

        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Recipe recipe = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals(1, recipe.getId());
        assertEquals("recipe", recipe.getName());
        assertEquals("ingredient", recipe.getIngredients().get(0).getName());
        assertEquals(1, recipe.getInstructions().size());

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
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        when(recipesRepository.save(any())).thenReturn(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipe added successfully. New recipe id: 1")));

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
                new InstructionDto("melt the chocolate"),
                new InstructionDto("dip strawberries into chocolate")));
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertEquals("Recipe not found with id: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
        verify(recipesRepository, times(1)).findById(1L);
    }
}