package com.wenjie.modules.visa.mapper;

import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.visa.dto.VisaAlertQueryDTO;
import com.wenjie.modules.visa.entity.VisaComplianceAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisaComplianceAlertMapper {
    int insert(VisaComplianceAlert alert);
    int resolve(VisaComplianceAlert alert);
    VisaComplianceAlert selectById(@Param("id") Long id);
    long countPage(@Param("query") VisaAlertQueryDTO query);
    List<VisaComplianceAlert> selectPage(@Param("query") VisaAlertQueryDTO query);
    List<VisaComplianceAlert> selectByStudentId(@Param("studentId") Long studentId);
    int countOpenTargetAlert(@Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("alertType") String alertType);
    List<StudentProfile> selectStudentsForCompliance();
}
