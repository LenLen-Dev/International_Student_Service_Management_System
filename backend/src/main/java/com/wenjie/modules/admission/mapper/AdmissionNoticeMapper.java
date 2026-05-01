package com.wenjie.modules.admission.mapper;

import com.wenjie.modules.admission.entity.AdmissionNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdmissionNoticeMapper {
    int insert(AdmissionNotice entity);
    int incrementDownloadCount(@Param("id") Long id);
    AdmissionNotice selectByApplicationId(@Param("applicationId") Long applicationId);
}
