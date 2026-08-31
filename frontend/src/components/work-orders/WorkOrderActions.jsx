import { Button, Space } from "antd";
export default function WorkOrderActions({workOrder,role,onAction,onAssign}) {
  const s=workOrder.status;
  const canManage=["MANAGER","DISPATCHER"].includes(role);
  const canRunWork=["MANAGER","TECHNICIAN"].includes(role);
  return <Space wrap>
    {canManage&&!["CLOSED","CANCELLED"].includes(s)&&<Button onClick={onAssign}>Assign technician</Button>}
    {canRunWork&&s==="ASSIGNED"&&<Button onClick={()=>onAction("accept")}>Accept</Button>}
    {canRunWork&&["ASSIGNED","ACCEPTED"].includes(s)&&<Button type="primary" onClick={()=>onAction("start")}>Start</Button>}
    {canRunWork&&s==="IN_PROGRESS"&&<Button onClick={()=>onAction("hold")}>Hold</Button>}
    {canRunWork&&s==="ON_HOLD"&&<Button onClick={()=>onAction("resume")}>Resume</Button>}
    {canRunWork&&s==="IN_PROGRESS"&&<Button type="primary" onClick={()=>onAction("complete")}>Complete</Button>}
    {canManage&&!["COMPLETED","CLOSED","CANCELLED"].includes(s)&&<Button danger onClick={()=>onAction("cancel")}>Cancel</Button>}
    {canManage&&s==="COMPLETED"&&<Button type="primary" onClick={()=>onAction("close")}>Close</Button>}
  </Space>;
}
