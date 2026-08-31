import { useEffect,useState } from "react";
import { Button,Form,Modal,message } from "antd";
import { Play } from "lucide-react";
import PageHeader from "@/components/common/PageHeader";
import TimeLogForm from "@/components/time-logs/TimeLogForm";
import TimeLogTable from "@/components/time-logs/TimeLogTable";
import { getTimeLogs,startTimeLog,stopTimeLog } from "@/api/timeLogs.api";
import useAuth from "@/hooks/useAuth";
import { getErrorMessage } from "@/utils/errorHandler";

export default function TimeLogsPage() {
  const {user}=useAuth();const technician=user?.role==="TECHNICIAN";
  const [rows,setRows]=useState([]),[loading,setLoading]=useState(false),[open,setOpen]=useState(false);
  const [form]=Form.useForm();
  const load=async()=>{setLoading(true);try{setRows((await getTimeLogs()).data||[])}catch(e){message.error(getErrorMessage(e))}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const start=async()=>{try{const v=await form.validateFields();await startTimeLog({...v,technicianId:user?.id??v.technicianId});message.success("Time log started");setOpen(false);load()}catch(e){if(e?.response)message.error(getErrorMessage(e))}};
  const stop=async id=>{try{await stopTimeLog(id);message.success("Time log stopped");load()}catch(e){message.error(getErrorMessage(e))}};
  return <><PageHeader eyebrow="Labor tracking" title="Time logs" description="Track technician effort against active work orders." action={technician?<Button type="primary" icon={<Play size={16}/>} onClick={()=>{form.resetFields();form.setFieldValue("technicianId",user?.id);setOpen(true)}}>Start timer</Button>:null}/><div className="panel table-panel"><TimeLogTable data={rows} loading={loading} onStop={stop} canStop={technician}/></div><Modal title="Start time log" open={open} onCancel={()=>setOpen(false)} onOk={start} okText="Start"><TimeLogForm form={form} technicianId={user?.id}/></Modal></>;
}
