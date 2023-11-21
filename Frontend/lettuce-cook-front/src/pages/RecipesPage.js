import React from "react";
import { useParams } from "react-router-dom";
import RecipesList from "../components/recipe/RecipesList";

export default function RecipesPage() {
  const { category } = useParams();

  let title =
    category === "all"
      ? "All"
      : category.charAt(0).toUpperCase() + category.slice(1);

  return (
    <React.Fragment>
      <h2>{title} Recipes</h2>
      {category === "all" ? (
        <RecipesList random="false" />
      ) : (
        <RecipesList category={category} random="true" />
      )}
    </React.Fragment>
  );
}
