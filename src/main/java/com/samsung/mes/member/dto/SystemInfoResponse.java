package com.samsung.mes.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemInfoResponse {

    private Long id;

    private String systemCode;
    private String systemName;
    private String systemGroup;
    private String owner;

    private String version;
    private String status;

    private String useYn;
    private String remark;
}