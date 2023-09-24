import React from "react";
import { useNavigate } from "react-router-dom";
import Diets from "../recipe/Diets";

export default function RecipePreview(props) {
  const recipe = props.recipe;
  const navigate = useNavigate();

  let { id, name, pictureUrl } = recipe;

  return (
    <section
      className="card preview-card"
      onClick={() => {
        console.log("id ", id);
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
