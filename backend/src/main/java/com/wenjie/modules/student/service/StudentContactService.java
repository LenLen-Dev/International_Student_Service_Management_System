/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service
 * @className: StudentContactService
 * @description: TODO 留学生联系人业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service;

import com.wenjie.modules.student.dto.StudentContactDTO;
import com.wenjie.modules.student.vo.StudentContactVO;

import java.util.List;

public interface StudentContactService {
    List<StudentContactVO> listByStudentId(Long studentId);
    Long createContact(Long studentId, StudentContactDTO dto);
    void updateContact(Long id, StudentContactDTO dto);
    void deleteContact(Long id);
}
