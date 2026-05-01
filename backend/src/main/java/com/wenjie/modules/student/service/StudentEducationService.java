/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service
 * @className: StudentEducationService
 * @description: TODO 留学生教育背景业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service;

import com.wenjie.modules.student.dto.StudentEducationDTO;
import com.wenjie.modules.student.vo.StudentEducationVO;

import java.util.List;

public interface StudentEducationService {
    List<StudentEducationVO> listByStudentId(Long studentId);
    Long createEducation(Long studentId, StudentEducationDTO dto);
    void updateEducation(Long id, StudentEducationDTO dto);
    void deleteEducation(Long id);
}
