export const addToFavorites = async (recipeId) => {
  return fetch(`${global.dataUrl}/favorite/add`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
    body: JSON.stringify({
      recipeId: recipeId,
      username: localStorage.getItem("username"),
    }),
    withCredentials: true,
  })
    .then((res) => {
      if (res.status === 201) {
        return true;
      }
      return false;
    })
    .catch((e) => {
      console.error("An error occurred during post request: ", e);
    });
};

export const removeFromFavorites = async (recipeId) => {
  return fetch(`${global.dataUrl}/favorite/remove`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
    body: JSON.stringify({
      recipeId: recipeId,
      username: localStorage.getItem("username"),
    }),
    withCredentials: true,
  })
    .then((res) => {
      if (res.status === 200) {
        return true;
      }
      return false;
    })
    .catch((e) => {
      console.error("An error occurred during delete request: ", e);
    });
};
