import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  VisaAlertQuery,
  VisaAlertResolveForm,
  VisaComplianceAlert,
  VisaMyOverview,
  VisaNotificationQuery,
  VisaReminderNotification
} from '@/types/visa'

export function getVisaAlertPage(params: VisaAlertQuery) {
  return request.get<PageResult<VisaComplianceAlert>, PageResult<VisaComplianceAlert>>('/visa/alerts', { params })
}

export function getVisaAlertDetail(id: number) {
  return request.get<VisaComplianceAlert, VisaComplianceAlert>(`/visa/alerts/${id}`)
}

export function resolveVisaAlert(id: number, data: VisaAlertResolveForm) {
  return request.put<void, void>(`/visa/alerts/${id}/resolve`, data)
}

export function generateVisaAlerts() {
  return request.post<number, number>('/visa/alerts/generate')
}

export function notifyVisaAlert(id: number) {
  return request.post<void, void>(`/visa/alerts/${id}/notify`)
}

export function getVisaNotificationPage(params: VisaNotificationQuery) {
  return request.get<PageResult<VisaReminderNotification>, PageResult<VisaReminderNotification>>('/visa/notifications', { params })
}

export function markVisaNotificationRead(id: number) {
  return request.put<void, void>(`/visa/notifications/${id}/read`)
}

export function getMyVisaOverview() {
  return request.get<VisaMyOverview, VisaMyOverview>('/visa/me')
}

export function getMyVisaNotifications(params: VisaNotificationQuery) {
  return request.get<PageResult<VisaReminderNotification>, PageResult<VisaReminderNotification>>('/visa/me/notifications', { params })
}
