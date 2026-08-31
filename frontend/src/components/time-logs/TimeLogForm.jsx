import { Form, Input, InputNumber } from "antd";
export default function TimeLogForm({form,technicianId}) {
  return <Form form={form} layout="vertical" requiredMark={false}><div className="form-grid"><Form.Item name="workOrderId" label="Work order ID" rules={[{required:true}]}><InputNumber min={1} className="w-full"/></Form.Item>{technicianId?<Form.Item name="technicianId" hidden><InputNumber /></Form.Item>:<Form.Item name="technicianId" label="Technician ID" rules={[{required:true}]}><InputNumber min={1} className="w-full"/></Form.Item>}</div><Form.Item name="notes" label="Notes"><Input.TextArea rows={3}/></Form.Item></Form>;
}
