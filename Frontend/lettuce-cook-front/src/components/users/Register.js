import { useState } from "react";

export default function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState({ email: "", error: null });
  const [password, setPassword] = useState({
    password: "",
    show: false,
  });
  const [confirmPassword, setConfirmPassword] = useState({
    confirmPassword: "",
    error: null,
    show: false,
  });

  function isValidEmail(email) {
    return /\S+@\S+\.\S+/.test(email);
  }

  function handleEmailChange(event) {
    let currentEmail = event.target.value;
    if (isValidEmail(currentEmail)) {
      setEmail({ ...email, error: null });
    } else {
      setEmail({ ...email, error: "Email is invalid" });
    }
    setEmail({ ...email, email: currentEmail });
  }

  function handleconfirmPasswordChange(event) {
    let currentConfirmPassword = event.target.value;
    if (currentConfirmPassword === password) {
      setConfirmPassword({ ...confirmPassword, error: null });
    } else {
      setConfirmPassword({
        ...confirmPassword,
        error: "Password confirmation does not match",
      });
    }
    setConfirmPassword({
      ...confirmPassword,
      confirmPassword: currentConfirmPassword,
    });
  }

  function handleSubmit(event) {
    event.preventDefault();
    console.log("username ", username);
    console.log("email ", email.email);
    console.log("password ", password.password);
    console.log("confirm password ", confirmPassword.confirmPassword);
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
            value={email.email}
            onChange={handleEmailChange}
          />
        </div>
        {email.error && <div className="error-message">{email.error}</div>}

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
            onChange={(e) => {
              setPassword({ ...password, password: e.target.value });
            }}
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
        {confirmPassword.error && (
          <div className="error-message">{confirmPassword.error}</div>
        )}

        <div className="form-group">
          {username &&
          email.email &&
          password.password &&
          confirmPassword.confirmPassword &&
          !email.error &&
          !confirmPassword.error ? (
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
        <p className="text-center">
          Have an account? <a href="/login">Log In</a>
        </p>
      </form>
    </div>
  );
}
