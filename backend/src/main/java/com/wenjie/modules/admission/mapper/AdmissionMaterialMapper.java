package com.wenjie.modules.admission.mapper;

import com.wenjie.modules.admission.entity.AdmissionMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdmissionMaterialMapper {
    int insert(AdmissionMaterial entity);
    int updateReview(AdmissionMaterial entity);
    int logicalDelete(@Param("id") Long id);
    AdmissionMaterial selectById(@Param("id") Long id);
    List<AdmissionMaterial> selectByApplicationId(@Param("applicationId") Long applicationId);
}
