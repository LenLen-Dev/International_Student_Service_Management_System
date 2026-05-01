import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { TeachingGrade, TeachingGradeForm, TeachingQuery } from '@/types/teaching'

export function getTeachingGradePage(params: TeachingQuery) {
  return request.get<PageResult<TeachingGrade>, PageResult<TeachingGrade>>('/teaching/grades', { params })
}

export function updateTeachingGrade(id: number, data: TeachingGradeForm) {
  return request.put<void, void>(`/teaching/grades/${id}`, data)
}

export function importTeachingGrades(offeringId: number, data: TeachingGradeForm[]) {
  return request.post<void, void>(`/teaching/offerings/${offeringId}/grades/import`, data)
}

export function publishTeachingGrades(offeringId: number) {
  return request.put<void, void>(`/teaching/offerings/${offeringId}/grades/publish`)
}

export function getMyTeachingGrades() {
  return request.get<TeachingGrade[], TeachingGrade[]>('/teaching/grades/me')
}
