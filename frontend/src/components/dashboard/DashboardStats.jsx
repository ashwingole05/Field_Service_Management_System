import { ClipboardList, Clock3, CircleCheckBig, Users, MapPinned, PackageSearch } from "lucide-react";
import StatCard from "@/components/common/StatCard";
export default function DashboardStats({ data }) {
  const stats=[["Total work orders",data.totalWorkOrders,ClipboardList,"Across all statuses"],["In progress",data.inProgressWorkOrders,Clock3,"Technicians currently active"],["Closed",data.closedWorkOrders,CircleCheckBig,"Completed lifecycle"],["Customers",data.totalCustomers,Users,"Customer records"],["Sites",data.totalSites,MapPinned,"Service locations"],["Parts",data.totalParts,PackageSearch,"Inventory catalog"]];
  return <div className="stats-grid">{stats.map(([l,v,i,h])=><StatCard key={l} label={l} value={v} icon={i} hint={h}/>)}</div>;
}
