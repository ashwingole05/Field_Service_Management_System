import { useCallback, useEffect,useState } from "react";
import { message } from "antd";
import { useParams } from "react-router-dom";
import PageHeader from "@/components/common/PageHeader";
import LoadingScreen from "@/components/common/LoadingScreen";
import WorkOrderDetails from "@/components/work-orders/WorkOrderDetails";
import WorkOrderActions from "@/components/work-orders/WorkOrderActions";
import AssignTechnicianDialog from "@/components/work-orders/AssignTechnicianDialog";
import useAuth from "@/hooks/useAuth";
import { getWorkOrder,assignTechnician,acceptWorkOrder,startWorkOrder,holdWorkOrder,resumeWorkOrder,completeWorkOrder,cancelWorkOrder,closeWorkOrder } from "@/api/workOrders.api";
import { getTechnicians } from "@/api/users.api";
import { getErrorMessage } from "@/utils/errorHandler";
const actionMap={accept:acceptWorkOrder,start:startWorkOrder,hold:holdWorkOrder,resume:resumeWorkOrder,complete:completeWorkOrder,cancel:cancelWorkOrder,close:closeWorkOrder};

export default function WorkOrderDetailsPage() {
  const {id}=useParams(),{user}=useAuth();
  const [row,setRow]=useState(null),[assignOpen,setAssignOpen]=useState(false),[loading,setLoading]=useState(false);
  const [technicians,setTechnicians]=useState([]);
  const load=useCallback(()=>getWorkOrder(id).then(r=>setRow(r.data)),[id]);
  useEffect(()=>{load()},[load]);
  useEffect(()=>{if(["MANAGER","DISPATCHER"].includes(user?.role))getTechnicians().then(r=>setTechnicians(r.data||[])).catch(e=>message.error(getErrorMessage(e,"Unable to load technicians")))},[user?.role]);
  const act=async action=>{setLoading(true);try{await actionMap[action](id);message.success(`Work order ${action} successful`);await load()}catch(e){message.error(getErrorMessage(e))}finally{setLoading(false)}};
  if(!row)return <LoadingScreen/>;
  return <><PageHeader eyebrow={row.workOrderNumber} title={row.title} description="Lifecycle, assignment and scheduling details for this field job." action={<WorkOrderActions workOrder={row} role={user?.role} onAction={act} onAssign={()=>setAssignOpen(true)}/>}/><div className="panel"><WorkOrderDetails workOrder={row} technicians={technicians}/></div><AssignTechnicianDialog open={assignOpen} loading={loading} technicians={technicians} onCancel={()=>setAssignOpen(false)} onSubmit={async technicianId=>{setLoading(true);try{await assignTechnician(id,technicianId);message.success("Technician assigned");setAssignOpen(false);load()}catch(e){message.error(getErrorMessage(e))}finally{setLoading(false)}}}/></>;
}
