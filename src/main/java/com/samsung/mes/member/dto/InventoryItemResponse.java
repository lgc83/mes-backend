package com.samsung.mes.member.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InventoryItemResponse {

private String itemCode, itemName, itemGroup, spec, warehouse, location, useYn, remark;
private Integer stockQty, safetyStock;
private BigDecimal inPrice, outPrice;
private LocalDateTime updatedAAt;

}
