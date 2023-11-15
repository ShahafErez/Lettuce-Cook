package com.shahaf.lettucecook.reposetory.recipe;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Repository
public interface RecipesRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE ?1 MEMBER OF r.categories")
    Optional<List<Recipe>> getRecipesByCategory(Category category);

    @Query("SELECT r FROM Recipe r WHERE ?1 MEMBER OF r.categories")
    Optional<List<Recipe>> getNRecipesByCategory(Category category, Pageable pageable);
}
