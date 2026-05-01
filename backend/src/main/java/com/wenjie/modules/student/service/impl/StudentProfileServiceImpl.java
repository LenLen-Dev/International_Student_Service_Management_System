/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service.impl
 * @className: StudentProfileServiceImpl
 * @description: TODO 留学生档案业务实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import com.wenjie.modules.student.converter.StudentProfileConverter;
import com.wenjie.modules.student.dto.StudentProfileCreateDTO;
import com.wenjie.modules.student.dto.StudentProfileQueryDTO;
import com.wenjie.modules.student.dto.StudentProfileUpdateDTO;
import com.wenjie.modules.student.dto.StudentStatusUpdateDTO;
import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.student.entity.StudentStatusLog;
import com.wenjie.modules.student.mapper.StudentContactMapper;
import com.wenjie.modules.student.mapper.StudentDocumentMapper;
import com.wenjie.modules.student.mapper.StudentEducationMapper;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.student.service.StudentProfileService;
import com.wenjie.modules.student.service.StudentStatusLogService;
import com.wenjie.modules.student.vo.StudentProfileDetailVO;
import com.wenjie.modules.student.vo.StudentProfileListVO;
import com.wenjie.security.AuthUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileMapper profileMapper;
    private final StudentContactMapper contactMapper;
    private final StudentEducationMapper educationMapper;
    private final StudentDocumentMapper documentMapper;
    private final StudentStatusLogService statusLogService;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<StudentProfileListVO> pageProfiles(StudentProfileQueryDTO query) {
        long total = profileMapper.countPage(query);
        List<StudentProfileListVO> records = profileMapper.selectPage(query, query.getOffset(), query.getLimit()).stream()
                .map(StudentProfileConverter::toListVO)
                .toList();
        return new PageResult<>(total, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProfile(StudentProfileCreateDTO dto) {
        ensureStudentNoUnique(dto.getStudentNo(), null);
        ensureUserBindingUnique(dto.getUserId(), null);
        StudentProfile profile = StudentProfileConverter.toEntity(dto);
        profileMapper.insert(profile);
        createStatusLog(profile.getId(), null, profile.getStudentStatus(), "创建留学生档案", null);
        return profile.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long id, StudentProfileUpdateDTO dto) {
        ensureExists(id);
        ensureStudentNoUnique(dto.getStudentNo(), id);
        ensureUserBindingUnique(dto.getUserId(), id);
        profileMapper.updateById(StudentProfileConverter.toEntity(id, dto));
    }

    @Override
    public void deleteProfile(Long id) {
        ensureExists(id);
        profileMapper.deleteById(id);
    }

    @Override
    public StudentProfileDetailVO getProfileDetail(Long id) {
        StudentProfile profile = ensureExists(id);
        return buildDetail(profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfileStatus(Long id, StudentStatusUpdateDTO dto) {
        StudentProfile profile = ensureExists(id);
        profileMapper.updateStatus(id, dto.getNewStatus());
        createStatusLog(id, profile.getStudentStatus(), dto.getNewStatus(), dto.getChangeReason(), dto.getRemark());
    }

    @Override
    public StudentProfileDetailVO getMyProfile() {
        StudentProfile profile = profileMapper.selectByUserId(AuthUserContext.currentUserId());
        if (profile == null) {
            throw new BusinessException(404, "当前用户未绑定留学生档案");
        }
        return buildDetail(profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMyProfile(StudentProfileUpdateDTO dto) {
        StudentProfile existing = profileMapper.selectByUserId(AuthUserContext.currentUserId());
        if (existing == null) {
            throw new BusinessException(404, "当前用户未绑定留学生档案");
        }
        StudentProfile profile = new StudentProfile();
        profile.setId(existing.getId());
        profile.setUserId(existing.getUserId());
        profile.setStudentNo(existing.getStudentNo());
        profile.setApplicationNo(existing.getApplicationNo());
        profile.setChineseName(dto.getChineseName() == null ? existing.getChineseName() : dto.getChineseName());
        profile.setEnglishName(existing.getEnglishName());
        profile.setGender(dto.getGender() == null ? existing.getGender() : dto.getGender());
        profile.setBirthDate(dto.getBirthDate() == null ? existing.getBirthDate() : dto.getBirthDate());
        profile.setNationality(existing.getNationality());
        profile.setNativeLanguage(dto.getNativeLanguage() == null ? existing.getNativeLanguage() : dto.getNativeLanguage());
        profile.setEmail(dto.getEmail() == null ? existing.getEmail() : dto.getEmail());
        profile.setPhone(dto.getPhone() == null ? existing.getPhone() : dto.getPhone());
        profile.setWechat(dto.getWechat() == null ? existing.getWechat() : dto.getWechat());
        profile.setPassportNo(dto.getPassportNo() == null ? existing.getPassportNo() : dto.getPassportNo());
        profile.setPassportCountry(dto.getPassportCountry() == null ? existing.getPassportCountry() : dto.getPassportCountry());
        profile.setPassportIssueDate(dto.getPassportIssueDate() == null ? existing.getPassportIssueDate() : dto.getPassportIssueDate());
        profile.setPassportExpireDate(dto.getPassportExpireDate() == null ? existing.getPassportExpireDate() : dto.getPassportExpireDate());
        profile.setCollege(existing.getCollege());
        profile.setMajor(existing.getMajor());
        profile.setDegreeLevel(existing.getDegreeLevel());
        profile.setGrade(existing.getGrade());
        profile.setClassName(existing.getClassName());
        profile.setEnrollmentDate(existing.getEnrollmentDate());
        profile.setExpectedGraduationDate(existing.getExpectedGraduationDate());
        profile.setStudentStatus(existing.getStudentStatus());
        profile.setAvatarUrl(dto.getAvatarUrl() == null ? existing.getAvatarUrl() : dto.getAvatarUrl());
        profile.setRemark(dto.getRemark() == null ? existing.getRemark() : dto.getRemark());
        profile.setStatus(existing.getStatus());
        profileMapper.updateById(profile);
    }

    private StudentProfile ensureExists(Long id) {
        StudentProfile profile = profileMapper.selectById(id);
        if (profile == null) {
            throw new BusinessException(404, "留学生档案不存在");
        }
        return profile;
    }

    private void ensureStudentNoUnique(String studentNo, Long currentId) {
        StudentProfile existing = profileMapper.selectByStudentNo(studentNo);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException("学号已存在");
        }
    }

    private void ensureUserBindingUnique(Long userId, Long currentId) {
        if (userId == null) {
            return;
        }
        StudentProfile existing = profileMapper.selectByUserId(userId);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException("该系统用户已绑定其他留学生档案");
        }
    }

    private StudentProfileDetailVO buildDetail(StudentProfile profile) {
        StudentProfileDetailVO vo = StudentProfileConverter.toDetailVO(profile);
        vo.setContacts(contactMapper.selectByStudentId(profile.getId()).stream().map(StudentProfileConverter::toVO).toList());
        vo.setEducations(educationMapper.selectByStudentId(profile.getId()).stream().map(StudentProfileConverter::toVO).toList());
        vo.setDocuments(documentMapper.selectByStudentId(profile.getId()).stream().map(StudentProfileConverter::toVO).toList());
        vo.setStatusLogs(statusLogService.listByStudentId(profile.getId()));
        return vo;
    }

    private void createStatusLog(Long studentId, String oldStatus, String newStatus, String reason, String remark) {
        Long operatorId = AuthUserContext.currentUserId();
        SysUser operator = userMapper.selectById(operatorId);
        StudentStatusLog log = new StudentStatusLog();
        log.setStudentId(studentId);
        log.setOldStatus(oldStatus);
        log.setNewStatus(newStatus);
        log.setChangeReason(reason);
        log.setOperatorId(operatorId);
        log.setOperatorName(operator == null ? null : operator.getRealName());
        log.setRemark(remark);
        statusLogService.createStatusLog(log);
    }
}
