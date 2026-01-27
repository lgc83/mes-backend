package com.samsung.mes.member.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemInfoRequest {

    @NotBlank(message = "systemCode는 필수입니다")
    private String systemCode;
    private String systemName;
    private String systemGroup;
    private String owner;

    private String version;
    private String status;

    @Pattern(regexp = "Y|N", message = "useYn은 Y 또는 N")
    private String useYn;
    private String remark;
}