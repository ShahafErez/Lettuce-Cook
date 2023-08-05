package com.shahaf.lettucecook.reposetory.recipe;

import com.shahaf.lettucecook.entity.recipe.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f FROM Favorite f WHERE f.recipe.id = ?1 AND f.user.id = ?2")
    Optional<Favorite> findByRecipeIdAndUserId(Long recipeId, Long userId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.recipe.id = ?1")
    void deleteAllFavoritesByRecipe(Long recipeId);
}
