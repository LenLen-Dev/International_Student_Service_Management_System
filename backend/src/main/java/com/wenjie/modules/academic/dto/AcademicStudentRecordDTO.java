package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicStudentRecordDTO {
    @NotNull(message = "学生不能为空")
    private Long studentId;
    @NotNull(message = "专业不能为空")
    private Long majorId;
    @NotNull(message = "年级不能为空")
    private Long gradeId;
    private Long classId;
    @NotBlank(message = "学籍状态不能为空")
    private String studentStatus;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    private LocalDate actualLeaveDate;
    private String remark;
}
