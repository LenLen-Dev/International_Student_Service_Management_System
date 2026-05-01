package com.wenjie.modules.admission.service;

import com.wenjie.common.PageResult;
import com.wenjie.modules.admission.dto.AdmissionApplicationForm;
import com.wenjie.modules.admission.dto.AdmissionApplicationQuery;
import com.wenjie.modules.admission.dto.AdmissionMaterialReviewRequest;
import com.wenjie.modules.admission.dto.AdmissionReviewRequest;
import com.wenjie.modules.admission.entity.AdmissionApplication;
import com.wenjie.modules.admission.entity.AdmissionMaterial;
import com.wenjie.modules.admission.vo.AdmissionApplicationDetailVO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AdmissionApplicationService {
    AdmissionApplicationDetailVO getMyApplication();
    Long createMyApplication(AdmissionApplicationForm form);
    void updateMyApplication(AdmissionApplicationForm form);
    void submitMyApplication();
    AdmissionMaterial uploadMyMaterial(String materialType, MultipartFile file);
    void deleteMyMaterial(Long materialId);
    Resource downloadMyNotice();
    PageResult<AdmissionApplication> page(AdmissionApplicationQuery query);
    AdmissionApplicationDetailVO detail(Long id);
    void returnApplication(Long id, AdmissionReviewRequest request);
    void rejectApplication(Long id, AdmissionReviewRequest request);
    void admitApplication(Long id, AdmissionReviewRequest request);
    void reviewMaterial(Long materialId, AdmissionMaterialReviewRequest request);
    void generateNotice(Long id);
    Resource downloadNotice(Long id);
}
