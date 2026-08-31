package com.FieldService.Controller;

import com.FieldService.DTO.WorkOrderPartRequestDTO;
import com.FieldService.Entity.WorkOrderPart;
import com.FieldService.Service.WorkOrderPartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workorder-parts")
public class WorkOrderPartController {

    private final WorkOrderPartService workOrderPartService;

    public WorkOrderPartController(
            WorkOrderPartService workOrderPartService) {

        this.workOrderPartService = workOrderPartService;
    }

    // USE PART
    @PreAuthorize("hasAuthority('USE_PARTS')")
    @PostMapping("/use")
    public ResponseEntity<WorkOrderPart> usePart(
            @RequestBody WorkOrderPartRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        workOrderPartService.usePart(dto)
                );
    }

    // GET ALL PART USAGE
    @PreAuthorize("hasAuthority('VIEW_PARTS')")
    @GetMapping
    public ResponseEntity<List<WorkOrderPart>>
    getAllUsage() {

        return ResponseEntity.ok(
                workOrderPartService.getAllUsage()
        );
    }

    // GET PARTS USED ON A WORK ORDER
    @PreAuthorize("hasAuthority('VIEW_PARTS')")
    @GetMapping("/workorder/{workOrderId}")
    public ResponseEntity<List<WorkOrderPart>>
    getPartsByWorkOrder(
            @PathVariable Long workOrderId) {

        return ResponseEntity.ok(
                workOrderPartService
                        .getPartsByWorkOrder(workOrderId)
        );
    }
}