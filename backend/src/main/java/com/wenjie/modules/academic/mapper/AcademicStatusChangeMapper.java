package com.wenjie.modules.academic.mapper;

import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicStatusChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicStatusChangeMapper {
    int insert(AcademicStatusChange change);
    int updateById(AcademicStatusChange change);
    int updateApproval(AcademicStatusChange change);
    AcademicStatusChange selectById(@Param("id") Long id);
    long countPage(@Param("query") AcademicQueryDTO query);
    List<AcademicStatusChange> selectPage(@Param("query") AcademicQueryDTO query);
}
