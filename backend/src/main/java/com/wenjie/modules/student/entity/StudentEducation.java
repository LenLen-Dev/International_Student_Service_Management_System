/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.entity
 * @className: StudentEducation
 * @description: TODO 留学生教育背景实体
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentEducation {
    /** 主键ID */
    private Long id;
    /** 留学生档案ID */
    private Long studentId;
    /** 学校名称 */
    private String schoolName;
    /** 国家/地区 */
    private String country;
    /** 学历层次 */
    private String degreeLevel;
    /** 专业 */
    private String major;
    /** 开始日期 */
    private LocalDate startDate;
    /** 结束日期 */
    private LocalDate endDate;
    /** 证书附件地址 */
    private String certificateUrl;
    /** 备注 */
    private String remark;
    /** 逻辑删除 */
    private Integer deleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
