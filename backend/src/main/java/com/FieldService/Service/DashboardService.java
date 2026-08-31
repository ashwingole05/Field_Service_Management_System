package com.FieldService.Service;

import com.FieldService.DTO.DashboardResponseDTO;
import com.FieldService.ENUM.ServiceRequestStatus;
import com.FieldService.ENUM.WorkOrderStatus;
import com.FieldService.Repository.CustomerRepository;
import com.FieldService.Repository.PartRepository;
import com.FieldService.Repository.ServiceRequestRepository;
import com.FieldService.Repository.SiteRepository;
import com.FieldService.Repository.WorkOrderRepository;

import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final WorkOrderRepository workOrderRepository;
    private final CustomerRepository customerRepository;
    private final SiteRepository siteRepository;
    private final PartRepository partRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final SLAService slaService;

    public DashboardService(
            WorkOrderRepository workOrderRepository,
            CustomerRepository customerRepository,
            SiteRepository siteRepository,
            PartRepository partRepository,
            ServiceRequestRepository serviceRequestRepository,
            SLAService slaService) {

        this.workOrderRepository = workOrderRepository;
        this.customerRepository = customerRepository;
        this.siteRepository = siteRepository;
        this.partRepository = partRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.slaService = slaService;
    }

    public DashboardResponseDTO getDashboardData() {

        long totalWorkOrders =
                workOrderRepository.count();

        long openWorkOrders =
                workOrderRepository.countByStatus(
                        WorkOrderStatus.OPEN
                );

        long assignedWorkOrders =
                workOrderRepository.countByStatus(
                        WorkOrderStatus.ASSIGNED
                );

        long inProgressWorkOrders =
                workOrderRepository.countByStatus(
                        WorkOrderStatus.IN_PROGRESS
                );

        long completedWorkOrders =
                workOrderRepository.countByStatus(
                        WorkOrderStatus.COMPLETED
                );

        long closedWorkOrders =
                workOrderRepository.countByStatus(
                        WorkOrderStatus.CLOSED
                );

        long totalCustomers =
                customerRepository.count();

        long totalSites =
                siteRepository.count();

        long totalParts =
                partRepository.count();

        long totalServiceRequests =
                serviceRequestRepository.count();

        long openServiceRequests =
                serviceRequestRepository.countByStatus(
                        ServiceRequestStatus.OPEN
                );

        long inReviewServiceRequests =
                serviceRequestRepository.countByStatus(
                        ServiceRequestStatus.IN_REVIEW
                );

        long overdueWorkOrders =
                slaService.getOverdueCount();

        return DashboardResponseDTO.builder()
                .totalWorkOrders(totalWorkOrders)
                .openWorkOrders(openWorkOrders)
                .assignedWorkOrders(assignedWorkOrders)
                .inProgressWorkOrders(inProgressWorkOrders)
                .completedWorkOrders(completedWorkOrders)
                .closedWorkOrders(closedWorkOrders)
                .totalCustomers(totalCustomers)
                .totalSites(totalSites)
                .totalParts(totalParts)
                .totalServiceRequests(totalServiceRequests)
                .openServiceRequests(openServiceRequests)
                .inReviewServiceRequests(inReviewServiceRequests)
                .overdueWorkOrders(overdueWorkOrders)
                .build();
    }
}