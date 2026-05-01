package com.wenjie.modules.visa.mapper;

import com.wenjie.modules.visa.dto.ResidencePermitQueryDTO;
import com.wenjie.modules.visa.entity.ResidencePermit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ResidencePermitMapper {
    int insert(ResidencePermit permit);
    int updateById(ResidencePermit permit);
    int updateValidUntil(@Param("id") Long id, @Param("validUntil") LocalDate validUntil, @Param("status") String status);
    int deleteById(@Param("id") Long id);
    ResidencePermit selectById(@Param("id") Long id);
    long countPage(@Param("query") ResidencePermitQueryDTO query);
    List<ResidencePermit> selectPage(@Param("query") ResidencePermitQueryDTO query);
    List<ResidencePermit> selectByStudentId(@Param("studentId") Long studentId);
    List<ResidencePermit> selectAlertCandidates(@Param("deadline") LocalDate deadline);
}
