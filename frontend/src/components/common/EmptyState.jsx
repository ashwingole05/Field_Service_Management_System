import { Inbox } from "lucide-react";
export default function EmptyState({ title = "Nothing here yet", description }) {
  return <div className="empty-state"><Inbox size={34}/><strong>{title}</strong>{description && <span>{description}</span>}</div>;
}
