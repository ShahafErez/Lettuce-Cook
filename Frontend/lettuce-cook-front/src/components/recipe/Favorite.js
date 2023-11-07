import React, { useState } from "react";

export default function Favorite(props) {
  //  TODO- test after jwt token. Currently being blocked by authentication.
  const [isFavoriteByUser, setIsFavoriteByUser] = useState(
    props.isFavoriteByUser
  );
  let recipeId = props.recipeId;

  function addFavoriteToRecipe() {
    let requestBody = {
      recipeId: recipeId,
      username: "user1",
    };
    fetch(`${global.dataUrl}/favorite/add`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
      withCredentials: true,
    })
      .then((res) => {
        if (res.status === 201) {
          setIsFavoriteByUser(true);
        }
      })
      .catch((e) => {
        console.error("An error occurred during post request: ", e);
      });
  }

  function removeFavoriteFromRecipe() {
    let requestBody = {
      recipeId: recipeId,
      username: "user1",
    };

    fetch(`${global.dataUrl}/favorite/remove`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
      withCredentials: true,
    })
      .then((res) => {
        if (res.status === 200) {
          setIsFavoriteByUser(false);
        }
      })
      .catch((e) => {
        console.error("An error occurred during delete request: ", e);
      });
  }

  function handleClick() {
    if (isFavoriteByUser) {
      removeFavoriteFromRecipe();
    } else {
      addFavoriteToRecipe();
    }
  }

  return (
    <div className="favorite" onClick={handleClick}>
      {props.isFavorite ? (
        <i className="bi bi-suit-heart-fill"></i>
      ) : (
        <i className="bi bi-suit-heart"></i>
      )}
    </div>
  );
}
