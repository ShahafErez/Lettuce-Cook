import { useState } from "react";

export default function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [errors, setErrors] = useState({ email: null, confirmPassword: null });
  const [password, setPassword] = useState({
    password: "",
    show: false,
  });
  const [confirmPassword, setConfirmPassword] = useState({
    confirmPassword: "",
    show: false,
  });
  const [responseMessage, setResponseMessage] = useState({
    message: "",
    status: "",
  });

  function isValidEmail(email) {
    return /\S+@\S+\.\S+/.test(email);
  }

  function handleEmailChange(event) {
    let currentEmail = event.target.value;
    if (isValidEmail(currentEmail)) {
      setErrors({ ...errors, email: null });
    } else {
      setErrors({ ...errors, email: "Email is invalid" });
    }
    setEmail(currentEmail);
  }

  function handlePasswordChange(event) {
    let currentPassword = event.target.value;
    if (confirmPassword.confirmPassword) {
      validatePasswordsMatching(
        currentPassword,
        confirmPassword.confirmPassword
      );
    }
    setPassword({
      ...password,
      password: currentPassword,
    });
  }

  function handleconfirmPasswordChange(event) {
    let currentConfirmPassword = event.target.value;
    validatePasswordsMatching(password.password, currentConfirmPassword);
    setConfirmPassword({
      ...confirmPassword,
      confirmPassword: currentConfirmPassword,
    });
  }

  function validatePasswordsMatching(password, confirmationPassword) {
    if (password === confirmationPassword) {
      setErrors({ ...errors, confirmPassword: null });
    } else {
      setErrors({
        ...errors,
        confirmPassword: "Password confirmation does not match",
      });
    }
  }

  async function registerRequest() {
    let isRegisteredSuccessfully = false;
    let response = null;

    let body = {
      username: username,
      email: email,
      password: password.password,
    };

    await fetch(`${global.dataUrl}/auth/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
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
    return { isRegisteredSuccessfully, response };
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const { isRegisteredSuccessfully, response } = await registerRequest();

    if (isRegisteredSuccessfully) {
      setResponseMessage({
        message: "Acount created successfully",
        status: "success",
      });
      resetFormValues();
    } else {
      setResponseMessage({
        message: Object.values(response).join(" "),
        status: "failure",
      });
    }
  }

  function resetFormValues() {
    setUsername("");
    setEmail("");
    setPassword({
      password: "",
      show: false,
    });
    setConfirmPassword({
      confirmPassword: "",
      show: false,
    });
  }

  return (
    <div className="users-pages">
      <h1>Register</h1>
      <form>
        {/* username */}
        <div className="form-group input-group">
          <div className="input-group-prepend">
            <span className="input-group-text">
              <i className="bi bi-person"></i>
            </span>
          </div>
          <input
            name="username"
            className="form-control"
            placeholder="username"
            type="text"
            autoComplete="off"
            value={username}
            onChange={(event) => {
              setUsername(event.target.value);
            }}
          />
        </div>

        {/* email */}
        <div className="form-group input-group">
          <div className="input-group-prepend">
            <span className="input-group-text">
              <i className="bi bi-envelope"></i>
            </span>
          </div>
          <input
            name="email"
            className="form-control"
            placeholder="Email address"
            type="email"
            autoComplete="on"
            value={email}
            onChange={handleEmailChange}
          />
        </div>
        {errors.email && <div className="error-message">{errors.email}</div>}

        {/* password */}
        <div className="form-group input-group">
          <div className="input-group-prepend">
            <span className="input-group-text">
              <i className="bi bi-lock"></i>
            </span>
          </div>
          <input
            name="password"
            className="form-control"
            placeholder="Create password"
            type={password.show ? "text" : "password"}
            autoComplete="off"
            value={password.password}
            onChange={handlePasswordChange}
          />
          <span
            className="show-password-icon"
            onClick={() => {
              setPassword({
                ...password,
                show: !password.show,
              });
            }}
          >
            {password.show ? (
              <i className="bi bi-eye-slash"></i>
            ) : (
              <i className="bi bi-eye"></i>
            )}
          </span>
        </div>

        <div className="form-group input-group">
          <div className="input-group-prepend">
            <span className="input-group-text">
              <i className="bi bi-lock"></i>
            </span>
          </div>
          <input
            name="confirm-password"
            className="form-control"
            placeholder="Confirm password"
            type={confirmPassword.show ? "text" : "password"}
            value={confirmPassword.confirmPassword}
            autoComplete="off"
            onChange={handleconfirmPasswordChange}
          />
          <span
            className="show-password-icon"
            onClick={() => {
              setConfirmPassword({
                ...confirmPassword,
                show: !confirmPassword.show,
              });
            }}
          >
            {confirmPassword.show ? (
              <i className="bi bi-eye-slash"></i>
            ) : (
              <i className="bi bi-eye"></i>
            )}
          </span>
        </div>
        {errors.confirmPassword && (
          <div className="error-message">{errors.confirmPassword}</div>
        )}

        <div className="form-group">
          {username &&
          email &&
          password.password &&
          confirmPassword.confirmPassword &&
          !errors.email &&
          !errors.confirmPassword ? (
            <button
              type="submit"
              className="btn btn-primary btn-block"
              onClick={handleSubmit}
            >
              Create Account
            </button>
          ) : (
            <button
              type="submit"
              className="btn btn-primary btn-block disabled"
            >
              Create Account
            </button>
          )}
        </div>

        {responseMessage.message && (
          <p className={`${responseMessage.status} response-message`}>
            {responseMessage.message}
          </p>
        )}

        <p className="text-center" style={{ marginTop: "45px" }}>
          Have an account? <a href="/login">Log In</a>
        </p>
      </form>
    </div>
  );
}
