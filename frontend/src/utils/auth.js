export function decodeJwt(token) {
  try {
    const payload = token.split(".")[1];
    return JSON.parse(atob(payload.replace(/-/g, "+").replace(/_/g, "/")));
  } catch { return null; }
}
export function userFromToken(token) {
  const payload = decodeJwt(token);
  if (!payload) return null;
  return { email: payload.sub, role: payload.role, expiresAt: payload.exp ? payload.exp * 1000 : null };
}

export function normalizeAuthUser(user) {
  if (!user) return null;
  return {
    id: user.id ?? null,
    userName: user.userName ?? user.name ?? null,
    userEmail: user.userEmail ?? user.email ?? null,
    email: user.userEmail ?? user.email ?? null,
    phone: user.phone ?? null,
    role: user.role ?? null,
    customerId: user.customerId ?? null,
    expiresAt: user.expiresAt ?? null,
  };
}
