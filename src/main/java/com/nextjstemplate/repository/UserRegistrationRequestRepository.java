package com.nextjstemplate.repository;

import com.nextjstemplate.domain.UserRegistrationRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserRegistrationRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRegistrationRequestRepository
    extends JpaRepository<UserRegistrationRequest, Long>, JpaSpecificationExecutor<UserRegistrationRequest> {}
