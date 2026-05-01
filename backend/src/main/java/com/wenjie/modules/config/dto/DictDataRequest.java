package com.wenjie.modules.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DictDataRequest {
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;
    @NotBlank(message = "字典标签不能为空")
    private String dictLabel;
    @NotBlank(message = "字典值不能为空")
    private String dictValue;
    private String tagType;
    private String description;
    private Integer status = 1;
    private Integer sort = 0;
}
