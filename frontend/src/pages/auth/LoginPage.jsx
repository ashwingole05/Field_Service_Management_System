import { useState } from "react";
import { Form, Input, message } from "antd";
import {
  LockKeyhole,
  Mail,
  ArrowRight,
} from "lucide-react";

import {
  Link,
  useLocation,
  useNavigate,
} from "react-router-dom";

import { Button } from "@/components/ui/button";

import useAuth from "@/hooks/useAuth";
import { getErrorMessage } from "@/utils/errorHandler";

export default function LoginPage() {

  const { login } = useAuth();

  const navigate = useNavigate();
  const location = useLocation();

  const [loading, setLoading] =
    useState(false);

  const submit = async (values) => {

    setLoading(true);

    try {

      await login(values);

      message.success(
        "Welcome back to KEYSTONE"
      );

      navigate(
        location.state?.from?.pathname || "/",
        {
          replace: true,
        }
      );

    } catch (error) {

      message.error(
        getErrorMessage(
          error,
          "Login failed"
        )
      );

    } finally {

      setLoading(false);

    }
  };

  return (

    <div className="auth-card">

      <div className="auth-card-head">

        <span>
          Secure access
        </span>

        <h2>
          Welcome back
        </h2>

        <p>
          Sign in to continue to your
          field-service workspace.
        </p>

      </div>

      <Form
        layout="vertical"
        onFinish={submit}
        requiredMark={false}
      >

        <Form.Item
          name="userEmail"
          label="Email"
          rules={[
            {
              required: true,
              message:
                "Please enter your email",
            },

            {
              type: "email",
              message:
                "Enter a valid email",
            },
          ]}
        >

          <Input
            size="large"
            prefix={
              <Mail size={16} />
            }
            placeholder=
              "manager@company.com"
          />

        </Form.Item>

        <Form.Item
          name="password"
          label="Password"
          rules={[
            {
              required: true,
              message:
                "Please enter your password",
            },
          ]}
        >

          <Input.Password
            size="large"
            prefix={
              <LockKeyhole size={16} />
            }
            placeholder="Your password"
          />

        </Form.Item>

        <div className="auth-row">

          <span />

          <Link to="/forgot-password">
            Forgot password?
          </Link>

        </div>

        <Button
          className="w-full mt-4 h-11"
          disabled={loading}
        >

          {loading
            ? "Signing in..."
            : (
              <>
                Sign in

                <ArrowRight size={16} />
              </>
            )
          }

        </Button>

      </Form>

      <div className="register-login-link">

        <span>
          New to KEYSTONE?
        </span>

        <Link to="/register">
          Create account
        </Link>

      </div>

      <div className="auth-foot">

        Protected by JWT authentication
        and role-based access control.

      </div>

    </div>
  );
}