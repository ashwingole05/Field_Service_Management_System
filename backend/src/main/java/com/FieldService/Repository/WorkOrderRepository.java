package com.FieldService.Repository;

import com.FieldService.Entity.WorkOrder;
import com.FieldService.ENUM.WorkOrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface WorkOrderRepository
        extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findBySiteId(Long siteId);

    List<WorkOrder> findByAssignedTechnicianId(Long technicianId);

    List<WorkOrder> findByStatus(WorkOrderStatus status);

    long countByStatus(WorkOrderStatus status);

    List<WorkOrder> findByScheduledAtBeforeAndStatusNotIn(
            LocalDateTime time,
            Collection<WorkOrderStatus> statuses
    );

    long countByScheduledAtBeforeAndStatusNotIn(
            LocalDateTime time,
            Collection<WorkOrderStatus> statuses
    );
}