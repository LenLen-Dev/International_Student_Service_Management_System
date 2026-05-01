package com.wenjie.modules.teaching.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeachingAlertHandleDTO {
    @NotBlank(message = "预警状态不能为空")
    private String alertStatus;
    private String handleRemark;
}
