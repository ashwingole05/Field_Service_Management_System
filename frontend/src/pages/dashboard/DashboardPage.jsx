import { useEffect, useState } from "react";
import { getDashboard } from "@/api/dashboard.api";
import PageHeader from "@/components/common/PageHeader";
import LoadingScreen from "@/components/common/LoadingScreen";
import DashboardStats from "@/components/dashboard/DashboardStats";
import WorkOrderChart from "@/components/dashboard/WorkOrderChart";
import ServiceRequestChart from "@/components/dashboard/ServiceRequestChart";
import RecentActivity from "@/components/dashboard/RecentActivity";

export default function DashboardPage() {
  const [data,setData]=useState(null);
  useEffect(()=>{getDashboard().then(r=>setData(r.data))},[]);
  if(!data)return <LoadingScreen label="Loading command center..."/>;
  return <><PageHeader eyebrow="Operations overview" title="Dashboard" description="A live view of field-service workload, customers, inventory and SLA signals."/><DashboardStats data={data}/><div className="dashboard-grid"><WorkOrderChart data={data}/><ServiceRequestChart data={data}/><RecentActivity data={data}/></div></>;
}
