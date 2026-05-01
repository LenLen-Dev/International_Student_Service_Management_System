package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcademicClassDTO {
    @NotNull(message = "专业不能为空")
    private Long majorId;
    @NotNull(message = "年级不能为空")
    private Long gradeId;
    @NotBlank(message = "班级编码不能为空")
    private String classCode;
    @NotBlank(message = "班级名称不能为空")
    private String className;
    private Long advisorId;
    private String advisorName;
    @NotNull(message = "状态不能为空")
    private Integer status;
    private String remark;
}
