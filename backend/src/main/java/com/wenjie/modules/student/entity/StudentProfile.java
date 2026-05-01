/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.entity
 * @className: StudentProfile
 * @description: TODO 留学生主档案实体
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
public class StudentProfile {
    /** 主键ID */
    private Long id;
    /** 关联系统用户ID */
    private Long userId;
    /** 学号 */
    private String studentNo;
    /** 申请编号 */
    private String applicationNo;
    /** 中文姓名 */
    private String chineseName;
    /** 英文姓名 */
    private String englishName;
    /** 性别 */
    private String gender;
    /** 出生日期 */
    private LocalDate birthDate;
    /** 国籍 */
    private String nationality;
    /** 母语 */
    private String nativeLanguage;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phone;
    /** 微信号 */
    private String wechat;
    /** 护照号码 */
    private String passportNo;
    /** 护照签发国家 */
    private String passportCountry;
    /** 护照签发日期 */
    private LocalDate passportIssueDate;
    /** 护照有效期 */
    private LocalDate passportExpireDate;
    /** 学院 */
    private String college;
    /** 专业 */
    private String major;
    /** 学历层次 */
    private String degreeLevel;
    /** 年级 */
    private String grade;
    /** 班级 */
    private String className;
    /** 入学日期 */
    private LocalDate enrollmentDate;
    /** 预计毕业日期 */
    private LocalDate expectedGraduationDate;
    /** 学生状态 */
    private String studentStatus;
    /** 头像/证件照地址 */
    private String avatarUrl;
    /** 备注 */
    private String remark;
    /** 启用状态 */
    private Integer status;
    /** 逻辑删除 */
    private Integer deleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
