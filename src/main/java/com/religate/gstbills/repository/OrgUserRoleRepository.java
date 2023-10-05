package com.religate.gstbills.repository;

import com.religate.gstbills.domain.OrgUserRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrgUserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUserRoleRepository extends JpaRepository<OrgUserRole, Long> {}
