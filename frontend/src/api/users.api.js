import api from "./axios";

const base = "/api/user_auth";

export const getTechnicians = () =>
  api.get(`${base}/technicians`);
