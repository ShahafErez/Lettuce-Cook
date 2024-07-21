package shahaf.search_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shahaf.search_service.entity.Recipe;
import shahaf.search_service.entity.RecipeElastic;
import shahaf.search_service.enums.Category;
import shahaf.search_service.repository.RecipeElasticRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecipeElasticRepository recipeElasticRepository;
    private RecipeElastic recipeSalad;
    private RecipeElastic recipeCake;
    private final String searchTerm = "carrot";

    @BeforeEach
    void setup() {
        // creating mock recipes
        recipeSalad = new RecipeElastic();
        recipeSalad.setId(1L);
        recipeSalad.setName("Salad");
        recipeSalad.setSummary("Salad!");
        recipeSalad.setIngredients(List.of("carrot", "cucumber", "tomato", "onion"));

        recipeCake = new RecipeElastic();
        recipeCake.setId(2L);
        recipeCake.setName("Carrot cake");
        recipeCake.setSummary("Great cake");
        recipeCake.setIngredients(List.of("carrot", "sugar", "flour"));
    }

    @Test
    void searchAllRecipes() throws Exception {
        when(recipeElasticRepository.findAll()).thenReturn(List.of(recipeCake, recipeSalad));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        assertEquals(2, recipesListResponse.size());

        verify(recipeElasticRepository, times(1)).findAll();
    }

    @Test
    void searchRecipesByCategory() throws Exception {
        Category category = Category.SALAD;
        when(recipeElasticRepository.findByCategories(category)).thenReturn(List.of(recipeSalad));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/v1/search?category=%s", category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe = recipesListResponse.get(0);
        assertEquals(recipeSalad.getName(), responseRecipe.getName());
        assertEquals(1, recipesListResponse.size());

        verify(recipeElasticRepository, times(1)).findByCategories(category);
    }

    @Test
    void searchRecipesBySearchTerm() throws Exception {
        List<RecipeElastic> recipeElasticMockList = List.of(recipeCake, recipeSalad);
        when(recipeElasticRepository.findByNameOrSummaryOrIngredients(searchTerm)).thenReturn(recipeElasticMockList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/v1/search?searchTerm=%s", searchTerm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe1 = recipesListResponse.get(0);
        Recipe responseRecipe2 = recipesListResponse.get(1);
        assertEquals(recipeCake.getName(), responseRecipe1.getName());
        assertEquals(recipeSalad.getName(), responseRecipe2.getName());
        assertEquals(2, recipesListResponse.size());

        verify(recipeElasticRepository, times(1)).findByNameOrSummaryOrIngredients(searchTerm);
    }

    @Test
    void searchRecipesBySearchTermAndCategory() throws Exception {
        Category category = Category.SALAD;
        List<RecipeElastic> recipeElasticMockList = List.of(recipeSalad);
        when(recipeElasticRepository.findByNameOrSummaryOrIngredientsAndCategory(searchTerm, category)).thenReturn(recipeElasticMockList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/v1/search?searchTerm=%s&category=%s", searchTerm, category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Recipe> recipesListResponse = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        Recipe responseRecipe = recipesListResponse.get(0);
        assertEquals(recipeSalad.getName(), responseRecipe.getName());
        assertEquals(1, recipesListResponse.size());

        verify(recipeElasticRepository, times(1)).findByNameOrSummaryOrIngredientsAndCategory(searchTerm, category);
    }


}