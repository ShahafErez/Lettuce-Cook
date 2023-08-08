package com.shahaf.lettucecook.controller.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.Role;
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
    void getFavoritesByUser() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("test recipe");
        List<Favorite> mockFavoriteList = List.of(new Favorite(1L, recipe, mockUser));

        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        when(favoriteRecipeRepository.getFavoritesByUser(mockUser.getId())).thenReturn(Optional.of(mockFavoriteList));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorite/get/" + mockUser.getActualUsername())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> favoriteListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals(1, favoriteListResponse.size());
        assertEquals("test recipe", favoriteListResponse.get(0).getName());

        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(favoriteRecipeRepository, times(1)).getFavoritesByUser(mockUser.getId());
    }

    @Test
    void getFavoritesByUserNotExists() throws Exception {
        when(userRepository.findByUsername("userNotExists")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorite/get/userNotExists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("User not found with username: userNotExists")));

        verify(userRepository, times(1)).findByUsername("userNotExists");
    }

    @Test
    void getFavoritesByUserHaveNoFavorites() throws Exception {
        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        when(favoriteRecipeRepository.getFavoritesByUser(mockUser.getId())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorite/get/" + mockUser.getActualUsername())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("User testUser doesn't have favorite recipes")));

        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(favoriteRecipeRepository, times(1)).getFavoritesByUser(mockUser.getId());
    }

    @Test
    void addFavoriteRecipe() throws Exception {
        FavoriteRecipeDto mockfavoriteRecipeDto = new FavoriteRecipeDto();
        mockfavoriteRecipeDto.setRecipeId(1L);
        mockfavoriteRecipeDto.setUsername(mockUser.getActualUsername());

        Favorite savedFavoriteRecipe = new Favorite();
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        savedFavoriteRecipe.setRecipe(mockRecipe);
        savedFavoriteRecipe.setUser(mockUser);

        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        when(favoriteRecipeRepository.save(any())).thenReturn(savedFavoriteRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockfavoriteRecipeDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipes successfully added to favorites.")));

        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(recipesRepository, times(1)).findById(1L);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(1L, 1L);
        verify(favoriteRecipeRepository, times(1)).save(any());
    }

    @Test
    void addFavoriteRecipeNotExists() throws Exception {
        FavoriteRecipeDto mockfavoriteRecipeDto = new FavoriteRecipeDto();
        mockfavoriteRecipeDto.setRecipeId(1L);
        mockfavoriteRecipeDto.setUsername(mockUser.getActualUsername());

        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        when(recipesRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockfavoriteRecipeDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipe not found with id: 1")));

        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(recipesRepository, times(1)).findById(1L);
    }

    @Test
    void addFavoriteRecipeAlreadyAdded() throws Exception {
        FavoriteRecipeDto mockfavoriteRecipeDto = new FavoriteRecipeDto();
        mockfavoriteRecipeDto.setRecipeId(1L);
        mockfavoriteRecipeDto.setUsername(mockUser.getActualUsername());

        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        Favorite existsFavorite = new Favorite(1L, mockRecipe, mockUser);

        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        when(recipesRepository.findById(1L)).thenReturn(Optional.of(mockRecipe));
        when(favoriteRecipeRepository.findByRecipeIdAndUserId(1L, 1L)).thenReturn(Optional.of(existsFavorite));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/favorite/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockfavoriteRecipeDto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipe 1 already saved as favorite by user testUser")));

        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(recipesRepository, times(1)).findById(1L);
        verify(favoriteRecipeRepository, times(1)).findByRecipeIdAndUserId(1L, 1L);
    }

    @Test
    void removeFavoriteRecipe() throws Exception {
        FavoriteRecipeDto mockfavoriteRecipeDto = new FavoriteRecipeDto();
        mockfavoriteRecipeDto.setRecipeId(1L);
        mockfavoriteRecipeDto.setUsername(mockUser.getActualUsername());

        when(userRepository.findByUsername(mockUser.getActualUsername())).thenReturn(Optional.of(mockUser));
        doNothing().when(favoriteRecipeRepository).deleteByRecipeIdAndUserId(1L, mockUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/favorite/remove")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockfavoriteRecipeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Recipes 1 successfully removed from favorites for user testUser.")));

        verify(userRepository, times(1)).findByUsername(mockUser.getActualUsername());
        verify(favoriteRecipeRepository, times(1)).deleteByRecipeIdAndUserId(1L, mockUser.getId());
    }
}