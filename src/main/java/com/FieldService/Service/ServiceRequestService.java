package com.FieldService.Service;

import com.FieldService.DTO.ServiceRequestDTO;
import com.FieldService.Entity.ServiceRequest;
import com.FieldService.Entity.WorkOrder;
import com.FieldService.ENUM.Priority;
import com.FieldService.ENUM.ServiceRequestStatus;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Repository.CustomerRepository;
import com.FieldService.Repository.ServiceRequestRepository;
import com.FieldService.Repository.SiteRepository;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final CustomerRepository customerRepository;
    private final SiteRepository siteRepository;
    private final WorkOrderRepository workOrderRepository;

    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            CustomerRepository customerRepository,
            SiteRepository siteRepository,
            WorkOrderRepository workOrderRepository) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.customerRepository = customerRepository;
        this.siteRepository = siteRepository;
        this.workOrderRepository = workOrderRepository;
    }

    public ServiceRequest createRequest(
            ServiceRequestDTO dto) {

        if (!customerRepository.existsById(dto.getCustomerId())) {
            throw new RuntimeException("Customer not found");
        }

        if (dto.getSiteId() != null
                && !siteRepository.existsById(dto.getSiteId())) {

            throw new RuntimeException("Site not found");
        }

        ServiceRequest request =
                ServiceRequest.builder()
                        .customerId(dto.getCustomerId())
                        .siteId(dto.getSiteId())
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .status(ServiceRequestStatus.OPEN)
                        .build();

        return serviceRequestRepository.save(request);
    }

    public List<ServiceRequest> getAllRequests() {

        return serviceRequestRepository.findAll();
    }

    public ServiceRequest getRequestById(Long id) {

        return serviceRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Service request not found with id: " + id
                        )
                );
    }

    public List<ServiceRequest> getByCustomer(
            Long customerId) {

        return serviceRequestRepository
                .findByCustomerId(customerId);
    }

    public List<ServiceRequest> getByStatus(
            ServiceRequestStatus status) {

        return serviceRequestRepository
                .findByStatus(status);
    }

    public ServiceRequest markInReview(Long id) {

        ServiceRequest request =
                getRequestById(id);

        if (request.getStatus()
                != ServiceRequestStatus.OPEN) {

            throw new RuntimeException(
                    "Only OPEN requests can be moved to review"
            );
        }

        request.setStatus(
                ServiceRequestStatus.IN_REVIEW
        );

        return serviceRequestRepository.save(request);
    }

    @Transactional
    public ServiceRequest convertToWorkOrder(
            Long requestId) {

        ServiceRequest request =
                getRequestById(requestId);

        if (request.getStatus()
                == ServiceRequestStatus.CONVERTED_TO_WORK_ORDER) {

            throw new RuntimeException(
                    "Service request is already converted"
            );
        }

        if (request.getStatus()
                == ServiceRequestStatus.CLOSED
                || request.getStatus()
                == ServiceRequestStatus.CANCELLED) {

            throw new RuntimeException(
                    "Closed or cancelled request cannot be converted"
            );
        }

        WorkOrder workOrder =
                WorkOrder.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .priority(Priority.MEDIUM)
                        .status(WorkOrderStatus.OPEN)
                        .siteId(request.getSiteId())
                        .assignedTechnicianId(null)
                        .build();

        WorkOrder savedWorkOrder =
                workOrderRepository.save(workOrder);

        request.setWorkOrderId(
                savedWorkOrder.getId()
        );

        request.setStatus(
                ServiceRequestStatus.CONVERTED_TO_WORK_ORDER
        );

        return serviceRequestRepository.save(request);
    }

    public ServiceRequest closeRequest(Long id) {

        ServiceRequest request =
                getRequestById(id);

        request.setStatus(
                ServiceRequestStatus.CLOSED
        );

        return serviceRequestRepository.save(request);
    }

    public ServiceRequest cancelRequest(Long id) {

        ServiceRequest request =
                getRequestById(id);

        if (request.getStatus()
                == ServiceRequestStatus.CONVERTED_TO_WORK_ORDER) {

            throw new RuntimeException(
                    "Converted request cannot be cancelled"
            );
        }

        request.setStatus(
                ServiceRequestStatus.CANCELLED
        );

        return serviceRequestRepository.save(request);
    }
}