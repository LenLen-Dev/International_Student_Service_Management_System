package com.wenjie.modules.teaching.mapper;

import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingAcademicAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachingAlertMapper {
    int insert(TeachingAcademicAlert alert);
    int updateHandle(TeachingAcademicAlert alert);
    TeachingAcademicAlert selectById(@Param("id") Long id);
    TeachingAcademicAlert selectExisting(@Param("studentId") Long studentId, @Param("alertType") String alertType, @Param("sourceId") Long sourceId);
    long countPage(@Param("query") TeachingQueryDTO query);
    List<TeachingAcademicAlert> selectPage(@Param("query") TeachingQueryDTO query);
    List<TeachingAcademicAlert> selectByStudent(@Param("studentId") Long studentId);
}
