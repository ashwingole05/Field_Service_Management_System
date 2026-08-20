package com.FieldService.Service;

import com.FieldService.DTO.WorkOrderPartRequestDTO;
import com.FieldService.Entity.Part;
import com.FieldService.Entity.WorkOrder;
import com.FieldService.Entity.WorkOrderPart;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Repository.PartRepository;
import com.FieldService.Repository.WorkOrderPartRepository;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkOrderPartService {

    private final WorkOrderPartRepository workOrderPartRepository;
    private final WorkOrderRepository workOrderRepository;
    private final PartRepository partRepository;

    public WorkOrderPartService(
            WorkOrderPartRepository workOrderPartRepository,
            WorkOrderRepository workOrderRepository,
            PartRepository partRepository) {

        this.workOrderPartRepository = workOrderPartRepository;
        this.workOrderRepository = workOrderRepository;
        this.partRepository = partRepository;
    }

    // USE PART ON WORK ORDER
    @Transactional
    public WorkOrderPart usePart(
            WorkOrderPartRequestDTO dto) {

        WorkOrder workOrder =
                workOrderRepository.findById(
                        dto.getWorkOrderId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Work order not found"
                        )
                );

        if (workOrder.getStatus() == WorkOrderStatus.CLOSED
                || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Parts cannot be added to a closed or cancelled work order"
            );
        }

        Part part =
                partRepository.findById(
                        dto.getPartId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Part not found"
                        )
                );

        if (dto.getQuantityUsed() == null
                || dto.getQuantityUsed() <= 0) {

            throw new RuntimeException(
                    "Quantity used must be greater than 0"
            );
        }

        if (part.getQuantity() < dto.getQuantityUsed()) {

            throw new RuntimeException(
                    "Insufficient stock. Available quantity: "
                            + part.getQuantity()
            );
        }

        // REDUCE STOCK
        part.setQuantity(
                part.getQuantity()
                        - dto.getQuantityUsed()
        );

        partRepository.save(part);

        // SAVE USAGE
        WorkOrderPart workOrderPart =
                WorkOrderPart.builder()
                        .workOrderId(dto.getWorkOrderId())
                        .partId(dto.getPartId())
                        .quantityUsed(dto.getQuantityUsed())
                        .build();

        return workOrderPartRepository.save(
                workOrderPart
        );
    }

    // GET PARTS USED ON WORK ORDER
    public List<WorkOrderPart> getPartsByWorkOrder(
            Long workOrderId) {

        return workOrderPartRepository
                .findByWorkOrderId(workOrderId);
    }

    // GET ALL USAGE
    public List<WorkOrderPart> getAllUsage() {

        return workOrderPartRepository.findAll();
    }
}