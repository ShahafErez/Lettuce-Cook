import React from "react";

export default function Instructions(props) {
  let instructions = props.instructions;
  // sort by index
  instructions.sort((a, b) => (a.index > b.index ? 1 : -1));

  return (
    <React.Fragment>
      <h2>Instructions</h2>
      <div className="instructions">
        {instructions.map((instruction) => (
          <div key={instruction.id}>
            <p className="step">step {instruction.index}</p>
            <p>{instruction.description}</p>
          </div>
        ))}
      </div>
    </React.Fragment>
  );
}
