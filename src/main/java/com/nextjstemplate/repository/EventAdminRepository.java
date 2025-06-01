package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventAdmin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAdmin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAdminRepository extends JpaRepository<EventAdmin, Long>, JpaSpecificationExecutor<EventAdmin> {}
