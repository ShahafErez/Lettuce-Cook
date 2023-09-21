import React from "react";

export default function Ingredients(props) {
  let ingredients = props.ingredients;

  return (
    <React.Fragment>
      <h2>Ingredients</h2>
      <div className="ingredients">
        {ingredients.map((ingredient) => (
          <div key={ingredient.id}>
            <p>
              {ingredient.amount} {ingredient.unit}
              <span className="ingredient-name"> {ingredient.name}</span>{" "}
            </p>
          </div>
        ))}
      </div>
    </React.Fragment>
  );
}
