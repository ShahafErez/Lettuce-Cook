import React from "react";
import ReactDom from "react-dom";

function Greeting() {
  return <h4>Hello</h4>;
}

ReactDom.render(<Greeting />, document.getElementById("root"));
