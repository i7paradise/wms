package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.CompanyProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyProductRepository extends JpaRepository<CompanyProduct, Long> {}
