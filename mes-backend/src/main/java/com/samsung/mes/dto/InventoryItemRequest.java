package com.samsung.mes.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemRequest {

    @NotBlank
    private String itemCode;

    @NotBlank
    private String itemName;

    @NotBlank
    private String itemGroup;

    @NotBlank
    private String spec;

    @NotBlank
    private String warehouse;

    @NotBlank
    private String location;

    @Min(0)
    private Integer stockQty;

    @Min(0)
    private Integer safetyStock;

    @DecimalMin("0.0")
    private BigDecimal inPrice;

    @DecimalMin("0.0")
    private BigDecimal outPrice;

    @Pattern(regexp = "Y|N", message = "useYn은 Y또는 N")
    private String useYn;

    private String remark;
}