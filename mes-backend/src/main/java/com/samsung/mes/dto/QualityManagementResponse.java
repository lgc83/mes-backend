package com.samsung.mes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.samsung.mes.entity.QualityManagement;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QualityManagementResponse {

    private Long id;
    private String inspectionStandard;
    private String processInspection;
    private String defectManagement;
    private String qualityHistory;

    public static QualityManagementResponse from(QualityManagement entity) {
        return QualityManagementResponse.builder()
                .id(entity.getId())
                .inspectionStandard(entity.getInspectionStandard())
                .processInspection(entity.getProcessInspection())
                .defectManagement(entity.getDefectManagement())
                .qualityHistory(entity.getQualityHistory())
                .build();
    }
}