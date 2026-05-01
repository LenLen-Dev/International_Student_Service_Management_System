package com.wenjie.modules.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlowNodeRequest {
    private Long id;
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;
    private String nodeCode;
    @NotBlank(message = "节点类型不能为空")
    private String nodeType;
    private String approverRoleCode;
    private String description;
    private Integer status = 1;
    private Integer sort = 0;
}
