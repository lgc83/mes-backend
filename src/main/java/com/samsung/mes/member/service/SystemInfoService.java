package com.samsung.mes.member.service;

import com.samsung.mes.member.dto.SystemInfoRequest;
import com.samsung.mes.member.dto.SystemInfoResponse;
import com.samsung.mes.member.entity.SystemInfo;
import com.samsung.mes.member.repository.SystemInfoRepository;
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

    //필수로 기억 모든 리스트는 페이징
    public Page<SystemInfoResponse> list (Pageable pageable) {
        return repo.findAll(pageable).map(this::toRes);
    }

    public SystemInfoResponse getOne(Long id){
        SystemInfo e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("시스템 정보가 없습니다. id=" + id));
        return toRes(e);
    }

    @Transactional
    public SystemInfoResponse create(SystemInfoRequest req){
        SystemInfo e = new SystemInfo();
        applyReq(e, req);
        return toRes(repo.save(e));
    }

    @Transactional
    public SystemInfoResponse update(Long id, SystemInfoRequest req){
        SystemInfo e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("시스템 정보가 없습니다. id=" + id));
        applyReq(e, req);
        return toRes(e);
    }

    @Transactional
    public void delete(Long id){
        if(!repo.existsById(id)){
            throw new NoSuchElementException("KPI가 없습니다. id=" + id);
        }
        repo.deleteById(id);
    }


    private void applyReq(SystemInfo e, SystemInfoRequest req) {
        e.setSystemCode(req.getSystemCode());
        e.setSystemName(req.getSystemName());
        e.setSystemGroup(req.getSystemGroup());
        e.setOwner(req.getOwner());
        e.setVersion(req.getVersion());
        e.setStatus(req.getStatus());
        e.setUseYn(req.getUseYn());
        e.setRemark(req.getRemark());
    }

    private SystemInfoResponse toRes(SystemInfo e) {
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
                .build();
    }
}