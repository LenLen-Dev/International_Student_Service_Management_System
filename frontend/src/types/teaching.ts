import type { PageQuery } from './api'

export interface TeachingCourse {
  id: number
  courseCode: string
  courseName: string
  credits: number
  totalHours?: number
  courseType: string
  college?: string
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface TeachingCourseForm {
  id?: number
  courseCode: string
  courseName: string
  credits?: number
  totalHours?: number
  courseType: string
  college?: string
  status: number
  remark?: string
}

export interface TeachingOffering {
  id: number
  courseId: number
  teacherId: number
  teacherName: string
  academicYear: string
  semester: string
  capacity: number
  selectedCount: number
  selectionStartTime?: string
  selectionEndTime?: string
  classTime?: string
  classroom?: string
  offeringStatus: string
  remark?: string
  createTime?: string
  updateTime?: string
  courseCode?: string
  courseName?: string
  courseType?: string
  college?: string
  credits?: number
  totalHours?: number
}

export interface TeachingOfferingForm {
  id?: number
  courseId?: number
  teacherId?: number
  academicYear: string
  semester: string
  capacity?: number
  selectionStartTime?: string
  selectionEndTime?: string
  classTime?: string
  classroom?: string
  offeringStatus: string
  remark?: string
}

export interface TeachingEnrollment {
  id: number
  offeringId: number
  studentId: number
  enrollmentStatus: string
  selectTime?: string
  dropTime?: string
  remark?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  courseCode?: string
  courseName?: string
  academicYear?: string
  semester?: string
  teacherName?: string
  classTime?: string
  classroom?: string
  offeringStatus?: string
}

export interface TeachingGrade {
  id: number
  enrollmentId: number
  offeringId: number
  studentId: number
  usualScore?: number
  finalScore?: number
  totalScore?: number
  gradePoint?: number
  passed?: number
  gradeStatus: string
  remark?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  courseCode?: string
  courseName?: string
  academicYear?: string
  semester?: string
  teacherName?: string
}

export interface TeachingGradeForm {
  id?: number
  enrollmentId?: number
  usualScore?: number
  finalScore?: number
  totalScore?: number
  remark?: string
}

export interface TeachingAttendance {
  id?: number
  offeringId: number
  studentId: number
  attendanceDate: string
  attendanceStatus: string
  remark?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  courseCode?: string
  courseName?: string
  academicYear?: string
  semester?: string
  teacherName?: string
}

export interface TeachingAttendanceItem {
  studentId?: number
  attendanceDate?: string
  attendanceStatus: string
  remark?: string
}

export interface TeachingAcademicAlert {
  id: number
  studentId: number
  alertType: string
  alertLevel: string
  sourceId?: number
  title: string
  content?: string
  alertStatus: string
  handlerId?: number
  handlerName?: string
  handleTime?: string
  handleRemark?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  createTime?: string
}

export interface TeachingAlertHandleForm {
  alertStatus: string
  handleRemark?: string
}

export interface TeachingQuery extends PageQuery {
  courseId?: number | string
  offeringId?: number | string
  teacherId?: number | string
  courseType?: string
  academicYear?: string
  semester?: string
  offeringStatus?: string
  enrollmentStatus?: string
  gradeStatus?: string
  attendanceStatus?: string
  attendanceDate?: string
  alertType?: string
  alertLevel?: string
  alertStatus?: string
}

export interface TeachingMyOverview {
  selections: TeachingEnrollment[]
  grades: TeachingGrade[]
  attendance: TeachingAttendance[]
  alerts: TeachingAcademicAlert[]
}
