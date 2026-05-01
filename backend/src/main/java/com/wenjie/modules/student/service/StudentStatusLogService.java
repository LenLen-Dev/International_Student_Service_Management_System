/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service
 * @className: StudentStatusLogService
 * @description: TODO 留学生状态变更日志业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service;

import com.wenjie.modules.student.entity.StudentStatusLog;
import com.wenjie.modules.student.vo.StudentStatusLogVO;

import java.util.List;

public interface StudentStatusLogService {
    List<StudentStatusLogVO> listByStudentId(Long studentId);
    Long createStatusLog(StudentStatusLog statusLog);
}
