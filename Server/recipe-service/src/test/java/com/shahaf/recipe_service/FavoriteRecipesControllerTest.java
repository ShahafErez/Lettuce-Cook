package com.shahaf.recipe_service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.recipe_service.dto.FavoriteDto;
import com.shahaf.recipe_service.entity.Favorite;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.repository.FavoriteRecipeRepository;
import com.shahaf.recipe_service.repository.RecipesRepository;
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
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @MockBean
    private RecipesRepository recipesRepository;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlckBnbWFpbC5jb20iLCJpYXQiOjE2OTE0ODIxMjEsImV4cCI6MTY5MzYyOTYwNX0.xwskTABCC7hz4JET_D5EHIjS4joAkBcJFHFngEtmB60";
    private static final Long recipeId = 1L;
    private final String username = "user1";


    @Test
    void getFavoritesByUser() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setName("recipe");
        List<Favorite> mockFavoriteList = List.of(new Favorite(1L, recipeId, username));
        List<Long> recipeIdsList = List.of(recipeId);
        List<Recipe> mockRecipesList = List.of(recipe);

        when(favoriteRecipeRepository.getFavoritesByUser(username)).thenReturn(Optional.of(mockFavoriteList));
        when(recipesRepository.findByIdIn(recipeIdsList)).thenReturn(mockRecipesList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/favorite/get/" + username)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> favoriteListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals(1, favoriteListResponse.size());
        assertEquals("recipe", favoriteListResponse.get(0).getName());

        verify(favoriteRecipeRepository, times(1)).getFavoritesByUser(username);
        verify(recipesRepository, times(1)).findByIdIn(recipeIdsList);
    }

    @Test
    void addFavoriteRecipe() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(recipeId, username);

        Favorite savedFavoriteRecipe = new Favorite();
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);
        savedFavoriteRecipe.setRecipeId(recipeId);
        savedFavoriteRecipe.setUsername(username);

        when(recipesRepository.existsById(recipeId)).thenReturn(true);
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, username)).thenReturn(Optional.empty());
        when(favoriteRecipeRepository.save(any())).thenReturn(savedFavoriteRecipe);

        mockMvc.perform(MockMvcRequestBuilders.
                        post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(
                        String.format("Recipe 1 successfully added as favorite by user user1.", recipeId)
                )));

        verify(recipesRepository, times(1)).existsById(recipeId);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(recipeId, username);
        verify(favoriteRecipeRepository, times(1)).save(any());
    }

    @Test
    void addFavoriteRecipeNotFound() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(recipeId, username);

        when(recipesRepository.existsById(recipeId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe 1 was not found.")));

        verify(recipesRepository, times(1)).existsById(recipeId);
    }

    @Test
    void addFavoriteRecipeAlreadyAdded() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(recipeId, username);

        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);
        Favorite existsFavorite = new Favorite(recipeId, recipeId, username);

        when(recipesRepository.existsById(recipeId)).thenReturn(true);
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, username)).thenReturn(Optional.of(existsFavorite));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteDto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe 1 already saved as favorite by user user1"))
                );

        verify(recipesRepository, times(1)).existsById(recipeId);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(recipeId, username);
    }

    @Test
    void removeFavoriteRecipe() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(recipeId, username);

        when(recipesRepository.existsById(recipeId)).thenReturn(true);
        when(favoriteRecipeRepository.existsByRecipeIdAndUserId(recipeId, username)).thenReturn(true);
        doNothing().when(favoriteRecipeRepository).deleteByRecipeIdAndUserId(recipeId, username);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/favorite/remove")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe 1 successfully removed as favorite by user user1.")));

        verify(recipesRepository, times(1)).existsById(recipeId);
        verify(favoriteRecipeRepository, times(1)).existsByRecipeIdAndUserId(recipeId, username);
        verify(favoriteRecipeRepository, times(1)).deleteByRecipeIdAndUserId(recipeId, username);
    }

    @Test
    void removeFavoriteRecipeWasNotFavoriteByUser() throws Exception {
        FavoriteDto favoriteDto = new FavoriteDto(recipeId, username);

        when(recipesRepository.existsById(recipeId)).thenReturn(true);
        when(favoriteRecipeRepository.existsByRecipeIdAndUserId(recipeId, username)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/favorite/remove")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Recipe 1 was not added as favorite by user user1.")));

        verify(recipesRepository, times(1)).existsById(recipeId);
        verify(favoriteRecipeRepository, times(1)).existsByRecipeIdAndUserId(recipeId, username);
    }
}