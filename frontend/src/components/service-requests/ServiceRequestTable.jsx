import { Table } from "antd";
import StatusBadge from "@/components/common/StatusBadge";
import ServiceRequestActions from "./ServiceRequestActions";
import { formatDate } from "@/utils/formatDate";
export default function ServiceRequestTable({data,loading,role,onAction}) {
  const columns=[
    {title:"ID",dataIndex:"id",width:70},{title:"Title",dataIndex:"title"},{title:"Customer",dataIndex:"customerId",render:v=>`#${v}`},{title:"Site",dataIndex:"siteId",render:v=>v?`#${v}`:"—"},
    {title:"Status",dataIndex:"status",render:v=><StatusBadge value={v}/>},{title:"Work order",dataIndex:"workOrderId",render:v=>v?`#${v}`:"—"},{title:"Created",dataIndex:"createdAt",render:formatDate},
    {title:"Actions",width:300,render:(_,row)=><ServiceRequestActions row={row} role={role} onAction={onAction}/>}
  ];
  return <Table rowKey="id" columns={columns} dataSource={data} loading={loading} scroll={{x:1050}}/>;
}
