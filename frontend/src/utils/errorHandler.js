export function getErrorMessage(error, fallback = "Something went wrong") {
  const data = error?.response?.data;
  if (typeof data === "string") return data;
  if (data?.message) return data.message;
  if (data?.errors) return Object.values(data.errors).join(", ");
  return error?.message || fallback;
}
