package com.wenjie.modules.academic.mapper;

import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicClassMapper {
    int insert(AcademicClass clazz);
    int updateById(AcademicClass clazz);
    int deleteById(@Param("id") Long id);
    AcademicClass selectById(@Param("id") Long id);
    AcademicClass selectByCode(@Param("classCode") String classCode);
    long countPage(@Param("query") AcademicQueryDTO query);
    List<AcademicClass> selectPage(@Param("query") AcademicQueryDTO query);
    List<AcademicClass> selectAllEnabled();
    int countUsed(@Param("id") Long id);
}
