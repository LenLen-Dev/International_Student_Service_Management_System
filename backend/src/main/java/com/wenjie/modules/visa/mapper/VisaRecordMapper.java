package com.wenjie.modules.visa.mapper;

import com.wenjie.modules.visa.dto.VisaRecordQueryDTO;
import com.wenjie.modules.visa.entity.VisaRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface VisaRecordMapper {
    int insert(VisaRecord record);
    int updateById(VisaRecord record);
    int updateValidUntil(@Param("id") Long id, @Param("validUntil") LocalDate validUntil, @Param("status") String status);
    int deleteById(@Param("id") Long id);
    VisaRecord selectById(@Param("id") Long id);
    long countPage(@Param("query") VisaRecordQueryDTO query);
    List<VisaRecord> selectPage(@Param("query") VisaRecordQueryDTO query);
    List<VisaRecord> selectByStudentId(@Param("studentId") Long studentId);
    List<VisaRecord> selectAlertCandidates(@Param("deadline") LocalDate deadline);
}
