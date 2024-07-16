package com.shahaf.recipe_service.repository;

import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.enums.Category;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Hidden
@Repository
public interface RecipesRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findRecipesByOrderByIdDesc();

    List<Recipe> findNRecipesByOrderByIdDesc(Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE ?1 MEMBER OF r.categories")
    Optional<List<Recipe>> getRecipesByCategory(Category category);

    @Query("SELECT r FROM Recipe r WHERE ?1 MEMBER OF r.categories")
    Optional<List<Recipe>> getNRecipesByCategory(Category category, Pageable pageable);
}
