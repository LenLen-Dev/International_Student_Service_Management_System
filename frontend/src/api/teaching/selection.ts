import request from '@/utils/request'
import type { TeachingEnrollment, TeachingOffering, TeachingQuery } from '@/types/teaching'

export function getAvailableTeachingOfferings(params: TeachingQuery) {
  return request.get<TeachingOffering[], TeachingOffering[]>('/teaching/selections/available', { params })
}

export function getMyTeachingSelections() {
  return request.get<TeachingEnrollment[], TeachingEnrollment[]>('/teaching/selections/me')
}

export function selectTeachingCourse(offeringId: number) {
  return request.post<void, void>(`/teaching/selections/${offeringId}`)
}

export function dropTeachingCourse(offeringId: number) {
  return request.delete<void, void>(`/teaching/selections/${offeringId}`)
}
