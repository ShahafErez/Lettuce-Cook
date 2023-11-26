package com.shahaf.lettucecook.reposetory.redis;

import com.shahaf.lettucecook.entity.recipe.RecipeRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRedisRepository extends CrudRepository<RecipeRedis, Long> {
}