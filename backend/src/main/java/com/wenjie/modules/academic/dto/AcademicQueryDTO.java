package com.wenjie.modules.academic.dto;

import lombok.Data;

@Data
public class AcademicQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer status;
    private Long majorId;
    private Long gradeId;
    private Long classId;
    private String studentStatus;
    private String leaveStatus;
    private String changeStatus;
    private String changeType;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        return (page - 1) * getLimit();
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
