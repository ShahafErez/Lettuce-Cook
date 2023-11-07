import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  addToFavorites,
  removeFromFavorites,
} from "../../services/favoriteService";

export default function Favorite(props) {
  const navigate = useNavigate();
  const [isFavorite, setIsFavorite] = useState(props.isFavorite);
  let recipeId = props.recipeId;

  async function handleClick() {
    // checking if a user is logged in
    if (!localStorage.getItem("username")) {
      navigate("/login");
    }

    let isRequestSuccessful;
    if (isFavorite) {
      isRequestSuccessful = await removeFromFavorites(recipeId);
    } else {
      isRequestSuccessful = await addToFavorites(recipeId);
    }
    if (isRequestSuccessful) {
      setIsFavorite(!isFavorite);
    }
  }

  return (
    <div className="favorite" onClick={handleClick}>
      {isFavorite ? (
        <i className="bi bi-suit-heart-fill"></i>
      ) : (
        <i className="bi bi-suit-heart"></i>
      )}
    </div>
  );
}
