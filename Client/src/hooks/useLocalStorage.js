import { useEffect, useState } from "react";

export function useLocalStorage(key) {
  const [value, setValue] = useState(localStorage.getItem(key));

  useEffect(() => {
    setValue(localStorage.getItem(key));
    const handleStorageChange = () => {
      setValue(localStorage.getItem(key));
    };
    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, [key]);

  return value;
}
