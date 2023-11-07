import React from "react";
import Error from "../components/Error";
import Loading from "../components/Loading";
import RecipePreview from "../components/recipe/RecipePreview";
import useFetch from "../hooks/useFetch";

export default function Home() {
  const { isLoading, isError, data } = useFetch(
    `${global.dataUrl}/recipes/get-all`
  );
  if (isLoading) {
    return <Loading />;
  }

  return (
    <React.Fragment>
      <img src="/images/logo.png" alt="logo" height="200" />

      {isError ? (
        <Error message={"for all recipes"} style={{ textAlign: "center" }} />
      ) : (
        <React.Fragment>
          <h2 className="recipes-group-title">Newest</h2>
          <div className="row row-cols-1 row-cols-md-4 g-4">
            {data.map((recipeInfo) => (
              <div className="col" key={recipeInfo.recipe.id}>
                <RecipePreview
                  recipe={recipeInfo.recipe}
                  isFavoriteByUser={recipeInfo.isFavoriteByUser}
                />
              </div>
            ))}
          </div>
        </React.Fragment>
      )}
    </React.Fragment>
  );
}
