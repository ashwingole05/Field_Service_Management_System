import { Navigate, Outlet } from "react-router-dom";
import useAuth from "@/hooks/useAuth";
export default function PermissionRoute({roles}){const{user}=useAuth();return roles.includes(user?.role)?<Outlet/>:<Navigate to="/forbidden" replace/>}
