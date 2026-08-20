package com.FieldService.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponseDTO {

    private long totalWorkOrders;
    private long openWorkOrders;
    private long assignedWorkOrders;
    private long inProgressWorkOrders;
    private long completedWorkOrders;
    private long closedWorkOrders;

    private long totalCustomers;
    private long totalSites;
    private long totalParts;

    private long totalServiceRequests;
    private long openServiceRequests;
    private long inReviewServiceRequests;
    private long overdueWorkOrders;
}