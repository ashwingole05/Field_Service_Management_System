package com.FieldService.Controller;

import com.FieldService.DTO.ServiceRequestDTO;

import com.FieldService.Entity.ServiceRequest;

import com.FieldService.ENUM.ServiceRequestStatus;

import com.FieldService.Service.ServiceRequestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/service-requests"
)
public class ServiceRequestController {

    private final ServiceRequestService
            serviceRequestService;


    public ServiceRequestController(
            ServiceRequestService serviceRequestService) {

        this.serviceRequestService =
                serviceRequestService;
    }


    // =========================================
    // CUSTOMER - CREATE
    // =========================================

    @PreAuthorize(
            "hasAuthority('RAISE_REQUEST')"
    )
    @PostMapping
    public ResponseEntity<ServiceRequest>
    createRequest(
            @Valid
            @RequestBody
            ServiceRequestDTO dto,

            Authentication authentication) {

        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        serviceRequestService
                                .createRequest(
                                        dto,
                                        authentication
                                                .getName()
                                )
                );
    }


    // =========================================
    // CUSTOMER - VIEW OWN REQUESTS
    // =========================================

    @PreAuthorize(
            "hasAuthority('VIEW_OWN_REQUEST')"
    )
    @GetMapping("/mine")
    public ResponseEntity<List<ServiceRequest>>
    getMyRequests(
            Authentication authentication) {

        return ResponseEntity.ok(
                serviceRequestService
                        .getMyRequests(
                                authentication
                                        .getName()
                        )
        );
    }


    // =========================================
    // MANAGER / DISPATCHER - VIEW ALL
    // =========================================

    @PreAuthorize(
            "hasAuthority('REVIEW_REQUEST')"
    )
    @GetMapping
    public ResponseEntity<List<ServiceRequest>>
    getAllRequests() {

        return ResponseEntity.ok(
                serviceRequestService
                        .getAllRequests()
        );
    }


    // =========================================
    // MANAGER / DISPATCHER - CUSTOMER FILTER
    // =========================================

    @PreAuthorize(
            "hasAuthority('REVIEW_REQUEST')"
    )
    @GetMapping(
            "/customer/{customerId}"
    )
    public ResponseEntity<List<ServiceRequest>>
    getByCustomer(
            @PathVariable
            Long customerId) {

        return ResponseEntity.ok(
                serviceRequestService
                        .getByCustomer(
                                customerId
                        )
        );
    }


    // =========================================
    // MANAGER / DISPATCHER - STATUS FILTER
    // =========================================

    @PreAuthorize(
            "hasAuthority('REVIEW_REQUEST')"
    )
    @GetMapping(
            "/status/{status}"
    )
    public ResponseEntity<List<ServiceRequest>>
    getByStatus(
            @PathVariable
            ServiceRequestStatus status) {

        return ResponseEntity.ok(
                serviceRequestService
                        .getByStatus(
                                status
                        )
        );
    }


    // =========================================
    // MANAGER / DISPATCHER - GET BY ID
    // =========================================

    @PreAuthorize(
            "hasAuthority('REVIEW_REQUEST')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest>
    getById(
            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                serviceRequestService
                        .getRequestById(
                                id
                        )
        );
    }


    // =========================================
    // REVIEW
    // =========================================

    @PreAuthorize(
            "hasAuthority('REVIEW_REQUEST')"
    )
    @PutMapping("/{id}/review")
    public ResponseEntity<ServiceRequest>
    reviewRequest(
            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                serviceRequestService
                        .markInReview(
                                id
                        )
        );
    }


    // =========================================
    // CONVERT
    // =========================================

    @PreAuthorize(
            "hasAuthority('CONVERT_REQUEST')"
    )
    @PostMapping("/{id}/convert")
    public ResponseEntity<ServiceRequest>
    convertToWorkOrder(
            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                serviceRequestService
                        .convertToWorkOrder(
                                id
                        )
        );
    }


    // =========================================
    // CLOSE
    // =========================================

    @PreAuthorize(
            "hasAuthority('CLOSE_REQUEST')"
    )
    @PutMapping("/{id}/close")
    public ResponseEntity<ServiceRequest>
    closeRequest(
            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                serviceRequestService
                        .closeRequest(
                                id
                        )
        );
    }


    // =========================================
    // CANCEL
    // =========================================

    @PreAuthorize(
            "hasAuthority('CANCEL_REQUEST')"
    )
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ServiceRequest>
    cancelRequest(
            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                serviceRequestService
                        .cancelRequest(
                                id
                        )
        );
    }
}