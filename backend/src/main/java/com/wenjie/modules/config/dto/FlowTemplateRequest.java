package com.wenjie.modules.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlowTemplateRequest {
    @NotBlank(message = "流程名称不能为空")
    private String flowName;
    @NotBlank(message = "流程编码不能为空")
    private String flowCode;
    private String businessType;
    private String description;
    private Integer status = 1;
    private Integer sort = 0;
}
