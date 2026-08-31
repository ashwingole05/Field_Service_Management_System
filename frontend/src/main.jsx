import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { ConfigProvider, App as AntApp } from "antd";
import { Toaster } from "sonner";
import { AuthProvider } from "@/context/AuthContext";
import App from "./App";
import "./index.css";
import "./styles/app.css";

const theme = {
  token: {
    colorPrimary: "#6366f1",
    borderRadius: 10,
    fontFamily: "Geist, Inter, ui-sans-serif, system-ui, sans-serif",
    colorBgContainer: "#ffffff",
  },
};

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
      <ConfigProvider theme={theme}>
        <AntApp>
          <AuthProvider>
            <App />
            <Toaster richColors position="top-right" />
          </AuthProvider>
        </AntApp>
      </ConfigProvider>
    </BrowserRouter>
  </StrictMode>
);
