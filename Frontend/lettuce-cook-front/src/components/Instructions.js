import React from "react";

export default function Instructions(props) {
  let instructions = props.instructions;

  return (
    <React.Fragment>
      <h2>Instructions</h2>
      <div className="instructions">
        {instructions.map((instruction) => (
          <div className="instruction" key={instruction.id}>
            <p className="step">step {instruction.id}:</p>
            <p>{instruction.description}</p>
          </div>
        ))}
      </div>
    </React.Fragment>
  );
}
