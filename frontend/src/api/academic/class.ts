import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { AcademicClass, AcademicClassForm, AcademicGrade, AcademicGradeForm, AcademicQuery } from '@/types/academic'

export function getAcademicGradePage(params: AcademicQuery) {
  return request.get<PageResult<AcademicGrade>, PageResult<AcademicGrade>>('/academic/grades', { params })
}

export function getEnabledAcademicGrades() {
  return request.get<AcademicGrade[], AcademicGrade[]>('/academic/grades/enabled')
}

export function createAcademicGrade(data: AcademicGradeForm) {
  return request.post<number, number>('/academic/grades', data)
}

export function updateAcademicGrade(id: number, data: AcademicGradeForm) {
  return request.put<void, void>(`/academic/grades/${id}`, data)
}

export function deleteAcademicGrade(id: number) {
  return request.delete<void, void>(`/academic/grades/${id}`)
}

export function getAcademicClassPage(params: AcademicQuery) {
  return request.get<PageResult<AcademicClass>, PageResult<AcademicClass>>('/academic/classes', { params })
}

export function getEnabledAcademicClasses() {
  return request.get<AcademicClass[], AcademicClass[]>('/academic/classes/enabled')
}

export function createAcademicClass(data: AcademicClassForm) {
  return request.post<number, number>('/academic/classes', data)
}

export function updateAcademicClass(id: number, data: AcademicClassForm) {
  return request.put<void, void>(`/academic/classes/${id}`, data)
}

export function deleteAcademicClass(id: number) {
  return request.delete<void, void>(`/academic/classes/${id}`)
}
