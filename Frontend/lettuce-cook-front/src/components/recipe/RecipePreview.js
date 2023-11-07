import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Diets from "../recipe/Diets";

export default function RecipePreview(props) {
  const recipe = props.recipeInfo.recipe;
  const isFavoriteByUser = props.recipeInfo.isFavoriteByUser;

  const navigate = useNavigate();

  const { id, name, pictureData } = recipe;
  const [imageUrl, setImageUrl] = useState(null);

  useEffect(() => {
    if (pictureData) {
      // Use the decoded base64 data directly to create the data URL
      const dataUrl = `data:image/jpeg;base64,${pictureData}`;
      setImageUrl(dataUrl);
    }
  }, [pictureData]);

  return (
    <section
      className="card preview-card"
      onClick={() => {
        navigate(`/recipe/${id}`);
      }}
    >
      <div className="image-container">
        {imageUrl && <img src={imageUrl} className="card-img-top" alt={name} />}
        <div className="favorite">
          {isFavoriteByUser && <i className="bi bi-suit-heart-fill"></i>}
        </div>
      </div>
      <div className="card-body">
        <h5 className="card-title">{name}</h5>
        <Diets recipe={recipe} symbolSize="40px" />
      </div>
    </section>
  );
}
