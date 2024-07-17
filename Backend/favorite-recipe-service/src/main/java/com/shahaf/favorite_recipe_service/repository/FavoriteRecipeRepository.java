package com.shahaf.favorite_recipe_service.repository;

import com.shahaf.favorite_recipe_service.entity.Favorite;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Hidden
@Repository
// TODO- change to string and long?
public interface FavoriteRecipeRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Favorite f WHERE f.recipe.id = ?1 AND f.user.id = ?2")
    boolean existsByRecipeIdAndUserId(Long recipeId, Long userId);

    @Query("SELECT f FROM Favorite f WHERE f.recipe.id = ?1 AND f.user.id = ?2")
    Optional<Favorite> findByRecipeIdAndUserId(Long recipeId, Long userId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.recipe.id = ?1")
    void deleteAllFavoritesByRecipe(Long recipeId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.recipe.id = ?1 AND f.user.id = ?2")
    void deleteByRecipeIdAndUserId(Long recipeId, Long userId);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = ?1")
    Optional<List<Favorite>> getFavoritesByUser(Long userId);
}
