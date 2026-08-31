import api from "./axios";

const base = "/api/service-requests";

// MANAGER / DISPATCHER
export const getServiceRequests = () =>
    api.get(base);

export const getServiceRequest = (id) =>
    api.get(`${base}/${id}`);

export const getServiceRequestsByCustomer = (customerId) =>
    api.get(`${base}/customer/${customerId}`);

export const getServiceRequestsByStatus = (status) =>
    api.get(`${base}/status/${status}`);

// CUSTOMER - OWN REQUESTS
export const getMyServiceRequests = () =>
    api.get(`${base}/mine`);

// CUSTOMER - CREATE
export const createServiceRequest = (payload) =>
    api.post(base, payload);

// MANAGER / DISPATCHER
export const reviewServiceRequest = (id) =>
    api.put(`${base}/${id}/review`);

export const convertServiceRequest = (id) =>
    api.post(`${base}/${id}/convert`);

export const closeServiceRequest = (id) =>
    api.put(`${base}/${id}/close`);

export const cancelServiceRequest = (id) =>
    api.put(`${base}/${id}/cancel`);