package com.wenjie.modules.visa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VisaAlertResolveDTO {
    @NotBlank(message = "处理状态不能为空")
    private String alertStatus;
    private String handleRemark;
}
