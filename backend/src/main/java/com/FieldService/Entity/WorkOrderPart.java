package com.FieldService.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_order_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long workOrderId;

    @Column(nullable = false)
    private Long partId;

    @Column(nullable = false)
    private Integer quantityUsed;

    private LocalDateTime usedAt;

    @PrePersist
    protected void onCreate() {

        if (usedAt == null) {
            usedAt = LocalDateTime.now();
        }
    }
}