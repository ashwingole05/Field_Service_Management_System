import { Button, Space } from "antd";
export default function ServiceRequestActions({row,role,onAction}) {
  if(!["MANAGER","DISPATCHER"].includes(role))return null;
  return <Space wrap>
    {row.status==="OPEN"&&<Button onClick={()=>onAction(row,"review")}>Review</Button>}
    {["OPEN","IN_REVIEW"].includes(row.status)&&<Button type="primary" onClick={()=>onAction(row,"convert")}>Convert</Button>}
    {!["CLOSED","CANCELLED"].includes(row.status)&&<Button onClick={()=>onAction(row,"close")}>Close</Button>}
    {!["CONVERTED_TO_WORK_ORDER","CLOSED","CANCELLED"].includes(row.status)&&<Button danger onClick={()=>onAction(row,"cancel")}>Cancel</Button>}
  </Space>;
}
