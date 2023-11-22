package com.shahaf.lettucecook.reposetory.elasticsearch;

import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RecipeElasticRepository extends ElasticsearchRepository<RecipeElastic, String> {
    List<RecipeElastic> findByName(String name);

}