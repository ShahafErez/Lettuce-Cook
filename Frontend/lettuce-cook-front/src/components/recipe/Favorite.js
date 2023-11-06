import React from "react";

export default function Favorite(props) {
  let recipeId = props.recipeId;

  return (
    <div className="favorite">
      <i className="bi bi-suit-heart"></i>
    </div>
  );
}
