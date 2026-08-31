package com.FieldService.Service;

import com.FieldService.DTO.CustomerRequestDTO;
import com.FieldService.DTO.CustomerResponseDTO;
import com.FieldService.Entity.Customer;
import com.FieldService.Repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;


    // CREATE CUSTOMER
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Customer with this email already exists");
        }

        Customer customer = Customer.builder()
                .companyName(request.getCompanyName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .active(true)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return convertToResponse(savedCustomer);
    }


    // GET ALL CUSTOMERS
    public List<CustomerResponseDTO> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // GET CUSTOMER BY ID
    public CustomerResponseDTO getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found with id: " + id)
                );

        return convertToResponse(customer);
    }


    // UPDATE CUSTOMER
    public CustomerResponseDTO updateCustomer(
            Long id,
            CustomerRequestDTO request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found with id: " + id)
                );

        customer.setCompanyName(request.getCompanyName());
        customer.setContactPerson(request.getContactPerson());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPostalCode(request.getPostalCode());

        Customer updatedCustomer = customerRepository.save(customer);

        return convertToResponse(updatedCustomer);
    }


    // DELETE CUSTOMER
    public String deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found with id: " + id)
                );

        customerRepository.delete(customer);

        return "Customer deleted successfully";
    }


    // CONVERT ENTITY TO DTO
    private CustomerResponseDTO convertToResponse(Customer customer) {

        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .companyName(customer.getCompanyName())
                .contactPerson(customer.getContactPerson())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .postalCode(customer.getPostalCode())
                .active(customer.isActive())
                .build();
    }
}