import api from "./axios";
export const getOverdueWorkOrders = () => api.get("/api/sla/overdue");
export const getOverdueCount = () => api.get("/api/sla/overdue/count");
