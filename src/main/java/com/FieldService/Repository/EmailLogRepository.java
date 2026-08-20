package com.FieldService.Repository;

import com.FieldService.Entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository
        extends JpaRepository<EmailLog, Long> {
}