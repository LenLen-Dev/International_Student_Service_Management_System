import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { TeachingAcademicAlert, TeachingAlertHandleForm, TeachingMyOverview, TeachingQuery } from '@/types/teaching'

export function getTeachingAlertPage(params: TeachingQuery) {
  return request.get<PageResult<TeachingAcademicAlert>, PageResult<TeachingAcademicAlert>>('/teaching/alerts', { params })
}

export function generateTeachingAlerts() {
  return request.post<number, number>('/teaching/alerts/generate')
}

export function handleTeachingAlert(id: number, data: TeachingAlertHandleForm) {
  return request.put<void, void>(`/teaching/alerts/${id}/handle`, data)
}

export function getMyTeachingAlerts() {
  return request.get<TeachingAcademicAlert[], TeachingAcademicAlert[]>('/teaching/alerts/me')
}

export function getMyTeachingOverview() {
  return request.get<TeachingMyOverview, TeachingMyOverview>('/teaching/me')
}
