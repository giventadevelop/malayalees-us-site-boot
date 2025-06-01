package com.nextjstemplate.repository;

import com.nextjstemplate.domain.BulkOperationLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BulkOperationLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BulkOperationLogRepository extends JpaRepository<BulkOperationLog, Long>, JpaSpecificationExecutor<BulkOperationLog> {}
