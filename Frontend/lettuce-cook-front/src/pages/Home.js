import React from "react";
import Loading from "../components/loading";
import RecipePreview from "../components/recipe/RecipePreview";
import useFetch from "../hooks/useFetch";

export default function Home() {
  const { isLoading, data } = useFetch(`${global.dataUrl}/recipes/get-all`);

  console.log("loading ", isLoading);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <React.Fragment>
      <img src="logo.png" alt="logo" height="200" />

      <h2 className="recipes-group-title">Newest</h2>
      <div className="row row-cols-1 row-cols-md-4 g-4">
        {data.map((recipe) => (
          <div className="col" key={recipe.id}>
            <RecipePreview recipe={recipe} />
          </div>
        ))}
      </div>
    </React.Fragment>
  );
}
