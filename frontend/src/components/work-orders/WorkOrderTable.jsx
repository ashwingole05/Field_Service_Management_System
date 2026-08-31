import { Button, Space, Table } from "antd";
import { Eye, Pencil, Trash2 } from "lucide-react";
import StatusBadge from "@/components/common/StatusBadge";
import { formatDate } from "@/utils/formatDate";
export default function WorkOrderTable({ data, loading, onView, onEdit, onDelete, canEdit, canDelete, sites = [], technicians = [] }) {
  const siteById=new Map(sites.map(site=>[site.id,site]));
  const technicianById=new Map(technicians.map(technician=>[technician.id,technician]));
  const columns=[
    {title:"ID",dataIndex:"id",fixed:"left",width:80,render:v=>`#${v}`},
    {title:"WO #",dataIndex:"workOrderNumber"},{title:"Title",dataIndex:"title"},
    {title:"Priority",dataIndex:"priority",render:v=><StatusBadge value={v}/>},{title:"Status",dataIndex:"status",render:v=><StatusBadge value={v}/>},
    {title:"Site",dataIndex:"siteId",render:v=>{const site=siteById.get(v);return site?`${site.siteName} (#${v})`:`#${v}`}},
    {title:"Technician",dataIndex:"assignedTechnicianId",render:v=>{if(!v)return "Unassigned";const technician=technicianById.get(v);return technician?`${technician.userName || technician.userEmail} (#${v})`:`#${v}`}},
    {title:"Scheduled",dataIndex:"scheduledAt",render:formatDate},
    {title:"Actions",fixed:"right",width:150,render:(_,row)=><Space><Button type="text" icon={<Eye size={16}/>} onClick={()=>onView(row)}/>{canEdit&&<Button type="text" icon={<Pencil size={16}/>} onClick={()=>onEdit(row)}/>} {canDelete&&<Button type="text" danger icon={<Trash2 size={16}/>} onClick={()=>onDelete(row)}/>}</Space>}
  ];
  return <Table rowKey="id" columns={columns} dataSource={data} loading={loading} scroll={{x:1100}}/>;
}
