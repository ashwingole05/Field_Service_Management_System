import useAuth from "./useAuth";
import { can } from "@/constants/permissions";
export default function usePermissions() {
  const { user } = useAuth();
  return { can: (permission) => can(user?.role, permission), role: user?.role };
}
