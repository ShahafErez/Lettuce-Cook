import React from "react";
import Error from "../components/Error";
import Loading from "../components/Loading";
import RecipePreview from "../components/recipe/RecipePreview";
import useFetch from "../hooks/useFetch";

export default function Favorites() {
  let username = localStorage.getItem("username");
  const { isLoading, isError, data } = useFetch(
    `${global.dataUrl}/favorite/get/${username}`
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
      <div className="row row-cols-1 row-cols-md-4 g-4">
        {data.map((recipe) => (
          <div className="col" key={recipe.id}>
            <RecipePreview recipe={recipe} isFavoriteByUser={true} />
          </div>
        ))}
      </div>
    </React.Fragment>
  );
}
