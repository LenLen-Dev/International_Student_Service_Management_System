import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { TeachingCourse, TeachingCourseForm, TeachingQuery } from '@/types/teaching'

export function getTeachingCoursePage(params: TeachingQuery) {
  return request.get<PageResult<TeachingCourse>, PageResult<TeachingCourse>>('/teaching/courses', { params })
}

export function getEnabledTeachingCourses() {
  return request.get<TeachingCourse[], TeachingCourse[]>('/teaching/courses/enabled')
}

export function createTeachingCourse(data: TeachingCourseForm) {
  return request.post<number, number>('/teaching/courses', data)
}

export function updateTeachingCourse(id: number, data: TeachingCourseForm) {
  return request.put<void, void>(`/teaching/courses/${id}`, data)
}

export function updateTeachingCourseStatus(id: number, status: number) {
  return request.put<void, void>(`/teaching/courses/${id}/status`, { status })
}

export function deleteTeachingCourse(id: number) {
  return request.delete<void, void>(`/teaching/courses/${id}`)
}
