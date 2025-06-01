package com.nextjstemplate.repository;

import com.nextjstemplate.domain.UserPaymentTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserPaymentTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPaymentTransactionRepository
    extends JpaRepository<UserPaymentTransaction, Long>, JpaSpecificationExecutor<UserPaymentTransaction> {}
