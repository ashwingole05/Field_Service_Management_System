import { useState } from "react";
import { Form, Input, message } from "antd";
import { LockKeyhole } from "lucide-react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { resetPasswordRequest } from "@/api/auth.api";
import { getErrorMessage } from "@/utils/errorHandler";

export default function ResetPasswordPage() {
  const [params] = useSearchParams();
  const token = params.get("token");
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const submit = async ({ newPassword }) => {
    if (!token) return message.error("Reset token is missing.");
    setLoading(true);
    try {
      await resetPasswordRequest(token, newPassword);
      message.success("Password reset successfully");
      navigate("/login");
    } catch (e) {
      message.error(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-card">
      <div className="auth-card-head"><span>New credentials</span><h2>Create a new password</h2><p>Choose a secure password for your KEYSTONE account.</p></div>
      <Form layout="vertical" onFinish={submit}>
        <Form.Item name="newPassword" label="New password" rules={[{ required: true }, { min: 6 }]}>
          <Input.Password size="large" prefix={<LockKeyhole size={16} />} />
        </Form.Item>
        <Form.Item
          name="confirm"
          label="Confirm password"
          dependencies={["newPassword"]}
          rules={[
            { required: true },
            ({ getFieldValue }) => ({
              validator(_, value) {
                return !value || getFieldValue("newPassword") === value
                  ? Promise.resolve()
                  : Promise.reject(new Error("Passwords do not match"));
              },
            }),
          ]}
        >
          <Input.Password size="large" />
        </Form.Item>
        <Button className="w-full h-11" disabled={loading}>{loading ? "Updating..." : "Update password"}</Button>
      </Form>
    </div>
  );
}
