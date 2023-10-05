package com.religate.gstbills.repository;

import com.religate.gstbills.domain.Clients;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Clients entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {}
