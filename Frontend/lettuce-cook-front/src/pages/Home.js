import React from "react";
import NewestRecipes from "../components/recipe/recipesList/NewestRecipes";
import RecipesByCategory from "../components/recipe/recipesList/RecipesByCategory";

export default function Home() {
  return (
    <React.Fragment>
      <img src="/images/logo.png" alt="logo" height="200" />

      <NewestRecipes />
      <RecipesByCategory category="dinner" />
      <RecipesByCategory category="lunch" />
      <RecipesByCategory category="salad" />
      <RecipesByCategory category="snack" />
      <RecipesByCategory category="dessert" />
    </React.Fragment>
  );
}
