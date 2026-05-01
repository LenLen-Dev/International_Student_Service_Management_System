package com.wenjie.modules.config.mapper;

import com.wenjie.modules.config.dto.OperationLogQueryDTO;
import com.wenjie.modules.config.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysOperationLogMapper {
    SysOperationLog selectById(@Param("id") Long id);
    List<SysOperationLog> selectPage(OperationLogQueryDTO query);
    long count(OperationLogQueryDTO query);
    int insert(SysOperationLog entity);
    int logicalDelete(@Param("id") Long id);
}
