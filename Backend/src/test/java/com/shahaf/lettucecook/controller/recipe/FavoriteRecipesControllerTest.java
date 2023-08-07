package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.config.JwtAuthenticationFilter;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.entity.recipe.Ingredient;
import com.shahaf.lettucecook.entity.recipe.Instruction;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.Role;
import com.shahaf.lettucecook.reposetory.UserRepository;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @MockBean
    private UserRepository userRepository;
    private static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTY5MTQxNTY2NSwiZXhwIjoxNjkzNTYzMTQ4fQ.ycuIn9fMTjcneKOhGjZjA_yv7jotqqh00YgMOS-ROUg";
    private static User mockUser;

    @BeforeEach
    void setup() {
        // setting a user for jwt authentication
        String userEmail = "user@gmail.com";
        // TODO- change to testUser
        mockUser = new User(1L, "user", userEmail, "abc", Role.USER);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
    }

    @Test
    void getFavoritesByUser() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        List<Favorite> mockFavoriteList = List.of(new Favorite(1L, recipe, mockUser));

        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        when(favoriteRecipeRepository.getFavoritesByUser(mockUser.getId())).thenReturn(Optional.of(mockFavoriteList));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorite/get/user")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Recipe> favoriteListResponse = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(1, favoriteListResponse.size());
        assertEquals(1L, favoriteListResponse.get(0).getId());


        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(favoriteRecipeRepository, times(1)).getFavoritesByUser(mockUser.getId());
    }

    @Test
    void addFavoriteRecipe() throws Exception {
        FavoriteRecipeDto mockfavoriteRecipeDto = new FavoriteRecipeDto();
        mockfavoriteRecipeDto.setRecipeId(1L);
        mockfavoriteRecipeDto.setUsername("user");
        // configuring the behavior of the save method on the mocked repository
        Favorite savedFavoriteRecipe = new Favorite();
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        savedFavoriteRecipe.setRecipe(mockRecipe);
        savedFavoriteRecipe.setUser(mockUser);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(mockUser));
        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        when(favoriteRecipeRepository.save(any())).thenReturn(savedFavoriteRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockfavoriteRecipeDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipes successfully added to favorites.")));

        verify(userRepository, times(1)).findByUsername("user");
        verify(recipesRepository, times(1)).findById(1L);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(1L, 1L);
        verify(favoriteRecipeRepository, times(1)).save(any());
    }

    @Test
    void removeFavoriteRecipe() throws Exception {

    }

}