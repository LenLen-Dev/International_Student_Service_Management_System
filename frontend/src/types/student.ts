import type { PageQuery } from './api'

export type Gender = 'MALE' | 'FEMALE' | 'UNKNOWN'
export type StudentStatus = 'PRE_ADMITTED' | 'ENROLLED' | 'SUSPENDED' | 'GRADUATED' | 'DROPPED' | 'LEFT'
export type ContactType = 'EMERGENCY' | 'FAMILY' | 'GUARDIAN' | 'OTHER'
export type DocumentType =
  | 'PASSPORT'
  | 'PHOTO'
  | 'ADMISSION_NOTICE'
  | 'DEGREE_CERTIFICATE'
  | 'LANGUAGE_SCORE'
  | 'PHYSICAL_EXAM'
  | 'INSURANCE'
  | 'OTHER'
export type DocumentReviewStatus = 'PENDING' | 'APPROVED' | 'REJECTED'

export interface StudentProfileQuery extends PageQuery {
  studentNo?: string
  chineseName?: string
  englishName?: string
  nationality?: string
  college?: string
  major?: string
  studentStatus?: string
}

export interface StudentProfileListItem {
  id: number
  userId?: number
  studentNo: string
  chineseName?: string
  englishName: string
  gender: Gender | string
  nationality: string
  college?: string
  major?: string
  degreeLevel?: string
  grade?: string
  studentStatus: StudentStatus | string
  status: number
  createTime?: string
}

export interface StudentProfileForm {
  id?: number
  userId?: number
  studentNo: string
  applicationNo?: string
  chineseName?: string
  englishName: string
  gender: Gender | string
  birthDate?: string
  nationality: string
  nativeLanguage?: string
  email?: string
  phone?: string
  wechat?: string
  passportNo?: string
  passportCountry?: string
  passportIssueDate?: string
  passportExpireDate?: string
  college?: string
  major?: string
  degreeLevel?: string
  grade?: string
  className?: string
  enrollmentDate?: string
  expectedGraduationDate?: string
  studentStatus: StudentStatus | string
  avatarUrl?: string
  remark?: string
  status: number
}

export interface StudentContact {
  id: number
  studentId: number
  contactType: ContactType | string
  contactName: string
  relationship?: string
  phone?: string
  email?: string
  address?: string
  isPrimary: number
  createTime?: string
  updateTime?: string
}

export interface StudentContactForm {
  contactType: ContactType | string
  contactName: string
  relationship?: string
  phone?: string
  email?: string
  address?: string
  isPrimary: number
}

export interface StudentEducation {
  id: number
  studentId: number
  schoolName: string
  country?: string
  degreeLevel?: string
  major?: string
  startDate?: string
  endDate?: string
  certificateUrl?: string
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface StudentEducationForm {
  schoolName: string
  country?: string
  degreeLevel?: string
  major?: string
  startDate?: string
  endDate?: string
  certificateUrl?: string
  remark?: string
}

export interface StudentDocument {
  id: number
  studentId: number
  documentType: DocumentType | string
  documentName: string
  fileUrl: string
  fileSize?: number
  mimeType?: string
  reviewStatus: DocumentReviewStatus | string
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface StudentDocumentForm {
  documentType: DocumentType | string
  documentName: string
  fileUrl: string
  fileSize?: number
  mimeType?: string
  reviewStatus: DocumentReviewStatus | string
  remark?: string
}

export interface StudentStatusLog {
  id: number
  studentId: number
  oldStatus?: string
  newStatus: string
  changeReason?: string
  operatorId?: number
  operatorName?: string
  remark?: string
  createTime?: string
}

export interface StudentProfileDetail extends StudentProfileForm {
  id: number
  contacts: StudentContact[]
  educations: StudentEducation[]
  documents: StudentDocument[]
  statusLogs: StudentStatusLog[]
  createTime?: string
  updateTime?: string
}

export interface StudentStatusUpdateForm {
  newStatus: string
  changeReason?: string
  remark?: string
}
