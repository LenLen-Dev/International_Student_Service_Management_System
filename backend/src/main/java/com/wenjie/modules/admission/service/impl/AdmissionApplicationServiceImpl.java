package com.wenjie.modules.admission.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.admission.dto.AdmissionApplicationForm;
import com.wenjie.modules.admission.dto.AdmissionApplicationQuery;
import com.wenjie.modules.admission.dto.AdmissionMaterialReviewRequest;
import com.wenjie.modules.admission.dto.AdmissionReviewRequest;
import com.wenjie.modules.admission.entity.AdmissionApplication;
import com.wenjie.modules.admission.entity.AdmissionMaterial;
import com.wenjie.modules.admission.entity.AdmissionNotice;
import com.wenjie.modules.admission.entity.AdmissionReviewRecord;
import com.wenjie.modules.admission.mapper.AdmissionApplicationMapper;
import com.wenjie.modules.admission.mapper.AdmissionMaterialMapper;
import com.wenjie.modules.admission.mapper.AdmissionNoticeMapper;
import com.wenjie.modules.admission.mapper.AdmissionReviewRecordMapper;
import com.wenjie.modules.admission.service.AdmissionApplicationService;
import com.wenjie.modules.admission.vo.AdmissionApplicationDetailVO;
import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdmissionApplicationServiceImpl implements AdmissionApplicationService {

    private static final Set<String> EDITABLE = Set.of("DRAFT", "RETURNED");
    private static final Set<String> ALLOWED_MIME = Set.of(
            "application/pdf", "image/jpeg", "image/png",
            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    private static final long MAX_FILE_SIZE = 20L * 1024 * 1024;
    private static final DateTimeFormatter NUMBER_DATE = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final AdmissionApplicationMapper applicationMapper;
    private final AdmissionMaterialMapper materialMapper;
    private final AdmissionReviewRecordMapper reviewRecordMapper;
    private final AdmissionNoticeMapper noticeMapper;
    private final StudentProfileMapper studentProfileMapper;
    private final SysUserMapper userMapper;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.notice-font-path:C:/Windows/Fonts/simsun.ttc}")
    private String noticeFontPath;

    @Override
    public AdmissionApplicationDetailVO getMyApplication() {
        AdmissionApplication application = applicationMapper.selectByUserId(currentUserId());
        return application == null ? null : buildDetail(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMyApplication(AdmissionApplicationForm form) {
        AdmissionApplication existing = applicationMapper.selectByUserId(currentUserId());
        if (existing != null && !"REJECTED".equals(existing.getApplicationStatus())) {
            throw new BusinessException("当前账号已有招生申请，请直接编辑或查看");
        }
        AdmissionApplication entity = toEntity(form);
        entity.setApplicationNo(generateApplicationNo());
        entity.setUserId(currentUserId());
        entity.setApplicationStatus("DRAFT");
        entity.setAdmissionStatus("PENDING");
        applicationMapper.insert(entity);
        addRecord(entity, "CREATE", null, "DRAFT", "创建招生申请草稿");
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMyApplication(AdmissionApplicationForm form) {
        AdmissionApplication application = ensureMyApplication();
        ensureEditable(application);
        AdmissionApplication entity = toEntity(form);
        entity.setId(application.getId());
        applicationMapper.updateById(entity);
        addRecord(application, "UPDATE", application.getApplicationStatus(), application.getApplicationStatus(), "更新招生申请");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitMyApplication() {
        AdmissionApplication application = ensureMyApplication();
        ensureEditable(application);
        List<AdmissionMaterial> materials = materialMapper.selectByApplicationId(application.getId());
        boolean hasPassport = materials.stream().anyMatch(item -> "PASSPORT".equals(item.getMaterialType()));
        boolean hasPhoto = materials.stream().anyMatch(item -> "PHOTO".equals(item.getMaterialType()));
        if (!hasPassport || !hasPhoto) {
            throw new BusinessException("请至少上传护照和证件照材料");
        }
        updateStatus(application, "UNDER_REVIEW", "PENDING", "申请已提交");
        addRecord(application, "SUBMIT", application.getApplicationStatus(), "UNDER_REVIEW", "提交招生申请");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdmissionMaterial uploadMyMaterial(String materialType, MultipartFile file) {
        AdmissionApplication application = ensureMyApplication();
        ensureEditable(application);
        validateFile(file);
        try {
            Path directory = Path.of(uploadDir, "admission", "materials", application.getApplicationNo()).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            String original = file.getOriginalFilename() == null ? "material" : file.getOriginalFilename();
            String suffix = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
            String fileName = materialType + "-" + UUID.randomUUID() + suffix;
            Path target = directory.resolve(fileName);
            file.transferTo(target);
            AdmissionMaterial material = new AdmissionMaterial();
            material.setApplicationId(application.getId());
            material.setMaterialType(materialType);
            material.setFileName(original);
            material.setFileUrl(target.toString());
            material.setFileSize(file.getSize());
            material.setMimeType(file.getContentType());
            material.setReviewStatus("PENDING");
            materialMapper.insert(material);
            addRecord(application, "UPLOAD_MATERIAL", application.getApplicationStatus(), application.getApplicationStatus(), "上传申请材料：" + materialType);
            return material;
        } catch (Exception ex) {
            throw new BusinessException("材料上传失败：" + ex.getMessage());
        }
    }

    @Override
    public void deleteMyMaterial(Long materialId) {
        AdmissionApplication application = ensureMyApplication();
        ensureEditable(application);
        AdmissionMaterial material = ensureMaterial(materialId);
        if (!material.getApplicationId().equals(application.getId())) {
            throw new BusinessException(403, "不能删除其他申请的材料");
        }
        materialMapper.logicalDelete(materialId);
        try {
            Files.deleteIfExists(Path.of(material.getFileUrl()));
        } catch (Exception ignored) {
        }
    }

    @Override
    public Resource downloadMyNotice() {
        AdmissionApplication application = ensureMyApplication();
        return downloadNotice(application.getId());
    }

    @Override
    public PageResult<AdmissionApplication> page(AdmissionApplicationQuery query) {
        return new PageResult<>(applicationMapper.count(query), applicationMapper.selectPage(query));
    }

    @Override
    public AdmissionApplicationDetailVO detail(Long id) {
        return buildDetail(ensureApplication(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnApplication(Long id, AdmissionReviewRequest request) {
        AdmissionApplication application = ensureApplication(id);
        updateStatus(application, "RETURNED", "PENDING", request.getOpinion());
        addRecord(application, "RETURN", application.getApplicationStatus(), "RETURNED", request.getOpinion());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectApplication(Long id, AdmissionReviewRequest request) {
        AdmissionApplication application = ensureApplication(id);
        updateStatus(application, "REJECTED", "REJECTED", request.getOpinion());
        addRecord(application, "REJECT", application.getApplicationStatus(), "REJECTED", request.getOpinion());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void admitApplication(Long id, AdmissionReviewRequest request) {
        AdmissionApplication application = ensureApplication(id);
        Long profileId = ensureStudentProfile(application);
        application.setStudentProfileId(profileId);
        updateStatus(application, "ADMITTED", "ADMITTED", request.getOpinion());
        addRecord(application, "ADMIT", application.getApplicationStatus(), "ADMITTED", request.getOpinion());
    }

    @Override
    public void reviewMaterial(Long materialId, AdmissionMaterialReviewRequest request) {
        AdmissionMaterial material = ensureMaterial(materialId);
        material.setReviewStatus(request.getReviewStatus());
        material.setReviewOpinion(request.getReviewOpinion());
        materialMapper.updateReview(material);
        AdmissionApplication application = ensureApplication(material.getApplicationId());
        addRecord(application, "REVIEW_MATERIAL", application.getApplicationStatus(), application.getApplicationStatus(),
                "材料审核：" + material.getMaterialType() + " - " + request.getReviewStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateNotice(Long id) {
        AdmissionApplication application = ensureApplication(id);
        if (!"ADMITTED".equals(application.getApplicationStatus()) && !"NOTICE_ISSUED".equals(application.getApplicationStatus())) {
            throw new BusinessException("只有已录取申请可以生成通知书");
        }
        if (noticeMapper.selectByApplicationId(id) != null) {
            throw new BusinessException("录取通知书已生成");
        }
        AdmissionNotice notice = createNotice(application);
        noticeMapper.insert(notice);
        updateStatus(application, "NOTICE_ISSUED", "ADMITTED", "录取通知书已生成");
        addRecord(application, "GENERATE_NOTICE", application.getApplicationStatus(), "NOTICE_ISSUED", "生成录取通知书");
    }

    @Override
    public Resource downloadNotice(Long id) {
        AdmissionNotice notice = noticeMapper.selectByApplicationId(id);
        if (notice == null) {
            throw new BusinessException(404, "录取通知书不存在");
        }
        FileSystemResource resource = new FileSystemResource(notice.getFilePath());
        if (!resource.exists()) {
            throw new BusinessException(404, "录取通知书文件不存在");
        }
        noticeMapper.incrementDownloadCount(notice.getId());
        return resource;
    }

    private AdmissionApplicationDetailVO buildDetail(AdmissionApplication application) {
        AdmissionApplicationDetailVO vo = new AdmissionApplicationDetailVO();
        vo.setApplication(application);
        vo.setMaterials(materialMapper.selectByApplicationId(application.getId()));
        vo.setReviews(reviewRecordMapper.selectByApplicationId(application.getId()));
        vo.setNotice(noticeMapper.selectByApplicationId(application.getId()));
        return vo;
    }

    private AdmissionApplication toEntity(AdmissionApplicationForm form) {
        AdmissionApplication entity = new AdmissionApplication();
        entity.setChineseName(form.getChineseName());
        entity.setEnglishName(form.getEnglishName());
        entity.setGender(form.getGender() == null ? "UNKNOWN" : form.getGender());
        entity.setBirthDate(form.getBirthDate());
        entity.setNationality(form.getNationality());
        entity.setEmail(form.getEmail());
        entity.setPhone(form.getPhone());
        entity.setPassportNo(form.getPassportNo());
        entity.setPassportCountry(form.getPassportCountry());
        entity.setPassportExpireDate(form.getPassportExpireDate());
        entity.setApplyCollege(form.getApplyCollege());
        entity.setApplyMajor(form.getApplyMajor());
        entity.setDegreeLevel(form.getDegreeLevel());
        entity.setProgramType(form.getProgramType());
        entity.setEducationBackground(form.getEducationBackground());
        entity.setRemark(form.getRemark());
        return entity;
    }

    private AdmissionApplication ensureMyApplication() {
        AdmissionApplication application = applicationMapper.selectByUserId(currentUserId());
        if (application == null) {
            throw new BusinessException(404, "当前账号暂无招生申请");
        }
        return application;
    }

    private AdmissionApplication ensureApplication(Long id) {
        AdmissionApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new BusinessException(404, "招生申请不存在");
        }
        return application;
    }

    private AdmissionMaterial ensureMaterial(Long id) {
        AdmissionMaterial material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException(404, "申请材料不存在");
        }
        return material;
    }

    private void ensureEditable(AdmissionApplication application) {
        if (!EDITABLE.contains(application.getApplicationStatus())) {
            throw new BusinessException("当前申请状态不可编辑");
        }
    }

    private void updateStatus(AdmissionApplication application, String applicationStatus, String admissionStatus, String opinion) {
        applicationMapper.updateStatus(application.getId(), applicationStatus, admissionStatus, opinion, application.getStudentProfileId());
    }

    private Long ensureStudentProfile(AdmissionApplication application) {
        if (application.getStudentProfileId() != null) {
            return application.getStudentProfileId();
        }
        StudentProfile existing = studentProfileMapper.selectByUserId(application.getUserId());
        if (existing != null) {
            return existing.getId();
        }
        StudentProfile profile = new StudentProfile();
        profile.setUserId(application.getUserId());
        profile.setStudentNo(generateStudentNo());
        profile.setApplicationNo(application.getApplicationNo());
        profile.setChineseName(application.getChineseName());
        profile.setEnglishName(application.getEnglishName());
        profile.setGender(application.getGender());
        profile.setBirthDate(application.getBirthDate());
        profile.setNationality(application.getNationality());
        profile.setEmail(application.getEmail());
        profile.setPhone(application.getPhone());
        profile.setPassportNo(application.getPassportNo());
        profile.setPassportCountry(application.getPassportCountry());
        profile.setPassportExpireDate(application.getPassportExpireDate());
        profile.setCollege(application.getApplyCollege());
        profile.setMajor(application.getApplyMajor());
        profile.setDegreeLevel(application.getDegreeLevel());
        profile.setStudentStatus("PRE_ADMITTED");
        profile.setStatus(1);
        profile.setRemark("由招生申请录取自动生成");
        studentProfileMapper.insert(profile);
        return profile.getId();
    }

    private AdmissionNotice createNotice(AdmissionApplication application) {
        try {
            Path font = Path.of(noticeFontPath);
            if (!Files.exists(font)) {
                throw new BusinessException("录取通知书字体文件不存在：" + noticeFontPath);
            }
            Path directory = Path.of(uploadDir, "admission", "notices").toAbsolutePath().normalize();
            Files.createDirectories(directory);
            String noticeNo = "NO" + NUMBER_DATE.format(LocalDateTime.now()) + application.getId();
            String fileName = noticeNo + ".pdf";
            Path target = directory.resolve(fileName);
            writeNoticePdf(application, noticeNo, target, font);
            AdmissionNotice notice = new AdmissionNotice();
            notice.setApplicationId(application.getId());
            notice.setNoticeNo(noticeNo);
            notice.setFileName(fileName);
            notice.setFilePath(target.toString());
            notice.setIssueDate(LocalDate.now());
            fillIssuer(notice);
            notice.setDownloadCount(0);
            return notice;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException("录取通知书生成失败：" + ex.getMessage());
        }
    }

    private void writeNoticePdf(AdmissionApplication application, String noticeNo, Path target, Path fontPath) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDType0Font font = PDType0Font.load(document, fontPath.toFile());
            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(font, 20);
                content.newLineAtOffset(190, 760);
                content.showText("录取通知书");
                content.setFont(font, 11);
                writeLine(content, 0, -42, "通知书编号：" + noticeNo);
                writeLine(content, 0, -28, "亲爱的 " + safe(application.getEnglishName()) + "：");
                writeLine(content, 0, -28, "经学校审核，您已被我校录取。录取信息如下：");
                writeLine(content, 24, -30, "申请编号：" + application.getApplicationNo());
                writeLine(content, 0, -24, "录取学院：" + safe(application.getApplyCollege()));
                writeLine(content, 0, -24, "录取专业：" + safe(application.getApplyMajor()));
                writeLine(content, 0, -24, "学历层次：" + safe(application.getDegreeLevel()));
                writeLine(content, 0, -24, "国籍：" + safe(application.getNationality()));
                writeLine(content, -24, -42, "请按学校后续通知办理报到、签证及住宿等手续。");
                writeLine(content, 260, -70, "留学生服务与管理系统");
                writeLine(content, 0, -24, "签发日期：" + LocalDate.now());
                content.endText();
            }
            document.save(target.toFile());
        }
    }

    private void writeLine(PDPageContentStream content, float x, float y, String text) throws Exception {
        content.newLineAtOffset(x, y);
        content.showText(text);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("上传文件不能超过 20MB");
        }
        String type = file.getContentType();
        if (type == null || !ALLOWED_MIME.contains(type)) {
            throw new BusinessException("仅支持 PDF/JPG/PNG/DOC/DOCX 文件");
        }
    }

    private void addRecord(AdmissionApplication application, String action, String from, String to, String opinion) {
        AdmissionReviewRecord record = new AdmissionReviewRecord();
        record.setApplicationId(application.getId());
        record.setActionType(action);
        record.setFromStatus(from);
        record.setToStatus(to);
        record.setOpinion(opinion);
        fillOperator(record);
        reviewRecordMapper.insert(record);
    }

    private void fillOperator(AdmissionReviewRecord record) {
        Long userId = currentUserId();
        SysUser user = userMapper.selectById(userId);
        record.setOperatorId(userId);
        record.setOperatorName(user == null ? String.valueOf(userId) : user.getRealName());
    }

    private void fillIssuer(AdmissionNotice notice) {
        Long userId = currentUserId();
        SysUser user = userMapper.selectById(userId);
        notice.setIssuerId(userId);
        notice.setIssuerName(user == null ? String.valueOf(userId) : user.getRealName());
    }

    private Long currentUserId() {
        return Long.valueOf(String.valueOf(StpUtil.getLoginId()));
    }

    private String generateApplicationNo() {
        return "A" + NUMBER_DATE.format(LocalDateTime.now()) + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private String generateStudentNo() {
        return "S" + NUMBER_DATE.format(LocalDateTime.now()) + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
