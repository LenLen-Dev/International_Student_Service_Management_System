package com.wenjie.modules.visa.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.student.entity.StudentProfile;
import com.wenjie.modules.student.mapper.StudentProfileMapper;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import com.wenjie.modules.visa.dto.ResidencePermitFormDTO;
import com.wenjie.modules.visa.dto.ResidencePermitQueryDTO;
import com.wenjie.modules.visa.dto.VisaAlertQueryDTO;
import com.wenjie.modules.visa.dto.VisaAlertResolveDTO;
import com.wenjie.modules.visa.dto.VisaNotificationQueryDTO;
import com.wenjie.modules.visa.dto.VisaRecordFormDTO;
import com.wenjie.modules.visa.dto.VisaRecordQueryDTO;
import com.wenjie.modules.visa.dto.VisaRenewalFormDTO;
import com.wenjie.modules.visa.dto.VisaRenewalQueryDTO;
import com.wenjie.modules.visa.dto.VisaRenewalResultDTO;
import com.wenjie.modules.visa.entity.ResidencePermit;
import com.wenjie.modules.visa.entity.VisaComplianceAlert;
import com.wenjie.modules.visa.entity.VisaRecord;
import com.wenjie.modules.visa.entity.VisaReminderNotification;
import com.wenjie.modules.visa.entity.VisaRenewalRecord;
import com.wenjie.modules.visa.mapper.ResidencePermitMapper;
import com.wenjie.modules.visa.mapper.VisaComplianceAlertMapper;
import com.wenjie.modules.visa.mapper.VisaRecordMapper;
import com.wenjie.modules.visa.mapper.VisaReminderNotificationMapper;
import com.wenjie.modules.visa.mapper.VisaRenewalRecordMapper;
import com.wenjie.modules.visa.service.VisaService;
import com.wenjie.modules.visa.vo.VisaMyOverviewVO;
import com.wenjie.security.AuthUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisaServiceImpl implements VisaService {

    private final VisaRecordMapper visaRecordMapper;
    private final ResidencePermitMapper residencePermitMapper;
    private final VisaRenewalRecordMapper renewalRecordMapper;
    private final VisaComplianceAlertMapper alertMapper;
    private final VisaReminderNotificationMapper notificationMapper;
    private final StudentProfileMapper studentProfileMapper;
    private final SysUserMapper userMapper;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${app.visa.reminder-thresholds:30,15,7,0}")
    private String reminderThresholds;

    @Value("${app.visa.email-enabled:false}")
    private boolean emailEnabled;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Override
    public PageResult<VisaRecord> pageVisaRecords(VisaRecordQueryDTO query) {
        return new PageResult<>(visaRecordMapper.countPage(query), visaRecordMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createVisaRecord(VisaRecordFormDTO dto) {
        ensureStudent(dto.getStudentId());
        VisaRecord record = toVisaRecord(null, dto);
        visaRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public VisaRecord getVisaRecord(Long id) {
        VisaRecord record = visaRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(404, "签证信息不存在");
        }
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVisaRecord(Long id, VisaRecordFormDTO dto) {
        getVisaRecord(id);
        ensureStudent(dto.getStudentId());
        visaRecordMapper.updateById(toVisaRecord(id, dto));
    }

    @Override
    public void deleteVisaRecord(Long id) {
        getVisaRecord(id);
        visaRecordMapper.deleteById(id);
    }

    @Override
    public PageResult<ResidencePermit> pageResidencePermits(ResidencePermitQueryDTO query) {
        return new PageResult<>(residencePermitMapper.countPage(query), residencePermitMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createResidencePermit(ResidencePermitFormDTO dto) {
        ensureStudent(dto.getStudentId());
        ResidencePermit permit = toResidencePermit(null, dto);
        residencePermitMapper.insert(permit);
        return permit.getId();
    }

    @Override
    public ResidencePermit getResidencePermit(Long id) {
        ResidencePermit permit = residencePermitMapper.selectById(id);
        if (permit == null) {
            throw new BusinessException(404, "居留许可不存在");
        }
        return permit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResidencePermit(Long id, ResidencePermitFormDTO dto) {
        getResidencePermit(id);
        ensureStudent(dto.getStudentId());
        residencePermitMapper.updateById(toResidencePermit(id, dto));
    }

    @Override
    public void deleteResidencePermit(Long id) {
        getResidencePermit(id);
        residencePermitMapper.deleteById(id);
    }

    @Override
    public PageResult<VisaRenewalRecord> pageRenewals(VisaRenewalQueryDTO query) {
        return new PageResult<>(renewalRecordMapper.countPage(query), renewalRecordMapper.selectPage(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRenewal(VisaRenewalFormDTO dto) {
        ensureStudent(dto.getStudentId());
        ensureRenewalTarget(dto.getRenewalType(), dto.getTargetId());
        VisaRenewalRecord record = toRenewal(null, dto);
        fillHandler(record);
        renewalRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public VisaRenewalRecord getRenewal(Long id) {
        VisaRenewalRecord record = renewalRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(404, "续签记录不存在");
        }
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRenewal(Long id, VisaRenewalFormDTO dto) {
        getRenewal(id);
        ensureStudent(dto.getStudentId());
        ensureRenewalTarget(dto.getRenewalType(), dto.getTargetId());
        VisaRenewalRecord record = toRenewal(id, dto);
        fillHandler(record);
        renewalRecordMapper.updateById(record);
    }

    @Override
    public void deleteRenewal(Long id) {
        getRenewal(id);
        renewalRecordMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRenewalResult(Long id, VisaRenewalResultDTO dto) {
        VisaRenewalRecord existing = getRenewal(id);
        existing.setResult(dto.getResult());
        existing.setRenewalStatus(dto.getRenewalStatus());
        existing.setCompleteDate(dto.getCompleteDate() == null ? LocalDate.now() : dto.getCompleteDate());
        existing.setNewValidUntil(dto.getNewValidUntil());
        existing.setRemark(dto.getRemark());
        fillHandler(existing);
        renewalRecordMapper.updateById(existing);
        if ("APPROVED".equals(dto.getRenewalStatus()) && dto.getNewValidUntil() != null && existing.getTargetId() != null) {
            if ("VISA".equals(existing.getRenewalType())) {
                visaRecordMapper.updateValidUntil(existing.getTargetId(), dto.getNewValidUntil(), "VALID");
            } else if ("RESIDENCE_PERMIT".equals(existing.getRenewalType())) {
                residencePermitMapper.updateValidUntil(existing.getTargetId(), dto.getNewValidUntil(), "VALID");
            }
        }
    }

    @Override
    public PageResult<VisaComplianceAlert> pageAlerts(VisaAlertQueryDTO query) {
        return new PageResult<>(alertMapper.countPage(query), alertMapper.selectPage(query));
    }

    @Override
    public VisaComplianceAlert getAlert(Long id) {
        VisaComplianceAlert alert = alertMapper.selectById(id);
        if (alert == null) {
            throw new BusinessException(404, "合规预警不存在");
        }
        return alert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolveAlert(Long id, VisaAlertResolveDTO dto) {
        VisaComplianceAlert alert = getAlert(id);
        alert.setAlertStatus(dto.getAlertStatus());
        alert.setHandleRemark(dto.getHandleRemark());
        fillHandler(alert);
        alertMapper.resolve(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateAlerts() {
        int count = 0;
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(maxThreshold());
        for (VisaRecord record : visaRecordMapper.selectAlertCandidates(deadline)) {
            count += createExpireAlert(record.getStudentId(), "VISA", record.getId(), record.getVisaNo(), record.getValidUntil());
            if (!"VALID".equals(record.getStatus()) && !"EXPIRING".equals(record.getStatus())) {
                count += createSimpleAlert(record.getStudentId(), "STATUS_ABNORMAL", "HIGH", "VISA", record.getId(), "签证状态异常",
                        "签证状态为 " + record.getStatus() + "，请核查。");
            }
        }
        for (ResidencePermit permit : residencePermitMapper.selectAlertCandidates(deadline)) {
            count += createExpireAlert(permit.getStudentId(), "PERMIT", permit.getId(), permit.getPermitNo(), permit.getValidUntil());
            if (!"VALID".equals(permit.getStatus()) && !"EXPIRING".equals(permit.getStatus())) {
                count += createSimpleAlert(permit.getStudentId(), "STATUS_ABNORMAL", "HIGH", "PERMIT", permit.getId(), "居留许可状态异常",
                        "居留许可状态为 " + permit.getStatus() + "，请核查。");
            }
        }
        for (StudentProfile student : alertMapper.selectStudentsForCompliance()) {
            if (isBlank(student.getPassportNo()) || student.getPassportExpireDate() == null) {
                count += createSimpleAlert(student.getId(), "DATA_MISSING", "MEDIUM", "STUDENT", student.getId(), "护照资料缺失",
                        "学生护照号码或护照有效期未完整维护，请补充。");
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyAlert(Long id) {
        createNotifications(getAlert(id));
    }

    @Override
    public PageResult<VisaReminderNotification> pageNotifications(VisaNotificationQueryDTO query) {
        return new PageResult<>(notificationMapper.countPage(query), notificationMapper.selectPage(query));
    }

    @Override
    public void markNotificationRead(Long id) {
        notificationMapper.markRead(id, AuthUserContext.currentUserId());
    }

    @Override
    public VisaMyOverviewVO getMyOverview() {
        StudentProfile student = currentStudent();
        VisaMyOverviewVO vo = new VisaMyOverviewVO();
        vo.setVisaRecords(visaRecordMapper.selectByStudentId(student.getId()));
        vo.setResidencePermits(residencePermitMapper.selectByStudentId(student.getId()));
        vo.setRenewals(renewalRecordMapper.selectByStudentId(student.getId()));
        vo.setAlerts(alertMapper.selectByStudentId(student.getId()));
        vo.setNotifications(notificationMapper.selectByUserId(AuthUserContext.currentUserId()));
        return vo;
    }

    @Override
    public PageResult<VisaReminderNotification> pageMyNotifications(VisaNotificationQueryDTO query) {
        List<VisaReminderNotification> all = notificationMapper.selectByUserId(AuthUserContext.currentUserId());
        List<VisaReminderNotification> filtered = all.stream()
                .filter(item -> query.getChannel() == null || query.getChannel().isBlank() || query.getChannel().equals(item.getChannel()))
                .filter(item -> query.getSendStatus() == null || query.getSendStatus().isBlank() || query.getSendStatus().equals(item.getSendStatus()))
                .filter(item -> query.getReadStatus() == null || query.getReadStatus().equals(item.getReadStatus()))
                .toList();
        int from = Math.min(query.getOffset(), filtered.size());
        int to = Math.min(from + query.getLimit(), filtered.size());
        return new PageResult<>((long) filtered.size(), filtered.subList(from, to));
    }

    private int createExpireAlert(Long studentId, String targetType, Long targetId, String targetNo, LocalDate validUntil) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), validUntil);
        String type = days < 0 ? "EXPIRED" : "EXPIRING";
        String level = days <= 7 ? "HIGH" : (days <= 15 ? "MEDIUM" : "LOW");
        String title = ("VISA".equals(targetType) ? "签证" : "居留许可") + (days < 0 ? "已逾期" : "即将到期");
        String content = title + "，编号：" + (targetNo == null ? "-" : targetNo) + "，到期日期：" + validUntil + "，剩余天数：" + days + "。";
        VisaComplianceAlert alert = buildAlert(studentId, type, level, targetType, targetId, title, content, validUntil, (int) days);
        return insertAlertIfAbsent(alert);
    }

    private int createSimpleAlert(Long studentId, String type, String level, String targetType, Long targetId, String title, String content) {
        VisaComplianceAlert alert = buildAlert(studentId, type, level, targetType, targetId, title, content, null, null);
        return insertAlertIfAbsent(alert);
    }

    private int insertAlertIfAbsent(VisaComplianceAlert alert) {
        if (alertMapper.countOpenTargetAlert(alert.getTargetType(), alert.getTargetId(), alert.getAlertType()) > 0) {
            return 0;
        }
        alertMapper.insert(alert);
        createNotifications(alertMapper.selectById(alert.getId()));
        return 1;
    }

    private VisaComplianceAlert buildAlert(Long studentId, String type, String level, String targetType, Long targetId,
                                           String title, String content, LocalDate expireDate, Integer remainingDays) {
        VisaComplianceAlert alert = new VisaComplianceAlert();
        alert.setStudentId(studentId);
        alert.setAlertType(type);
        alert.setAlertLevel(level);
        alert.setTargetType(targetType);
        alert.setTargetId(targetId);
        alert.setTitle(title);
        alert.setContent(content);
        alert.setExpireDate(expireDate);
        alert.setRemainingDays(remainingDays);
        alert.setAlertStatus("OPEN");
        return alert;
    }

    private void createNotifications(VisaComplianceAlert alert) {
        if (alert == null || alert.getUserId() == null) {
            return;
        }
        VisaReminderNotification inApp = baseNotification(alert, "IN_APP");
        inApp.setSendStatus("SENT");
        inApp.setSendTime(LocalDateTime.now());
        notificationMapper.insert(inApp);

        if (emailEnabled) {
            VisaReminderNotification email = baseNotification(alert, "EMAIL");
            try {
                if (isBlank(alert.getEmail())) {
                    throw new BusinessException("学生邮箱未维护");
                }
                JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
                if (mailSender == null || isBlank(mailFrom)) {
                    throw new BusinessException("邮件服务未配置");
                }
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(mailFrom);
                message.setTo(alert.getEmail());
                message.setSubject(alert.getTitle());
                message.setText(alert.getContent());
                mailSender.send(message);
                email.setSendStatus("SENT");
            } catch (Exception ex) {
                email.setSendStatus("FAILED");
                email.setErrorMessage(limit(ex.getMessage(), 480));
            }
            email.setSendTime(LocalDateTime.now());
            notificationMapper.insert(email);
        }
    }

    private VisaReminderNotification baseNotification(VisaComplianceAlert alert, String channel) {
        VisaReminderNotification notification = new VisaReminderNotification();
        notification.setAlertId(alert.getId());
        notification.setRecipientUserId(alert.getUserId());
        notification.setRecipientName(alert.getChineseName() == null ? alert.getEnglishName() : alert.getChineseName());
        notification.setChannel(channel);
        notification.setSendStatus("PENDING");
        notification.setTitle(alert.getTitle());
        notification.setContent(alert.getContent());
        notification.setReadStatus(0);
        return notification;
    }

    private VisaRecord toVisaRecord(Long id, VisaRecordFormDTO dto) {
        VisaRecord record = new VisaRecord();
        record.setId(id);
        record.setStudentId(dto.getStudentId());
        record.setPassportNo(dto.getPassportNo());
        record.setVisaType(dto.getVisaType());
        record.setVisaNo(dto.getVisaNo());
        record.setIssuePlace(dto.getIssuePlace());
        record.setIssueDate(dto.getIssueDate());
        record.setValidFrom(dto.getValidFrom());
        record.setValidUntil(dto.getValidUntil());
        record.setEntryCount(dto.getEntryCount());
        record.setStatus(dto.getStatus());
        record.setRemark(dto.getRemark());
        return record;
    }

    private ResidencePermit toResidencePermit(Long id, ResidencePermitFormDTO dto) {
        ResidencePermit permit = new ResidencePermit();
        permit.setId(id);
        permit.setStudentId(dto.getStudentId());
        permit.setPermitNo(dto.getPermitNo());
        permit.setPermitType(dto.getPermitType());
        permit.setResidenceAddress(dto.getResidenceAddress());
        permit.setIssueDate(dto.getIssueDate());
        permit.setValidFrom(dto.getValidFrom());
        permit.setValidUntil(dto.getValidUntil());
        permit.setStatus(dto.getStatus());
        permit.setRemark(dto.getRemark());
        return permit;
    }

    private VisaRenewalRecord toRenewal(Long id, VisaRenewalFormDTO dto) {
        VisaRenewalRecord record = new VisaRenewalRecord();
        record.setId(id);
        record.setStudentId(dto.getStudentId());
        record.setRenewalType(dto.getRenewalType());
        record.setTargetId(dto.getTargetId());
        record.setApplicationDate(dto.getApplicationDate());
        record.setAcceptanceDate(dto.getAcceptanceDate());
        record.setCompleteDate(dto.getCompleteDate());
        record.setRenewalStatus(dto.getRenewalStatus());
        record.setResult(dto.getResult());
        record.setNewValidUntil(dto.getNewValidUntil());
        record.setRemark(dto.getRemark());
        return record;
    }

    private StudentProfile ensureStudent(Long studentId) {
        StudentProfile profile = studentProfileMapper.selectById(studentId);
        if (profile == null) {
            throw new BusinessException(404, "留学生档案不存在");
        }
        return profile;
    }

    private StudentProfile currentStudent() {
        StudentProfile profile = studentProfileMapper.selectByUserId(AuthUserContext.currentUserId());
        if (profile == null) {
            throw new BusinessException(404, "当前用户未绑定留学生档案");
        }
        return profile;
    }

    private void ensureRenewalTarget(String renewalType, Long targetId) {
        if (targetId == null) {
            return;
        }
        if ("VISA".equals(renewalType)) {
            getVisaRecord(targetId);
        } else if ("RESIDENCE_PERMIT".equals(renewalType)) {
            getResidencePermit(targetId);
        }
    }

    private void fillHandler(VisaRenewalRecord record) {
        Long userId = AuthUserContext.currentUserId();
        SysUser user = userMapper.selectById(userId);
        record.setHandlerId(userId);
        record.setHandlerName(user == null ? String.valueOf(userId) : user.getRealName());
    }

    private void fillHandler(VisaComplianceAlert alert) {
        Long userId = AuthUserContext.currentUserId();
        SysUser user = userMapper.selectById(userId);
        alert.setHandlerId(userId);
        alert.setHandlerName(user == null ? String.valueOf(userId) : user.getRealName());
    }

    private int maxThreshold() {
        return Arrays.stream(reminderThresholds.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(30);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String limit(String text, int max) {
        if (text == null || text.length() <= max) {
            return text;
        }
        return text.substring(0, max);
    }
}
