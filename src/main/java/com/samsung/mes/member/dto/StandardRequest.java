package com.samsung.mes.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardRequest {

    @NotBlank(message = "stdCode는 필수입니다")
    private String stdCode;
    private String stdName;
    private String stdGroup;

    private String unit;

    @Pattern(regexp = "Y|N", message = "useYn은 Y 또는 N")
    private String useYn;
    private String remark;
}