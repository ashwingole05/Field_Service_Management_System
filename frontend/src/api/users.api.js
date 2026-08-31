import api from "./axios";

const base = "/api/user_auth";

export const getTechnicians = () =>
  api.get(`${base}/technicians`);

export const getStaffUsers = () =>
  api.get(`${base}/staff`);

export const createStaffUser = (payload) =>
  api.post(`${base}/staff`, payload);
