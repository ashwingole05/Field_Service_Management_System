import { Form, InputNumber, Modal } from "antd";
export default function UsePartDialog({open,loading,part,onCancel,onSubmit}) {
  const [form]=Form.useForm();
  return <Modal title={`Use ${part?.name||"part"}`} open={open} onCancel={onCancel} okText="Record usage" confirmLoading={loading} onOk={async()=>{const v=await form.validateFields();await onSubmit({partId:part.id,...v});form.resetFields()}}><Form form={form} layout="vertical"><Form.Item name="workOrderId" label="Work order ID" rules={[{required:true}]}><InputNumber min={1} className="w-full"/></Form.Item><Form.Item name="quantityUsed" label="Quantity used" rules={[{required:true}]}><InputNumber min={1} max={part?.quantity||undefined} className="w-full"/></Form.Item></Form></Modal>;
}
