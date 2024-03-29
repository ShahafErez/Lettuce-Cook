import React from "react";
import { useParams } from "react-router";
import Error from "../components/Error";
import Loading from "../components/Loading";
import Diets from "../components/recipe/Diets";
import Favorite from "../components/recipe/FavoriteSymbol";
import Ingredients from "../components/recipe/Ingredients";
import Instructions from "../components/recipe/Instructions";
import useFetch from "../hooks/useFetch";
import { useLocalStorage } from "../hooks/useLocalStorage";

export default function Recipe() {
  const recipeId = useParams().id;
  const { isLoading, isError, data } = useFetch(
    `${global.dataUrl}/recipes/get/${recipeId}`
  );
  const userLoggedIn = useLocalStorage("username") != null;

  if (isLoading) {
    return <Loading />;
  }

  if (isError) {
    return <Error message={`for recipe ${recipeId}`} />;
  }

  let {
    name,
    summary,
    makingTime,
    servings,
    pictureUrl,
    ingredients,
    instructions,
  } = data.recipe;
  let isFavoriteByUser = userLoggedIn && data.isFavoriteByUser;

  return (
    <div className="container" id="recipe-page">
      {/* recipe information */}
      <div className="row">
        <div className="col-4">
          <img src={pictureUrl} className="card-img-top" alt={name} />
        </div>
        <div className="col-8" style={{ textAlign: "left" }}>
          <h1>{name}</h1>
          <Favorite
            isFavorite={isFavoriteByUser}
            userLoggedIn={userLoggedIn}
            recipeId={recipeId}
          />

          <p style={{ width: "90%" }}>{summary}</p>

          {/* numeric information */}
          <div className="row recipe-numeric-info">
            <div className="col">
              <div className="number">{ingredients.length}</div>{" "}
              <p>ingredients</p>
            </div>
            <div
              className="col"
              style={{
                borderLeft: "1px solid #d2e877",
                borderRight: "1px solid #d2e877",
              }}
            >
              <div className="number">{makingTime}</div> <p>minutes</p>
            </div>
            <div className="col">
              <div className="number">{servings}</div> <p>servings</p>
            </div>
          </div>

          <Diets recipe={data.recipe} symbolSize="45px" />
        </div>
      </div>

      {/* ingredients and instructions */}
      <div className="row" style={{ marginTop: "25px" }}>
        <div className="col-4" style={{ borderRight: "1px solid #5ca43e" }}>
          <Ingredients ingredients={ingredients} />
        </div>
        <div className="col-8">
          <Instructions instructions={instructions} />
        </div>
      </div>
    </div>
  );
}
