import React from "react";

export default function RecipePreview(props) {
  let recipeId = props.recipeId;

  return <h2>Recipe Preview {recipeId}</h2>;
}
