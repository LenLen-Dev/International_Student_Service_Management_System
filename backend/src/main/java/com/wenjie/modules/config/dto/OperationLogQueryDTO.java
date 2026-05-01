package com.wenjie.modules.config.dto;

import lombok.Data;

@Data
public class OperationLogQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String moduleName;
    private String operationType;
    private String operatorName;
    private Integer success;
    private String startTime;
    private String endTime;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;
        return (page - 1) * size;
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
