package com.wenjie.modules.academic.vo;

import com.wenjie.modules.academic.entity.AcademicLeaveApplication;
import com.wenjie.modules.academic.entity.AcademicStudentRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AcademicMyOverviewVO {
    private AcademicStudentRecord record;
    private List<AcademicLeaveApplication> leaves = new ArrayList<>();
}
