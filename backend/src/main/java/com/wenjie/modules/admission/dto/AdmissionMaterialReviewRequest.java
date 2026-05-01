package com.wenjie.modules.admission.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdmissionMaterialReviewRequest {
    @NotBlank(message = "材料审核状态不能为空")
    private String reviewStatus;
    private String reviewOpinion;
}
