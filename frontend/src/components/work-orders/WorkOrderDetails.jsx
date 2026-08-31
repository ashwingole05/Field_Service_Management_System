import { Descriptions } from "antd";
import StatusBadge from "@/components/common/StatusBadge";
import { formatDate } from "@/utils/formatDate";

export default function WorkOrderDetails({ workOrder, technicians = [] }) {
  if (!workOrder) return null;

  const technician = technicians.find(
    (item) => item.id === workOrder.assignedTechnicianId
  );

  const technicianLabel = workOrder.assignedTechnicianId
    ? `${technician?.userName || technician?.userEmail || "Technician"} (#${workOrder.assignedTechnicianId})`
    : "Unassigned";

  const items = [
    ["Work order", workOrder.workOrderNumber],
    ["Title", workOrder.title],
    ["Priority", <StatusBadge value={workOrder.priority} />],
    ["Status", <StatusBadge value={workOrder.status} />],
    ["Site ID", workOrder.siteId],
    ["Technician", technicianLabel],
    ["Scheduled", formatDate(workOrder.scheduledAt)],
    ["Created", formatDate(workOrder.createdAt)],
    ["Description", workOrder.description || "-"],
  ];

  return (
    <Descriptions
      bordered
      column={1}
      items={items.map(([label, children], index) => ({
        key: index,
        label,
        children,
      }))}
    />
  );
}
