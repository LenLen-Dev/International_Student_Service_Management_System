package com.wenjie.modules.visa.vo;

import com.wenjie.modules.visa.entity.ResidencePermit;
import com.wenjie.modules.visa.entity.VisaComplianceAlert;
import com.wenjie.modules.visa.entity.VisaRecord;
import com.wenjie.modules.visa.entity.VisaReminderNotification;
import com.wenjie.modules.visa.entity.VisaRenewalRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VisaMyOverviewVO {
    private List<VisaRecord> visaRecords = new ArrayList<>();
    private List<ResidencePermit> residencePermits = new ArrayList<>();
    private List<VisaRenewalRecord> renewals = new ArrayList<>();
    private List<VisaComplianceAlert> alerts = new ArrayList<>();
    private List<VisaReminderNotification> notifications = new ArrayList<>();
}
