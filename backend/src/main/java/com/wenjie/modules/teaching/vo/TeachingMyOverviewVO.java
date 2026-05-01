package com.wenjie.modules.teaching.vo;

import com.wenjie.modules.teaching.entity.TeachingAcademicAlert;
import com.wenjie.modules.teaching.entity.TeachingAttendance;
import com.wenjie.modules.teaching.entity.TeachingCourseEnrollment;
import com.wenjie.modules.teaching.entity.TeachingGrade;
import lombok.Data;

import java.util.List;

@Data
public class TeachingMyOverviewVO {
    private List<TeachingCourseEnrollment> selections;
    private List<TeachingGrade> grades;
    private List<TeachingAttendance> attendance;
    private List<TeachingAcademicAlert> alerts;
}
