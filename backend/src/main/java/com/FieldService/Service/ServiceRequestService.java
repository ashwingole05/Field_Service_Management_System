package com.FieldService.Service;

import com.FieldService.DTO.ServiceRequestDTO;

import com.FieldService.Entity.ServiceRequest;
import com.FieldService.Entity.UserAuth;
import com.FieldService.Entity.WorkOrder;

import com.FieldService.ENUM.Priority;
import com.FieldService.ENUM.Role;
import com.FieldService.ENUM.ServiceRequestStatus;
import com.FieldService.ENUM.WorkOrderStatus;

import com.FieldService.Repository.ServiceRequestRepository;
import com.FieldService.Repository.SiteRepository;
import com.FieldService.Repository.UserRepository;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository
            serviceRequestRepository;

    private final SiteRepository
            siteRepository;

    private final WorkOrderRepository
            workOrderRepository;

    private final UserRepository
            userRepository;


    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            SiteRepository siteRepository,
            WorkOrderRepository workOrderRepository,
            UserRepository userRepository) {

        this.serviceRequestRepository =
                serviceRequestRepository;

        this.siteRepository =
                siteRepository;

        this.workOrderRepository =
                workOrderRepository;

        this.userRepository =
                userRepository;
    }


    // =========================================
    // CUSTOMER - CREATE REQUEST
    // =========================================

    public ServiceRequest createRequest(
            ServiceRequestDTO dto,
            String userEmail) {

        UserAuth user =
                getCustomerUser(
                        userEmail
                );


        Long customerId =
                user.getCustomerId();


        /*
         * A site is optional in the entity,
         * but if provided it MUST belong to
         * the logged-in customer.
         */
        if (dto.getSiteId()
                != null) {

            boolean belongsToCustomer =
                    siteRepository
                            .existsByIdAndCustomerId(
                                    dto.getSiteId(),
                                    customerId
                            );


            if (!belongsToCustomer) {

                throw new RuntimeException(
                        "Selected site does not belong to your customer account"
                );
            }
        }


        ServiceRequest request =
                ServiceRequest
                        .builder()
                        .customerId(
                                customerId
                        )
                        .siteId(
                                dto.getSiteId()
                        )
                        .title(
                                dto.getTitle()
                        )
                        .description(
                                dto.getDescription()
                        )
                        .status(
                                ServiceRequestStatus.OPEN
                        )
                        .build();


        return serviceRequestRepository
                .save(
                        request
                );
    }


    // =========================================
    // MANAGER / DISPATCHER
    // =========================================

    public List<ServiceRequest>
    getAllRequests() {

        return serviceRequestRepository
                .findAll();
    }


    public ServiceRequest
    getRequestById(
            Long id) {

        return serviceRequestRepository
                .findById(
                        id
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Service request not found with id: "
                                        + id
                        )
                );
    }


    // =========================================
    // CUSTOMER - GET OWN REQUESTS
    // =========================================

    public List<ServiceRequest>
    getMyRequests(
            String userEmail) {

        UserAuth user =
                getCustomerUser(
                        userEmail
                );


        return serviceRequestRepository
                .findByCustomerId(
                        user.getCustomerId()
                );
    }


    // =========================================
    // MANAGER / DISPATCHER FILTER BY CUSTOMER
    // =========================================

    public List<ServiceRequest>
    getByCustomer(
            Long customerId) {

        return serviceRequestRepository
                .findByCustomerId(
                        customerId
                );
    }


    // =========================================
    // FILTER BY STATUS
    // =========================================

    public List<ServiceRequest>
    getByStatus(
            ServiceRequestStatus status) {

        return serviceRequestRepository
                .findByStatus(
                        status
                );
    }


    // =========================================
    // REVIEW
    // =========================================

    public ServiceRequest markInReview(
            Long id) {

        ServiceRequest request =
                getRequestById(
                        id
                );


        if (request.getStatus()
                != ServiceRequestStatus.OPEN) {

            throw new RuntimeException(
                    "Only OPEN requests can be moved to review"
            );
        }


        request.setStatus(
                ServiceRequestStatus.IN_REVIEW
        );


        return serviceRequestRepository
                .save(
                        request
                );
    }


    // =========================================
    // CONVERT TO WORK ORDER
    // =========================================

    @Transactional
    public ServiceRequest
    convertToWorkOrder(
            Long requestId) {

        ServiceRequest request =
                getRequestById(
                        requestId
                );


        if (request.getStatus()
                == ServiceRequestStatus
                .CONVERTED_TO_WORK_ORDER) {

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
                WorkOrder
                        .builder()
                        .title(
                                request.getTitle()
                        )
                        .description(
                                request.getDescription()
                        )
                        .priority(
                                Priority.MEDIUM
                        )
                        .status(
                                WorkOrderStatus.OPEN
                        )
                        .siteId(
                                request.getSiteId()
                        )
                        .assignedTechnicianId(
                                null
                        )
                        .build();


        WorkOrder savedWorkOrder =
                workOrderRepository
                        .save(
                                workOrder
                        );


        request.setWorkOrderId(
                savedWorkOrder.getId()
        );


        request.setStatus(
                ServiceRequestStatus
                        .CONVERTED_TO_WORK_ORDER
        );


        return serviceRequestRepository
                .save(
                        request
                );
    }


    // =========================================
    // CLOSE
    // =========================================

    public ServiceRequest closeRequest(
            Long id) {

        ServiceRequest request =
                getRequestById(
                        id
                );


        if (request.getStatus()
                == ServiceRequestStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cancelled request cannot be closed"
            );
        }


        request.setStatus(
                ServiceRequestStatus.CLOSED
        );


        return serviceRequestRepository
                .save(
                        request
                );
    }


    // =========================================
    // CANCEL
    // =========================================

    public ServiceRequest cancelRequest(
            Long id) {

        ServiceRequest request =
                getRequestById(
                        id
                );


        if (request.getStatus()
                == ServiceRequestStatus
                .CONVERTED_TO_WORK_ORDER) {

            throw new RuntimeException(
                    "Converted request cannot be cancelled"
            );
        }


        if (request.getStatus()
                == ServiceRequestStatus.CLOSED) {

            throw new RuntimeException(
                    "Closed request cannot be cancelled"
            );
        }


        request.setStatus(
                ServiceRequestStatus.CANCELLED
        );


        return serviceRequestRepository
                .save(
                        request
                );
    }


    // =========================================
    // PRIVATE CUSTOMER IDENTITY HELPER
    // =========================================

    private UserAuth getCustomerUser(
            String userEmail) {

        UserAuth user =
                userRepository
                        .findByUserEmail(
                                userEmail
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Logged-in user not found"
                                )
                        );


        if (user.getRole()
                != Role.CUSTOMER) {

            throw new RuntimeException(
                    "Only customer accounts can perform this operation"
            );
        }


        if (user.getCustomerId()
                == null) {

            throw new RuntimeException(
                    "Customer account is not linked to a customer record"
            );
        }


        return user;
    }
}