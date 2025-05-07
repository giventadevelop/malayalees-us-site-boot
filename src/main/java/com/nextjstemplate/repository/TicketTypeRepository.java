package com.nextjstemplate.repository;

import com.nextjstemplate.domain.TicketType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long>, JpaSpecificationExecutor<TicketType> {}
