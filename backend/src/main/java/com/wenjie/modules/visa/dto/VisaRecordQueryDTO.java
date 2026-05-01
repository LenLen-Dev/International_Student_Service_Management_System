package com.wenjie.modules.visa.dto;

import lombok.Data;

@Data
public class VisaRecordQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String studentKeyword;
    private String visaType;
    private String status;
    private String expireStart;
    private String expireEnd;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = getLimit();
        return (page - 1) * size;
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
