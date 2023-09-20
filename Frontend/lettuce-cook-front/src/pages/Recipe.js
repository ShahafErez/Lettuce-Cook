import React from "react";
import { useParams } from "react-router";

export default function Recipe() {
  const params = useParams();

  return <h2>Recipe {params.id}</h2>;
}
