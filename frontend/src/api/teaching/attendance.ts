import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { TeachingAttendance, TeachingAttendanceItem, TeachingQuery } from '@/types/teaching'

export function getTeachingAttendancePage(params: TeachingQuery) {
  return request.get<PageResult<TeachingAttendance>, PageResult<TeachingAttendance>>('/teaching/attendance', { params })
}

export function saveTeachingAttendance(offeringId: number, records: TeachingAttendanceItem[]) {
  return request.put<void, void>(`/teaching/offerings/${offeringId}/attendance`, { records })
}

export function getMyTeachingAttendance() {
  return request.get<TeachingAttendance[], TeachingAttendance[]>('/teaching/attendance/me')
}
