package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcademicGradeDTO {
    @NotBlank(message = "年级编码不能为空")
    private String gradeCode;
    @NotBlank(message = "年级名称不能为空")
    private String gradeName;
    @NotNull(message = "入学年份不能为空")
    private Integer enrollmentYear;
    private Integer graduationYear;
    @NotNull(message = "状态不能为空")
    private Integer status;
    private String remark;
}
