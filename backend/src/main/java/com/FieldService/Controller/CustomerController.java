package com.FieldService.Controller;

import com.FieldService.DTO.CustomerRequestDTO;
import com.FieldService.DTO.CustomerResponseDTO;
import com.FieldService.Service.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    // CREATE
    @PreAuthorize("hasAuthority('CREATE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @RequestBody CustomerRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.createCustomer(request));
    }


    // GET ALL
    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {

        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }


    // GET BY ID
    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.getCustomerById(id)
        );
    }


    // UPDATE
    @PreAuthorize("hasAuthority('UPDATE_CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequestDTO request) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, request)
        );
    }


    // DELETE
    @PreAuthorize("hasAuthority('DELETE_CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.deleteCustomer(id)
        );
    }
}
