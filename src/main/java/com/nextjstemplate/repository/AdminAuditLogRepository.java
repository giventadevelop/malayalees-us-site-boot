package com.nextjstemplate.repository;

import com.nextjstemplate.domain.AdminAuditLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdminAuditLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {}
