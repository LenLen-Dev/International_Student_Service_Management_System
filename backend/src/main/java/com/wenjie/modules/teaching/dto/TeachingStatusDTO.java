package com.wenjie.modules.teaching.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeachingStatusDTO {
    private Integer status;
    @NotBlank(message = "状态不能为空")
    private String offeringStatus;
}
