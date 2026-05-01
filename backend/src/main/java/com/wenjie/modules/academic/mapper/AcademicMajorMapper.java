package com.wenjie.modules.academic.mapper;

import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicMajor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicMajorMapper {
    int insert(AcademicMajor major);
    int updateById(AcademicMajor major);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
    AcademicMajor selectById(@Param("id") Long id);
    AcademicMajor selectByCode(@Param("majorCode") String majorCode);
    long countPage(@Param("query") AcademicQueryDTO query);
    List<AcademicMajor> selectPage(@Param("query") AcademicQueryDTO query);
    List<AcademicMajor> selectAllEnabled();
    int countUsed(@Param("id") Long id);
}
