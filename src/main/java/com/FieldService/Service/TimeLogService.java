package com.FieldService.Service;

import com.FieldService.DTO.TimeLogRequestDTO;
import com.FieldService.Entity.TimeLog;
import com.FieldService.Entity.WorkOrder;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Repository.TimeLogRepository;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final WorkOrderRepository workOrderRepository;

    public TimeLogService(
            TimeLogRepository timeLogRepository,
            WorkOrderRepository workOrderRepository) {

        this.timeLogRepository = timeLogRepository;
        this.workOrderRepository = workOrderRepository;
    }

    public TimeLog startTimeLog(TimeLogRequestDTO dto) {

        WorkOrder workOrder = workOrderRepository
                .findById(dto.getWorkOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Work order not found")
                );

        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new RuntimeException(
                    "Work order must be IN_PROGRESS before logging time"
            );
        }

        TimeLog timeLog = TimeLog.builder()
                .workOrderId(dto.getWorkOrderId())
                .technicianId(dto.getTechnicianId())
                .startTime(LocalDateTime.now())
                .notes(dto.getNotes())
                .build();

        return timeLogRepository.save(timeLog);
    }

    public TimeLog stopTimeLog(Long id) {

        TimeLog timeLog = timeLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Time log not found")
                );

        if (timeLog.getEndTime() != null) {
            throw new RuntimeException(
                    "Time log already stopped"
            );
        }

        LocalDateTime endTime = LocalDateTime.now();

        timeLog.setEndTime(endTime);

        long minutes = Duration.between(
                timeLog.getStartTime(),
                endTime
        ).toMinutes();

        timeLog.setTotalMinutes(minutes);

        return timeLogRepository.save(timeLog);
    }

    public List<TimeLog> getAllTimeLogs() {
        return timeLogRepository.findAll();
    }

    public List<TimeLog> getByWorkOrder(Long workOrderId) {

        return timeLogRepository
                .findByWorkOrderId(workOrderId);
    }

    public List<TimeLog> getByTechnician(Long technicianId) {

        return timeLogRepository
                .findByTechnicianId(technicianId);
    }
}