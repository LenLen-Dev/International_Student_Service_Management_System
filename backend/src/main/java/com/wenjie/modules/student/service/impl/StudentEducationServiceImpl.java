/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service.impl
 * @className: StudentEducationServiceImpl
 * @description: TODO 留学生教育背景业务实现
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
import com.wenjie.modules.student.dto.StudentEducationDTO;
import com.wenjie.modules.student.entity.StudentEducation;
import com.wenjie.modules.student.mapper.StudentEducationMapper;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.student.service.StudentEducationService;
import com.wenjie.modules.student.vo.StudentEducationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentEducationServiceImpl implements StudentEducationService {

    private final StudentEducationMapper educationMapper;
    private final StudentProfileMapper profileMapper;

    @Override
    public List<StudentEducationVO> listByStudentId(Long studentId) {
        ensureStudentExists(studentId);
        return educationMapper.selectByStudentId(studentId).stream().map(StudentProfileConverter::toVO).toList();
    }

    @Override
    public Long createEducation(Long studentId, StudentEducationDTO dto) {
        ensureStudentExists(studentId);
        StudentEducation education = StudentProfileConverter.toEntity(studentId, dto);
        educationMapper.insert(education);
        return education.getId();
    }

    @Override
    public void updateEducation(Long id, StudentEducationDTO dto) {
        StudentEducation existing = ensureExists(id);
        StudentEducation education = StudentProfileConverter.toEntity(existing.getStudentId(), dto);
        education.setId(id);
        educationMapper.updateById(education);
    }

    @Override
    public void deleteEducation(Long id) {
        ensureExists(id);
        educationMapper.deleteById(id);
    }

    private void ensureStudentExists(Long studentId) {
        if (profileMapper.selectById(studentId) == null) {
            throw new BusinessException(404, "留学生档案不存在");
        }
    }

    private StudentEducation ensureExists(Long id) {
        StudentEducation education = educationMapper.selectById(id);
        if (education == null) {
            throw new BusinessException(404, "教育背景不存在");
        }
        return education;
    }
}
