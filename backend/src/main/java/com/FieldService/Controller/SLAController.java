package com.FieldService.Controller;

import com.FieldService.Entity.WorkOrder;
import com.FieldService.Service.SLAService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sla")
public class SLAController {

    private final SLAService slaService;

    public SLAController(
            SLAService slaService) {

        this.slaService = slaService;
    }

    @PreAuthorize("hasAuthority('VIEW_REPORTS')")
    @GetMapping("/overdue")
    public ResponseEntity<List<WorkOrder>>
    getOverdueWorkOrders() {

        return ResponseEntity.ok(
                slaService.getOverdueWorkOrders()
        );
    }

    @PreAuthorize("hasAuthority('VIEW_REPORTS')")
    @GetMapping("/overdue/count")
    public ResponseEntity<Map<String, Long>>
    getOverdueCount() {

        return ResponseEntity.ok(
                Map.of(
                        "overdueWorkOrders",
                        slaService.getOverdueCount()
                )
        );
    }
}