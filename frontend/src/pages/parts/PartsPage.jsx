import { useEffect,useState } from "react";
import { Button,Form,InputNumber,Modal,message } from "antd";
import { Plus } from "lucide-react";
import PageHeader from "@/components/common/PageHeader";
import PartForm from "@/components/parts/PartForm";
import PartTable from "@/components/parts/PartTable";
import UsePartDialog from "@/components/parts/UsePartDialog";
import { confirmAction } from "@/components/common/ConfirmDialog";
import { addStock,createPart,deletePart,getParts,updatePart,usePart as recordPartUsage } from "@/api/parts.api";
import useAuth from "@/hooks/useAuth";
import { getErrorMessage } from "@/utils/errorHandler";

export default function PartsPage() {
  const {user}=useAuth();const manager=user?.role==="MANAGER";
  const [rows,setRows]=useState([]),[loading,setLoading]=useState(false),[open,setOpen]=useState(false),[editing,setEditing]=useState(null),[useOpen,setUseOpen]=useState(false),[selected,setSelected]=useState(null);
  const [form]=Form.useForm();
  const load=async()=>{setLoading(true);try{setRows((await getParts()).data||[])}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const save=async()=>{try{const v=await form.validateFields();editing?await updatePart(editing.id,v):await createPart(v);message.success(editing?"Part updated":"Part created");setOpen(false);load()}catch(e){if(e?.response)message.error(getErrorMessage(e))}};
  const stock=row=>{let quantity=1;Modal.confirm({title:`Add stock: ${row.name}`,content:<InputNumber min={1} defaultValue={1} onChange={v=>quantity=v}/>,onOk:async()=>{await addStock(row.id,quantity);message.success("Stock added");load()}})};
  const remove=row=>confirmAction({title:`Delete ${row.name}?`,danger:true,onOk:async()=>{try{await deletePart(row.id);message.success("Part deleted");load()}catch(e){message.error(getErrorMessage(e))}}});
  return <><PageHeader eyebrow="Inventory" title="Parts" description="Maintain stock levels and record parts consumed on field jobs." action={manager?<Button type="primary" icon={<Plus size={16}/>} onClick={()=>{setEditing(null);form.resetFields();setOpen(true)}}>New part</Button>:null}/><div className="panel table-panel"><PartTable data={rows} loading={loading} role={user?.role} onEdit={r=>{setEditing(r);form.setFieldsValue(r);setOpen(true)}} onDelete={remove} onAddStock={stock} onUse={r=>{setSelected(r);setUseOpen(true)}}/></div><Modal title={editing?"Edit part":"New part"} open={open} onCancel={()=>setOpen(false)} onOk={save}><PartForm form={form}/></Modal><UsePartDialog open={useOpen} loading={loading} part={selected} onCancel={()=>setUseOpen(false)} onSubmit={async payload=>{try{await recordPartUsage(payload);message.success("Part usage recorded");setUseOpen(false);load()}catch(e){message.error(getErrorMessage(e))}}}/></>;
}
