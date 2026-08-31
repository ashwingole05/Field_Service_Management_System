import { Avatar, Button, Dropdown } from "antd";
import { Bell, ChevronDown, LogOut } from "lucide-react";
import useAuth from "@/hooks/useAuth";
export default function Header() {
  const {user,logout}=useAuth();
  const items=[{key:"logout",icon:<LogOut size={15}/>,label:"Logout",onClick:logout}];
  return <header className="app-header"><div><div className="header-kicker">KEYSTONE COMMAND CENTER</div><div className="header-title">Field operations</div></div><div className="header-actions"><Button type="text" shape="circle" icon={<Bell size={18}/>}/><Dropdown menu={{items}} trigger={["click"]}><button className="profile-button"><Avatar>{user?.email?.[0]?.toUpperCase()||"U"}</Avatar><span><strong>{user?.email}</strong><small>{user?.role}</small></span><ChevronDown size={15}/></button></Dropdown></div></header>;
}
