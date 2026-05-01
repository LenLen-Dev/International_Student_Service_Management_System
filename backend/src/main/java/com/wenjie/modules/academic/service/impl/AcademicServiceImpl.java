package com.wenjie.modules.academic.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.academic.dto.AcademicApproveDTO;
import com.wenjie.modules.academic.dto.AcademicClassDTO;
import com.wenjie.modules.academic.dto.AcademicGradeDTO;
import com.wenjie.modules.academic.dto.AcademicLeaveApplyDTO;
import com.wenjie.modules.academic.dto.AcademicMajorDTO;
import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.dto.AcademicRecordStatusDTO;
import com.wenjie.modules.academic.dto.AcademicStatusChangeDTO;
import com.wenjie.modules.academic.dto.AcademicStudentRecordDTO;
import com.wenjie.modules.academic.entity.AcademicClass;
import com.wenjie.modules.academic.entity.AcademicGrade;
import com.wenjie.modules.academic.entity.AcademicLeaveApplication;
import com.wenjie.modules.academic.entity.AcademicMajor;
import com.wenjie.modules.academic.entity.AcademicStatusChange;
import com.wenjie.modules.academic.entity.AcademicStudentRecord;
import com.wenjie.modules.academic.mapper.AcademicClassMapper;
import com.wenjie.modules.academic.mapper.AcademicGradeMapper;
import com.wenjie.modules.academic.mapper.AcademicLeaveMapper;
import com.wenjie.modules.academic.mapper.AcademicMajorMapper;
import com.wenjie.modules.academic.mapper.AcademicStatusChangeMapper;
import com.wenjie.modules.academic.mapper.AcademicStudentRecordMapper;
import com.wenjie.modules.academic.service.AcademicService;
import com.wenjie.modules.academic.vo.AcademicMyOverviewVO;
import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import com.wenjie.security.AuthUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private final AcademicMajorMapper majorMapper;
    private final AcademicGradeMapper gradeMapper;
    private final AcademicClassMapper classMapper;
    private final AcademicStudentRecordMapper recordMapper;
    private final AcademicLeaveMapper leaveMapper;
    private final AcademicStatusChangeMapper changeMapper;
    private final StudentProfileMapper studentProfileMapper;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<AcademicMajor> pageMajors(AcademicQueryDTO query) {
        return new PageResult<>(majorMapper.countPage(query), majorMapper.selectPage(query));
    }

    @Override
    public List<AcademicMajor> enabledMajors() {
        return majorMapper.selectAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMajor(AcademicMajorDTO dto) {
        ensureMajorCodeUnique(dto.getMajorCode(), null);
        AcademicMajor major = toMajor(null, dto);
        majorMapper.insert(major);
        return major.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMajor(Long id, AcademicMajorDTO dto) {
        ensureMajor(id);
        ensureMajorCodeUnique(dto.getMajorCode(), id);
        majorMapper.updateById(toMajor(id, dto));
    }

    @Override
    public void updateMajorStatus(Long id, Integer status) {
        ensureMajor(id);
        majorMapper.updateStatus(id, status);
    }

    @Override
    public void deleteMajor(Long id) {
        ensureMajor(id);
        if (majorMapper.countUsed(id) > 0) {
            throw new BusinessException("专业已被学籍记录引用，请先禁用");
        }
        majorMapper.deleteById(id);
    }

    @Override
    public PageResult<AcademicGrade> pageGrades(AcademicQueryDTO query) {
        return new PageResult<>(gradeMapper.countPage(query), gradeMapper.selectPage(query));
    }

    @Override
    public List<AcademicGrade> enabledGrades() {
        return gradeMapper.selectAllEnabled();
    }

    @Override
    public Long createGrade(AcademicGradeDTO dto) {
        ensureGradeCodeUnique(dto.getGradeCode(), null);
        AcademicGrade grade = toGrade(null, dto);
        gradeMapper.insert(grade);
        return grade.getId();
    }

    @Override
    public void updateGrade(Long id, AcademicGradeDTO dto) {
        ensureGrade(id);
        ensureGradeCodeUnique(dto.getGradeCode(), id);
        gradeMapper.updateById(toGrade(id, dto));
    }

    @Override
    public void deleteGrade(Long id) {
        ensureGrade(id);
        if (gradeMapper.countUsed(id) > 0) {
            throw new BusinessException("年级已被学籍记录引用，不能删除");
        }
        gradeMapper.deleteById(id);
    }

    @Override
    public PageResult<AcademicClass> pageClasses(AcademicQueryDTO query) {
        return new PageResult<>(classMapper.countPage(query), classMapper.selectPage(query));
    }

    @Override
    public List<AcademicClass> enabledClasses() {
        return classMapper.selectAllEnabled();
    }

    @Override
    public Long createClass(AcademicClassDTO dto) {
        ensureMajor(dto.getMajorId());
        ensureGrade(dto.getGradeId());
        ensureClassCodeUnique(dto.getClassCode(), null);
        AcademicClass clazz = toClass(null, dto);
        classMapper.insert(clazz);
        return clazz.getId();
    }

    @Override
    public void updateClass(Long id, AcademicClassDTO dto) {
        ensureClass(id);
        ensureMajor(dto.getMajorId());
        ensureGrade(dto.getGradeId());
        ensureClassCodeUnique(dto.getClassCode(), id);
        classMapper.updateById(toClass(id, dto));
    }

    @Override
    public void deleteClass(Long id) {
        ensureClass(id);
        if (classMapper.countUsed(id) > 0) {
            throw new BusinessException("班级已被学籍记录引用，不能删除");
        }
        classMapper.deleteById(id);
    }

    @Override
    public PageResult<AcademicStudentRecord> pageRecords(AcademicQueryDTO query) {
        return new PageResult<>(recordMapper.countPage(query), recordMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRecord(AcademicStudentRecordDTO dto) {
        ensureStudent(dto.getStudentId());
        if (recordMapper.selectByStudentId(dto.getStudentId()) != null) {
            throw new BusinessException("该学生已存在学籍记录");
        }
        validateAcademicRelation(dto.getMajorId(), dto.getGradeId(), dto.getClassId());
        AcademicStudentRecord record = toRecord(null, dto);
        recordMapper.insert(record);
        syncStudentProfile(recordMapper.selectById(record.getId()));
        return record.getId();
    }

    @Override
    public AcademicStudentRecord getRecord(Long id) {
        AcademicStudentRecord record = recordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(404, "学籍记录不存在");
        }
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecord(Long id, AcademicStudentRecordDTO dto) {
        getRecord(id);
        ensureStudent(dto.getStudentId());
        validateAcademicRelation(dto.getMajorId(), dto.getGradeId(), dto.getClassId());
        recordMapper.updateById(toRecord(id, dto));
        syncStudentProfile(recordMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordStatus(Long id, AcademicRecordStatusDTO dto) {
        AcademicStudentRecord record = getRecord(id);
        record.setStudentStatus(dto.getStudentStatus());
        record.setActualLeaveDate(dto.getActualLeaveDate());
        record.setRemark(dto.getRemark());
        recordMapper.updateStatus(record);
        syncStudentProfile(recordMapper.selectById(id));
    }

    @Override
    public PageResult<AcademicLeaveApplication> pageLeaves(AcademicQueryDTO query) {
        return new PageResult<>(leaveMapper.countPage(query), leaveMapper.selectPage(query));
    }

    @Override
    public AcademicLeaveApplication getLeave(Long id) {
        AcademicLeaveApplication leave = leaveMapper.selectById(id);
        if (leave == null) {
            throw new BusinessException(404, "请假申请不存在");
        }
        return leave;
    }

    @Override
    public void approveLeave(Long id, AcademicApproveDTO dto) {
        approveLeaveInternal(id, "APPROVED", dto);
    }

    @Override
    public void rejectLeave(Long id, AcademicApproveDTO dto) {
        approveLeaveInternal(id, "REJECTED", dto);
    }

    @Override
    public List<AcademicLeaveApplication> myLeaves() {
        return leaveMapper.selectByStudentId(currentStudent().getId());
    }

    @Override
    public Long applyMyLeave(AcademicLeaveApplyDTO dto) {
        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new BusinessException("请假结束时间必须晚于开始时间");
        }
        AcademicLeaveApplication leave = new AcademicLeaveApplication();
        leave.setStudentId(currentStudent().getId());
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartTime(dto.getStartTime());
        leave.setEndTime(dto.getEndTime());
        leave.setReason(dto.getReason());
        leave.setLeaveStatus("PENDING");
        leave.setRemark(dto.getRemark());
        leaveMapper.insert(leave);
        return leave.getId();
    }

    @Override
    public void cancelMyLeave(Long id) {
        AcademicLeaveApplication leave = getLeave(id);
        if (!leave.getStudentId().equals(currentStudent().getId())) {
            throw new BusinessException(403, "不能撤回他人的请假申请");
        }
        if (!"PENDING".equals(leave.getLeaveStatus())) {
            throw new BusinessException("仅待审批请假可以撤回");
        }
        leave.setLeaveStatus("CANCELLED");
        leaveMapper.updateApproval(leave);
    }

    @Override
    public PageResult<AcademicStatusChange> pageChanges(AcademicQueryDTO query) {
        return new PageResult<>(changeMapper.countPage(query), changeMapper.selectPage(query));
    }

    @Override
    public Long createChange(AcademicStatusChangeDTO dto) {
        AcademicStudentRecord record = ensureStudentRecord(dto.getStudentId());
        AcademicStatusChange change = toChange(null, dto, record);
        validateChangeTarget(change);
        changeMapper.insert(change);
        return change.getId();
    }

    @Override
    public AcademicStatusChange getChange(Long id) {
        AcademicStatusChange change = changeMapper.selectById(id);
        if (change == null) {
            throw new BusinessException(404, "学籍异动不存在");
        }
        return change;
    }

    @Override
    public void updateChange(Long id, AcademicStatusChangeDTO dto) {
        AcademicStatusChange existing = getChange(id);
        if (!"PENDING".equals(existing.getChangeStatus())) {
            throw new BusinessException("仅待审批异动可以编辑");
        }
        AcademicStudentRecord record = ensureStudentRecord(dto.getStudentId());
        AcademicStatusChange change = toChange(id, dto, record);
        validateChangeTarget(change);
        changeMapper.updateById(change);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveChange(Long id, AcademicApproveDTO dto) {
        AcademicStatusChange change = getChange(id);
        if (!"PENDING".equals(change.getChangeStatus())) {
            throw new BusinessException("仅待审批异动可以审批");
        }
        fillApproval(change, "APPROVED", dto);
        changeMapper.updateApproval(change);
        applyChange(change);
    }

    @Override
    public void rejectChange(Long id, AcademicApproveDTO dto) {
        AcademicStatusChange change = getChange(id);
        if (!"PENDING".equals(change.getChangeStatus())) {
            throw new BusinessException("仅待审批异动可以审批");
        }
        fillApproval(change, "REJECTED", dto);
        changeMapper.updateApproval(change);
    }

    @Override
    public AcademicMyOverviewVO myOverview() {
        StudentProfile student = currentStudent();
        AcademicMyOverviewVO vo = new AcademicMyOverviewVO();
        vo.setRecord(recordMapper.selectByStudentId(student.getId()));
        vo.setLeaves(leaveMapper.selectByStudentId(student.getId()));
        return vo;
    }

    private void approveLeaveInternal(Long id, String status, AcademicApproveDTO dto) {
        AcademicLeaveApplication leave = getLeave(id);
        if (!"PENDING".equals(leave.getLeaveStatus())) {
            throw new BusinessException("仅待审批请假可以审批");
        }
        Long userId = AuthUserContext.currentUserId();
        SysUser user = userMapper.selectById(userId);
        leave.setLeaveStatus(status);
        leave.setApproverId(userId);
        leave.setApproverName(user == null ? String.valueOf(userId) : user.getRealName());
        leave.setApproveTime(LocalDateTime.now());
        leave.setApproveOpinion(dto == null ? null : dto.getApproveOpinion());
        leaveMapper.updateApproval(leave);
    }

    private void applyChange(AcademicStatusChange change) {
        AcademicStudentRecord record = ensureStudentRecord(change.getStudentId());
        record.setMajorId(change.getNewMajorId() == null ? record.getMajorId() : change.getNewMajorId());
        record.setGradeId(change.getNewGradeId() == null ? record.getGradeId() : change.getNewGradeId());
        record.setClassId(change.getNewClassId() == null ? record.getClassId() : change.getNewClassId());
        record.setStudentStatus(change.getNewStatus() == null ? record.getStudentStatus() : change.getNewStatus());
        if (List.of("GRADUATE", "DROP_OUT", "LEAVE_SCHOOL").contains(change.getChangeType())) {
            record.setActualLeaveDate(change.getEffectiveDate() == null ? LocalDate.now() : change.getEffectiveDate());
        }
        validateAcademicRelation(record.getMajorId(), record.getGradeId(), record.getClassId());
        recordMapper.updateById(record);
        syncStudentProfile(recordMapper.selectById(record.getId()));
    }

    private void syncStudentProfile(AcademicStudentRecord record) {
        if (record == null) {
            return;
        }
        StudentProfile profile = new StudentProfile();
        profile.setId(record.getStudentId());
        profile.setCollege(record.getCollege());
        profile.setMajor(record.getMajorName());
        profile.setDegreeLevel(record.getDegreeLevel());
        profile.setGrade(record.getGradeName());
        profile.setClassName(record.getClassName());
        profile.setEnrollmentDate(record.getEnrollmentDate());
        profile.setExpectedGraduationDate(record.getExpectedGraduationDate());
        profile.setStudentStatus(record.getStudentStatus());
        studentProfileMapper.updateAcademicFields(profile);
    }

    private void validateAcademicRelation(Long majorId, Long gradeId, Long classId) {
        ensureMajor(majorId);
        ensureGrade(gradeId);
        if (classId != null) {
            AcademicClass clazz = ensureClass(classId);
            if (!clazz.getMajorId().equals(majorId) || !clazz.getGradeId().equals(gradeId)) {
                throw new BusinessException("班级所属专业或年级与学籍记录不一致");
            }
        }
    }

    private void validateChangeTarget(AcademicStatusChange change) {
        Long majorId = change.getNewMajorId() == null ? change.getOldMajorId() : change.getNewMajorId();
        Long gradeId = change.getNewGradeId() == null ? change.getOldGradeId() : change.getNewGradeId();
        Long classId = change.getNewClassId() == null ? change.getOldClassId() : change.getNewClassId();
        validateAcademicRelation(majorId, gradeId, classId);
    }

    private AcademicStudentRecord ensureStudentRecord(Long studentId) {
        ensureStudent(studentId);
        AcademicStudentRecord record = recordMapper.selectByStudentId(studentId);
        if (record == null) {
            throw new BusinessException(404, "学生学籍记录不存在");
        }
        return record;
    }

    private StudentProfile ensureStudent(Long studentId) {
        StudentProfile student = studentProfileMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(404, "留学生档案不存在");
        }
        return student;
    }

    private StudentProfile currentStudent() {
        StudentProfile student = studentProfileMapper.selectByUserId(AuthUserContext.currentUserId());
        if (student == null) {
            throw new BusinessException(404, "当前用户未绑定留学生档案");
        }
        return student;
    }

    private AcademicMajor ensureMajor(Long id) {
        AcademicMajor major = majorMapper.selectById(id);
        if (major == null) {
            throw new BusinessException(404, "专业不存在");
        }
        return major;
    }

    private AcademicGrade ensureGrade(Long id) {
        AcademicGrade grade = gradeMapper.selectById(id);
        if (grade == null) {
            throw new BusinessException(404, "年级不存在");
        }
        return grade;
    }

    private AcademicClass ensureClass(Long id) {
        AcademicClass clazz = classMapper.selectById(id);
        if (clazz == null) {
            throw new BusinessException(404, "班级不存在");
        }
        return clazz;
    }

    private void ensureMajorCodeUnique(String code, Long currentId) {
        AcademicMajor existing = majorMapper.selectByCode(code);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException("专业编码已存在");
        }
    }

    private void ensureGradeCodeUnique(String code, Long currentId) {
        AcademicGrade existing = gradeMapper.selectByCode(code);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException("年级编码已存在");
        }
    }

    private void ensureClassCodeUnique(String code, Long currentId) {
        AcademicClass existing = classMapper.selectByCode(code);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException("班级编码已存在");
        }
    }

    private void fillApproval(AcademicStatusChange change, String status, AcademicApproveDTO dto) {
        Long userId = AuthUserContext.currentUserId();
        SysUser user = userMapper.selectById(userId);
        change.setChangeStatus(status);
        change.setApproverId(userId);
        change.setApproverName(user == null ? String.valueOf(userId) : user.getRealName());
        change.setApproveTime(LocalDateTime.now());
        change.setApproveOpinion(dto == null ? null : dto.getApproveOpinion());
    }

    private AcademicMajor toMajor(Long id, AcademicMajorDTO dto) {
        AcademicMajor major = new AcademicMajor();
        major.setId(id);
        major.setCollege(dto.getCollege());
        major.setMajorCode(dto.getMajorCode());
        major.setMajorName(dto.getMajorName());
        major.setDegreeLevel(dto.getDegreeLevel());
        major.setStudyDuration(dto.getStudyDuration());
        major.setStatus(dto.getStatus());
        major.setRemark(dto.getRemark());
        return major;
    }

    private AcademicGrade toGrade(Long id, AcademicGradeDTO dto) {
        AcademicGrade grade = new AcademicGrade();
        grade.setId(id);
        grade.setGradeCode(dto.getGradeCode());
        grade.setGradeName(dto.getGradeName());
        grade.setEnrollmentYear(dto.getEnrollmentYear());
        grade.setGraduationYear(dto.getGraduationYear());
        grade.setStatus(dto.getStatus());
        grade.setRemark(dto.getRemark());
        return grade;
    }

    private AcademicClass toClass(Long id, AcademicClassDTO dto) {
        AcademicClass clazz = new AcademicClass();
        clazz.setId(id);
        clazz.setMajorId(dto.getMajorId());
        clazz.setGradeId(dto.getGradeId());
        clazz.setClassCode(dto.getClassCode());
        clazz.setClassName(dto.getClassName());
        clazz.setAdvisorId(dto.getAdvisorId());
        clazz.setAdvisorName(dto.getAdvisorName());
        clazz.setStatus(dto.getStatus());
        clazz.setRemark(dto.getRemark());
        return clazz;
    }

    private AcademicStudentRecord toRecord(Long id, AcademicStudentRecordDTO dto) {
        AcademicStudentRecord record = new AcademicStudentRecord();
        record.setId(id);
        record.setStudentId(dto.getStudentId());
        record.setMajorId(dto.getMajorId());
        record.setGradeId(dto.getGradeId());
        record.setClassId(dto.getClassId());
        record.setStudentStatus(dto.getStudentStatus());
        record.setEnrollmentDate(dto.getEnrollmentDate());
        record.setExpectedGraduationDate(dto.getExpectedGraduationDate());
        record.setActualLeaveDate(dto.getActualLeaveDate());
        record.setRemark(dto.getRemark());
        return record;
    }

    private AcademicStatusChange toChange(Long id, AcademicStatusChangeDTO dto, AcademicStudentRecord record) {
        AcademicStatusChange change = new AcademicStatusChange();
        change.setId(id);
        change.setStudentId(dto.getStudentId());
        change.setChangeType(dto.getChangeType());
        change.setOldStatus(record.getStudentStatus());
        change.setNewStatus(resolveNewStatus(dto.getChangeType(), dto.getNewStatus()));
        change.setOldMajorId(record.getMajorId());
        change.setNewMajorId(dto.getNewMajorId());
        change.setOldGradeId(record.getGradeId());
        change.setNewGradeId(dto.getNewGradeId());
        change.setOldClassId(record.getClassId());
        change.setNewClassId(dto.getNewClassId());
        change.setEffectiveDate(dto.getEffectiveDate());
        change.setReason(dto.getReason());
        change.setChangeStatus("PENDING");
        change.setRemark(dto.getRemark());
        return change;
    }

    private String resolveNewStatus(String changeType, String newStatus) {
        return switch (changeType) {
            case "SUSPEND" -> "SUSPENDED";
            case "RESUME" -> "ENROLLED";
            case "GRADUATE" -> "GRADUATED";
            case "DROP_OUT" -> "DROPPED";
            case "LEAVE_SCHOOL" -> "LEFT";
            default -> newStatus;
        };
    }
}
