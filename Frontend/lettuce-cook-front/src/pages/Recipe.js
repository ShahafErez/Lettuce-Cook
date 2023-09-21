import React from "react";
import { useParams } from "react-router";

export default function Recipe() {
  const recipeId = useParams().id;

  let recipe = {
    id: 1,
    title: "food",
    summary:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Malesuada pellentesque elit eget gravida. Risus nullam eget felis eget nunc. Porta non pulvinar neque laoreet suspendisse interdum consectetur libero. Sem et tortor consequat id.",
    vegeterian: true,
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
    vegeterian,
    vegan,
    glutenFree,
    dairyFree,
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
          <h2>{title}</h2>
          <p style={{ width: "90%" }}>{summary}</p>

          <div className="row recipe-numeric-info">
            {/* numeric information */}
            <div className="col">
              <div className="number">{ingredients.length}</div>{" "}
              <p>ingredients</p>
            </div>
            <div
              className="col"
              style={{
                borderLeft: "1px solid #5ca43e",
                borderRight: "1px solid #5ca43e",
              }}
            >
              <div className="number">{makingTime}</div> <p>minutes</p>
            </div>
            <div className="col">
              <div className="number">{serving}</div> <p>servings</p>
            </div>
          </div>
        </div>
      </div>

      {/* ingredients and instructions */}
      <div className="row" style={{ marginTop: "25px" }}>
        <div className="col-4" style={{ borderRight: "1px solid #d2e877" }}>
          {ingredients.map((ingredient) => (
            <div key={ingredient.id}>{ingredient.name}</div>
          ))}
        </div>
        <div className="col-8">
          {instructions.map((instruction) => (
            <div key={instruction.id}>{instruction.description}</div>
          ))}
        </div>
      </div>
    </div>
  );
}
