import React from "react";
import RecipePreview from "../components/RecipePreview";

export const RecipeContext = React.createContext();

export default function Home() {
  let recipe = {
    id: 1,
    title: "food",
    summary:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Malesuada pellentesque elit eget gravida. Risus nullam eget felis eget nunc. Porta non pulvinar neque laoreet suspendisse interdum consectetur libero. Sem et tortor consequat id.",
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
          <RecipeContext.Provider value={{ ...recipe }}>
            <RecipePreview />
          </RecipeContext.Provider>
        </div>
        {/* <div className="col">
          <RecipePreview {...recipe} />
        </div>
        <div className="col">
          <RecipePreview {...recipe} />
        </div>
        <div className="col">
          <RecipePreview {...recipe} />
        </div> */}
      </div>
    </React.Fragment>
  );
}
