/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service.impl
 * @className: StudentContactServiceImpl
 * @description: TODO 留学生联系人业务实现
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
import com.wenjie.modules.student.dto.StudentContactDTO;
import com.wenjie.modules.student.entity.StudentContact;
import com.wenjie.modules.student.mapper.StudentContactMapper;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.student.service.StudentContactService;
import com.wenjie.modules.student.vo.StudentContactVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentContactServiceImpl implements StudentContactService {

    private final StudentContactMapper contactMapper;
    private final StudentProfileMapper profileMapper;

    @Override
    public List<StudentContactVO> listByStudentId(Long studentId) {
        ensureStudentExists(studentId);
        return contactMapper.selectByStudentId(studentId).stream().map(StudentProfileConverter::toVO).toList();
    }

    @Override
    public Long createContact(Long studentId, StudentContactDTO dto) {
        ensureStudentExists(studentId);
        StudentContact contact = StudentProfileConverter.toEntity(studentId, dto);
        contactMapper.insert(contact);
        return contact.getId();
    }

    @Override
    public void updateContact(Long id, StudentContactDTO dto) {
        StudentContact existing = ensureExists(id);
        StudentContact contact = StudentProfileConverter.toEntity(existing.getStudentId(), dto);
        contact.setId(id);
        contactMapper.updateById(contact);
    }

    @Override
    public void deleteContact(Long id) {
        ensureExists(id);
        contactMapper.deleteById(id);
    }

    private void ensureStudentExists(Long studentId) {
        if (profileMapper.selectById(studentId) == null) {
            throw new BusinessException(404, "留学生档案不存在");
        }
    }

    private StudentContact ensureExists(Long id) {
        StudentContact contact = contactMapper.selectById(id);
        if (contact == null) {
            throw new BusinessException(404, "联系人不存在");
        }
        return contact;
    }
}
