package com.wenjie.modules.config.service;

import com.wenjie.common.PageResult;
import com.wenjie.modules.config.dto.OperationLogQueryDTO;
import com.wenjie.modules.config.entity.SysOperationLog;

public interface OperationLogService {
    PageResult<SysOperationLog> page(OperationLogQueryDTO query);
    SysOperationLog detail(Long id);
    byte[] export(OperationLogQueryDTO query);
    void delete(Long id);
}
