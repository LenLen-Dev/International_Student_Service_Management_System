package com.wenjie.modules.teaching.mapper;

import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachingCourseMapper {
    int insert(TeachingCourse course);
    int updateById(TeachingCourse course);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
    TeachingCourse selectById(@Param("id") Long id);
    TeachingCourse selectByCode(@Param("courseCode") String courseCode);
    long countPage(@Param("query") TeachingQueryDTO query);
    List<TeachingCourse> selectPage(@Param("query") TeachingQueryDTO query);
    List<TeachingCourse> selectAllEnabled();
    int countUsed(@Param("id") Long id);
}
