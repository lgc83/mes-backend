package com.samsung.mes.service;

import com.samsung.mes.dto.SystemInfoRequest;
import com.samsung.mes.dto.SystemInfoResponse;
import com.samsung.mes.entity.SystemInfo;
import com.samsung.mes.repository.SystemInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemInfoService {
    private final SystemInfoRepository repo;

    /**
     * ✅ 목록
     * 기본은 전체 조회
     * useYn 파라미터가 있을 때만 Y/N 필터 적용
     */
    public Page<SystemInfoResponse> list(String useYn, String searchType, String keyword, String status, Pageable pageable) {
        String u = (useYn == null || useYn.isBlank()) ? null : useYn.trim();
        String k = (keyword == null) ? "" : keyword.trim();
        String st = (status == null) ? "" : status.trim();

        Page<SystemInfo> page;

        // 1. 검색/필터 아무것도 없으면 전체 조회
        if (u == null && k.isBlank() && st.isBlank()) {
            page = repo.findAll(pageable);
            return page.map(this::toResponse);
        }

        // 2. useYn만 있고 검색어/상태 없으면 useYn 필터
        if (u != null && k.isBlank() && st.isBlank()) {
            page = repo.findByUseYn(u, pageable);
            return page.map(this::toResponse);
        }

        // 3. useYn + status + 이름검색
        if (u != null && !st.isBlank() && !k.isBlank() &&
                (searchType == null || searchType.isBlank() || "NAME".equalsIgnoreCase(searchType))) {
            page = repo.findByUseYnAndStatusAndSystemNameContainingIgnoreCase(u, st, k, pageable);
            return page.map(this::toResponse);
        }

        // 4. useYn + status 만
        if (u != null && k.isBlank() && !st.isBlank()) {
            page = repo.findByUseYnAndStatus(u, st, pageable);
            return page.map(this::toResponse);
        }

        // 5. useYn + keyword
        if (u != null && !k.isBlank()) {
            String type = (searchType == null || searchType.isBlank()) ? "NAME" : searchType.toUpperCase();

            switch (type) {
                case "GROUP" -> page = repo.findByUseYnAndSystemGroupContainingIgnoreCase(u, k, pageable);
                case "OWNER" -> page = repo.findByUseYnAndOwnerContainingIgnoreCase(u, k, pageable);
                case "STATUS" -> page = repo.findByUseYnAndStatus(u, k.toUpperCase(), pageable);
                default -> page = repo.findByUseYnAndSystemNameContainingIgnoreCase(u, k, pageable);
            }
            return page.map(this::toResponse);
        }

        // 6. useYn 없이 검색/상태가 들어온 경우
        // 현재 Repository에는 전체검색용 메서드가 없으므로 일단 전체 조회
        // 나중에 필요하면 findBySystemNameContainingIgnoreCase 같은 메서드 추가해서 고도화
        page = repo.findAll(pageable);
        return page.map(this::toResponse);
    }

    // ✅ 단건
    public SystemInfoResponse getOne(Long id) {
        SystemInfo e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SystemInfo가 없습니다. id=" + id));
        return toResponse(e);
    }

    // ✅ 등록
    @Transactional
    public SystemInfoResponse create(SystemInfoRequest req) {
        String code = req.getSystemCode().trim();
        if (repo.existsBySystemCode(code)) {
            throw new IllegalArgumentException("이미 존재하는 systemCode 입니다: " + code);
        }

        SystemInfo e = SystemInfo.builder()
                .systemCode(code)
                .systemName(req.getSystemName().trim())
                .systemGroup(req.getSystemGroup().trim())
                .owner(req.getOwner())
                .version(req.getVersion())
                .status(req.getStatus())
                .useYn(req.getUseYn())
                .remark(req.getRemark())
                .build();

        return toResponse(repo.save(e));
    }

    // ✅ 수정
    @Transactional
    public SystemInfoResponse update(Long id, SystemInfoRequest req) {
        SystemInfo e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SystemInfo가 없습니다. id=" + id));

        String newCode = req.getSystemCode().trim();
        if (!e.getSystemCode().equals(newCode) && repo.existsBySystemCode(newCode)) {
            throw new IllegalArgumentException("이미 존재하는 systemCode 입니다: " + newCode);
        }

        e.setSystemCode(newCode);
        e.setSystemName(req.getSystemName().trim());
        e.setSystemGroup(req.getSystemGroup().trim());
        e.setOwner(req.getOwner());
        e.setVersion(req.getVersion());

        if (req.getStatus() != null && !req.getStatus().isBlank()) {
            e.setStatus(req.getStatus().trim());
        }
        if (req.getUseYn() != null && !req.getUseYn().isBlank()) {
            e.setUseYn(req.getUseYn().trim());
        }

        e.setRemark(req.getRemark());

        return toResponse(e);
    }

    // ✅ 삭제(물리)
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("SystemInfo가 없습니다. id=" + id);
        }
        repo.deleteById(id);
    }

    // ✅ 비활성
    @Transactional
    public SystemInfoResponse disable(Long id) {
        SystemInfo e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SystemInfo가 없습니다. id=" + id));
        e.setUseYn("N");
        return toResponse(e);
    }

    private SystemInfoResponse toResponse(SystemInfo e) {
        return SystemInfoResponse.builder()
                .id(e.getId())
                .systemCode(e.getSystemCode())
                .systemName(e.getSystemName())
                .systemGroup(e.getSystemGroup())
                .owner(e.getOwner())
                .version(e.getVersion())
                .status(e.getStatus())
                .useYn(e.getUseYn())
                .remark(e.getRemark())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}