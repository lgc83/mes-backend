package com.samsung.mes.member.controller;

import com.samsung.mes.member.dto.SystemInfoRequest;
import com.samsung.mes.member.dto.SystemInfoResponse;
import com.samsung.mes.member.service.SystemInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/systems")
public class SystemInfoController {

    private final SystemInfoService service;

    @GetMapping
    public Page<SystemInfoResponse> list(@PageableDefault(size = 10) Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public SystemInfoResponse getOne(@PathVariable Long id){
        return service.getOne(id);
    }

    @PostMapping
    public SystemInfoResponse create(@Valid @RequestBody SystemInfoRequest req){
        return service.create(req);
    }

    @PutMapping("/{id}")
    public SystemInfoResponse update(@PathVariable Long id, @Valid @RequestBody SystemInfoRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

}