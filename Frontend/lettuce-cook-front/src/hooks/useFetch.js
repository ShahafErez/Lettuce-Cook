import { useCallback, useEffect, useState } from "react";

export default function useFetch(url) {
  const [data, setData] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);

  const getRequest = useCallback(() => {
    const headers = new Headers();
    headers.append("Content-Type", "image/jpeg");

    if (localStorage.getItem("token")) {
      headers.append(
        "Authorization",
        `Bearer ${localStorage.getItem("token")}`
      );
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
  }, [url]);

  useEffect(() => {
    getRequest();
  }, [url, getRequest]);
  return { isLoading, isError, data };
}
