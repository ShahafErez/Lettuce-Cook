import { useCallback, useEffect, useState } from "react";
import { useLocalStorage } from "./useLocalStorage";

export default function useFetch(url) {
  const [data, setData] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);
  const token = useLocalStorage("token");

  const getRequest = useCallback(() => {
    const headers = new Headers();
    headers.append("Content-Type", "application/json");

    if (token) {
      headers.append("Authorization", `Bearer ${token}`);
    }

    fetch(url, {
      method: "GET",
      headers: headers,
    })
      .then((res) => {
        if (res.status < 200 || res.status >= 300) {
          setIsLoading(false);
          setIsError(true);
        } else {
          return res.json();
        }
      })
      .then((json) => {
        setData(json);
        setIsLoading(false);
      })
      .catch((e) => {
        setIsLoading(false);
        setIsError(true);
        console.error("An error occurred during get request: ", e);
      });
  }, [url, token]);

  useEffect(() => {
    getRequest();
  }, [url, getRequest]);
  return { isLoading, isError, data };
}
