import { useCallback, useMemo, useState } from "react";
import { getCurrentUserRequest, loginRequest, logoutRequest } from "@/api/auth.api";
import { normalizeAuthUser, userFromToken } from "@/utils/auth";
import { AuthContext } from "@/context/AuthContextValue";

const TOKEN_KEY = "keystone_token";
const USER_KEY = "keystone_user";

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem(USER_KEY);
    if (stored) {
      try {
        return normalizeAuthUser(JSON.parse(stored));
      } catch {
        localStorage.removeItem(USER_KEY);
      }
    }
    const t = localStorage.getItem(TOKEN_KEY);
    return t ? normalizeAuthUser(userFromToken(t)) : null;
  });

  const login = useCallback(async (credentials) => {
    const { data } = await loginRequest(credentials);
    localStorage.setItem(TOKEN_KEY, data.token);
    const currentUser = await getCurrentUserRequest();
    const nextUser = normalizeAuthUser({
      ...userFromToken(data.token),
      ...currentUser.data,
    });
    localStorage.setItem(USER_KEY, JSON.stringify(nextUser));
    setToken(data.token); setUser(nextUser);
    return data;
  }, []);

  const logout = useCallback(async () => {
    try {
      if (localStorage.getItem(TOKEN_KEY)) await logoutRequest();
    } catch {
      // Local logout should still clear client state if the token is expired.
    }
    localStorage.removeItem(TOKEN_KEY); localStorage.removeItem(USER_KEY);
    setToken(null); setUser(null);
  }, []);

  const value = useMemo(() => ({ token, user, isAuthenticated: Boolean(token && user), login, logout }), [token, user, login, logout]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
