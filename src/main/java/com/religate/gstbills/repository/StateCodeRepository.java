package com.religate.gstbills.repository;

import com.religate.gstbills.domain.StateCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StateCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateCodeRepository extends JpaRepository<StateCode, Long> {}
