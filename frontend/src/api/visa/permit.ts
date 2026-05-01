import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { ResidencePermit, ResidencePermitForm, ResidencePermitQuery } from '@/types/visa'

export function getResidencePermitPage(params: ResidencePermitQuery) {
  return request.get<PageResult<ResidencePermit>, PageResult<ResidencePermit>>('/visa/residence-permits', { params })
}

export function createResidencePermit(data: ResidencePermitForm) {
  return request.post<number, number>('/visa/residence-permits', data)
}

export function getResidencePermitDetail(id: number) {
  return request.get<ResidencePermit, ResidencePermit>(`/visa/residence-permits/${id}`)
}

export function updateResidencePermit(id: number, data: ResidencePermitForm) {
  return request.put<void, void>(`/visa/residence-permits/${id}`, data)
}

export function deleteResidencePermit(id: number) {
  return request.delete<void, void>(`/visa/residence-permits/${id}`)
}
