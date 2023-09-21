import React from "react";
import { useParams } from "react-router";
import Diets from "../components/Diets";
import Ingredients from "../components/Ingredients";
import Instructions from "../components/Instructions";

export default function Recipe() {
  const recipeId = useParams().id;

  let recipe = {
    id: 1,
    title: "food",
    summary:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    vegetarian: true,
    vegan: true,
    glutenFree: true,
    dairyFree: true,
    makingTime: 15,
    serving: 1,
    pictureUrl: "https://spoonacular.com/recipeImages/1098387-312x231.jpg",
    ingredients: [
      { id: 1, name: "apple", unit: "piece", amount: 4 },
      { id: 2, name: "peanut butter", unit: "cup", amount: 2 },
    ],
    instructions: [
      { id: 1, description: "make food" },
      { id: 2, description: "eat food" },
    ],
  };

  let {
    title,
    summary,
    makingTime,
    serving,
    pictureUrl,
    ingredients,
    instructions,
  } = recipe;

  return (
    <div className="container" id="recipe-page">
      {/* recipe information */}
      <div className="row">
        <div className="col-4">
          <img src={pictureUrl} className="card-img-top" alt={title} />
        </div>
        <div className="col-8" style={{ textAlign: "left" }}>
          <h1>{title}</h1>
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
              <div className="number">{serving}</div> <p>servings</p>
            </div>
          </div>

          <Diets recipe={recipe} symbolSize="45px" />
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
