import { Tag } from "antd";
const colors={OPEN:"blue",ASSIGNED:"geekblue",ACCEPTED:"cyan",IN_PROGRESS:"processing",ON_HOLD:"gold",COMPLETED:"green",CLOSED:"default",CANCELLED:"red",IN_REVIEW:"purple",CONVERTED_TO_WORK_ORDER:"green",LOW:"default",MEDIUM:"blue",HIGH:"orange",CRITICAL:"red"};
export default function StatusBadge({ value }) {
  return <Tag color={colors[value]||"default"}>{String(value||"—").replaceAll("_"," ")}</Tag>;
}
