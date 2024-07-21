import React, { useState } from "react";
import Error from "../components/Error";
import Loading from "../components/Loading";
import RecipePreview from "../components/recipe/RecipePreview";
import { search } from "../services/searchService";

export default function Search() {
  const [category, setCategory] = useState();
  const [searchTerm, setSearchTerm] = useState("");

  const [searchResults, setSearchResults] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  async function handleSubmit() {
    setIsLoading(true);
    let data = await search(searchTerm, category);
    setIsLoading(false);
    if (data === -1) {
      setIsError(true);
    } else {
      setSearchResults(data);
    }
  }

  return (
    <div className="container" id="search-page">
      <h2>Search</h2>
      <div className="input-group" id="search-bar">
        <div className="dropdown">
          <button
            className="btn btn-primary dropdown-toggle"
            type="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            {category || "All Categories"}
          </button>
          <ul className="dropdown-menu">
            <li
              className="dropdown-item"
              onClick={() => {
                setCategory();
              }}
            >
              All Categories
            </li>
            {global.categoriesList.map((category, index) => (
              <li
                key={index}
                className="dropdown-item"
                onClick={() => {
                  setCategory(category);
                }}
              >
                {category}
              </li>
            ))}
          </ul>
        </div>

        <input
          type="search"
          placeholder="Search by free text"
          className="form-control "
          aria-label="Search"
          aria-describedby="search-addon"
          onChange={(event) => {
            setSearchTerm(event.target.value);
          }}
        />
        <button
          type="button"
          className="btn btn-outline-primary"
          data-mdb-ripple-init
          onClick={handleSubmit}
        >
          <i className="bi bi-search"></i>
        </button>
      </div>

      {isLoading && <Loading />}

      {isError && <Error />}

      {searchResults && (
        <div className="row row-cols-1 row-cols-md-4 g-4">
          {searchResults.map((recipeInfo) => (
            <div className="col" key={recipeInfo.id}>
              <RecipePreview recipe={recipeInfo} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
