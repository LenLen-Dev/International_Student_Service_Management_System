package com.wenjie.modules.teaching.mapper;

import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingAttendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TeachingAttendanceMapper {
    int upsert(TeachingAttendance attendance);
    long countPage(@Param("query") TeachingQueryDTO query);
    List<TeachingAttendance> selectPage(@Param("query") TeachingQueryDTO query);
    List<TeachingAttendance> selectByStudent(@Param("studentId") Long studentId);
    int countAbsenceByStudent(@Param("studentId") Long studentId);
    int countAbsenceByStudentOffering(@Param("studentId") Long studentId, @Param("offeringId") Long offeringId);
    TeachingAttendance selectOne(@Param("offeringId") Long offeringId, @Param("studentId") Long studentId, @Param("attendanceDate") LocalDate attendanceDate);
}
