import { Form, Modal, Select } from "antd";
export default function AssignTechnicianDialog({open,loading,onCancel,onSubmit,technicians=[]}) {
  const [form]=Form.useForm();
  return <Modal title="Assign technician" open={open} onCancel={onCancel} okText="Assign" confirmLoading={loading} onOk={async()=>{const v=await form.validateFields();await onSubmit(v.technicianId);form.resetFields()}}><Form form={form} layout="vertical"><Form.Item name="technicianId" label="Technician" rules={[{required:true}]}><Select showSearch optionFilterProp="label" placeholder="Select technician" options={technicians.map(technician=>({value:technician.id,label:`${technician.userName || technician.userEmail} (#${technician.id})`}))}/></Form.Item></Form></Modal>;
}
