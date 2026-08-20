package com.FieldService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {

    private Long id;

    private String companyName;

    private String contactPerson;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String postalCode;

    private boolean active;
}