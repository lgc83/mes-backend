package com.samsung.mes.member.service;

import com.samsung.mes.member.dto.StandardRequest;
import com.samsung.mes.member.dto.StandardResponse;
import com.samsung.mes.member.entity.Standard;
import com.samsung.mes.member.repository.StandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StandardService {
    private final StandardRepository repo;

    public Page<StandardResponse> list (Pageable pageable) {
        return repo.findAll(pageable).map(this::toRes);
    }

    public StandardResponse getOne(Long id){
        Standard e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Standard가 없습니다. id=" + id));
        return toRes(e);
    }

    @Transactional
    public StandardResponse create(StandardRequest req){
        Standard e = new Standard();
        applyReq(e, req);
        return toRes(repo.save(e));
    }

    @Transactional
    public StandardResponse update(Long id, StandardRequest req){
        Standard e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Standard가 없습니다. id=" + id));
        applyReq(e, req);
        return toRes(e);
    }

    @Transactional
    public void delete(Long id){
        if(!repo.existsById(id)){
            throw new NoSuchElementException("Standard가 없습니다. id=" + id);
        }
        repo.deleteById(id);
    }

    private void applyReq(Standard e, StandardRequest req) {
        e.setStdCode(req.getStdCode());
        e.setStdName(req.getStdName());
        e.setStdGroup(req.getStdGroup());
        e.setUnit(req.getUnit());
        e.setUseYn(req.getUseYn());
        e.setRemark(req.getRemark());
    }

    private StandardResponse toRes(Standard e) {
        return StandardResponse.builder()
                .id(e.getId())
                .stdCode(e.getStdCode())
                .stdName(e.getStdName())
                .stdGroup(e.getStdGroup())
                .unit(e.getUnit())
                .useYn(e.getUseYn())
                .remark(e.getRemark())
                .build();
    }

}