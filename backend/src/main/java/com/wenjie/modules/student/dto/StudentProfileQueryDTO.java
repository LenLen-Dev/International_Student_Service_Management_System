/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.dto
 * @className: StudentProfileQueryDTO
 * @description: TODO 留学生档案分页查询参数
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StudentProfileQueryDTO {
    @Min(value = 1, message = "必须大于等于1")
    private Integer pageNum = 1;
    @Min(value = 1, message = "必须大于等于1")
    @Max(value = 200, message = "不能大于200")
    private Integer pageSize = 10;
    private String studentNo;
    private String chineseName;
    private String englishName;
    private String nationality;
    private String college;
    private String major;
    private String studentStatus;
    private Integer status;

    public int getOffset() {
        int current = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;
        return (current - 1) * size;
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
