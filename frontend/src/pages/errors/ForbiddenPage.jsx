import { Button,Result } from "antd";import { useNavigate } from "react-router-dom";
export default function ForbiddenPage(){const n=useNavigate();return <Result status="403" title="403" subTitle="Your role does not have access to this workspace." extra={<Button type="primary" onClick={()=>n("/")}>Return home</Button>}/>}
