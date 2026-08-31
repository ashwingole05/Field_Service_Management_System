import { Form, Input, InputNumber } from "antd";
export default function PartForm({form}) {
  return <Form form={form} layout="vertical" requiredMark={false}><Form.Item name="name" label="Part name" rules={[{required:true}]}><Input/></Form.Item><Form.Item name="sku" label="SKU" rules={[{required:true}]}><Input/></Form.Item><div className="form-grid"><Form.Item name="quantity" label="Quantity" rules={[{required:true}]}><InputNumber min={0} className="w-full"/></Form.Item><Form.Item name="unitPrice" label="Unit price" rules={[{required:true}]}><InputNumber min={0} className="w-full" prefix="₹"/></Form.Item></div></Form>;
}
