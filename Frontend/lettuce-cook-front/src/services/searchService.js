export const search = async (searchTerm) => {
  return fetch(`${global.dataUrl}/search/`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  })
    .then((res) => {
      if (res.status !== 200) {
        return -1;
      } else {
        return res.json();
      }
    })

    .catch((e) => {
      console.error("An error occurred during post request: ", e);
    });
};
