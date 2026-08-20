package com.FieldService.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long workOrderId;

    @Column(nullable = false)
    private Long technicianId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long totalMinutes;

    @Column(length = 2000)
    private String notes;
}