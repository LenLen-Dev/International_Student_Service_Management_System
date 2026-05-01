package com.wenjie.modules.admission.vo;

import com.wenjie.modules.admission.entity.AdmissionApplication;
import com.wenjie.modules.admission.entity.AdmissionMaterial;
import com.wenjie.modules.admission.entity.AdmissionNotice;
import com.wenjie.modules.admission.entity.AdmissionReviewRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdmissionApplicationDetailVO {
    private AdmissionApplication application;
    private List<AdmissionMaterial> materials = new ArrayList<>();
    private List<AdmissionReviewRecord> reviews = new ArrayList<>();
    private AdmissionNotice notice;
}
