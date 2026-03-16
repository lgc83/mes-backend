package com.samsung.mes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quality_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QualityManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inspection_standard", nullable = false, length = 500)
    private String inspectionStandard;

    @Column(name = "process_inspection", nullable = false, length = 500)
    private String processInspection;

    @Column(name = "defect_management", nullable = false, length = 500)
    private String defectManagement;

    @Column(name = "quality_history", nullable = false, length = 500)
    private String qualityHistory;
}