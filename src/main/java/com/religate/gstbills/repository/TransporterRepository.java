package com.religate.gstbills.repository;

import com.religate.gstbills.domain.Transporter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transporter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransporterRepository extends JpaRepository<Transporter, Long> {}
