import api from "./axios";

export const loginRequest = (payload) =>
    api.post("/api/user_auth/login", payload);

export const registerRequest = (payload) =>
    api.post("/api/user_auth/register", payload);

export const logoutRequest = () =>
    api.post("/api/user_auth/logout");

export const getCurrentUserRequest = () =>
    api.get("/api/user_auth/me");

export const forgotPasswordRequest = (userEmail) =>
    api.post(
        "/api/user_auth/forgetPassword",
        null,
        {
            params: {
                userEmail,
            },
        }
    );

export const resetPasswordRequest = (
    token,
    newPassword
) =>
    api.post(
        "/api/user_auth/resetPassword",
        null,
        {
            params: {
                token,
                newPassword,
            },
        }
    );
