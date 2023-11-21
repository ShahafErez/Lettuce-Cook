import React, { useState } from "react";
import RecipesList from "../components/recipe/RecipesList";

export default function Home() {
  let categoriesList = ["Dinner", "Lunch", "Salad", "Snack", "Dessert"];
  const [category, setCategory] = useState(categoriesList[0]);

  return (
    <React.Fragment>
      <img src="/images/logo.png" alt="logo" height="200" />

      <h2 className="recipes-group-title">Explore Recipes</h2>
      <RecipesList random="true" />

      <h2 className="recipes-group-title">Newest</h2>
      <RecipesList random="false" />

      <div className="recipes-list-category">
        <h2 className="recipes-group-title">{category}</h2>
        {/* Select different category */}
        <div class="dropdown">
          <button type="button" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-arrow-left-right"></i>
          </button>
          <ul class="dropdown-menu">
            {categoriesList.map((category, index) => (
              <li
                key={index}
                class="dropdown-item"
                onClick={() => {
                  setCategory(category);
                }}
              >
                {category}
              </li>
            ))}
          </ul>
        </div>
      </div>

      <RecipesList category={category} random="true" />
    </React.Fragment>
  );
}
