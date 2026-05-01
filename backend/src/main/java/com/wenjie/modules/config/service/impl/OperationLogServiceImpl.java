package com.wenjie.modules.config.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.config.dto.OperationLogQueryDTO;
import com.wenjie.modules.config.entity.SysOperationLog;
import com.wenjie.modules.config.mapper.SysOperationLogMapper;
import com.wenjie.modules.config.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final SysOperationLogMapper operationLogMapper;

    @Override
    public PageResult<SysOperationLog> page(OperationLogQueryDTO query) {
        return new PageResult<>(operationLogMapper.count(query), operationLogMapper.selectPage(query));
    }

    @Override
    public SysOperationLog detail(Long id) {
        SysOperationLog log = operationLogMapper.selectById(id);
        if (log == null) {
            throw new BusinessException(404, "操作日志不存在");
        }
        return log;
    }

    @Override
    public byte[] export(OperationLogQueryDTO query) {
        query.setPageNum(1);
        query.setPageSize(5000);
        List<SysOperationLog> logs = operationLogMapper.selectPage(query);
        StringBuilder builder = new StringBuilder();
        builder.append("ID,模块,操作类型,操作名称,操作人,是否成功,请求方法,请求地址,耗时(ms),创建时间\n");
        for (SysOperationLog log : logs) {
            builder.append(csv(log.getId())).append(',')
                    .append(csv(log.getModuleName())).append(',')
                    .append(csv(log.getOperationType())).append(',')
                    .append(csv(log.getOperationName())).append(',')
                    .append(csv(log.getOperatorName())).append(',')
                    .append(csv(log.getSuccess() != null && log.getSuccess() == 1 ? "成功" : "失败")).append(',')
                    .append(csv(log.getRequestMethod())).append(',')
                    .append(csv(log.getRequestUri())).append(',')
                    .append(csv(log.getCostTime())).append(',')
                    .append(csv(log.getCreateTime()))
                    .append('\n');
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void delete(Long id) {
        detail(id);
        operationLogMapper.logicalDelete(id);
    }

    private String csv(Object value) {
        String text = value == null ? "" : String.valueOf(value);
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }
}
