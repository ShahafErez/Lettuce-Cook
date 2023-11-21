import React from "react";
import { useLocalStorage } from "../hooks/useLocalStorage";
import { logout } from "../services/authService";

export default function Navbar() {
  const username = useLocalStorage("username");
  let categoriesList = ["dinner", "lunch", "salad", "snack", "dessert"];

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

            <li className="nav-item dropdown">
              <div
                className="nav-link dropdown-toggle"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Recipes
              </div>
              <ul className="dropdown-menu">
                <li className="dropdown-item">
                  <a className="nav-link" href="/recipes/all">
                    All Recipes
                  </a>
                </li>
                {categoriesList.map((category, index) => (
                  <li key={index} className="dropdown-item">
                    <a className="nav-link" href={`/recipes/${category}`}>
                      {category}
                    </a>
                  </li>
                ))}
              </ul>
            </li>

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
