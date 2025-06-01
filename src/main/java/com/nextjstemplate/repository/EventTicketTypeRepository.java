package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTicketTypeRepository extends JpaRepository<EventTicketType, Long>, JpaSpecificationExecutor<EventTicketType> {}
