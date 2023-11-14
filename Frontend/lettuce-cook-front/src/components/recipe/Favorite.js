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
  let userLoggedIn = props.userLoggedIn;

  async function handleClick() {
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
