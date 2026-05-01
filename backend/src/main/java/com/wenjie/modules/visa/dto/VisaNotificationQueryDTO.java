package com.wenjie.modules.visa.dto;

import lombok.Data;

@Data
public class VisaNotificationQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String channel;
    private String sendStatus;
    private Integer readStatus;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        return (page - 1) * getLimit();
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
