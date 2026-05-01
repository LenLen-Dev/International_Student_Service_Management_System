import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { AcademicMajor, AcademicMajorForm, AcademicQuery } from '@/types/academic'

export function getAcademicMajorPage(params: AcademicQuery) {
  return request.get<PageResult<AcademicMajor>, PageResult<AcademicMajor>>('/academic/majors', { params })
}

export function getEnabledAcademicMajors() {
  return request.get<AcademicMajor[], AcademicMajor[]>('/academic/majors/enabled')
}

export function createAcademicMajor(data: AcademicMajorForm) {
  return request.post<number, number>('/academic/majors', data)
}

export function updateAcademicMajor(id: number, data: AcademicMajorForm) {
  return request.put<void, void>(`/academic/majors/${id}`, data)
}

export function updateAcademicMajorStatus(id: number, status: number) {
  return request.put<void, void>(`/academic/majors/${id}/status`, { status })
}

export function deleteAcademicMajor(id: number) {
  return request.delete<void, void>(`/academic/majors/${id}`)
}
