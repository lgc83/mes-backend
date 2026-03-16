package com.samsung.mes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.samsung.mes.dto.QualityManagementRequest;
import com.samsung.mes.entity.QualityManagement;
import com.samsung.mes.repository.QualityManagementRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QualityManagementService {

    private final QualityManagementRepository qualityManagementRepository;

    public QualityManagement create(QualityManagementRequest request) {
        validate(request);

        QualityManagement entity = new QualityManagement();
        entity.setInspectionStandard(request.getInspectionStandard().trim());
        entity.setProcessInspection(request.getProcessInspection().trim());
        entity.setDefectManagement(request.getDefectManagement().trim());
        entity.setQualityHistory(request.getQualityHistory().trim());

        return qualityManagementRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Page<QualityManagement> getList(Pageable pageable) {
        return qualityManagementRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public QualityManagement getById(Long id) {
        return qualityManagementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 품질관리 데이터가 없습니다. id=" + id));
    }

    public QualityManagement update(Long id, QualityManagementRequest request) {
        validate(request);

        QualityManagement entity = getById(id);
        entity.setInspectionStandard(request.getInspectionStandard().trim());
        entity.setProcessInspection(request.getProcessInspection().trim());
        entity.setDefectManagement(request.getDefectManagement().trim());
        entity.setQualityHistory(request.getQualityHistory().trim());

        return entity;
    }

    public void delete(Long id) {
        QualityManagement entity = getById(id);
        qualityManagementRepository.delete(entity);
    }

    private void validate(QualityManagementRequest request) {
        if (request.getInspectionStandard() == null || request.getInspectionStandard().isBlank()) {
            throw new IllegalArgumentException("검사기준관리를 입력하세요.");
        }
        if (request.getProcessInspection() == null || request.getProcessInspection().isBlank()) {
            throw new IllegalArgumentException("공정검사를 입력하세요.");
        }
        if (request.getDefectManagement() == null || request.getDefectManagement().isBlank()) {
            throw new IllegalArgumentException("불량관리를 입력하세요.");
        }
        if (request.getQualityHistory() == null || request.getQualityHistory().isBlank()) {
            throw new IllegalArgumentException("품질이력을 입력하세요.");
        }
    }
}