import React from "react";
import RecipePreview from "../components/RecipePreview";

export default function Home() {
  let recipe = {
    title: "food",
    vegeterian: true,
    vegan: true,
    glutenFree: true,
    dairyFree: true,
    pictureUrl: "https://spoonacular.com/recipeImages/1098387-312x231.jpg",
  };

  return (
    <React.Fragment>
      <img src="logo.png" alt="logo" height="200" />

      <h2 className="recipes-group-title">Newest</h2>
      <div className="row row-cols-1 row-cols-md-4 g-4">
        <div className="col">
          <RecipePreview {...recipe} />
        </div>
        <div className="col">
          <RecipePreview {...recipe} />
        </div>
        <div className="col">
          <RecipePreview {...recipe} />
        </div>
        <div className="col">
          <RecipePreview {...recipe} />
        </div>
      </div>
    </React.Fragment>
  );
}
