package com.FieldService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDTO {

    @NotNull(
            message = "Site ID is required"
    )
    private Long siteId;

    @NotBlank(
            message = "Title is required"
    )
    private String title;

    private String description;
}
