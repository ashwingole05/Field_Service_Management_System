import { AlertTriangle, CheckCircle2, Clock3 } from "lucide-react";
export default function RecentActivity({ data }) {
  const items=[{icon:Clock3,label:`${data.inProgressWorkOrders||0} work orders are in progress`,tone:"info"},{icon:AlertTriangle,label:`${data.overdueWorkOrders||0} work orders are overdue`,tone:"warn"},{icon:CheckCircle2,label:`${data.closedWorkOrders||0} work orders are closed`,tone:"ok"}];
  return <div className="panel"><div className="panel-heading"><div><span>Live overview</span><h3>Operational signals</h3></div></div><div className="activity-list">{items.map(({icon:Icon,label,tone})=><div className={`activity-item ${tone}`} key={label}><Icon size={18}/><span>{label}</span></div>)}</div></div>;
}
