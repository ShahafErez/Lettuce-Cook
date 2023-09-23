import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import Diets from "../components/Diets";
import { RecipeContext } from "../pages/Home";

export default function RecipePreview() {
  const recipe = useContext(RecipeContext);
  const navigate = useNavigate();

  let { id, name, pictureUrl } = recipe;

  return (
    <section
      className="card preview-card"
      onClick={() => {
        navigate(`/recipe/${id}`);
      }}
    >
      <img src={pictureUrl} className="card-img-top" alt={name} />
      <div className="card-body">
        <h5 className="card-title">{name}</h5>
        <Diets recipe={recipe} symbolSize="40px" />
      </div>
    </section>
  );
}
