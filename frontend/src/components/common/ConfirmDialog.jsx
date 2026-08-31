import { Modal } from "antd";
export function confirmAction({ title="Are you sure?", content="This action cannot be undone.", onOk, okText="Confirm", danger=false }) {
  Modal.confirm({ title, content, onOk, okText, okButtonProps:{ danger } });
}
