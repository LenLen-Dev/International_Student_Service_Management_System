import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { AcademicApproveForm, AcademicLeave, AcademicLeaveForm, AcademicQuery } from '@/types/academic'

export function getAcademicLeavePage(params: AcademicQuery) {
  return request.get<PageResult<AcademicLeave>, PageResult<AcademicLeave>>('/academic/leaves', { params })
}

export function getAcademicLeaveDetail(id: number) {
  return request.get<AcademicLeave, AcademicLeave>(`/academic/leaves/${id}`)
}

export function approveAcademicLeave(id: number, data: AcademicApproveForm) {
  return request.put<void, void>(`/academic/leaves/${id}/approve`, data)
}

export function rejectAcademicLeave(id: number, data: AcademicApproveForm) {
  return request.put<void, void>(`/academic/leaves/${id}/reject`, data)
}

export function getMyAcademicLeaves() {
  return request.get<AcademicLeave[], AcademicLeave[]>('/academic/leaves/me')
}

export function applyMyAcademicLeave(data: AcademicLeaveForm) {
  return request.post<number, number>('/academic/leaves/me', data)
}

export function cancelMyAcademicLeave(id: number) {
  return request.put<void, void>(`/academic/leaves/me/${id}/cancel`)
}
