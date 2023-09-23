import React from "react";
import RecipePreview from "../components/RecipePreview";
import useFetch from "../hooks/useFetch";

export const RecipeContext = React.createContext();

export default function Home() {
  const { data } = useFetch(`${global.dataUrl}/api/v1/recipes/get-all`);

  return (
    <React.Fragment>
      <img src="logo.png" alt="logo" height="200" />

      <h2 className="recipes-group-title">Newest</h2>
      <div className="row row-cols-1 row-cols-md-4 g-4">
        {data.map((recipe) => (
          <div className="col" key={recipe.id}>
            <RecipeContext.Provider value={{ ...recipe }}>
              <RecipePreview />
            </RecipeContext.Provider>
          </div>
        ))}
      </div>
    </React.Fragment>
  );
}
