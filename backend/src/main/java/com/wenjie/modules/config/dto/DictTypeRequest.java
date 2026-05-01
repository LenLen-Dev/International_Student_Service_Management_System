package com.wenjie.modules.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DictTypeRequest {
    @NotBlank(message = "字典名称不能为空")
    private String dictName;
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;
    private String description;
    private Integer status = 1;
    private Integer sort = 0;
}
