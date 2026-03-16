package com.samsung.mes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.samsung.mes.dto.PageResponse;
import com.samsung.mes.dto.QualityManagementRequest;
import com.samsung.mes.dto.QualityManagementResponse;
import com.samsung.mes.entity.QualityManagement;
import com.samsung.mes.service.QualityManagementService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/quality-management")
public class QualityManagementController {

    private final QualityManagementService qualityManagementService;

    @GetMapping
    public ResponseEntity<PageResponse<QualityManagementResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<QualityManagement> result = qualityManagementService.getList(PageRequest.of(page, size));
        Page<QualityManagementResponse> mapped = result.map(QualityManagementResponse::from);
        return ResponseEntity.ok(PageResponse.from(mapped));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityManagementResponse> getById(@PathVariable Long id) {
        QualityManagement entity = qualityManagementService.getById(id);
        return ResponseEntity.ok(QualityManagementResponse.from(entity));
    }

    @PostMapping
    public ResponseEntity<QualityManagementResponse> create(@RequestBody QualityManagementRequest request) {
        QualityManagement saved = qualityManagementService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(QualityManagementResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QualityManagementResponse> update(
            @PathVariable Long id,
            @RequestBody QualityManagementRequest request
    ) {
        QualityManagement updated = qualityManagementService.update(id, request);
        return ResponseEntity.ok(QualityManagementResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        qualityManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}