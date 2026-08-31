import { Button, Space, Table } from "antd";
import { Pencil, PackagePlus, Wrench, Trash2 } from "lucide-react";
import { formatCurrency } from "@/utils/formatCurrency";
export default function PartTable({data,loading,onEdit,onDelete,onAddStock,onUse,role}) {
  const manage=role==="MANAGER",use=["MANAGER","TECHNICIAN"].includes(role);
  const columns=[
    {title:"Part",dataIndex:"name"},{title:"SKU",dataIndex:"sku"},{title:"Stock",dataIndex:"quantity"},{title:"Unit price",dataIndex:"unitPrice",render:formatCurrency},
    {title:"Actions",width:190,render:(_,row)=><Space>{manage&&<Button type="text" icon={<Pencil size={16}/>} onClick={()=>onEdit(row)}/>}
      {manage&&<Button type="text" icon={<PackagePlus size={16}/>} onClick={()=>onAddStock(row)}/>}
      {use&&<Button type="text" icon={<Wrench size={16}/>} onClick={()=>onUse(row)}/>}
      {manage&&<Button type="text" danger icon={<Trash2 size={16}/>} onClick={()=>onDelete(row)}/>}</Space>}
  ];
  return <Table rowKey="id" columns={columns} dataSource={data} loading={loading} scroll={{x:750}}/>;
}
