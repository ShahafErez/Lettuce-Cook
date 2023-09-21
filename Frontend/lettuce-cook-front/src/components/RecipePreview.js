import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { RecipeContext } from "../pages/Home";

export default function RecipePreview() {
  const recipe = useContext(RecipeContext);
  const navigate = useNavigate();

  let { id, title, vegeterian, vegan, glutenFree, dairyFree, pictureUrl } =
    recipe;

  return (
    <section
      className="card preview-card"
      onClick={() => {
        navigate(`/recipe/${id}`);
      }}
    >
      <img src={pictureUrl} className="card-img-top" alt={title} />
      <div className="card-body">
        <h5 className="card-title">{title}</h5>
        <div className="diet">
          {vegeterian && (
            <img
              src="vegetarian.png"
              alt="vegetarian"
              title="vegetarian"
              height="40"
            />
          )}
          {vegan && (
            <img src="vegan.png" alt="vegan" title="vegan" height="40" />
          )}
          {glutenFree && (
            <img
              src="gluten-free.png"
              alt="gluten-free"
              title="gluten free"
              height="40"
            />
          )}
          {dairyFree && (
            <img
              src="dairy-free.png"
              alt="dairy-free"
              title="dairy free"
              height="40"
            />
          )}
        </div>
      </div>
    </section>
  );
}
