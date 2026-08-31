import { useCallback, useState } from "react";
import { getErrorMessage } from "@/utils/errorHandler";
export default function useApi() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const run = useCallback(async (request) => {
    setLoading(true); setError("");
    try { return await request(); }
    catch (e) { setError(getErrorMessage(e)); throw e; }
    finally { setLoading(false); }
  }, []);
  return { loading, error, run, setError };
}
