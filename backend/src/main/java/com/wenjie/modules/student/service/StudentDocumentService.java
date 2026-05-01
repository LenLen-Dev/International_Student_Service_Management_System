/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service
 * @className: StudentDocumentService
 * @description: TODO 留学生附件材料业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service;

import com.wenjie.modules.student.dto.StudentDocumentDTO;
import com.wenjie.modules.student.vo.StudentDocumentVO;

import java.util.List;

public interface StudentDocumentService {
    List<StudentDocumentVO> listByStudentId(Long studentId);
    Long createDocument(Long studentId, StudentDocumentDTO dto);
    void updateDocument(Long id, StudentDocumentDTO dto);
    void deleteDocument(Long id);
}
