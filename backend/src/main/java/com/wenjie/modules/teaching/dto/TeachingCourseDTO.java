package com.wenjie.modules.teaching.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeachingCourseDTO {
    @NotBlank(message = "课程编码不能为空")
    private String courseCode;
    @NotBlank(message = "课程名称不能为空")
    private String courseName;
    @NotNull(message = "学分不能为空")
    private BigDecimal credits;
    private Integer totalHours;
    @NotBlank(message = "课程类型不能为空")
    private String courseType;
    private String college;
    @NotNull(message = "状态不能为空")
    private Integer status;
    private String remark;
}
