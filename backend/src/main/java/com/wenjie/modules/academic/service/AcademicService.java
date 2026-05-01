package com.wenjie.modules.academic.service;

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
import com.wenjie.modules.academic.vo.AcademicMyOverviewVO;

import java.util.List;

public interface AcademicService {
    PageResult<AcademicMajor> pageMajors(AcademicQueryDTO query);
    List<AcademicMajor> enabledMajors();
    Long createMajor(AcademicMajorDTO dto);
    void updateMajor(Long id, AcademicMajorDTO dto);
    void updateMajorStatus(Long id, Integer status);
    void deleteMajor(Long id);

    PageResult<AcademicGrade> pageGrades(AcademicQueryDTO query);
    List<AcademicGrade> enabledGrades();
    Long createGrade(AcademicGradeDTO dto);
    void updateGrade(Long id, AcademicGradeDTO dto);
    void deleteGrade(Long id);

    PageResult<AcademicClass> pageClasses(AcademicQueryDTO query);
    List<AcademicClass> enabledClasses();
    Long createClass(AcademicClassDTO dto);
    void updateClass(Long id, AcademicClassDTO dto);
    void deleteClass(Long id);

    PageResult<AcademicStudentRecord> pageRecords(AcademicQueryDTO query);
    Long createRecord(AcademicStudentRecordDTO dto);
    AcademicStudentRecord getRecord(Long id);
    void updateRecord(Long id, AcademicStudentRecordDTO dto);
    void updateRecordStatus(Long id, AcademicRecordStatusDTO dto);

    PageResult<AcademicLeaveApplication> pageLeaves(AcademicQueryDTO query);
    AcademicLeaveApplication getLeave(Long id);
    void approveLeave(Long id, AcademicApproveDTO dto);
    void rejectLeave(Long id, AcademicApproveDTO dto);
    List<AcademicLeaveApplication> myLeaves();
    Long applyMyLeave(AcademicLeaveApplyDTO dto);
    void cancelMyLeave(Long id);

    PageResult<AcademicStatusChange> pageChanges(AcademicQueryDTO query);
    Long createChange(AcademicStatusChangeDTO dto);
    AcademicStatusChange getChange(Long id);
    void updateChange(Long id, AcademicStatusChangeDTO dto);
    void approveChange(Long id, AcademicApproveDTO dto);
    void rejectChange(Long id, AcademicApproveDTO dto);

    AcademicMyOverviewVO myOverview();
}
