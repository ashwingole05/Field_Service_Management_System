import { useEffect, useState } from "react";
import { Button, Form, Modal, message } from "antd";
import { Plus } from "lucide-react";
import PageHeader from "@/components/common/PageHeader";
import CustomerForm from "@/components/customers/CustomerForm";
import CustomerTable from "@/components/customers/CustomerTable";
import { confirmAction } from "@/components/common/ConfirmDialog";
import { createCustomer,deleteCustomer,getCustomers,updateCustomer } from "@/api/customers.api";
import { getErrorMessage } from "@/utils/errorHandler";

export default function CustomersPage() {
  const [rows,setRows]=useState([]),[loading,setLoading]=useState(false),[open,setOpen]=useState(false),[editing,setEditing]=useState(null);
  const [form]=Form.useForm();
  const load=async()=>{setLoading(true);try{setRows((await getCustomers()).data||[])}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const showCreate=()=>{setEditing(null);form.resetFields();form.setFieldValue("active",true);setOpen(true)};
  const showEdit=row=>{setEditing(row);form.setFieldsValue(row);setOpen(true)};
  const save=async()=>{try{const v=await form.validateFields();editing?await updateCustomer(editing.id,v):await createCustomer(v);message.success(editing?"Customer updated":"Customer created");setOpen(false);load()}catch(e){if(e?.response)message.error(getErrorMessage(e))}};
  const remove=row=>confirmAction({title:`Delete ${row.companyName}?`,danger:true,onOk:async()=>{try{await deleteCustomer(row.id);message.success("Customer deleted");load()}catch(e){message.error(getErrorMessage(e))}}});
  return <><PageHeader eyebrow="Directory" title="Customers" description="Manage organizations and primary service contacts." action={<Button type="primary" icon={<Plus size={16}/>} onClick={showCreate}>New customer</Button>}/><div className="panel table-panel"><CustomerTable data={rows} loading={loading} onEdit={showEdit} onDelete={remove}/></div><Modal title={editing?"Edit customer":"New customer"} open={open} onCancel={()=>setOpen(false)} onOk={save} okText={editing?"Save changes":"Create customer"}><CustomerForm form={form}/></Modal></>;
}
