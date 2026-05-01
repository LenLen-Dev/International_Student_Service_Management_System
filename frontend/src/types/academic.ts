import type { PageQuery } from './api'

export interface AcademicMajor {
  id: number
  college: string
  majorCode: string
  majorName: string
  degreeLevel?: string
  studyDuration?: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface AcademicMajorForm {
  id?: number
  college: string
  majorCode: string
  majorName: string
  degreeLevel?: string
  studyDuration?: number
  status: number
  remark?: string
}

export interface AcademicGrade {
  id: number
  gradeCode: string
  gradeName: string
  enrollmentYear: number
  graduationYear?: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface AcademicGradeForm {
  id?: number
  gradeCode: string
  gradeName: string
  enrollmentYear?: number
  graduationYear?: number
  status: number
  remark?: string
}

export interface AcademicClass {
  id: number
  majorId: number
  gradeId: number
  classCode: string
  className: string
  advisorId?: number
  advisorName?: string
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
  college?: string
  majorName?: string
  gradeName?: string
}

export interface AcademicClassForm {
  id?: number
  majorId?: number
  gradeId?: number
  classCode: string
  className: string
  advisorId?: number
  advisorName?: string
  status: number
  remark?: string
}

export interface AcademicRecord {
  id: number
  studentId: number
  majorId: number
  gradeId: number
  classId?: number
  studentStatus: string
  enrollmentDate?: string
  expectedGraduationDate?: string
  actualLeaveDate?: string
  remark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  college?: string
  majorName?: string
  degreeLevel?: string
  gradeName?: string
  className?: string
}

export interface AcademicRecordForm {
  id?: number
  studentId?: number
  majorId?: number
  gradeId?: number
  classId?: number
  studentStatus: string
  enrollmentDate?: string
  expectedGraduationDate?: string
  actualLeaveDate?: string
  remark?: string
}

export interface AcademicRecordStatusForm {
  studentStatus: string
  actualLeaveDate?: string
  remark?: string
}

export interface AcademicLeave {
  id: number
  studentId: number
  leaveType: string
  startTime: string
  endTime: string
  reason: string
  leaveStatus: string
  approverId?: number
  approverName?: string
  approveTime?: string
  approveOpinion?: string
  remark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  className?: string
}

export interface AcademicLeaveForm {
  leaveType: string
  startTime?: string
  endTime?: string
  reason: string
  remark?: string
}

export interface AcademicApproveForm {
  approveOpinion?: string
}

export interface AcademicChange {
  id: number
  studentId: number
  changeType: string
  oldStatus?: string
  newStatus?: string
  oldMajorId?: number
  newMajorId?: number
  oldGradeId?: number
  newGradeId?: number
  oldClassId?: number
  newClassId?: number
  effectiveDate?: string
  reason: string
  changeStatus: string
  approverId?: number
  approverName?: string
  approveTime?: string
  approveOpinion?: string
  remark?: string
  createTime?: string
  updateTime?: string
  studentNo?: string
  chineseName?: string
  englishName?: string
  newMajorName?: string
  newGradeName?: string
  newClassName?: string
}

export interface AcademicChangeForm {
  id?: number
  studentId?: number
  changeType: string
  newStatus?: string
  newMajorId?: number
  newGradeId?: number
  newClassId?: number
  effectiveDate?: string
  reason: string
  remark?: string
}

export interface AcademicQuery extends PageQuery {
  majorId?: number | string
  gradeId?: number | string
  classId?: number | string
  studentStatus?: string
  leaveStatus?: string
  changeStatus?: string
  changeType?: string
}

export interface AcademicMyOverview {
  record?: AcademicRecord
  leaves: AcademicLeave[]
}
