import { NavLink } from "react-router-dom";
import { LayoutDashboard, Users, MapPinned, ClipboardList, MessageSquareText, PackageSearch, TimerReset, ShieldAlert, Sparkles } from "lucide-react";
import useAuth from "@/hooks/useAuth";
const nav=[
{to:"/",label:"Dashboard",icon:LayoutDashboard,roles:["MANAGER","DISPATCHER"]},
{to:"/customers",label:"Customers",icon:Users,roles:["MANAGER","DISPATCHER"]},
{to:"/sites",label:"Sites",icon:MapPinned,roles:["MANAGER","DISPATCHER","CUSTOMER"]},
{to:"/work-orders",label:"Work orders",icon:ClipboardList,roles:["MANAGER","DISPATCHER","TECHNICIAN"]},
{to:"/service-requests",label:"Service requests",icon:MessageSquareText,roles:["MANAGER","DISPATCHER","CUSTOMER"]},
{to:"/parts",label:"Parts",icon:PackageSearch,roles:["MANAGER","TECHNICIAN"]},
{to:"/time-logs",label:"Time logs",icon:TimerReset,roles:["MANAGER","TECHNICIAN"]},
{to:"/sla",label:"SLA monitor",icon:ShieldAlert,roles:["MANAGER"]},
];
export default function Sidebar(){const{user}=useAuth();return <aside className="sidebar"><div className="brand"><div className="brand-mark"><Sparkles size={19}/></div><div><strong>KEYSTONE</strong><span>Field Service</span></div></div><nav className="nav-list">{nav.filter(n=>n.roles.includes(user?.role)).map(({to,label,icon:Icon})=><NavLink key={to} to={to} end={to==="/"} className={({isActive})=>`nav-item ${isActive?"active":""}`}><Icon size={18}/><span>{label}</span></NavLink>)}</nav><div className="sidebar-footer"><span>Production API</span><strong className="online-dot">Connected</strong></div></aside>}
