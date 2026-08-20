package com.FieldService.Controller;

import com.FieldService.DTO.TimeLogRequestDTO;
import com.FieldService.Entity.TimeLog;
import com.FieldService.Service.TimeLogService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-logs")
public class TimeLogController {

    private final TimeLogService timeLogService;

    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @PreAuthorize("hasAuthority('ADD_LOG_TIME')")
    @PostMapping("/start")
    public ResponseEntity<TimeLog> startTimeLog(
            @RequestBody TimeLogRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(timeLogService.startTimeLog(dto));
    }
    @PreAuthorize("hasAuthority('ADD_LOG_TIME')")
    @PutMapping("/{id}/stop")
    public ResponseEntity<TimeLog> stopTimeLog(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                timeLogService.stopTimeLog(id)
        );
    }
    @PreAuthorize("hasAuthority('VIEW_LOG_TIME')")
    @GetMapping
    public ResponseEntity<List<TimeLog>> getAllTimeLogs() {

        return ResponseEntity.ok(
                timeLogService.getAllTimeLogs()
        );
    }

    @GetMapping("/workorder/{workOrderId}")
    public ResponseEntity<List<TimeLog>> getByWorkOrder(
            @PathVariable Long workOrderId) {

        return ResponseEntity.ok(
                timeLogService.getByWorkOrder(workOrderId)
        );
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<TimeLog>> getByTechnician(
            @PathVariable Long technicianId) {

        return ResponseEntity.ok(
                timeLogService.getByTechnician(technicianId)
        );
    }
}