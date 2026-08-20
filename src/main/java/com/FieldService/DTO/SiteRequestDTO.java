package com.FieldService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteRequestDTO {

    private String siteName;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private Long customerId;
}