import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { VisaRenewalForm, VisaRenewalQuery, VisaRenewalRecord, VisaRenewalResultForm } from '@/types/visa'

export function getVisaRenewalPage(params: VisaRenewalQuery) {
  return request.get<PageResult<VisaRenewalRecord>, PageResult<VisaRenewalRecord>>('/visa/renewals', { params })
}

export function createVisaRenewal(data: VisaRenewalForm) {
  return request.post<number, number>('/visa/renewals', data)
}

export function getVisaRenewalDetail(id: number) {
  return request.get<VisaRenewalRecord, VisaRenewalRecord>(`/visa/renewals/${id}`)
}

export function updateVisaRenewal(id: number, data: VisaRenewalForm) {
  return request.put<void, void>(`/visa/renewals/${id}`, data)
}

export function deleteVisaRenewal(id: number) {
  return request.delete<void, void>(`/visa/renewals/${id}`)
}

export function updateVisaRenewalResult(id: number, data: VisaRenewalResultForm) {
  return request.put<void, void>(`/visa/renewals/${id}/result`, data)
}
