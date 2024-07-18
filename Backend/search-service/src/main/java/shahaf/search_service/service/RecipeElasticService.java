package shahaf.search_service.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shahaf.search_service.entity.Recipe;
import shahaf.search_service.entity.RecipeElastic;
import shahaf.search_service.enums.Category;
import shahaf.search_service.exceptions.ErrorOccurredException;
import shahaf.search_service.mpper.RecipeElasticMapper;
import shahaf.search_service.repository.RecipeElasticRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecipeElasticService {
    @Autowired
    RecipeElasticRepository recipeElasticRepository;
    @Autowired
    RecipeElasticMapper recipeElasticMapper;

    private static final Logger logger = LoggerFactory.getLogger(RecipeElasticService.class);

    public void addRecipeToElastic(Recipe recipe) {
        try {
            logger.info("Saving recipe to Elastic");
            recipeElasticRepository.save(recipeElasticMapper.recipeToElasticRecipe(recipe));
        } catch (Exception e) {
            String errorMessage = String.format("Failed to add recipe %d to Elastic", recipe.getId());
            logger.error(errorMessage);
            throw new ErrorOccurredException(errorMessage);
        }
    }

    public void deleteRecipeById(Long id) {
        logger.info("Deleting recipe {} from Elastic", id);
        recipeElasticRepository.deleteById(id);
    }

    public void deleteAllRecipes() {
        logger.info("Deleting all recipes from Elastic");
        recipeElasticRepository.deleteAll();
    }

    public List<Recipe> searchRecipes(String searchTerm, Category category) {
        logger.info("Fetching recipes. Search term: {}, Category: {}", searchTerm, category);

        List<RecipeElastic> queryResult;
        if ((searchTerm == null || searchTerm.isEmpty()) && category == null) {
            queryResult = StreamSupport.stream(recipeElasticRepository.findAll().spliterator(), false).collect(Collectors.toList());
        } else if (searchTerm == null || searchTerm.isEmpty()) {
            queryResult = recipeElasticRepository.findByCategories(category);
        } else if (category == null) {
            queryResult = recipeElasticRepository.findByNameOrSummaryOrIngredients(searchTerm);
        } else {
            queryResult = recipeElasticRepository.findByNameOrSummaryOrIngredientsAndCategory(searchTerm, category);
        }
        return queryResultToRecipe(queryResult);
    }

    private List<Recipe> queryResultToRecipe(List<RecipeElastic> queryResult) {
        return queryResult.stream().map(recipeElastic ->
                recipeElasticMapper.recipeElasticToRecipe(recipeElastic)).collect(Collectors.toList());
    }
}
