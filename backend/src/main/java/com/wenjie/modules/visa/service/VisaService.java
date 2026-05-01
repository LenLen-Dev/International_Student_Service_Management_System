package com.wenjie.modules.visa.service;

import com.wenjie.common.PageResult;
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
import com.wenjie.modules.visa.vo.VisaMyOverviewVO;

public interface VisaService {
    PageResult<VisaRecord> pageVisaRecords(VisaRecordQueryDTO query);
    Long createVisaRecord(VisaRecordFormDTO dto);
    VisaRecord getVisaRecord(Long id);
    void updateVisaRecord(Long id, VisaRecordFormDTO dto);
    void deleteVisaRecord(Long id);

    PageResult<ResidencePermit> pageResidencePermits(ResidencePermitQueryDTO query);
    Long createResidencePermit(ResidencePermitFormDTO dto);
    ResidencePermit getResidencePermit(Long id);
    void updateResidencePermit(Long id, ResidencePermitFormDTO dto);
    void deleteResidencePermit(Long id);

    PageResult<VisaRenewalRecord> pageRenewals(VisaRenewalQueryDTO query);
    Long createRenewal(VisaRenewalFormDTO dto);
    VisaRenewalRecord getRenewal(Long id);
    void updateRenewal(Long id, VisaRenewalFormDTO dto);
    void deleteRenewal(Long id);
    void updateRenewalResult(Long id, VisaRenewalResultDTO dto);

    PageResult<VisaComplianceAlert> pageAlerts(VisaAlertQueryDTO query);
    VisaComplianceAlert getAlert(Long id);
    void resolveAlert(Long id, VisaAlertResolveDTO dto);
    int generateAlerts();
    void notifyAlert(Long id);

    PageResult<VisaReminderNotification> pageNotifications(VisaNotificationQueryDTO query);
    void markNotificationRead(Long id);
    VisaMyOverviewVO getMyOverview();
    PageResult<VisaReminderNotification> pageMyNotifications(VisaNotificationQueryDTO query);
}
