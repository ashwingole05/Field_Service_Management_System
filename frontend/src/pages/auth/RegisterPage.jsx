import { useState } from "react";
import { Form, Input, message } from "antd";
import {
    User,
    Building2,
    Mail,
    Phone,
    LockKeyhole,
    ArrowLeft,
    ArrowRight,
} from "lucide-react";

import { Link, useNavigate } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { registerRequest } from "@/api/auth.api";
import { getErrorMessage } from "@/utils/errorHandler";

export default function RegisterPage() {
    const navigate = useNavigate();

    const [loading, setLoading] = useState(false);

    const submit = async (values) => {
        setLoading(true);

        try {
            const payload = {
                userName: values.userName,
                companyName: values.companyName,
                userEmail: values.userEmail,
                phone: values.phone,
                password: values.password,
            };

            await registerRequest(payload);

            message.success(
                "Customer account created successfully. You can now sign in."
            );

            navigate("/login");
        } catch (error) {
            message.error(
                getErrorMessage(
                    error,
                    "Unable to create your account"
                )
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-card">

            <div className="auth-card-head">
                <span>Create account</span>

                <h2>Join KEYSTONE</h2>

                <p>
                    Create your customer account to raise and
                    track service requests.
                </p>
            </div>

            <Form
                layout="vertical"
                onFinish={submit}
                requiredMark={false}
            >

                {/* FULL NAME */}
                <Form.Item
                    name="userName"
                    label="Full name"
                    rules={[
                        {
                            required: true,
                            message: "Please enter your full name",
                        },
                        {
                            min: 2,
                            message:
                                "Name must contain at least 2 characters",
                        },
                    ]}
                >
                    <Input
                        size="large"
                        prefix={<User size={16} />}
                        placeholder="Your full name"
                    />
                </Form.Item>


                {/* COMPANY NAME */}
                <Form.Item
                    name="companyName"
                    label="Company name"
                    rules={[
                        {
                            required: true,
                            message:
                                "Please enter your company name",
                        },
                        {
                            min: 2,
                            message:
                                "Company name must contain at least 2 characters",
                        },
                    ]}
                >
                    <Input
                        size="large"
                        prefix={<Building2 size={16} />}
                        placeholder="Your company or organization"
                    />
                </Form.Item>


                {/* EMAIL */}
                <Form.Item
                    name="userEmail"
                    label="Email"
                    rules={[
                        {
                            required: true,
                            message: "Please enter your email",
                        },
                        {
                            type: "email",
                            message:
                                "Please enter a valid email address",
                        },
                    ]}
                >
                    <Input
                        size="large"
                        prefix={<Mail size={16} />}
                        placeholder="you@example.com"
                    />
                </Form.Item>


                {/* PHONE */}
                <Form.Item
                    name="phone"
                    label="Phone"
                    rules={[
                        {
                            required: true,
                            message:
                                "Please enter your phone number",
                        },
                        {
                            pattern: /^[0-9]{10}$/,
                            message:
                                "Phone number must contain exactly 10 digits",
                        },
                    ]}
                >
                    <Input
                        size="large"
                        prefix={<Phone size={16} />}
                        placeholder="9876543210"
                        maxLength={10}
                    />
                </Form.Item>


                {/* PASSWORD */}
                <Form.Item
                    name="password"
                    label="Password"
                    rules={[
                        {
                            required: true,
                            message:
                                "Please enter a password",
                        },
                        {
                            min: 6,
                            message:
                                "Password must contain at least 6 characters",
                        },
                    ]}
                >
                    <Input.Password
                        size="large"
                        prefix={<LockKeyhole size={16} />}
                        placeholder="Create password"
                    />
                </Form.Item>


                {/* CONFIRM PASSWORD */}
                <Form.Item
                    name="confirmPassword"
                    label="Confirm password"
                    dependencies={["password"]}
                    rules={[
                        {
                            required: true,
                            message:
                                "Please confirm your password",
                        },

                        ({ getFieldValue }) => ({
                            validator(_, value) {

                                if (
                                    !value ||
                                    getFieldValue("password") === value
                                ) {
                                    return Promise.resolve();
                                }

                                return Promise.reject(
                                    new Error(
                                        "Passwords do not match"
                                    )
                                );
                            },
                        }),
                    ]}
                >
                    <Input.Password
                        size="large"
                        prefix={<LockKeyhole size={16} />}
                        placeholder="Confirm password"
                    />
                </Form.Item>


                {/* CREATE ACCOUNT */}
                <Button
                    className="w-full h-11"
                    disabled={loading}
                >
                    {loading
                        ? "Creating account..."
                        : (
                            <>
                                Create account
                                <ArrowRight size={16} />
                            </>
                        )
                    }
                </Button>

            </Form>


            <div className="register-login-link">

        <span>
          Already have an account?
        </span>

                <Link to="/login">
                    Sign in
                </Link>

            </div>


            <Link
                className="back-link"
                to="/login"
            >
                <ArrowLeft size={15} />

                Back to sign in
            </Link>

        </div>
    );
}