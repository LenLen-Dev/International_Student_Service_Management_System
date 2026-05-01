package com.wenjie.modules.teaching.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeachingGradeDTO {
    private Long id;
    @NotNull(message = "选课记录不能为空")
    private Long enrollmentId;
    private BigDecimal usualScore;
    private BigDecimal finalScore;
    private BigDecimal totalScore;
    private String remark;
}
