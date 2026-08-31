import api from "./axios";
const base = "/api/time-logs";
export const getTimeLogs = () => api.get(base);
export const getTimeLogsByWorkOrder = (workOrderId) => api.get(`${base}/workorder/${workOrderId}`);
export const getTimeLogsByTechnician = (technicianId) => api.get(`${base}/technician/${technicianId}`);
export const startTimeLog = (payload) => api.post(`${base}/start`, payload);
export const stopTimeLog = (id) => api.put(`${base}/${id}/stop`);
