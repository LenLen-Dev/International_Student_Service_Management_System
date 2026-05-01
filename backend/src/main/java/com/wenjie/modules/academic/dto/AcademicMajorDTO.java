package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcademicMajorDTO {
    @NotBlank(message = "学院不能为空")
    private String college;
    @NotBlank(message = "专业编码不能为空")
    private String majorCode;
    @NotBlank(message = "专业名称不能为空")
    private String majorName;
    private String degreeLevel;
    private Integer studyDuration;
    @NotNull(message = "状态不能为空")
    private Integer status;
    private String remark;
}
