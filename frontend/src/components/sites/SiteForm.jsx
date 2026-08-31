import { Form, Input, Select } from "antd";
export default function SiteForm({ form, customers = [], customersLoading = false, showCustomer = true }) {
  return (
    <Form form={form} layout="vertical" requiredMark={false}>
      <Form.Item name="siteName" label="Site name" rules={[{ required:true }]}><Input /></Form.Item>
      <Form.Item name="address" label="Address" rules={[{ required:true }]}><Input /></Form.Item>
      <div className="form-grid">
        <Form.Item name="city" label="City" rules={[{ required:true }]}><Input /></Form.Item>
        <Form.Item name="state" label="State" rules={[{ required:true }]}><Input /></Form.Item>
      </div>
      <div className="form-grid">
        <Form.Item name="pincode" label="Pincode" rules={[{ required:true }]}><Input /></Form.Item>
        {showCustomer ? (
          <Form.Item name="customerId" label="Customer" rules={[{ required:true }]}>
            <Select
              loading={customersLoading}
              showSearch
              optionFilterProp="label"
              placeholder="Select customer"
              options={customers.map(customer=>({
                value:customer.id,
                label:`${customer.companyName} (#${customer.id})`,
              }))}
            />
          </Form.Item>
        ) : null}
      </div>
    </Form>
  );
}
