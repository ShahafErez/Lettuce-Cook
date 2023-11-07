import React from "react";

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

            {username ? (
              <span className="navbar-users">
                <li className="nav-item">
                  <p>hello {username}</p>
                </li>
                <li className="nav-item">
                  <p>logout</p>
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
