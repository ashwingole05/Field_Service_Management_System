package com.FieldService.DTO;

import com.FieldService.ENUM.Priority;
import com.FieldService.ENUM.WorkOrderStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private WorkOrderStatus status;

    @NotNull(message = "Site ID is required")
    private Long siteId;

    private Long assignedTechnicianId;

    private LocalDateTime scheduledAt;
}