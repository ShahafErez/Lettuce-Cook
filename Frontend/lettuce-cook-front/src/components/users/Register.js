import { useState } from "react";

export default function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState(null);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [confirmPasswordError, setConfirmPasswordError] = useState(null);

  function isValidEmail(email) {
    return /\S+@\S+\.\S+/.test(email);
  }

  function handleEmailChange(event) {
    let currentEmail = event.target.value;
    if (!isValidEmail(currentEmail)) {
      setEmailError("Email is invalid");
    } else {
      setEmailError(null);
    }
    setEmail(currentEmail);
  }

  function handleconfirmPasswordChange(event) {
    let currentConfirmPassword = event.target.value;
    if (currentConfirmPassword === password) {
      setConfirmPasswordError(null);
    } else {
      setConfirmPasswordError("Password confirmation does not match");
    }
    setConfirmPassword(currentConfirmPassword);
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
            onChange={(e) => {
              setUsername(e.target.value);
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
        {emailError && <div className="error-message">{emailError}</div>}

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
            type="password"
            autoComplete="off"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
            }}
          />
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
            type="password"
            value={confirmPassword}
            autoComplete="off"
            onChange={handleconfirmPasswordChange}
          />
        </div>
        {confirmPasswordError && (
          <div className="error-message">{confirmPasswordError}</div>
        )}

        <div className="form-group">
          {username &&
          email &&
          password &&
          confirmPassword &&
          !emailError &&
          !confirmPasswordError ? (
            <button type="submit" className="btn btn-primary btn-block">
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
        <p className="text-center">
          Have an account? <a href="/login">Log In</a>
        </p>
      </form>
    </div>
  );
}
