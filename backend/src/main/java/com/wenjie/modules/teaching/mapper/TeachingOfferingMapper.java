package com.wenjie.modules.teaching.mapper;

import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingCourseOffering;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachingOfferingMapper {
    int insert(TeachingCourseOffering offering);
    int updateById(TeachingCourseOffering offering);
    int updateStatus(@Param("id") Long id, @Param("offeringStatus") String offeringStatus);
    int updateSelectedCount(@Param("id") Long id, @Param("delta") int delta);
    int deleteById(@Param("id") Long id);
    TeachingCourseOffering selectById(@Param("id") Long id);
    long countPage(@Param("query") TeachingQueryDTO query);
    List<TeachingCourseOffering> selectPage(@Param("query") TeachingQueryDTO query);
    List<TeachingCourseOffering> selectMy(@Param("teacherId") Long teacherId);
    List<TeachingCourseOffering> selectAvailable(TeachingQueryDTO query);
    int countUsed(@Param("id") Long id);
}
