package com.wenjie.modules.teaching.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import com.wenjie.modules.teaching.dto.TeachingAlertHandleDTO;
import com.wenjie.modules.teaching.dto.TeachingAttendanceItemDTO;
import com.wenjie.modules.teaching.dto.TeachingAttendanceSaveDTO;
import com.wenjie.modules.teaching.dto.TeachingCourseDTO;
import com.wenjie.modules.teaching.dto.TeachingGradeDTO;
import com.wenjie.modules.teaching.dto.TeachingOfferingDTO;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingAcademicAlert;
import com.wenjie.modules.teaching.entity.TeachingAttendance;
import com.wenjie.modules.teaching.entity.TeachingCourse;
import com.wenjie.modules.teaching.entity.TeachingCourseEnrollment;
import com.wenjie.modules.teaching.entity.TeachingCourseOffering;
import com.wenjie.modules.teaching.entity.TeachingGrade;
import com.wenjie.modules.teaching.mapper.TeachingAlertMapper;
import com.wenjie.modules.teaching.mapper.TeachingAttendanceMapper;
import com.wenjie.modules.teaching.mapper.TeachingCourseMapper;
import com.wenjie.modules.teaching.mapper.TeachingEnrollmentMapper;
import com.wenjie.modules.teaching.mapper.TeachingGradeMapper;
import com.wenjie.modules.teaching.mapper.TeachingOfferingMapper;
import com.wenjie.modules.teaching.service.TeachingService;
import com.wenjie.modules.teaching.vo.TeachingMyOverviewVO;
import com.wenjie.security.AuthUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeachingServiceImpl implements TeachingService {

    private final TeachingCourseMapper courseMapper;
    private final TeachingOfferingMapper offeringMapper;
    private final TeachingEnrollmentMapper enrollmentMapper;
    private final TeachingGradeMapper gradeMapper;
    private final TeachingAttendanceMapper attendanceMapper;
    private final TeachingAlertMapper alertMapper;
    private final StudentProfileMapper studentProfileMapper;
    private final SysUserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public PageResult<TeachingCourse> pageCourses(TeachingQueryDTO query) {
        return new PageResult<>(courseMapper.countPage(query), courseMapper.selectPage(query));
    }

    @Override
    public List<TeachingCourse> enabledCourses() {
        return courseMapper.selectAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCourse(TeachingCourseDTO dto) {
        ensureCourseCodeUnique(dto.getCourseCode(), null);
        TeachingCourse course = toCourse(null, dto);
        courseMapper.insert(course);
        return course.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourse(Long id, TeachingCourseDTO dto) {
        ensureCourse(id);
        ensureCourseCodeUnique(dto.getCourseCode(), id);
        courseMapper.updateById(toCourse(id, dto));
    }

    @Override
    public void updateCourseStatus(Long id, Integer status) {
        ensureCourse(id);
        courseMapper.updateStatus(id, status);
    }

    @Override
    public void deleteCourse(Long id) {
        ensureCourse(id);
        if (courseMapper.countUsed(id) > 0) {
            throw new BusinessException("课程已被教学班引用，不能删除");
        }
        courseMapper.deleteById(id);
    }

    @Override
    public PageResult<TeachingCourseOffering> pageOfferings(TeachingQueryDTO query) {
        restrictTeacherQuery(query);
        return new PageResult<>(offeringMapper.countPage(query), offeringMapper.selectPage(query));
    }

    @Override
    public List<TeachingCourseOffering> myOfferings() {
        return offeringMapper.selectMy(AuthUserContext.currentUserId());
    }

    @Override
    public List<TeachingCourseOffering> availableOfferings(TeachingQueryDTO query) {
        return offeringMapper.selectAvailable(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOffering(TeachingOfferingDTO dto) {
        ensureCourse(dto.getCourseId());
        Long teacherId = resolveTeacherId(dto.getTeacherId());
        SysUser teacher = ensureUser(teacherId);
        TeachingCourseOffering offering = toOffering(null, dto, teacher);
        offeringMapper.insert(offering);
        return offering.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOffering(Long id, TeachingOfferingDTO dto) {
        TeachingCourseOffering existing = ensureOffering(id);
        ensureOfferingOwner(existing);
        ensureCourse(dto.getCourseId());
        if (existing.getSelectedCount() != null && dto.getCapacity() != null && dto.getCapacity() < existing.getSelectedCount()) {
            throw new BusinessException("容量不能小于已选人数");
        }
        Long teacherId = resolveTeacherId(dto.getTeacherId());
        SysUser teacher = ensureUser(teacherId);
        offeringMapper.updateById(toOffering(id, dto, teacher));
    }

    @Override
    public void updateOfferingStatus(Long id, String status) {
        TeachingCourseOffering offering = ensureOffering(id);
        ensureOfferingOwner(offering);
        offeringMapper.updateStatus(id, status);
    }

    @Override
    public void deleteOffering(Long id) {
        TeachingCourseOffering offering = ensureOffering(id);
        ensureOfferingOwner(offering);
        if (offeringMapper.countUsed(id) > 0) {
            throw new BusinessException("教学班已有选课记录，不能删除");
        }
        offeringMapper.deleteById(id);
    }

    @Override
    public List<TeachingCourseEnrollment> offeringStudents(Long offeringId) {
        TeachingCourseOffering offering = ensureOffering(offeringId);
        ensureOfferingOwner(offering);
        return enrollmentMapper.selectSelectedByOffering(offeringId);
    }

    @Override
    public PageResult<TeachingCourseEnrollment> pageEnrollments(TeachingQueryDTO query) {
        return new PageResult<>(enrollmentMapper.countPage(query), enrollmentMapper.selectPage(query));
    }

    @Override
    public List<TeachingCourseEnrollment> mySelections() {
        return enrollmentMapper.selectByStudent(currentStudent().getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectCourse(Long offeringId) {
        StudentProfile student = currentStudent();
        TeachingCourseOffering offering = ensureOffering(offeringId);
        ensureSelectable(offering);
        TeachingCourseEnrollment existing = enrollmentMapper.selectByOfferingAndStudent(offeringId, student.getId());
        if (existing != null && "SELECTED".equals(existing.getEnrollmentStatus())) {
            throw new BusinessException("已选择该课程");
        }
        if (offering.getCapacity() != null && offering.getSelectedCount() != null && offering.getSelectedCount() >= offering.getCapacity()) {
            throw new BusinessException("课程容量已满");
        }
        TeachingCourseEnrollment enrollment;
        if (existing != null) {
            enrollmentMapper.reselect(existing.getId());
            enrollment = enrollmentMapper.selectById(existing.getId());
        } else {
            enrollment = new TeachingCourseEnrollment();
            enrollment.setOfferingId(offeringId);
            enrollment.setStudentId(student.getId());
            enrollment.setEnrollmentStatus("SELECTED");
            enrollment.setSelectTime(LocalDateTime.now());
            enrollmentMapper.insert(enrollment);
        }
        offeringMapper.updateSelectedCount(offeringId, 1);
        ensureDraftGrade(enrollment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dropCourse(Long offeringId) {
        StudentProfile student = currentStudent();
        TeachingCourseOffering offering = ensureOffering(offeringId);
        ensureSelectable(offering);
        TeachingCourseEnrollment enrollment = enrollmentMapper.selectByOfferingAndStudent(offeringId, student.getId());
        if (enrollment == null || !"SELECTED".equals(enrollment.getEnrollmentStatus())) {
            throw new BusinessException("未选择该课程");
        }
        if (gradeMapper.countPublishedByEnrollment(enrollment.getId()) > 0) {
            throw new BusinessException("成绩已发布，不能退课");
        }
        enrollmentMapper.drop(enrollment.getId());
        gradeMapper.deleteDraftByEnrollment(enrollment.getId());
        offeringMapper.updateSelectedCount(offeringId, -1);
    }

    @Override
    public PageResult<TeachingGrade> pageGrades(TeachingQueryDTO query) {
        restrictTeacherQuery(query);
        return new PageResult<>(gradeMapper.countPage(query), gradeMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGrade(Long id, TeachingGradeDTO dto) {
        TeachingGrade existing = ensureGrade(id);
        ensureOfferingOwner(ensureOffering(existing.getOfferingId()));
        TeachingGrade grade = buildGrade(existing.getEnrollmentId(), dto);
        grade.setId(id);
        gradeMapper.updateById(grade);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importGrades(Long offeringId, List<TeachingGradeDTO> grades) {
        TeachingCourseOffering offering = ensureOffering(offeringId);
        ensureOfferingOwner(offering);
        for (TeachingGradeDTO dto : grades) {
            TeachingCourseEnrollment enrollment = ensureEnrollment(dto.getEnrollmentId());
            if (!offeringId.equals(enrollment.getOfferingId())) {
                throw new BusinessException("成绩记录不属于当前教学班");
            }
            TeachingGrade existing = gradeMapper.selectByEnrollmentId(enrollment.getId());
            TeachingGrade grade = buildGrade(enrollment.getId(), dto);
            if (existing == null) {
                gradeMapper.insert(grade);
            } else {
                grade.setId(existing.getId());
                gradeMapper.updateById(grade);
            }
        }
    }

    @Override
    public void publishGrades(Long offeringId) {
        TeachingCourseOffering offering = ensureOffering(offeringId);
        ensureOfferingOwner(offering);
        gradeMapper.publishByOffering(offeringId);
    }

    @Override
    public List<TeachingGrade> myGrades() {
        return gradeMapper.selectByStudent(currentStudent().getId(), true);
    }

    @Override
    public PageResult<TeachingAttendance> pageAttendance(TeachingQueryDTO query) {
        restrictTeacherQuery(query);
        return new PageResult<>(attendanceMapper.countPage(query), attendanceMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttendance(Long offeringId, TeachingAttendanceSaveDTO dto) {
        TeachingCourseOffering offering = ensureOffering(offeringId);
        ensureOfferingOwner(offering);
        for (TeachingAttendanceItemDTO item : dto.getRecords()) {
            TeachingCourseEnrollment enrollment = enrollmentMapper.selectByOfferingAndStudent(offeringId, item.getStudentId());
            if (enrollment == null || !"SELECTED".equals(enrollment.getEnrollmentStatus())) {
                throw new BusinessException("学生未选择当前教学班");
            }
            TeachingAttendance attendance = new TeachingAttendance();
            attendance.setOfferingId(offeringId);
            attendance.setStudentId(item.getStudentId());
            attendance.setAttendanceDate(item.getAttendanceDate());
            attendance.setAttendanceStatus(item.getAttendanceStatus());
            attendance.setRemark(item.getRemark());
            attendanceMapper.upsert(attendance);
        }
    }

    @Override
    public List<TeachingAttendance> myAttendance() {
        return attendanceMapper.selectByStudent(currentStudent().getId());
    }

    @Override
    public PageResult<TeachingAcademicAlert> pageAlerts(TeachingQueryDTO query) {
        return new PageResult<>(alertMapper.countPage(query), alertMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateAlerts() {
        int count = 0;
        for (TeachingGrade grade : gradeMapper.selectFailedOrLowScores()) {
            String type = grade.getPassed() != null && grade.getPassed() == 0 ? "FAILED_COURSE" : "LOW_SCORE";
            String level = "FAILED_COURSE".equals(type) ? "HIGH" : "MEDIUM";
            count += createAlertIfAbsent(grade.getStudentId(), type, level, grade.getId(),
                    "课程成绩预警", "课程「" + grade.getCourseName() + "」总评成绩为 " + grade.getTotalScore());
        }

        List<java.util.Map<String, Object>> absences = jdbcTemplate.queryForList("""
                SELECT student_id, offering_id, COUNT(1) absence_count
                FROM teaching_attendance
                WHERE deleted=0 AND attendance_status='ABSENT'
                GROUP BY student_id, offering_id
                HAVING COUNT(1) >= 3
                """);
        for (java.util.Map<String, Object> item : absences) {
            Long studentId = ((Number) item.get("student_id")).longValue();
            Long offeringId = ((Number) item.get("offering_id")).longValue();
            count += createAlertIfAbsent(studentId, "ABSENCE", "HIGH", offeringId,
                    "出勤异常预警", "当前课程缺勤次数已达到 3 次及以上");
        }

        List<Long> noSelectionStudents = jdbcTemplate.queryForList("""
                SELECT sp.id
                FROM student_profile sp
                WHERE sp.deleted=0 AND sp.status=1
                  AND NOT EXISTS (
                      SELECT 1 FROM teaching_course_enrollment e
                      WHERE e.student_id=sp.id AND e.deleted=0 AND e.enrollment_status='SELECTED'
                  )
                """, Long.class);
        for (Long studentId : noSelectionStudents) {
            count += createAlertIfAbsent(studentId, "NO_SELECTION", "MEDIUM", null,
                    "未选课预警", "当前学生暂无有效选课记录");
        }
        return count;
    }

    @Override
    public void handleAlert(Long id, TeachingAlertHandleDTO dto) {
        TeachingAcademicAlert alert = ensureAlert(id);
        Long userId = AuthUserContext.currentUserId();
        SysUser user = userMapper.selectById(userId);
        alert.setAlertStatus(dto.getAlertStatus());
        alert.setHandlerId(userId);
        alert.setHandlerName(user == null ? String.valueOf(userId) : user.getRealName());
        alert.setHandleTime(LocalDateTime.now());
        alert.setHandleRemark(dto.getHandleRemark());
        alertMapper.updateHandle(alert);
    }

    @Override
    public List<TeachingAcademicAlert> myAlerts() {
        return alertMapper.selectByStudent(currentStudent().getId());
    }

    @Override
    public TeachingMyOverviewVO myOverview() {
        StudentProfile student = currentStudent();
        TeachingMyOverviewVO vo = new TeachingMyOverviewVO();
        vo.setSelections(enrollmentMapper.selectByStudent(student.getId()));
        vo.setGrades(gradeMapper.selectByStudent(student.getId(), true));
        vo.setAttendance(attendanceMapper.selectByStudent(student.getId()));
        vo.setAlerts(alertMapper.selectByStudent(student.getId()));
        return vo;
    }

    private int createAlertIfAbsent(Long studentId, String type, String level, Long sourceId, String title, String content) {
        if (alertMapper.selectExisting(studentId, type, sourceId) != null) {
            return 0;
        }
        TeachingAcademicAlert alert = new TeachingAcademicAlert();
        alert.setStudentId(studentId);
        alert.setAlertType(type);
        alert.setAlertLevel(level);
        alert.setSourceId(sourceId);
        alert.setTitle(title);
        alert.setContent(content);
        alert.setAlertStatus("PENDING");
        alertMapper.insert(alert);
        return 1;
    }

    private void ensureSelectable(TeachingCourseOffering offering) {
        if (!"OPEN".equals(offering.getOfferingStatus())) {
            throw new BusinessException("教学班未开放选课");
        }
        LocalDateTime now = LocalDateTime.now();
        if (offering.getSelectionStartTime() != null && now.isBefore(offering.getSelectionStartTime())) {
            throw new BusinessException("选课尚未开始");
        }
        if (offering.getSelectionEndTime() != null && now.isAfter(offering.getSelectionEndTime())) {
            throw new BusinessException("选课已结束");
        }
    }

    private void ensureDraftGrade(TeachingCourseEnrollment enrollment) {
        if (gradeMapper.selectByEnrollmentId(enrollment.getId()) != null) {
            return;
        }
        TeachingGrade grade = new TeachingGrade();
        grade.setEnrollmentId(enrollment.getId());
        grade.setOfferingId(enrollment.getOfferingId());
        grade.setStudentId(enrollment.getStudentId());
        grade.setGradeStatus("DRAFT");
        gradeMapper.insert(grade);
    }

    private TeachingGrade buildGrade(Long enrollmentId, TeachingGradeDTO dto) {
        TeachingCourseEnrollment enrollment = ensureEnrollment(enrollmentId);
        TeachingGrade grade = new TeachingGrade();
        grade.setEnrollmentId(enrollmentId);
        grade.setOfferingId(enrollment.getOfferingId());
        grade.setStudentId(enrollment.getStudentId());
        grade.setUsualScore(dto.getUsualScore());
        grade.setFinalScore(dto.getFinalScore());
        BigDecimal total = dto.getTotalScore();
        if (total == null && dto.getUsualScore() != null && dto.getFinalScore() != null) {
            total = dto.getUsualScore().multiply(new BigDecimal("0.4")).add(dto.getFinalScore().multiply(new BigDecimal("0.6"))).setScale(2, RoundingMode.HALF_UP);
        }
        grade.setTotalScore(total);
        grade.setPassed(total == null ? null : total.compareTo(new BigDecimal("60")) >= 0 ? 1 : 0);
        grade.setGradePoint(calculateGradePoint(total));
        grade.setGradeStatus("DRAFT");
        grade.setRemark(dto.getRemark());
        return grade;
    }

    private BigDecimal calculateGradePoint(BigDecimal total) {
        if (total == null) {
            return null;
        }
        if (total.compareTo(new BigDecimal("90")) >= 0) return new BigDecimal("4.0");
        if (total.compareTo(new BigDecimal("80")) >= 0) return new BigDecimal("3.0");
        if (total.compareTo(new BigDecimal("70")) >= 0) return new BigDecimal("2.0");
        if (total.compareTo(new BigDecimal("60")) >= 0) return new BigDecimal("1.0");
        return BigDecimal.ZERO.setScale(1);
    }

    private TeachingCourse toCourse(Long id, TeachingCourseDTO dto) {
        TeachingCourse course = new TeachingCourse();
        course.setId(id);
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());
        course.setTotalHours(dto.getTotalHours());
        course.setCourseType(dto.getCourseType());
        course.setCollege(dto.getCollege());
        course.setStatus(dto.getStatus());
        course.setRemark(dto.getRemark());
        return course;
    }

    private TeachingCourseOffering toOffering(Long id, TeachingOfferingDTO dto, SysUser teacher) {
        TeachingCourseOffering offering = new TeachingCourseOffering();
        offering.setId(id);
        offering.setCourseId(dto.getCourseId());
        offering.setTeacherId(teacher.getId());
        offering.setTeacherName(teacher.getRealName() == null ? teacher.getUsername() : teacher.getRealName());
        offering.setAcademicYear(dto.getAcademicYear());
        offering.setSemester(dto.getSemester());
        offering.setCapacity(dto.getCapacity());
        offering.setSelectionStartTime(dto.getSelectionStartTime());
        offering.setSelectionEndTime(dto.getSelectionEndTime());
        offering.setClassTime(dto.getClassTime());
        offering.setClassroom(dto.getClassroom());
        offering.setOfferingStatus(dto.getOfferingStatus());
        offering.setRemark(dto.getRemark());
        return offering;
    }

    private void ensureCourseCodeUnique(String code, Long currentId) {
        TeachingCourse existing = courseMapper.selectByCode(code);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException("课程编码已存在");
        }
    }

    private TeachingCourse ensureCourse(Long id) {
        TeachingCourse course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        return course;
    }

    private TeachingCourseOffering ensureOffering(Long id) {
        TeachingCourseOffering offering = offeringMapper.selectById(id);
        if (offering == null) {
            throw new BusinessException(404, "教学班不存在");
        }
        return offering;
    }

    private TeachingCourseEnrollment ensureEnrollment(Long id) {
        TeachingCourseEnrollment enrollment = enrollmentMapper.selectById(id);
        if (enrollment == null) {
            throw new BusinessException(404, "选课记录不存在");
        }
        return enrollment;
    }

    private TeachingGrade ensureGrade(Long id) {
        TeachingGrade grade = gradeMapper.selectById(id);
        if (grade == null) {
            throw new BusinessException(404, "成绩记录不存在");
        }
        return grade;
    }

    private TeachingAcademicAlert ensureAlert(Long id) {
        TeachingAcademicAlert alert = alertMapper.selectById(id);
        if (alert == null) {
            throw new BusinessException(404, "学业预警不存在");
        }
        return alert;
    }

    private SysUser ensureUser(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "教师用户不存在");
        }
        return user;
    }

    private StudentProfile currentStudent() {
        StudentProfile student = studentProfileMapper.selectByUserId(AuthUserContext.currentUserId());
        if (student == null) {
            throw new BusinessException(404, "当前用户未绑定留学生档案");
        }
        return student;
    }

    private Long resolveTeacherId(Long requestedTeacherId) {
        Long currentUserId = AuthUserContext.currentUserId();
        if (isTeacherOnly()) {
            return currentUserId;
        }
        return requestedTeacherId == null ? currentUserId : requestedTeacherId;
    }

    private void restrictTeacherQuery(TeachingQueryDTO query) {
        if (isTeacherOnly()) {
            query.setTeacherId(AuthUserContext.currentUserId());
        }
    }

    private void ensureOfferingOwner(TeachingCourseOffering offering) {
        if (isTeacherOnly() && !offering.getTeacherId().equals(AuthUserContext.currentUserId())) {
            throw new BusinessException(403, "不能维护其他教师的教学班");
        }
    }

    private boolean isTeacherOnly() {
        List<String> roles = userMapper.selectRoleCodesByUserId(AuthUserContext.currentUserId());
        return roles.contains("TEACHER") && !roles.contains("SUPER_ADMIN") && !roles.contains("ACADEMIC_ADMIN");
    }
}
