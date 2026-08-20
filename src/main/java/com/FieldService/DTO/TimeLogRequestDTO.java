package com.FieldService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeLogRequestDTO {

    private Long workOrderId;

    private Long technicianId;

    private String notes;
}