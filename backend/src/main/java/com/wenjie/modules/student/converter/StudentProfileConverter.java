/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.converter
 * @className: StudentProfileConverter
 * @description: TODO 留学生档案对象转换器
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.converter;

import com.wenjie.modules.student.dto.StudentContactDTO;
import com.wenjie.modules.student.dto.StudentDocumentDTO;
import com.wenjie.modules.student.dto.StudentEducationDTO;
import com.wenjie.modules.student.dto.StudentProfileCreateDTO;
import com.wenjie.modules.student.dto.StudentProfileUpdateDTO;
import com.wenjie.modules.student.entity.StudentContact;
import com.wenjie.modules.student.entity.StudentDocument;
import com.wenjie.modules.student.entity.StudentEducation;
import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.student.entity.StudentStatusLog;
import com.wenjie.modules.student.vo.StudentContactVO;
import com.wenjie.modules.student.vo.StudentDocumentVO;
import com.wenjie.modules.student.vo.StudentEducationVO;
import com.wenjie.modules.student.vo.StudentProfileDetailVO;
import com.wenjie.modules.student.vo.StudentProfileListVO;
import com.wenjie.modules.student.vo.StudentStatusLogVO;

public final class StudentProfileConverter {

    private StudentProfileConverter() {
    }

    public static StudentProfile toEntity(StudentProfileCreateDTO dto) {
        StudentProfile entity = new StudentProfile();
        copyProfile(dto, entity);
        return entity;
    }

    public static StudentProfile toEntity(Long id, StudentProfileUpdateDTO dto) {
        StudentProfile entity = new StudentProfile();
        entity.setId(id);
        copyProfile(dto, entity);
        return entity;
    }

    private static void copyProfile(StudentProfileCreateDTO dto, StudentProfile entity) {
        entity.setUserId(dto.getUserId());
        entity.setStudentNo(dto.getStudentNo());
        entity.setApplicationNo(dto.getApplicationNo());
        entity.setChineseName(dto.getChineseName());
        entity.setEnglishName(dto.getEnglishName());
        entity.setGender(dto.getGender());
        entity.setBirthDate(dto.getBirthDate());
        entity.setNationality(dto.getNationality());
        entity.setNativeLanguage(dto.getNativeLanguage());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setWechat(dto.getWechat());
        entity.setPassportNo(dto.getPassportNo());
        entity.setPassportCountry(dto.getPassportCountry());
        entity.setPassportIssueDate(dto.getPassportIssueDate());
        entity.setPassportExpireDate(dto.getPassportExpireDate());
        entity.setCollege(dto.getCollege());
        entity.setMajor(dto.getMajor());
        entity.setDegreeLevel(dto.getDegreeLevel());
        entity.setGrade(dto.getGrade());
        entity.setClassName(dto.getClassName());
        entity.setEnrollmentDate(dto.getEnrollmentDate());
        entity.setExpectedGraduationDate(dto.getExpectedGraduationDate());
        entity.setStudentStatus(dto.getStudentStatus());
        entity.setAvatarUrl(dto.getAvatarUrl());
        entity.setRemark(dto.getRemark());
        entity.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
    }

    private static void copyProfile(StudentProfileUpdateDTO dto, StudentProfile entity) {
        entity.setUserId(dto.getUserId());
        entity.setStudentNo(dto.getStudentNo());
        entity.setApplicationNo(dto.getApplicationNo());
        entity.setChineseName(dto.getChineseName());
        entity.setEnglishName(dto.getEnglishName());
        entity.setGender(dto.getGender());
        entity.setBirthDate(dto.getBirthDate());
        entity.setNationality(dto.getNationality());
        entity.setNativeLanguage(dto.getNativeLanguage());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setWechat(dto.getWechat());
        entity.setPassportNo(dto.getPassportNo());
        entity.setPassportCountry(dto.getPassportCountry());
        entity.setPassportIssueDate(dto.getPassportIssueDate());
        entity.setPassportExpireDate(dto.getPassportExpireDate());
        entity.setCollege(dto.getCollege());
        entity.setMajor(dto.getMajor());
        entity.setDegreeLevel(dto.getDegreeLevel());
        entity.setGrade(dto.getGrade());
        entity.setClassName(dto.getClassName());
        entity.setEnrollmentDate(dto.getEnrollmentDate());
        entity.setExpectedGraduationDate(dto.getExpectedGraduationDate());
        entity.setStudentStatus(dto.getStudentStatus());
        entity.setAvatarUrl(dto.getAvatarUrl());
        entity.setRemark(dto.getRemark());
        entity.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
    }

    public static StudentProfileListVO toListVO(StudentProfile entity) {
        StudentProfileListVO vo = new StudentProfileListVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setStudentNo(entity.getStudentNo());
        vo.setChineseName(entity.getChineseName());
        vo.setEnglishName(entity.getEnglishName());
        vo.setGender(entity.getGender());
        vo.setNationality(entity.getNationality());
        vo.setCollege(entity.getCollege());
        vo.setMajor(entity.getMajor());
        vo.setDegreeLevel(entity.getDegreeLevel());
        vo.setGrade(entity.getGrade());
        vo.setStudentStatus(entity.getStudentStatus());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    public static StudentProfileDetailVO toDetailVO(StudentProfile entity) {
        StudentProfileDetailVO vo = new StudentProfileDetailVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setStudentNo(entity.getStudentNo());
        vo.setApplicationNo(entity.getApplicationNo());
        vo.setChineseName(entity.getChineseName());
        vo.setEnglishName(entity.getEnglishName());
        vo.setGender(entity.getGender());
        vo.setBirthDate(entity.getBirthDate());
        vo.setNationality(entity.getNationality());
        vo.setNativeLanguage(entity.getNativeLanguage());
        vo.setEmail(entity.getEmail());
        vo.setPhone(entity.getPhone());
        vo.setWechat(entity.getWechat());
        vo.setPassportNo(entity.getPassportNo());
        vo.setPassportCountry(entity.getPassportCountry());
        vo.setPassportIssueDate(entity.getPassportIssueDate());
        vo.setPassportExpireDate(entity.getPassportExpireDate());
        vo.setCollege(entity.getCollege());
        vo.setMajor(entity.getMajor());
        vo.setDegreeLevel(entity.getDegreeLevel());
        vo.setGrade(entity.getGrade());
        vo.setClassName(entity.getClassName());
        vo.setEnrollmentDate(entity.getEnrollmentDate());
        vo.setExpectedGraduationDate(entity.getExpectedGraduationDate());
        vo.setStudentStatus(entity.getStudentStatus());
        vo.setAvatarUrl(entity.getAvatarUrl());
        vo.setRemark(entity.getRemark());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static StudentContact toEntity(Long studentId, StudentContactDTO dto) {
        StudentContact entity = new StudentContact();
        entity.setStudentId(studentId);
        entity.setContactType(dto.getContactType());
        entity.setContactName(dto.getContactName());
        entity.setRelationship(dto.getRelationship());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setIsPrimary(dto.getIsPrimary() == null ? 0 : dto.getIsPrimary());
        return entity;
    }

    public static StudentContactVO toVO(StudentContact entity) {
        StudentContactVO vo = new StudentContactVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setContactType(entity.getContactType());
        vo.setContactName(entity.getContactName());
        vo.setRelationship(entity.getRelationship());
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());
        vo.setAddress(entity.getAddress());
        vo.setIsPrimary(entity.getIsPrimary());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static StudentEducation toEntity(Long studentId, StudentEducationDTO dto) {
        StudentEducation entity = new StudentEducation();
        entity.setStudentId(studentId);
        entity.setSchoolName(dto.getSchoolName());
        entity.setCountry(dto.getCountry());
        entity.setDegreeLevel(dto.getDegreeLevel());
        entity.setMajor(dto.getMajor());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setCertificateUrl(dto.getCertificateUrl());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public static StudentEducationVO toVO(StudentEducation entity) {
        StudentEducationVO vo = new StudentEducationVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setSchoolName(entity.getSchoolName());
        vo.setCountry(entity.getCountry());
        vo.setDegreeLevel(entity.getDegreeLevel());
        vo.setMajor(entity.getMajor());
        vo.setStartDate(entity.getStartDate());
        vo.setEndDate(entity.getEndDate());
        vo.setCertificateUrl(entity.getCertificateUrl());
        vo.setRemark(entity.getRemark());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static StudentDocument toEntity(Long studentId, StudentDocumentDTO dto) {
        StudentDocument entity = new StudentDocument();
        entity.setStudentId(studentId);
        entity.setDocumentType(dto.getDocumentType());
        entity.setDocumentName(dto.getDocumentName());
        entity.setFileUrl(dto.getFileUrl());
        entity.setFileSize(dto.getFileSize());
        entity.setMimeType(dto.getMimeType());
        entity.setReviewStatus(dto.getReviewStatus() == null ? "PENDING" : dto.getReviewStatus());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public static StudentDocumentVO toVO(StudentDocument entity) {
        StudentDocumentVO vo = new StudentDocumentVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setDocumentType(entity.getDocumentType());
        vo.setDocumentName(entity.getDocumentName());
        vo.setFileUrl(entity.getFileUrl());
        vo.setFileSize(entity.getFileSize());
        vo.setMimeType(entity.getMimeType());
        vo.setReviewStatus(entity.getReviewStatus());
        vo.setRemark(entity.getRemark());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static StudentStatusLogVO toVO(StudentStatusLog entity) {
        StudentStatusLogVO vo = new StudentStatusLogVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setOldStatus(entity.getOldStatus());
        vo.setNewStatus(entity.getNewStatus());
        vo.setChangeReason(entity.getChangeReason());
        vo.setOperatorId(entity.getOperatorId());
        vo.setOperatorName(entity.getOperatorName());
        vo.setRemark(entity.getRemark());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
