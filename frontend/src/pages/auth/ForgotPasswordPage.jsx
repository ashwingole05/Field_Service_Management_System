import { useState } from "react";
import { Form, Input, message } from "antd";
import { Mail, ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { forgotPasswordRequest } from "@/api/auth.api";
import { getErrorMessage } from "@/utils/errorHandler";

export default function ForgotPasswordPage() {
  const [loading, setLoading] = useState(false);
  const submit = async ({ userEmail }) => {
    setLoading(true);
    try {
      await forgotPasswordRequest(userEmail);
      message.success("Reset link sent. Check your email.");
    } catch (e) {
      message.error(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="auth-card">
      <div className="auth-card-head"><span>Account recovery</span><h2>Reset your password</h2><p>We’ll send a secure reset link to your registered email.</p></div>
      <Form layout="vertical" onFinish={submit}>
        <Form.Item name="userEmail" label="Email" rules={[{ required: true, type: "email" }]}>
          <Input size="large" prefix={<Mail size={16} />} />
        </Form.Item>
        <Button className="w-full h-11" disabled={loading}>{loading ? "Sending..." : "Send reset link"}</Button>
      </Form>
      <Link className="back-link" to="/login"><ArrowLeft size={15} /> Back to sign in</Link>
    </div>
  );
}
