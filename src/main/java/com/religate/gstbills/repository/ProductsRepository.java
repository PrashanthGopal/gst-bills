package com.religate.gstbills.repository;

import com.religate.gstbills.domain.Products;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Products entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {}
