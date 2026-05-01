package com.wenjie.modules.visa.dto;

import lombok.Data;

@Data
public class VisaAlertQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String studentKeyword;
    private String alertType;
    private String alertLevel;
    private String alertStatus;
    private String targetType;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        return (page - 1) * getLimit();
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
