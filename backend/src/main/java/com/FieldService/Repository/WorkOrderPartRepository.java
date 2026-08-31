package com.FieldService.Repository;

import com.FieldService.Entity.WorkOrderPart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderPartRepository
        extends JpaRepository<WorkOrderPart, Long> {

    List<WorkOrderPart> findByWorkOrderId(Long workOrderId);

    List<WorkOrderPart> findByPartId(Long partId);
}