import { Button, Space, Table } from "antd";
import { Pencil, Trash2 } from "lucide-react";
export default function SiteTable({ data, loading, onEdit, onDelete, customers = [], showCustomer = true, canDelete = true }) {
  const customerById=new Map(customers.map(customer=>[customer.id,customer]));
  const columns=[
    {title:"Site ID",dataIndex:"id",width:90,render:v=>`#${v}`},
    {title:"Site",dataIndex:"siteName"},{title:"Address",dataIndex:"address"},{title:"City",dataIndex:"city"},{title:"State",dataIndex:"state"},{title:"Pincode",dataIndex:"pincode"},
    showCustomer ? {title:"Customer",dataIndex:"customerId",render:v=>{const customer=customerById.get(v);return customer?`${customer.companyName} (#${v})`:`#${v}`}} : null,
    {title:"Actions",width:110,render:(_,row)=><Space><Button type="text" icon={<Pencil size={16}/>} onClick={()=>onEdit(row)}/>{canDelete?<Button type="text" danger icon={<Trash2 size={16}/>} onClick={()=>onDelete(row)}/>:null}</Space>}
  ].filter(Boolean);
  return <Table rowKey="id" columns={columns} dataSource={data} loading={loading} scroll={{x:850}}/>;
}
