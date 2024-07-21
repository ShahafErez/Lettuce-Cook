import React from "react";
import Error from "../components/Error";
import Loading from "../components/Loading";
import RecipePreview from "../components/recipe/RecipePreview";
import useFetch from "../hooks/useFetch";

export default function Favorites() {
  const { isLoading, isError, data } = useFetch(
    `${global.dataUrl}/favorite/get/${localStorage.getItem("username")}`
  );

  if (isLoading) {
    return <Loading />;
  }

  if (isError) {
    return <Error message={`Error showing favorite recipes`} />;
  }

  return (
    <React.Fragment>
      <h1>My Favorite Recipes</h1>
      {data.length === 0 ? (
        <div>No Favorite Recipe Found</div>
      ) : (
        <div className="row row-cols-1 row-cols-md-4 g-4">
          {data.map((recipe) => (
            <div className="col" key={recipe.id}>
              <RecipePreview recipe={recipe} />
            </div>
          ))}
        </div>
      )}
    </React.Fragment>
  );
}
