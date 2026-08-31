import api from "./axios";
const base = "/api/parts";
export const getParts = () => api.get(base);
export const getPart = (id) => api.get(`${base}/${id}`);
export const createPart = (payload) => api.post(base, payload);
export const updatePart = (id, payload) => api.put(`${base}/${id}`, payload);
export const deletePart = (id) => api.delete(`${base}/${id}`);
export const addStock = (id, quantity) => api.put(`${base}/${id}/stock/${quantity}`);
export const usePart = (payload) => api.post("/api/workorder-parts/use", payload);
export const getPartUsage = () => api.get("/api/workorder-parts");
export const getWorkOrderPartUsage = (workOrderId) =>
  api.get(`/api/workorder-parts/workorder/${workOrderId}`);
