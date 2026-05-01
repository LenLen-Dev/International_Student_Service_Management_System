package com.wenjie.modules.visa.dto;

import lombok.Data;

@Data
public class VisaRenewalQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String studentKeyword;
    private String renewalType;
    private String renewalStatus;
    private String startDate;
    private String endDate;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        return (page - 1) * getLimit();
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
