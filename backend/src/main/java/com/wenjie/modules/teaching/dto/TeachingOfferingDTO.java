package com.wenjie.modules.teaching.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeachingOfferingDTO {
    @NotNull(message = "课程不能为空")
    private Long courseId;
    private Long teacherId;
    @NotBlank(message = "学年不能为空")
    private String academicYear;
    @NotBlank(message = "学期不能为空")
    private String semester;
    @NotNull(message = "容量不能为空")
    private Integer capacity;
    private LocalDateTime selectionStartTime;
    private LocalDateTime selectionEndTime;
    private String classTime;
    private String classroom;
    @NotBlank(message = "开课状态不能为空")
    private String offeringStatus;
    private String remark;
}
