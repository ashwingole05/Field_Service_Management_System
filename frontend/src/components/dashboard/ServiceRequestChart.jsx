import { ResponsiveContainer, PieChart, Pie, Cell, Tooltip, Legend } from "recharts";
const COLORS=["#6366f1","#8b5cf6","#14b8a6"];
export default function ServiceRequestChart({ data }) {
  const total=data.totalServiceRequests||0,open=data.openServiceRequests||0,review=data.inReviewServiceRequests||0,other=Math.max(total-open-review,0);
  const chart=[{name:"Open",value:open},{name:"In review",value:review},{name:"Other",value:other}].filter(x=>x.value>0);
  return <div className="panel"><div className="panel-heading"><div><span>Customer care</span><h3>Service request mix</h3></div></div><div className="chart-wrap">{chart.length===0?<div className="chart-empty">No service-request data yet.</div>:<ResponsiveContainer width="100%" height="100%"><PieChart><Pie data={chart} dataKey="value" nameKey="name" innerRadius={55} outerRadius={82} paddingAngle={5}>{chart.map((_,i)=><Cell key={i} fill={COLORS[i%COLORS.length]}/>)}</Pie><Tooltip/><Legend/></PieChart></ResponsiveContainer>}</div></div>;
}
