package com.wenjie.modules.academic.mapper;

import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicLeaveApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicLeaveMapper {
    int insert(AcademicLeaveApplication leave);
    int updateApproval(AcademicLeaveApplication leave);
    AcademicLeaveApplication selectById(@Param("id") Long id);
    long countPage(@Param("query") AcademicQueryDTO query);
    List<AcademicLeaveApplication> selectPage(@Param("query") AcademicQueryDTO query);
    List<AcademicLeaveApplication> selectByStudentId(@Param("studentId") Long studentId);
}
