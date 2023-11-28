package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.recipe.IngredientDto;
import com.shahaf.lettucecook.dto.recipe.InstructionDto;
import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Ingredient;
import com.shahaf.lettucecook.entity.recipe.Instruction;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.enums.recipe.Unit;
import com.shahaf.lettucecook.enums.users.Role;
import com.shahaf.lettucecook.reposetory.UserRepository;
import com.shahaf.lettucecook.reposetory.elasticsearch.RecipeElasticRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private RecipeElasticRepository recipeElasticRepository;
    @MockBean
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @MockBean
    private UserRepository userRepository;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTY5MTQxNTY2NSwiZXhwIjoxNjkzNTYzMTQ4fQ.ycuIn9fMTjcneKOhGjZjA_yv7jotqqh00YgMOS-ROUg";
    private static User mockUser;

    @BeforeEach
    void setup() {
        // mocking jwt authentication
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
        List<RecipeUserDto> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe1 = recipesListResponse.get(0).getRecipe();
        Recipe responseRecipe2 = recipesListResponse.get(1).getRecipe();
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
        List<RecipeUserDto> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe = recipesListResponse.get(0).getRecipe();
        assertEquals(212, responseRecipe.getId());
        assertEquals("recipe 212", responseRecipe.getName());
        assertEquals(1, recipesListResponse.size());

        verify(recipesRepository, times(1)).getRecipesByCategory(Category.DINNER);
    }

    @Test
    void getRecipeByIdForLoggedInUser() throws Exception {
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        mockRecipe.setName("recipe");
        mockRecipe.setIngredients(List.of(new Ingredient(null, "ingredient", Unit.CUP, 1F)));
        mockRecipe.setInstructions(List.of(new Instruction(null, 1, "instruction")));

        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(1L, mockUser.getId())).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/get/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        RecipeUserDto RecipeUserDto = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe recipe = RecipeUserDto.getRecipe();
        assertEquals(1, recipe.getId());
        assertEquals("recipe", recipe.getName());
        assertEquals("ingredient", recipe.getIngredients().get(0).getName());
        assertEquals(1, recipe.getInstructions().size());
        assertFalse(RecipeUserDto.getIsFavoriteByUser());

        verify(recipesRepository, times(1)).findById(1L);
        verify(favoriteRecipeRepository, times(1)).existsByRecipeIdAndUserId(1L, mockUser.getId());
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

        RecipeElastic recipeElastic = new RecipeElastic();
        recipeElastic.setId(1L);
        recipeElastic.setIngredients(List.of("ingredient"));

        when(recipesRepository.save(any())).thenReturn(savedRecipe);
        when(recipeElasticRepository.save(any())).thenReturn(recipeElastic);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe added successfully. New recipe id: 1")));

        verify(recipesRepository, times(1)).save(any());
        verify(recipeElasticRepository, times(1)).save(any());
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

        RecipeElastic recipeElastic = new RecipeElastic();
        recipeElastic.setId(1L);
        recipeElastic.setIngredients(List.of("ingredient"));

        when(recipesRepository.save(any())).thenReturn(savedRecipe);
        when(recipeElasticRepository.save(any())).thenThrow(new RuntimeException());
        doNothing().when(recipesRepository).deleteById(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeToAdd)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Failed to add recipe 1 to Elastic", content);

        verify(recipesRepository, times(1)).save(any());
        verify(recipeElasticRepository, times(1)).save(any());
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
        doNothing().when(favoriteRecipeRepository).deleteAllFavoritesByRecipe(id);
        doNothing().when(recipesRepository).deleteById(id);
        doNothing().when(recipeElasticRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/delete/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(recipesRepository, times(1)).findById(id);
        verify(favoriteRecipeRepository, times(1)).deleteAllFavoritesByRecipe(id);
        verify(recipesRepository, times(1)).deleteById(id);
        verify(recipeElasticRepository, times(1)).deleteById(id);

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