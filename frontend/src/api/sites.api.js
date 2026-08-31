import api from "./axios";

const base = "/api/sites";

export const getSites = () =>
    api.get(base);

export const getSite = (id) =>
    api.get(`${base}/${id}`);

export const createSite = (payload) =>
    api.post(base, payload);

export const updateSite = (id, payload) =>
    api.put(`${base}/${id}`, payload);

export const deleteSite = (id) =>
    api.delete(`${base}/${id}`);

// CUSTOMER - GET ONLY LOGGED-IN CUSTOMER'S SITES
export const getMySites = () =>
    api.get(`${base}/mine`);

// MANAGER / DISPATCHER - GET SITES FOR A CUSTOMER
export const getSitesByCustomer = (customerId) =>
    api.get(`${base}/customer/${customerId}`);