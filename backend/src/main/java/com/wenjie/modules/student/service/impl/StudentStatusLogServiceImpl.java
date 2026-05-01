/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service.impl
 * @className: StudentStatusLogServiceImpl
 * @description: TODO 留学生状态变更日志业务实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service.impl;

import com.wenjie.modules.student.converter.StudentProfileConverter;
import com.wenjie.modules.student.entity.StudentStatusLog;
import com.wenjie.modules.student.mapper.StudentStatusLogMapper;
import com.wenjie.modules.student.service.StudentStatusLogService;
import com.wenjie.modules.student.vo.StudentStatusLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentStatusLogServiceImpl implements StudentStatusLogService {

    private final StudentStatusLogMapper statusLogMapper;

    @Override
    public List<StudentStatusLogVO> listByStudentId(Long studentId) {
        return statusLogMapper.selectByStudentId(studentId).stream()
                .map(StudentProfileConverter::toVO)
                .toList();
    }

    @Override
    public Long createStatusLog(StudentStatusLog statusLog) {
        statusLogMapper.insert(statusLog);
        return statusLog.getId();
    }
}
