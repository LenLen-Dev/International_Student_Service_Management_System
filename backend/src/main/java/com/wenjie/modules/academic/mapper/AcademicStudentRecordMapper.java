package com.wenjie.modules.academic.mapper;

import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicStudentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicStudentRecordMapper {
    int insert(AcademicStudentRecord record);
    int updateById(AcademicStudentRecord record);
    int updateStatus(AcademicStudentRecord record);
    AcademicStudentRecord selectById(@Param("id") Long id);
    AcademicStudentRecord selectByStudentId(@Param("studentId") Long studentId);
    long countPage(@Param("query") AcademicQueryDTO query);
    List<AcademicStudentRecord> selectPage(@Param("query") AcademicQueryDTO query);
}
