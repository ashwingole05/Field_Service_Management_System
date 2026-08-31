import { ResponsiveContainer, BarChart, Bar, CartesianGrid, XAxis, YAxis, Tooltip } from "recharts";
export default function WorkOrderChart({ data }) {
  const chart=[{name:"Open",value:data.openWorkOrders||0},{name:"Assigned",value:data.assignedWorkOrders||0},{name:"In progress",value:data.inProgressWorkOrders||0},{name:"Completed",value:data.completedWorkOrders||0},{name:"Closed",value:data.closedWorkOrders||0}];
  return <div className="panel"><div className="panel-heading"><div><span>Operations</span><h3>Work order pipeline</h3></div></div><div className="chart-wrap"><ResponsiveContainer width="100%" height="100%"><BarChart data={chart}><CartesianGrid strokeDasharray="3 3" vertical={false}/><XAxis dataKey="name" axisLine={false} tickLine={false}/><YAxis allowDecimals={false} axisLine={false} tickLine={false}/><Tooltip cursor={{fill:"rgba(99,102,241,.06)"}}/><Bar dataKey="value" fill="#6366f1" radius={[8,8,0,0]}/></BarChart></ResponsiveContainer></div></div>;
}
