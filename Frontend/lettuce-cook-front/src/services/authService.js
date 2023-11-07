export const login = async (email, password) => {
  let isLoggedInSuccessfully = false;
  let response;

  await fetch(`${global.dataUrl}/auth/authenticate`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email: email, password: password }),
    // withCredentials: true,
  })
    .then((res) => {
      if (res.status === 200) {
        isLoggedInSuccessfully = true;
        return res.json();
      }
      return res.text();
    })
    .then((json) => {
      response = json;
    })
    .catch((e) => {
      console.error("An error occurred during post request: ", e);
    });

  if (isLoggedInSuccessfully) {
    const { token, username } = response;
    localStorage.setItem("token", token);
    localStorage.setItem("username", username);
  }

  return { isLoggedInSuccessfully: isLoggedInSuccessfully, response };
};

export const register = async (registerBody) => {
  let isRegisteredSuccessfully = false;
  let response = null;

  await fetch(`${global.dataUrl}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(registerBody),
    withCredentials: true,
  })
    .then((res) => {
      if (res.status === 201) {
        isRegisteredSuccessfully = true;
      }
      return res.json();
    })
    .then((json) => {
      response = json;
    })
    .catch((e) => {
      console.error("An error occurred during post request: ", e);
    });

  if (isRegisteredSuccessfully) {
    const { token, username } = response;
    localStorage.setItem("token", token);
    localStorage.setItem("username", username);
  }

  return { isRegisteredSuccessfully, response };
};
