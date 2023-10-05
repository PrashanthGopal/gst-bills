package com.religate.gstbills.repository;

import com.religate.gstbills.domain.InvoiceItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceItemsRepository extends JpaRepository<InvoiceItems, Long> {}
