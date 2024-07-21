import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../services/authService";

export default function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState({
    password: "",
    show: false,
  });
  const [responseMessage, setResponseMessage] = useState({
    message: "",
    status: "",
  });

  async function handleSubmit(event) {
    event.preventDefault();

    const { isLoggedInSuccessfully, response } = await login(
      email,
      password.password
    );

    if (isLoggedInSuccessfully) {
      // navigate to previous page
      navigate(-1);
    } else {
      setResponseMessage({
        message: response,
        status: "failure",
      });
    }
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

        {responseMessage.message && (
          <p className={`${responseMessage.status} response-message`}>
            {responseMessage.message}
          </p>
        )}

        <p className="text-center" style={{ marginTop: "45px" }}>
          Don't have an account? <a href="/register">Register</a>
        </p>
      </form>
    </div>
  );
}
