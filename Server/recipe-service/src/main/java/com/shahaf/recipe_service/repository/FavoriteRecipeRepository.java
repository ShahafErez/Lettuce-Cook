package com.shahaf.recipe_service.repository;

import com.shahaf.recipe_service.entity.Favorite;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Hidden
@Repository
public interface FavoriteRecipeRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Favorite f WHERE f.recipeId = ?1 AND f.username = ?2")
    boolean existsByRecipeIdAndUserId(Long recipeId, String username);

    @Query("SELECT f FROM Favorite f WHERE f.recipeId = ?1 AND f.username = ?2")
    Optional<Favorite> findByRecipeIdAndUserId(Long recipeId, String username);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.recipeId = ?1")
    void deleteAllFavoritesByRecipe(Long recipeId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.recipeId = ?1 AND f.username = ?2")
    void deleteByRecipeIdAndUserId(Long recipeId, String username);

    @Query("SELECT f FROM Favorite f WHERE f.username = ?1")
    Optional<List<Favorite>> getFavoritesByUser(String username);
}
