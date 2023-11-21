import React from "react";
import useFetch from "../../hooks/useFetch";
import Error from "../Error";
import Loading from "../Loading";
import RecipePreview from "../recipe/RecipePreview";

export default function RecipesList(props) {
  let category = props.category || "";
  let numOfRecipes = global.recipesNumberHomePage;
  let random = props.random;

  let url = `${global.dataUrl}/recipes/get-recipes?numOfRecipes=${numOfRecipes}&category=${category}&random=${random}`;

  const { isLoading, isError, data } = useFetch(url);
  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="recipes-list">
      {isError ? (
        <Error style={{ textAlign: "center" }} />
      ) : (
        <React.Fragment>
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
