import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { AcademicMyOverview, AcademicQuery, AcademicRecord, AcademicRecordForm, AcademicRecordStatusForm } from '@/types/academic'

export function getAcademicRecordPage(params: AcademicQuery) {
  return request.get<PageResult<AcademicRecord>, PageResult<AcademicRecord>>('/academic/records', { params })
}

export function createAcademicRecord(data: AcademicRecordForm) {
  return request.post<number, number>('/academic/records', data)
}

export function getAcademicRecordDetail(id: number) {
  return request.get<AcademicRecord, AcademicRecord>(`/academic/records/${id}`)
}

export function updateAcademicRecord(id: number, data: AcademicRecordForm) {
  return request.put<void, void>(`/academic/records/${id}`, data)
}

export function updateAcademicRecordStatus(id: number, data: AcademicRecordStatusForm) {
  return request.put<void, void>(`/academic/records/${id}/status`, data)
}

export function getMyAcademicOverview() {
  return request.get<AcademicMyOverview, AcademicMyOverview>('/academic/me')
}
