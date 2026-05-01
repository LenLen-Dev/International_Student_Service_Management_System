package com.wenjie.modules.teaching.service;

import com.wenjie.common.PageResult;
import com.wenjie.modules.teaching.dto.TeachingAlertHandleDTO;
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
import com.wenjie.modules.teaching.vo.TeachingMyOverviewVO;

import java.util.List;

public interface TeachingService {
    PageResult<TeachingCourse> pageCourses(TeachingQueryDTO query);
    List<TeachingCourse> enabledCourses();
    Long createCourse(TeachingCourseDTO dto);
    void updateCourse(Long id, TeachingCourseDTO dto);
    void updateCourseStatus(Long id, Integer status);
    void deleteCourse(Long id);

    PageResult<TeachingCourseOffering> pageOfferings(TeachingQueryDTO query);
    List<TeachingCourseOffering> myOfferings();
    List<TeachingCourseOffering> availableOfferings(TeachingQueryDTO query);
    Long createOffering(TeachingOfferingDTO dto);
    void updateOffering(Long id, TeachingOfferingDTO dto);
    void updateOfferingStatus(Long id, String status);
    void deleteOffering(Long id);
    List<TeachingCourseEnrollment> offeringStudents(Long offeringId);

    PageResult<TeachingCourseEnrollment> pageEnrollments(TeachingQueryDTO query);
    List<TeachingCourseEnrollment> mySelections();
    void selectCourse(Long offeringId);
    void dropCourse(Long offeringId);

    PageResult<TeachingGrade> pageGrades(TeachingQueryDTO query);
    void updateGrade(Long id, TeachingGradeDTO dto);
    void importGrades(Long offeringId, List<TeachingGradeDTO> grades);
    void publishGrades(Long offeringId);
    List<TeachingGrade> myGrades();

    PageResult<TeachingAttendance> pageAttendance(TeachingQueryDTO query);
    void saveAttendance(Long offeringId, TeachingAttendanceSaveDTO dto);
    List<TeachingAttendance> myAttendance();

    PageResult<TeachingAcademicAlert> pageAlerts(TeachingQueryDTO query);
    int generateAlerts();
    void handleAlert(Long id, TeachingAlertHandleDTO dto);
    List<TeachingAcademicAlert> myAlerts();
    TeachingMyOverviewVO myOverview();
}
