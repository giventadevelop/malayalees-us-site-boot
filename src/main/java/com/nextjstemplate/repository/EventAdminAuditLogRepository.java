package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventAdminAuditLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAdminAuditLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAdminAuditLogRepository
    extends JpaRepository<EventAdminAuditLog, Long>, JpaSpecificationExecutor<EventAdminAuditLog> {}
