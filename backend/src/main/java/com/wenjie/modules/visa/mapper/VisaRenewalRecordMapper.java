package com.wenjie.modules.visa.mapper;

import com.wenjie.modules.visa.dto.VisaRenewalQueryDTO;
import com.wenjie.modules.visa.entity.VisaRenewalRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisaRenewalRecordMapper {
    int insert(VisaRenewalRecord record);
    int updateById(VisaRenewalRecord record);
    int deleteById(@Param("id") Long id);
    VisaRenewalRecord selectById(@Param("id") Long id);
    long countPage(@Param("query") VisaRenewalQueryDTO query);
    List<VisaRenewalRecord> selectPage(@Param("query") VisaRenewalQueryDTO query);
    List<VisaRenewalRecord> selectByStudentId(@Param("studentId") Long studentId);
}
