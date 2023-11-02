import { useState } from "react";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState({
    password: "",
    show: false,
  });

  function handleSubmit() {
    console.log("email ", email);
    console.log("password", password.password);
  }

  return (
    <div className="users-pages">
      <h1>Login</h1>
      <form>
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
            onChange={(event) => {
              setEmail(event.target.value);
            }}
          />
        </div>

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
            onChange={(event) => {
              setPassword({ ...password, password: event.target.value });
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

        <div className="form-group">
          {email && password.password ? (
            <button
              type="submit"
              className="btn btn-primary btn-block"
              onClick={handleSubmit}
            >
              Login
            </button>
          ) : (
            <button
              type="submit"
              className="btn btn-primary btn-block disabled"
            >
              Login
            </button>
          )}
        </div>
        <p className="text-center">
          Don't have an account? <a href="/register">Register</a>
        </p>
      </form>
    </div>
  );
}
