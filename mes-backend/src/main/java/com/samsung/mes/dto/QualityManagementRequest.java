package com.samsung.mes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityManagementRequest {

    private String inspectionStandard;
    private String processInspection;
    private String defectManagement;
    private String qualityHistory;
}