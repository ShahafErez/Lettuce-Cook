package com.shahaf.lettucecook.reposetory.elasticsearch;

import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import com.shahaf.lettucecook.enums.recipe.Category;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RecipeElasticRepository extends ElasticsearchRepository<RecipeElastic, String> {

    List<RecipeElastic> findAll();

    List<RecipeElastic> findByCategories(Category category);

    @Query("{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"summary\": \"?0\"}}, {\"wildcard\": {\"ingredients\": \"*?0*\"}}]}}")
    List<RecipeElastic> findByNameOrSummaryOrIngredients(String searchTerm);

    @Query("{\"bool\": {\"must\": [{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"summary\": \"?0\"}}, {\"wildcard\": {\"ingredients\": \"*?0*\"}}]}}], \"filter\": [{\"terms\": {\"categories\": [\"?1\"]}}]}}")
    List<RecipeElastic> findByNameOrSummaryOrIngredientsAndCategory(String searchTerm, Category category);


}
