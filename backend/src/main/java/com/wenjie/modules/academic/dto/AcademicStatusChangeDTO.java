package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicStatusChangeDTO {
    @NotNull(message = "学生不能为空")
    private Long studentId;
    @NotBlank(message = "异动类型不能为空")
    private String changeType;
    private String newStatus;
    private Long newMajorId;
    private Long newGradeId;
    private Long newClassId;
    private LocalDate effectiveDate;
    @NotBlank(message = "异动原因不能为空")
    private String reason;
    private String remark;
}
