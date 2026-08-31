import { Form, Input, Switch } from "antd";
export default function CustomerForm({ form }) {
  return (
    <Form form={form} layout="vertical" requiredMark={false}>
      <Form.Item name="companyName" label="Company name" rules={[{ required: true }]}><Input /></Form.Item>
      <Form.Item name="contactPerson" label="Contact person" rules={[{ required: true }]}><Input /></Form.Item>
      <Form.Item name="email" label="Email" rules={[{ required: true, type: "email" }]}><Input /></Form.Item>
      <div className="form-grid">
        <Form.Item name="phone" label="Phone"><Input /></Form.Item>
        <Form.Item name="postalCode" label="Postal code"><Input /></Form.Item>
      </div>
      <Form.Item name="address" label="Address"><Input.TextArea rows={2} /></Form.Item>
      <div className="form-grid">
        <Form.Item name="city" label="City"><Input /></Form.Item>
        <Form.Item name="state" label="State"><Input /></Form.Item>
      </div>
      <Form.Item name="active" label="Active" valuePropName="checked" initialValue><Switch /></Form.Item>
    </Form>
  );
}
