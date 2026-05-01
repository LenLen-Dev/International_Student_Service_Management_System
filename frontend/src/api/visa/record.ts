import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { VisaRecord, VisaRecordForm, VisaRecordQuery } from '@/types/visa'

export function getVisaRecordPage(params: VisaRecordQuery) {
  return request.get<PageResult<VisaRecord>, PageResult<VisaRecord>>('/visa/records', { params })
}

export function createVisaRecord(data: VisaRecordForm) {
  return request.post<number, number>('/visa/records', data)
}

export function getVisaRecordDetail(id: number) {
  return request.get<VisaRecord, VisaRecord>(`/visa/records/${id}`)
}

export function updateVisaRecord(id: number, data: VisaRecordForm) {
  return request.put<void, void>(`/visa/records/${id}`, data)
}

export function deleteVisaRecord(id: number) {
  return request.delete<void, void>(`/visa/records/${id}`)
}
