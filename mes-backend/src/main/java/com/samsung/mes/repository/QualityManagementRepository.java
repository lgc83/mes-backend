package com.samsung.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.samsung.mes.entity.QualityManagement;

@Repository
public interface QualityManagementRepository extends JpaRepository<QualityManagement, Long> {
}