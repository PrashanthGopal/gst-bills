package com.religate.gstbills.repository;

import com.religate.gstbills.domain.OrgUsers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrgUsers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUsersRepository extends JpaRepository<OrgUsers, Long> {}
