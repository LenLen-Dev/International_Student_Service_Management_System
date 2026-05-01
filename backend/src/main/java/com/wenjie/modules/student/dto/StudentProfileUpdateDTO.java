/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.dto
 * @className: StudentProfileUpdateDTO
 * @description: TODO 留学生档案修改参数
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentProfileUpdateDTO {
    private Long userId;
    @NotBlank(message = "不能为空")
    private String studentNo;
    private String applicationNo;
    private String chineseName;
    @NotBlank(message = "不能为空")
    private String englishName;
    @NotBlank(message = "不能为空")
    private String gender;
    private LocalDate birthDate;
    @NotBlank(message = "不能为空")
    private String nationality;
    private String nativeLanguage;
    @Email(message = "格式不正确")
    private String email;
    @Pattern(regexp = "^$|^[0-9+\\-()\\s]{6,32}$", message = "格式不正确")
    private String phone;
    private String wechat;
    private String passportNo;
    private String passportCountry;
    private LocalDate passportIssueDate;
    private LocalDate passportExpireDate;
    private String college;
    private String major;
    private String degreeLevel;
    private String grade;
    private String className;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    @NotBlank(message = "不能为空")
    private String studentStatus;
    private String avatarUrl;
    private String remark;
    private Integer status = 1;
}
