import { Button, Space, Table } from "antd";
import { Pencil, Trash2 } from "lucide-react";
export default function CustomerTable({ data, loading, onEdit, onDelete }) {
  const columns = [
    { title:"ID", dataIndex:"id", width:80, render:v=>`#${v}` },
    { title:"Company", dataIndex:"companyName" },
    { title:"Contact", dataIndex:"contactPerson" },
    { title:"Email", dataIndex:"email" },
    { title:"City", dataIndex:"city" },
    { title:"Phone", dataIndex:"phone" },
    { title:"Actions", width:120, render:(_,row)=><Space><Button type="text" icon={<Pencil size={16}/>} onClick={()=>onEdit(row)}/><Button type="text" danger icon={<Trash2 size={16}/>} onClick={()=>onDelete(row)}/></Space> },
  ];
  return <Table rowKey="id" columns={columns} dataSource={data} loading={loading} scroll={{x:800}} />;
}
