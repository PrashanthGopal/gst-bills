package com.religate.gstbills.repository;

import com.religate.gstbills.domain.OrdersItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrdersItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersItemsRepository extends JpaRepository<OrdersItems, Long> {}
