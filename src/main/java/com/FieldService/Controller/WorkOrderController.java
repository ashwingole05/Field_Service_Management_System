package com.FieldService.Controller;

import com.FieldService.DTO.WorkOrderRequestDTO;
import com.FieldService.Entity.WorkOrder;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Service.WorkOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(
            WorkOrderService workOrderService) {

        this.workOrderService = workOrderService;
    }

    // CREATE
    @PreAuthorize("hasAuthority('CREATE_WO')")
    @PostMapping
    public ResponseEntity<WorkOrder> createWorkOrder(
            @Valid  @RequestBody WorkOrderRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        workOrderService.createWorkOrder(dto)
                );
    }

    // GET ALL
    @PreAuthorize("hasAuthority('VIEW_WO')")
    @GetMapping
    public ResponseEntity<List<WorkOrder>> getAllWorkOrders() {

        return ResponseEntity.ok(
                workOrderService.getAllWorkOrders()
        );
    }

    // GET BY ID
    @PreAuthorize("hasAuthority('VIEW_WO')")
    @GetMapping("/{id}")
    public ResponseEntity<WorkOrder> getWorkOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.getWorkOrderById(id)
        );
    }

    // UPDATE
    @PreAuthorize("hasAuthority('UPDATE_WO')")
    @PutMapping("/{id}")
    public ResponseEntity<WorkOrder> updateWorkOrder(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderRequestDTO dto) {

        return ResponseEntity.ok(
                workOrderService.updateWorkOrder(id, dto)
        );
    }

    // DELETE
    @PreAuthorize("hasAuthority('DELETE_WO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.deleteWorkOrder(id)
        );
    }

    // GET BY SITE
    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<WorkOrder>> getBySite(
            @PathVariable Long siteId) {

        return ResponseEntity.ok(
                workOrderService.getBySite(siteId)
        );
    }

    // GET BY TECHNICIAN
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<WorkOrder>> getByTechnician(
            @PathVariable Long technicianId) {

        return ResponseEntity.ok(
                workOrderService.getByTechnician(technicianId)
        );
    }

    // GET BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<WorkOrder>> getByStatus(
            @PathVariable WorkOrderStatus status) {

        return ResponseEntity.ok(
                workOrderService.getByStatus(status)
        );
    }

    // ASSIGN TECHNICIAN
    @PreAuthorize("hasAuthority('ASSIGN_WO')")
    @PutMapping("/{id}/assign/{technicianId}")
    public ResponseEntity<WorkOrder> assignTechnician(
            @PathVariable Long id,
            @PathVariable Long technicianId) {

        return ResponseEntity.ok(
                workOrderService.assignTechnician(
                        id,
                        technicianId
                )
        );
    }


    // ACCEPT
    @PutMapping("/{id}/accept")
    public ResponseEntity<WorkOrder> acceptWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.acceptWorkOrder(id)
        );
    }


    // START
    @PreAuthorize("hasAuthority('START_WORK')")
    @PutMapping("/{id}/start")
    public ResponseEntity<WorkOrder> startWork(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.startWork(id)
        );
    }


    // HOLD
    @PreAuthorize("hasAuthority('HOLD_WORK')")
    @PutMapping("/{id}/hold")

    public ResponseEntity<WorkOrder> holdWork(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.holdWork(id)
        );
    }


    // RESUME
    @PreAuthorize("hasAuthority('RESUME_WORK')")
    @PutMapping("/{id}/resume")
    public ResponseEntity<WorkOrder> resumeWork(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.resumeWork(id)
        );
    }


    // COMPLETE
    @PreAuthorize("hasAuthority('COMPLETED_WORK')")
    @PutMapping("/{id}/complete")
    public ResponseEntity<WorkOrder> completeWork(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.completeWork(id)
        );
    }


    // CANCEL
    @PreAuthorize("hasAuthority('CANCEL_WO')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<WorkOrder> cancelWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.cancelWorkOrder(id)
        );
    }


    // CLOSE
    @PreAuthorize("hasAuthority('CLOSE_WO')")
    @PutMapping("/{id}/close")
    public ResponseEntity<WorkOrder> closeWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.closeWorkOrder(id)
        );
    }
}