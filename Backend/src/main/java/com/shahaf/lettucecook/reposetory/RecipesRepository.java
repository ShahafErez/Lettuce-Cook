package com.shahaf.lettucecook.reposetory;

import com.shahaf.lettucecook.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipesRepository extends JpaRepository<Recipe, Long> {
}
