package com.wenjie.modules.teaching.mapper;

import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingGrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachingGradeMapper {
    int insert(TeachingGrade grade);
    int updateById(TeachingGrade grade);
    int publishByOffering(@Param("offeringId") Long offeringId);
    int deleteDraftByEnrollment(@Param("enrollmentId") Long enrollmentId);
    TeachingGrade selectById(@Param("id") Long id);
    TeachingGrade selectByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
    long countPage(@Param("query") TeachingQueryDTO query);
    List<TeachingGrade> selectPage(@Param("query") TeachingQueryDTO query);
    List<TeachingGrade> selectByStudent(@Param("studentId") Long studentId, @Param("publishedOnly") boolean publishedOnly);
    int countPublishedByEnrollment(@Param("enrollmentId") Long enrollmentId);
    List<TeachingGrade> selectFailedOrLowScores();
}
