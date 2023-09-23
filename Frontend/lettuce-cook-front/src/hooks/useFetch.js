import { useCallback, useEffect, useState } from "react";

export default function useFetch(url) {
  const [data, setData] = useState([]);

  const getData = useCallback(() => {
    console.log("fetching ", url);
    fetch(url, {
      method: "get",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        console.log("res ", res);
        if (res.status < 200 || res.status >= 300) {
          console.log("error ", res);
        } else {
          return res.json();
        }
      })
      .then((json) => {
        setData(json);
      });
  }, [url]);

  useEffect(() => {
    getData();
  }, [url, getData]);
  return { data: data };
}
