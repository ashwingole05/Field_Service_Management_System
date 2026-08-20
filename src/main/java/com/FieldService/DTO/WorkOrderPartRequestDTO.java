package com.FieldService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderPartRequestDTO {

    private Long workOrderId;

    private Long partId;

    private Integer quantityUsed;
}