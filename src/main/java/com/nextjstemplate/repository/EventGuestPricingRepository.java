package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventGuestPricing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventGuestPricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventGuestPricingRepository extends JpaRepository<EventGuestPricing, Long>, JpaSpecificationExecutor<EventGuestPricing> {}
