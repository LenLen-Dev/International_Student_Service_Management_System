package com.wenjie.modules.teaching.mapper;

import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingCourseEnrollment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachingEnrollmentMapper {
    int insert(TeachingCourseEnrollment enrollment);
    int reselect(@Param("id") Long id);
    int drop(@Param("id") Long id);
    TeachingCourseEnrollment selectById(@Param("id") Long id);
    TeachingCourseEnrollment selectByOfferingAndStudent(@Param("offeringId") Long offeringId, @Param("studentId") Long studentId);
    long countPage(@Param("query") TeachingQueryDTO query);
    List<TeachingCourseEnrollment> selectPage(@Param("query") TeachingQueryDTO query);
    List<TeachingCourseEnrollment> selectByStudent(@Param("studentId") Long studentId);
    List<TeachingCourseEnrollment> selectSelectedByOffering(@Param("offeringId") Long offeringId);
}
