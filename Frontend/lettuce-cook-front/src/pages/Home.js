import React, { useState } from "react";
import RecipesList from "../components/recipe/RecipesList";

export default function Home() {
  const [category, setCategory] = useState("Dinner");

  return (
    <React.Fragment>
      <img src="/images/logo.png" alt="logo" height="200" />

      <h2 className="recipes-group-title">Explore Recipes</h2>
      <RecipesList random="true" />

      <h2 className="recipes-group-title">Newest</h2>
      <RecipesList random="false" />

      <h2 className="recipes-group-title">{category}</h2>
      <RecipesList category={category} random="true" />
    </React.Fragment>
  );
}
