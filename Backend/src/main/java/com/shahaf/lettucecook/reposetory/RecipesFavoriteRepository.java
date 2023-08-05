package com.shahaf.lettucecook.reposetory;

import com.shahaf.lettucecook.entity.Favorite;
import com.shahaf.lettucecook.entity.Recipe;
import com.shahaf.lettucecook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipesFavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f FROM Favorite f WHERE f.recipe = ?1 AND f.user = ?2")
    Optional<Favorite> findByRecipeIdAndUserId(Recipe recipe, User user);
}
