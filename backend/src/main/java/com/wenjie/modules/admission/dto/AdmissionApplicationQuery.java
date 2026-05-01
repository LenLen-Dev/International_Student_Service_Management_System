package com.wenjie.modules.admission.dto;

import lombok.Data;

@Data
public class AdmissionApplicationQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String applicationNo;
    private String name;
    private String nationality;
    private String applyCollege;
    private String applyMajor;
    private String applicationStatus;
    private String admissionStatus;
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
