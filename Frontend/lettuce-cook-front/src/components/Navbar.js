import React from "react";
import { logout } from "../services/authService";

export default function Navbar() {
  const username = localStorage.getItem("username");

  return (
    <nav id="navbar" className="navbar navbar-expand-lg bg-secondary">
      <div className="container">
        <a className="navbar-brand" href="/">
          <img src="/images/navbar-logo.png" alt="logo" height="40" />
        </a>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav">
            <li className="nav-item">
              <a className="nav-link" href="/about">
                About
              </a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/search">
                Search
              </a>
            </li>
            {username && (
              <li className="nav-item">
                <a className="nav-link" href="/favorites">
                  Favorites
                </a>
              </li>
            )}

            {username ? (
              <span className="navbar-users">
                <li className="nav-item">
                  <p className="nav-link">
                    hello <strong>{username}</strong>
                  </p>
                </li>
                <li className="nav-item">
                  <p onClick={logout} id="logout" className="nav-link">
                    logout
                  </p>
                </li>
              </span>
            ) : (
              <span className="navbar-users">
                <li className="nav-item">
                  <a className="nav-link" href="/login">
                    Login
                  </a>
                </li>
                <li className="nav-item">
                  <a className="nav-link" href="/register">
                    Register
                  </a>
                </li>
              </span>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}
