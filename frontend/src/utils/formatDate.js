import { format } from "date-fns";
export const formatDate = (value, fallback = "—") => {
  if (!value) return fallback;
  try { return format(new Date(value), "dd MMM yyyy, hh:mm a"); } catch { return fallback; }
};
