import { Spin } from "antd";
export default function LoadingScreen({ label = "Loading..." }) {
  return <div className="loading-screen"><Spin size="large" /><span>{label}</span></div>;
}
