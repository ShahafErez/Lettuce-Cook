package com.shahaf.lettucecook.reposetory.elasticsearch;

import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import com.shahaf.lettucecook.enums.recipe.Category;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface RecipeElasticRepository extends ElasticsearchRepository<RecipeElastic, Long> {
    List<RecipeElastic> findByCategories(Category category);

    @Query("{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"summary\": \"?0\"}}, {\"wildcard\": {\"ingredients\": \"*?0*\"}}]}}")
    List<RecipeElastic> findByNameOrSummaryOrIngredients(String searchTerm);

    @Query("{\"bool\": {\"must\": [{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"summary\": \"?0\"}}, {\"wildcard\": {\"ingredients\": \"*?0*\"}}]}}], \"filter\": [{\"terms\": {\"categories\": [\"?1\"]}}]}}")
    List<RecipeElastic> findByNameOrSummaryOrIngredientsAndCategory(String searchTerm, Category category);


}
