package com.shahaf.lettucecook.reposetory.elasticsearch;

import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeElasticRepository extends ElasticsearchRepository<RecipeElastic, String> {

}