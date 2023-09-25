import { useCallback, useEffect, useState } from "react";

export default function useFetch(url) {
  const [data, setData] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);

  const getData = useCallback(() => {
    fetch(url, {
      method: "get",
      headers: {
        "Content-Type": "application/json",
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
        console.error("An error occurred during the fetch: ", e);
      });
  }, [url]);

  useEffect(() => {
    getData();
  }, [url, getData]);
  return { isLoading, isError, data };
}
