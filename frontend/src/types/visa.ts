import type { PageQuery } from './api'

export interface VisaRecord {
  id: number
  studentId: number
  passportNo?: string
  visaType: string
  visaNo?: string
  issuePlace?: string
  issueDate?: string
  validFrom?: string
  validUntil?: string
  entryCount?: string
  status: string
  remark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
}

export interface VisaRecordForm {
  studentId?: number
  passportNo?: string
  visaType: string
  visaNo?: string
  issuePlace?: string
  issueDate?: string
  validFrom?: string
  validUntil?: string
  entryCount?: string
  status: string
  remark?: string
}

export interface VisaRecordQuery extends PageQuery {
  studentKeyword?: string
  visaType?: string
  expireStart?: string
  expireEnd?: string
}

export interface ResidencePermit {
  id: number
  studentId: number
  permitNo?: string
  permitType: string
  residenceAddress?: string
  issueDate?: string
  validFrom?: string
  validUntil?: string
  status: string
  remark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
}

export interface ResidencePermitForm {
  studentId?: number
  permitNo?: string
  permitType: string
  residenceAddress?: string
  issueDate?: string
  validFrom?: string
  validUntil?: string
  status: string
  remark?: string
}

export interface ResidencePermitQuery extends PageQuery {
  studentKeyword?: string
  permitType?: string
  expireStart?: string
  expireEnd?: string
}

export interface VisaRenewalRecord {
  id: number
  studentId: number
  renewalType: string
  targetId?: number
  applicationDate?: string
  acceptanceDate?: string
  completeDate?: string
  renewalStatus: string
  result?: string
  newValidUntil?: string
  handlerId?: number
  handlerName?: string
  remark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
}

export interface VisaRenewalForm {
  studentId?: number
  renewalType: string
  targetId?: number
  applicationDate?: string
  acceptanceDate?: string
  completeDate?: string
  renewalStatus: string
  result?: string
  newValidUntil?: string
  remark?: string
}

export interface VisaRenewalResultForm {
  result: string
  renewalStatus: string
  completeDate?: string
  newValidUntil?: string
  remark?: string
}

export interface VisaRenewalQuery extends PageQuery {
  studentKeyword?: string
  renewalType?: string
  renewalStatus?: string
  startDate?: string
  endDate?: string
}

export interface VisaComplianceAlert {
  id: number
  studentId: number
  alertType: string
  alertLevel: string
  targetType: string
  targetId?: number
  title: string
  content?: string
  expireDate?: string
  remainingDays?: number
  alertStatus: string
  handlerId?: number
  handlerName?: string
  handleTime?: string
  handleRemark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  userId?: number
  email?: string
}

export interface VisaAlertQuery extends PageQuery {
  studentKeyword?: string
  alertType?: string
  alertLevel?: string
  alertStatus?: string
  targetType?: string
}

export interface VisaAlertResolveForm {
  alertStatus: string
  handleRemark?: string
}

export interface VisaReminderNotification {
  id: number
  alertId?: number
  recipientUserId?: number
  recipientName?: string
  channel: string
  sendStatus: string
  title: string
  content?: string
  errorMessage?: string
  readStatus: number
  readTime?: string
  sendTime?: string
  createTime?: string
  updateTime?: string
}

export interface VisaNotificationQuery extends PageQuery {
  channel?: string
  sendStatus?: string
  readStatus?: number | string
}

export interface VisaMyOverview {
  visaRecords: VisaRecord[]
  residencePermits: ResidencePermit[]
  renewals: VisaRenewalRecord[]
  alerts: VisaComplianceAlert[]
  notifications: VisaReminderNotification[]
}
