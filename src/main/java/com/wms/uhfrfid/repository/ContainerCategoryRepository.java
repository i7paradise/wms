package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.ContainerCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContainerCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContainerCategoryRepository extends JpaRepository<ContainerCategory, Long> {}
