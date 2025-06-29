package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketTransactionItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTicketTransactionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTicketTransactionItemRepository
    extends JpaRepository<EventTicketTransactionItem, Long>, JpaSpecificationExecutor<EventTicketTransactionItem> {}
