import { useCallback, useEffect, useState } from "react";

export default function useFetch(url) {
  const [data, setData] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);

  const getRequest = useCallback(() => {
    fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
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
  }, [url]);

  useEffect(() => {
    getRequest();
  }, [url, getRequest]);
  return { isLoading, isError, data };
}
