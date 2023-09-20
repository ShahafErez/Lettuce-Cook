import React from "react";

export default function RecipePreview(props) {
  let { title, vegeterian, vegan, glutenFree, dairyFree, pictureUrl } = props;

  return (
    <section className="card preview-card">
      <img src={pictureUrl} className="card-img-top" alt={title} />
      <div className="card-body">
        <h5 className="card-title">{title}</h5>
        <div className="diet">
          {vegeterian && (
            <img src="vegetarian.png" alt="vegetarian" height="40" />
          )}
          {vegan && <img src="vegan.png" alt="vegan" height="40" />}
          {glutenFree && (
            <img src="gluten-free.png" alt="gluten-free" height="40" />
          )}
          {dairyFree && (
            <img src="dairy-free.png" alt="dairy-free" height="40" />
          )}
        </div>
      </div>
    </section>
  );
}
