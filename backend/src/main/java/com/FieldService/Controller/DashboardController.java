package com.FieldService.Controller;

import com.FieldService.DTO.DashboardResponseDTO;
import com.FieldService.Service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasAuthority('VIEW_DASHBOARD')")
    @GetMapping
    public ResponseEntity<DashboardResponseDTO>
    getDashboard() {

        return ResponseEntity.ok(
                dashboardService.getDashboardData()
        );
    }
}