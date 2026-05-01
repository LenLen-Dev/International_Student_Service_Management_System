package com.wenjie.modules.academic.mapper;

import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicGrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicGradeMapper {
    int insert(AcademicGrade grade);
    int updateById(AcademicGrade grade);
    int deleteById(@Param("id") Long id);
    AcademicGrade selectById(@Param("id") Long id);
    AcademicGrade selectByCode(@Param("gradeCode") String gradeCode);
    long countPage(@Param("query") AcademicQueryDTO query);
    List<AcademicGrade> selectPage(@Param("query") AcademicQueryDTO query);
    List<AcademicGrade> selectAllEnabled();
    int countUsed(@Param("id") Long id);
}
