package com.FieldService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long siteId;

    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 3000,
            message = "Description must not exceed 3000 characters")
    private String description;
}