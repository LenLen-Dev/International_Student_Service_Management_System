/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service.impl
 * @className: StudentDocumentServiceImpl
 * @description: TODO 留学生附件材料业务实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.modules.student.converter.StudentProfileConverter;
import com.wenjie.modules.student.dto.StudentDocumentDTO;
import com.wenjie.modules.student.entity.StudentDocument;
import com.wenjie.modules.student.mapper.StudentDocumentMapper;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.student.service.StudentDocumentService;
import com.wenjie.modules.student.vo.StudentDocumentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl implements StudentDocumentService {

    private final StudentDocumentMapper documentMapper;
    private final StudentProfileMapper profileMapper;

    @Override
    public List<StudentDocumentVO> listByStudentId(Long studentId) {
        ensureStudentExists(studentId);
        return documentMapper.selectByStudentId(studentId).stream().map(StudentProfileConverter::toVO).toList();
    }

    @Override
    public Long createDocument(Long studentId, StudentDocumentDTO dto) {
        ensureStudentExists(studentId);
        StudentDocument document = StudentProfileConverter.toEntity(studentId, dto);
        documentMapper.insert(document);
        return document.getId();
    }

    @Override
    public void updateDocument(Long id, StudentDocumentDTO dto) {
        StudentDocument existing = ensureExists(id);
        StudentDocument document = StudentProfileConverter.toEntity(existing.getStudentId(), dto);
        document.setId(id);
        documentMapper.updateById(document);
    }

    @Override
    public void deleteDocument(Long id) {
        ensureExists(id);
        documentMapper.deleteById(id);
    }

    private void ensureStudentExists(Long studentId) {
        if (profileMapper.selectById(studentId) == null) {
            throw new BusinessException(404, "留学生档案不存在");
        }
    }

    private StudentDocument ensureExists(Long id) {
        StudentDocument document = documentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException(404, "附件材料不存在");
        }
        return document;
    }
}
