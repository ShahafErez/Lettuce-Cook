import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useLocalStorage } from "../../hooks/useLocalStorage";
import {
  addToFavorites,
  removeFromFavorites,
} from "../../services/favoriteService";

export default function FavoriteButton(props) {
  const navigate = useNavigate();
  let userLoggedIn = useLocalStorage("username") != null;

  const [isFavorite, setIsFavorite] = useState(props.isFavorite);
  let recipeId = props.recipeId;

  async function handleClick(event) {
    event.stopPropagation();
    if (!userLoggedIn) {
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
      {userLoggedIn && isFavorite ? (
        <i className="bi bi-suit-heart-fill"></i>
      ) : (
        <i className="bi bi-suit-heart"></i>
      )}
    </div>
  );
}
