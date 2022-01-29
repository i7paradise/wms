package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.CompanyContainer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyContainer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyContainerRepository extends JpaRepository<CompanyContainer, Long> {}
