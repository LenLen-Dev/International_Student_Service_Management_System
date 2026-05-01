package com.wenjie.modules.config.dto;

import lombok.Data;

@Data
public class BackupQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String backupName;
    private String backupStatus;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;
        return (page - 1) * size;
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
