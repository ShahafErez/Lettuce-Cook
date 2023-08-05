package com.shahaf.lettucecook.reposetory.recipe;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipesRepository extends JpaRepository<Recipe, Long> {
}
