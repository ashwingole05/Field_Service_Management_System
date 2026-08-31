import { DatePicker, Form, Input, Select } from "antd";
import { PRIORITIES } from "@/constants/priorities";
import { WORK_ORDER_STATUS } from "@/constants/workOrderStatus";

export default function WorkOrderForm({ form, sites = [], technicians = [], lookupsLoading = false }) {
  return (
    <Form form={form} layout="vertical" requiredMark={false}>
      <Form.Item name="title" label="Title" rules={[{required:true}]}><Input /></Form.Item>
      <Form.Item name="description" label="Description"><Input.TextArea rows={3}/></Form.Item>
      <div className="form-grid">
        <Form.Item name="priority" label="Priority" rules={[{required:true}]}><Select options={PRIORITIES.map(value=>({value,label:value}))}/></Form.Item>
        <Form.Item name="status" label="Status" initialValue="OPEN"><Select options={WORK_ORDER_STATUS.map(value=>({value,label:value.replaceAll("_"," ")}))}/></Form.Item>
      </div>
      <div className="form-grid">
        <Form.Item name="siteId" label="Site" rules={[{required:true}]}>
          <Select
            loading={lookupsLoading}
            showSearch
            optionFilterProp="label"
            placeholder="Select site"
            options={sites.map(site=>({
              value:site.id,
              label:`${site.siteName} (#${site.id})`,
            }))}
          />
        </Form.Item>
        <Form.Item name="assignedTechnicianId" label="Technician">
          <Select
            allowClear
            loading={lookupsLoading}
            showSearch
            optionFilterProp="label"
            placeholder="Unassigned"
            options={technicians.map(technician=>({
              value:technician.id,
              label:`${technician.userName || technician.userEmail} (#${technician.id})`,
            }))}
          />
        </Form.Item>
      </div>
      <Form.Item name="scheduledAt" label="Scheduled at"><DatePicker showTime className="w-full"/></Form.Item>
    </Form>
  );
}
