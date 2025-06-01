package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTicketTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTicketTransactionRepository
    extends JpaRepository<EventTicketTransaction, Long>, JpaSpecificationExecutor<EventTicketTransaction> {}
