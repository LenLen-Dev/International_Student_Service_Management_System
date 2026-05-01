package com.wenjie.modules.admission.mapper;

import com.wenjie.modules.admission.entity.AdmissionReviewRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdmissionReviewRecordMapper {
    int insert(AdmissionReviewRecord entity);
    List<AdmissionReviewRecord> selectByApplicationId(@Param("applicationId") Long applicationId);
}
