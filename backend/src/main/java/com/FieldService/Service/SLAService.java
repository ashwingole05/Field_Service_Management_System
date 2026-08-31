package com.FieldService.Service;

import com.FieldService.Entity.WorkOrder;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SLAService {

    private final WorkOrderRepository workOrderRepository;

    public SLAService(
            WorkOrderRepository workOrderRepository) {

        this.workOrderRepository = workOrderRepository;
    }

    public List<WorkOrder> getOverdueWorkOrders() {

        List<WorkOrderStatus> excludedStatuses = List.of(
                WorkOrderStatus.COMPLETED,
                WorkOrderStatus.CLOSED,
                WorkOrderStatus.CANCELLED
        );

        return workOrderRepository
                .findByScheduledAtBeforeAndStatusNotIn(
                        LocalDateTime.now(),
                        excludedStatuses
                );
    }

    public long getOverdueCount() {

        List<WorkOrderStatus> excludedStatuses = List.of(
                WorkOrderStatus.COMPLETED,
                WorkOrderStatus.CLOSED,
                WorkOrderStatus.CANCELLED
        );

        return workOrderRepository
                .countByScheduledAtBeforeAndStatusNotIn(
                        LocalDateTime.now(),
                        excludedStatuses
                );
    }
}