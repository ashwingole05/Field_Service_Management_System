import {
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import AuthLayout from "@/layouts/AuthLayout";
import AppLayout from "@/layouts/AppLayout";

import ProtectedRoute
  from "./ProtectedRoute";

import PermissionRoute
  from "./PermissionRoute";


import LoginPage
  from "@/pages/auth/LoginPage";

import RegisterPage
  from "@/pages/auth/RegisterPage";

import ForgotPasswordPage
  from "@/pages/auth/ForgotPasswordPage";

import ResetPasswordPage
  from "@/pages/auth/ResetPasswordPage";


import DashboardPage
  from "@/pages/dashboard/DashboardPage";

import CustomersPage
  from "@/pages/customers/CustomersPage";

import SitesPage
  from "@/pages/sites/SitesPage";

import WorkOrdersPage
  from "@/pages/work-orders/WorkOrdersPage";

import WorkOrderDetailsPage
  from "@/pages/work-orders/WorkOrderDetailsPage";

import ServiceRequestsPage
  from "@/pages/service-requests/ServiceRequestsPage";

import PartsPage
  from "@/pages/parts/PartsPage";

import TimeLogsPage
  from "@/pages/time-logs/TimeLogsPage";

import SLAPage
  from "@/pages/sla/SLAPage";

import ForbiddenPage
  from "@/pages/errors/ForbiddenPage";

import NotFoundPage
  from "@/pages/errors/NotFoundPage";

import useAuth
  from "@/hooks/useAuth";


function HomeRedirect() {

  const { user } = useAuth();

  if (user?.role === "CUSTOMER") {

    return (
      <Navigate
        to="/service-requests"
        replace
      />
    );

  }

  if (user?.role === "TECHNICIAN") {

    return (
      <Navigate
        to="/work-orders"
        replace
      />
    );

  }

  return <DashboardPage />;
}


export default function AppRoutes() {

  return (

    <Routes>

      {/* =========================
          PUBLIC AUTH ROUTES
      ========================== */}

      <Route
        element={<AuthLayout />}
      >

        <Route
          path="/login"
          element={<LoginPage />}
        />

        <Route
          path="/register"
          element={<RegisterPage />}
        />

        <Route
          path="/forgot-password"
          element={
            <ForgotPasswordPage />
          }
        />

        <Route
          path="/reset-password"
          element={
            <ResetPasswordPage />
          }
        />

      </Route>


      {/* =========================
          AUTHENTICATED ROUTES
      ========================== */}

      <Route
        element={<ProtectedRoute />}
      >

        <Route
          element={<AppLayout />}
        >

          <Route
            index
            element={
              <HomeRedirect />
            }
          />


          {/* =====================
              CUSTOMERS
          ====================== */}

          <Route
            element={
              <PermissionRoute
                roles={[
                  "MANAGER",
                  "DISPATCHER",
                ]}
              />
            }
          >

            <Route
              path="/customers"
              element={
                <CustomersPage />
              }
            />

          </Route>


          {/* =====================
              SITES
          ====================== */}

          <Route
            element={
              <PermissionRoute
                roles={[
                  "MANAGER",
                  "DISPATCHER",
                  "CUSTOMER",
                ]}
              />
            }
          >

            <Route
              path="/sites"
              element={
                <SitesPage />
              }
            />

          </Route>


          {/* =====================
              WORK ORDERS
          ====================== */}

          <Route
            element={
              <PermissionRoute
                roles={[
                  "MANAGER",
                  "DISPATCHER",
                  "TECHNICIAN",
                ]}
              />
            }
          >

            <Route
              path="/work-orders"
              element={
                <WorkOrdersPage />
              }
            />

            <Route
              path="/work-orders/:id"
              element={
                <WorkOrderDetailsPage />
              }
            />

          </Route>


          {/* =====================
              SERVICE REQUESTS
          ====================== */}

          <Route
            element={
              <PermissionRoute
                roles={[
                  "MANAGER",
                  "DISPATCHER",
                  "CUSTOMER",
                ]}
              />
            }
          >

            <Route
              path="/service-requests"
              element={
                <ServiceRequestsPage />
              }
            />

          </Route>


          {/* =====================
              PARTS / TIME LOGS
          ====================== */}

          <Route
            element={
              <PermissionRoute
                roles={[
                  "MANAGER",
                  "TECHNICIAN",
                ]}
              />
            }
          >

            <Route
              path="/parts"
              element={
                <PartsPage />
              }
            />

            <Route
              path="/time-logs"
              element={
                <TimeLogsPage />
              }
            />

          </Route>


          {/* =====================
              MANAGER ONLY
          ====================== */}

          <Route
            element={
              <PermissionRoute
                roles={[
                  "MANAGER",
                ]}
              />
            }
          >

            <Route
              path="/sla"
              element={
                <SLAPage />
              }
            />

          </Route>


          <Route
            path="/forbidden"
            element={
              <ForbiddenPage />
            }
          />

        </Route>

      </Route>


      {/* =========================
          404
      ========================== */}

      <Route
        path="*"
        element={<NotFoundPage />}
      />

    </Routes>
  );
}
