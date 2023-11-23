import React, { useState } from "react";
import RecipesList from "../components/recipe/RecipesList";

export default function Home() {
  let numOfRecipes = global.recipesNumberHomePage;
  let categoriesList = global.categoriesList;
  const [category, setCategory] = useState(categoriesList[0]);

  return (
    <React.Fragment>
      <img src="/images/logo.png" alt="logo" height="200" />

      <h2 className="recipes-group-title">Explore Recipes</h2>
      <RecipesList numOfRecipes={numOfRecipes} random="true" />

      <h2 className="recipes-group-title">Newest</h2>
      <RecipesList numOfRecipes={numOfRecipes} random="false" />

      <div className="recipes-list-category">
        <h2 className="recipes-group-title">{category}</h2>
        {/* Select different category */}
        <div className="dropdown">
          <button type="button" data-bs-toggle="dropdown" aria-expanded="false">
            <i className="bi bi-arrow-left-right" title="change category"></i>
          </button>
          <ul className="dropdown-menu">
            {categoriesList.map((category, index) => (
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
        <a className="nav-link" href={`/recipes/${category}`}>
          All {category.toLowerCase()} recipes...
        </a>
      </div>

      <RecipesList
        numOfRecipes={numOfRecipes}
        category={category}
        random="true"
      />
    </React.Fragment>
  );
}
