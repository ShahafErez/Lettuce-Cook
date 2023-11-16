import React from "react";
import useFetch from "../../../hooks/useFetch";
import Error from "../../Error";
import Loading from "../../Loading";
import RecipePreview from "../RecipePreview";

export default function RecipesByCategory(props) {
  let category = props.category;
  const { isLoading, isError, data } = useFetch(
    `${global.dataUrl}/recipes/get-recipes?category=${category}&numOfRecipes=4&random=true`
  );
  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="recipes-list">
      {isError ? (
        <Error message={"for all recipes"} style={{ textAlign: "center" }} />
      ) : (
        <React.Fragment>
          <h2 className="recipes-group-title">{category}</h2>
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
    </div>
  );
}
