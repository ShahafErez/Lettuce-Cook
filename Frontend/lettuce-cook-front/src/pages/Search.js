import React, { useState } from "react";
import Error from "../components/Error";
import Loading from "../components/Loading";
import RecipePreview from "../components/recipe/RecipePreview";
import { search } from "../services/searchService";

export default function Search() {
  const [searchResults, setSearchResults] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  async function handleSubmit() {
    setIsLoading(true);
    let data = await search();
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
      <div class="input-group" id="search-bar">
        <input
          type="search"
          placeholder="Search by free text"
          class="form-control "
          aria-label="Search"
          aria-describedby="search-addon"
        />
        <button
          type="button"
          class="btn btn-outline-primary"
          data-mdb-ripple-init
          onClick={handleSubmit}
        >
          <i class="bi bi-search"></i>
        </button>
      </div>

      {isLoading && <Loading />}

      {isError && <Error />}

      {searchResults && (
        <div className="row row-cols-1 row-cols-md-4 g-4">
          {searchResults.map((recipeInfo) => (
            <div className="col" key={recipeInfo.recipe.id}>
              <RecipePreview
                recipe={recipeInfo.recipe}
                isFavoriteByUser={recipeInfo.isFavoriteByUser}
              />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
