package com.samsung.mes.member.service;

import com.samsung.mes.member.dto.KpiRequest;
import com.samsung.mes.member.dto.KpiResponse;
import com.samsung.mes.member.entity.Kpi;
import com.samsung.mes.member.repository.KpiRepository;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KpiService {
    private final KpiRepository repo;

    //필수로 기억 모든 리스트는 페이징
    public Page<KpiResponse> list (Pageable pageable) {
        return repo.findAll(pageable).map(this::toRes);
    }

    public KpiResponse getOne(Long id){
        Kpi e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("KPI가 없습니다. id=" + id));
        return toRes(e);
    }

    @Transactional
    public KpiResponse create(KpiRequest req){
        Kpi e = new Kpi();
        applyReq(e, req);
        return toRes(repo.save(e));
    }

    @Transactional
    public KpiResponse update(Long id, KpiRequest req){
        Kpi e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("KPI가 없습니다. id=" + id));
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


    private void applyReq(Kpi e, KpiRequest req) {
        e.setKpiName(req.getKpiName());
        e.setKpiGroup(req.getKpiGroup());
        e.setOwner(req.getOwner());
        e.setPeriodType(req.getPeriodType());
        e.setPeriodValue(req.getPeriodValue());
        e.setTargetValue(req.getTargetValue());
        e.setActualValue(req.getActualValue());
        e.setUnit(req.getUnit());
        e.setStatus(req.getStatus());
        e.setUseYn(req.getUseYn());
        e.setRemark(req.getRemark());
    }

    private KpiResponse toRes(Kpi e) {
        return KpiResponse.builder()
                .id(e.getId())
                .kpiName(e.getKpiName())
                .kpiGroup(e.getKpiGroup())
                .owner(e.getOwner())
                .periodType(e.getPeriodType())
                .periodValue(e.getPeriodValue())
                .targetValue(e.getTargetValue())
                .actualValue(e.getActualValue())
                .unit(e.getUnit())
                .status(e.getStatus())
                .useYn(e.getUseYn())
                .remark(e.getRemark())
                .build();
    }
}