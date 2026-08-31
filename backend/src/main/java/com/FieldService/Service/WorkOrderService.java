package com.FieldService.Service;

import com.FieldService.DTO.WorkOrderRequestDTO;
import com.FieldService.ENUM.Role;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Entity.UserAuth;
import com.FieldService.Entity.WorkOrder;
import com.FieldService.Exception.ResourceNotFoundException;
import com.FieldService.Repository.SiteRepository;
import com.FieldService.Repository.UserRepository;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final SiteRepository siteRepository;
    private final UserRepository userRepository;

    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            SiteRepository siteRepository,
            UserRepository userRepository) {

        this.workOrderRepository = workOrderRepository;
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public WorkOrder createWorkOrder(
            WorkOrderRequestDTO dto) {

        validateSite(dto.getSiteId());
        validateTechnician(dto.getAssignedTechnicianId());

        WorkOrder workOrder = WorkOrder.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(dto.getStatus())
                .siteId(dto.getSiteId())
                .assignedTechnicianId(
                        dto.getAssignedTechnicianId()
                )
                .scheduledAt(dto.getScheduledAt())
                .build();

        return workOrderRepository.save(workOrder);
    }

    // GET ALL
    public List<WorkOrder> getAllWorkOrders() {

        return workOrderRepository.findAll();
    }

    // GET BY ID
    public WorkOrder getWorkOrderById(Long id) {

        return workOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Work order not found with id: " + id
                        )
                );
    }

    // UPDATE
    public WorkOrder updateWorkOrder(
            Long id,
            WorkOrderRequestDTO dto) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        validateSite(dto.getSiteId());
        validateTechnician(dto.getAssignedTechnicianId());

        workOrder.setTitle(dto.getTitle());
        workOrder.setDescription(dto.getDescription());
        workOrder.setPriority(dto.getPriority());
        workOrder.setStatus(dto.getStatus());
        workOrder.setSiteId(dto.getSiteId());
        workOrder.setAssignedTechnicianId(
                dto.getAssignedTechnicianId()
        );
        workOrder.setScheduledAt(dto.getScheduledAt());

        return workOrderRepository.save(workOrder);
    }

    // DELETE
    public String deleteWorkOrder(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        workOrderRepository.delete(workOrder);

        return "Work order deleted successfully";
    }

    // GET BY SITE
    public List<WorkOrder> getBySite(Long siteId) {

        return workOrderRepository.findBySiteId(siteId);
    }

    // GET BY TECHNICIAN
    public List<WorkOrder> getByTechnician(
            Long technicianId) {

        return workOrderRepository
                .findByAssignedTechnicianId(technicianId);
    }

    // GET BY STATUS
    public List<WorkOrder> getByStatus(
            WorkOrderStatus status) {

        return workOrderRepository.findByStatus(status);
    }

    // ASSIGN TECHNICIAN
    public WorkOrder assignTechnician(
            Long workOrderId,
            Long technicianId) {

        WorkOrder workOrder =
                getWorkOrderById(workOrderId);

        if (workOrder.getStatus() == WorkOrderStatus.CLOSED
                || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cannot assign technician to closed/cancelled work order"
            );
        }

        validateTechnician(technicianId);

        workOrder.setAssignedTechnicianId(technicianId);
        workOrder.setStatus(WorkOrderStatus.ASSIGNED);

        return workOrderRepository.save(workOrder);
    }

    // ACCEPT WORK ORDER
    public WorkOrder acceptWorkOrder(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                != WorkOrderStatus.ASSIGNED) {

            throw new RuntimeException(
                    "Only assigned work orders can be accepted"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.ACCEPTED
        );

        return workOrderRepository.save(workOrder);
    }

    // START WORK
    public WorkOrder startWork(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                != WorkOrderStatus.ACCEPTED
                && workOrder.getStatus()
                != WorkOrderStatus.ASSIGNED) {

            throw new RuntimeException(
                    "Work order must be assigned or accepted before starting"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.IN_PROGRESS
        );

        return workOrderRepository.save(workOrder);
    }

    // HOLD WORK
    public WorkOrder holdWork(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                != WorkOrderStatus.IN_PROGRESS) {

            throw new RuntimeException(
                    "Only in-progress work orders can be put on hold"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.ON_HOLD
        );

        return workOrderRepository.save(workOrder);
    }

    // RESUME WORK
    public WorkOrder resumeWork(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                != WorkOrderStatus.ON_HOLD) {

            throw new RuntimeException(
                    "Only on-hold work orders can be resumed"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.IN_PROGRESS
        );

        return workOrderRepository.save(workOrder);
    }

    // COMPLETE WORK
    public WorkOrder completeWork(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                != WorkOrderStatus.IN_PROGRESS) {

            throw new RuntimeException(
                    "Only in-progress work orders can be completed"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.COMPLETED
        );

        return workOrderRepository.save(workOrder);
    }

    // CANCEL WORK ORDER
    public WorkOrder cancelWorkOrder(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                == WorkOrderStatus.COMPLETED
                || workOrder.getStatus()
                == WorkOrderStatus.CLOSED) {

            throw new RuntimeException(
                    "Completed or closed work order cannot be cancelled"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.CANCELLED
        );

        return workOrderRepository.save(workOrder);
    }

    // CLOSE WORK ORDER
    public WorkOrder closeWorkOrder(Long id) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        if (workOrder.getStatus()
                != WorkOrderStatus.COMPLETED) {

            throw new RuntimeException(
                    "Only completed work orders can be closed"
            );
        }

        workOrder.setStatus(
                WorkOrderStatus.CLOSED
        );

        return workOrderRepository.save(workOrder);
    }

    private void validateSite(Long siteId) {

        if (siteId == null || !siteRepository.existsById(siteId)) {
            throw new RuntimeException("Site not found with id: " + siteId);
        }
    }

    private void validateTechnician(Long technicianId) {

        if (technicianId == null) {
            return;
        }

        UserAuth technician = userRepository
                .findById(technicianId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Technician not found with id: " + technicianId
                        )
                );

        if (technician.getRole() != Role.TECHNICIAN) {
            throw new RuntimeException(
                    "Assigned user must be a technician"
            );
        }
    }
}
