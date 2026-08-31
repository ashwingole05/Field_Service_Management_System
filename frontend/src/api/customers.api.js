import api from "./axios";
const base = "/api/customers";
export const getCustomers = () => api.get(base);
export const getCustomer = (id) => api.get(`${base}/${id}`);
export const createCustomer = (payload) => api.post(base, payload);
export const updateCustomer = (id, payload) => api.put(`${base}/${id}`, payload);
export const deleteCustomer = (id) => api.delete(`${base}/${id}`);
