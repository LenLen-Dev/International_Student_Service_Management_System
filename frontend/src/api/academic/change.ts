import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { AcademicApproveForm, AcademicChange, AcademicChangeForm, AcademicQuery } from '@/types/academic'

export function getAcademicChangePage(params: AcademicQuery) {
  return request.get<PageResult<AcademicChange>, PageResult<AcademicChange>>('/academic/changes', { params })
}

export function createAcademicChange(data: AcademicChangeForm) {
  return request.post<number, number>('/academic/changes', data)
}

export function getAcademicChangeDetail(id: number) {
  return request.get<AcademicChange, AcademicChange>(`/academic/changes/${id}`)
}

export function updateAcademicChange(id: number, data: AcademicChangeForm) {
  return request.put<void, void>(`/academic/changes/${id}`, data)
}

export function approveAcademicChange(id: number, data: AcademicApproveForm) {
  return request.put<void, void>(`/academic/changes/${id}/approve`, data)
}

export function rejectAcademicChange(id: number, data: AcademicApproveForm) {
  return request.put<void, void>(`/academic/changes/${id}/reject`, data)
}
