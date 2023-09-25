import React from "react";

export default function Error(props) {
  return (
    <div className="error">
      <img src="/error.png" alt="error" height="100" />
      Error getting information {props.message}
    </div>
  );
}
