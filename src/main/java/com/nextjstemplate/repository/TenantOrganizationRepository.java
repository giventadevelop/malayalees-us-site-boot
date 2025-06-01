package com.nextjstemplate.repository;

import com.nextjstemplate.domain.TenantOrganization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantOrganization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantOrganizationRepository
    extends JpaRepository<TenantOrganization, Long>, JpaSpecificationExecutor<TenantOrganization> {}
