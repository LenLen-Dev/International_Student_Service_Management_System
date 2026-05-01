import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { TeachingEnrollment, TeachingOffering, TeachingOfferingForm, TeachingQuery } from '@/types/teaching'

export function getTeachingOfferingPage(params: TeachingQuery) {
  return request.get<PageResult<TeachingOffering>, PageResult<TeachingOffering>>('/teaching/offerings', { params })
}

export function getMyTeachingOfferings() {
  return request.get<TeachingOffering[], TeachingOffering[]>('/teaching/offerings/my')
}

export function getTeachingOfferingStudents(id: number) {
  return request.get<TeachingEnrollment[], TeachingEnrollment[]>(`/teaching/offerings/${id}/students`)
}

export function createTeachingOffering(data: TeachingOfferingForm) {
  return request.post<number, number>('/teaching/offerings', data)
}

export function updateTeachingOffering(id: number, data: TeachingOfferingForm) {
  return request.put<void, void>(`/teaching/offerings/${id}`, data)
}

export function updateTeachingOfferingStatus(id: number, offeringStatus: string) {
  return request.put<void, void>(`/teaching/offerings/${id}/status`, { offeringStatus })
}

export function deleteTeachingOffering(id: number) {
  return request.delete<void, void>(`/teaching/offerings/${id}`)
}
