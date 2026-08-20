package com.FieldService.Repository;

import com.FieldService.Entity.ServiceRequest;
import com.FieldService.ENUM.ServiceRequestStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByCustomerId(Long customerId);

    List<ServiceRequest> findBySiteId(Long siteId);

    List<ServiceRequest> findByStatus(
            ServiceRequestStatus status
    );

    long countByStatus(
            ServiceRequestStatus status
    );
}