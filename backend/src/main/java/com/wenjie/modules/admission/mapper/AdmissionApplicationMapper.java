package com.wenjie.modules.admission.mapper;

import com.wenjie.modules.admission.dto.AdmissionApplicationQuery;
import com.wenjie.modules.admission.entity.AdmissionApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdmissionApplicationMapper {
    int insert(AdmissionApplication entity);
    int updateById(AdmissionApplication entity);
    int updateStatus(@Param("id") Long id, @Param("applicationStatus") String applicationStatus,
                     @Param("admissionStatus") String admissionStatus, @Param("reviewOpinion") String reviewOpinion,
                     @Param("studentProfileId") Long studentProfileId);
    AdmissionApplication selectById(@Param("id") Long id);
    AdmissionApplication selectByUserId(@Param("userId") Long userId);
    AdmissionApplication selectByApplicationNo(@Param("applicationNo") String applicationNo);
    List<AdmissionApplication> selectPage(AdmissionApplicationQuery query);
    long count(AdmissionApplicationQuery query);
}
