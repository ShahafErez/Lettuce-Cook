package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.entity.recipe.Recipe;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FavoriteRecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @MockBean
    private RecipesRepository recipesRepository;
    @MockBean
    private UserRepository userRepository;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlckBnbWFpbC5jb20iLCJpYXQiOjE2OTE0ODIxMjEsImV4cCI6MTY5MzYyOTYwNX0.xwskTABCC7hz4JET_D5EHIjS4joAkBcJFHFngEtmB60";
    private static final Long recipeId = 1L;
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
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
    }

    @Test
    void getFavoritesByUser() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setName("recipe");
        List<Favorite> mockFavoriteList = List.of(new Favorite(1L, recipe, mockUser));

        when(favoriteRecipeRepository.getFavoritesByUser(mockUser.getId())).thenReturn(Optional.of(mockFavoriteList));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/favorite/get")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> favoriteListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals(1, favoriteListResponse.size());
        assertEquals("recipe", favoriteListResponse.get(0).getName());

        verify(favoriteRecipeRepository, times(1)).getFavoritesByUser(mockUser.getId());
    }

    @Test
    void addFavoriteRecipe() throws Exception {
        Favorite savedFavoriteRecipe = new Favorite();
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);
        savedFavoriteRecipe.setRecipe(mockRecipe);
        savedFavoriteRecipe.setUser(mockUser);

        when(recipesRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, 1L)).thenReturn(Optional.empty());
        when(favoriteRecipeRepository.save(any())).thenReturn(savedFavoriteRecipe);

        mockMvc.perform(MockMvcRequestBuilders.
                        post(String.format("/api/v1/favorite/add/%s", recipeId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(
                        String.format("Recipe %s successfully added to favorites by user.", recipeId)
                )));

        verify(recipesRepository, times(1)).findById(recipeId);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(recipeId, 1L);
        verify(favoriteRecipeRepository, times(1)).save(any());
    }

    @Test
    void addFavoriteRecipeNotFound() throws Exception {
        when(recipesRepository.findById(recipeId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(String.format("/api/v1/favorite/add/%s", recipeId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe not found. ID: 1")));

        verify(recipesRepository, times(1)).findById(recipeId);
    }

    @Test
    void addFavoriteRecipeAlreadyAdded() throws Exception {
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);
        Favorite existsFavorite = new Favorite(recipeId, mockRecipe, mockUser);

        when(recipesRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, mockUser.getId())).thenReturn(Optional.of(existsFavorite));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(String.format("/api/v1/favorite/add/%s", recipeId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(
                        String.format("Recipe %s already saved as favorite by user ID %s", recipeId, mockUser.getId())))
                );

        verify(recipesRepository, times(1)).findById(recipeId);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(recipeId, mockUser.getId());
    }

    @Test
    void removeFavoriteRecipe() throws Exception {
        when(recipesRepository.existsById(recipeId)).thenReturn(true);
        when(favoriteRecipeRepository.existsByRecipeIdAndUserId(recipeId, mockUser.getId())).thenReturn(true);
        doNothing().when(favoriteRecipeRepository).deleteByRecipeIdAndUserId(recipeId, mockUser.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(String.format("/api/v1/favorite/remove/%s", recipeId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(
                        String.format("Recipe %s successfully removed from favorites.", recipeId))));

        verify(recipesRepository, times(1)).existsById(recipeId);
        verify(favoriteRecipeRepository, times(1)).existsByRecipeIdAndUserId(recipeId, mockUser.getId());
        verify(favoriteRecipeRepository, times(1)).deleteByRecipeIdAndUserId(recipeId, mockUser.getId());
    }

    @Test
    void removeFavoriteRecipeWasNotFavoriteByUser() throws Exception {
        when(recipesRepository.existsById(recipeId)).thenReturn(true);
        when(favoriteRecipeRepository.existsByRecipeIdAndUserId(recipeId, mockUser.getId())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(String.format("/api/v1/favorite/remove/%s", recipeId))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(
                        String.format("Recipe %d is not favorite by user %s.", recipeId, mockUser.getActualUsername()))));

        verify(recipesRepository, times(1)).existsById(recipeId);
        verify(favoriteRecipeRepository, times(1)).existsByRecipeIdAndUserId(recipeId, mockUser.getId());
    }
}