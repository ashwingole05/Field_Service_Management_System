import { useEffect,useState } from "react";
import { Table } from "antd";
import { ShieldAlert } from "lucide-react";
import PageHeader from "@/components/common/PageHeader";
import StatCard from "@/components/common/StatCard";
import StatusBadge from "@/components/common/StatusBadge";
import { getOverdueCount,getOverdueWorkOrders } from "@/api/sla.api";
import { formatDate } from "@/utils/formatDate";

export default function SLAPage() {
  const [rows,setRows]=useState([]),[count,setCount]=useState(0),[loading,setLoading]=useState(true);
  useEffect(()=>{Promise.all([getOverdueWorkOrders(),getOverdueCount()]).then(([a,b])=>{setRows(a.data||[]);setCount(b.data?.overdueWorkOrders||0)}).finally(()=>setLoading(false))},[]);
  const columns=[{title:"WO #",dataIndex:"workOrderNumber"},{title:"Title",dataIndex:"title"},{title:"Priority",dataIndex:"priority",render:v=><StatusBadge value={v}/>},{title:"Status",dataIndex:"status",render:v=><StatusBadge value={v}/>},{title:"Scheduled",dataIndex:"scheduledAt",render:formatDate}];
  return <><PageHeader eyebrow="SLA intelligence" title="Overdue work orders" description="Identify scheduled jobs that require immediate operational attention."/><div className="sla-stat"><StatCard label="Currently overdue" value={count} icon={ShieldAlert} hint="Excludes completed, closed and cancelled work"/></div><div className="panel table-panel"><Table rowKey="id" columns={columns} dataSource={rows} loading={loading}/></div></>;
}
