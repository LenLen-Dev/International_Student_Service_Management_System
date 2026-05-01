import type { PageQuery } from './api'

export type AdmissionApplicationStatus =
  | 'DRAFT'
  | 'SUBMITTED'
  | 'UNDER_REVIEW'
  | 'RETURNED'
  | 'REJECTED'
  | 'ADMITTED'
  | 'NOTICE_ISSUED'

export type AdmissionMaterialReviewStatus = 'PENDING' | 'APPROVED' | 'REJECTED'

export interface AdmissionApplicationQuery extends PageQuery {
  applicationNo?: string
  name?: string
  nationality?: string
  applyCollege?: string
  applyMajor?: string
  applicationStatus?: string
  admissionStatus?: string
  startTime?: string
  endTime?: string
}

export interface AdmissionApplicationForm {
  chineseName?: string
  englishName: string
  gender?: string
  birthDate?: string
  nationality: string
  email?: string
  phone?: string
  passportNo?: string
  passportCountry?: string
  passportExpireDate?: string
  applyCollege?: string
  applyMajor?: string
  degreeLevel?: string
  programType?: string
  educationBackground?: string
  remark?: string
}

export interface AdmissionApplication extends AdmissionApplicationForm {
  id: number
  applicationNo: string
  userId: number
  applicationStatus: AdmissionApplicationStatus | string
  admissionStatus: string
  reviewOpinion?: string
  studentProfileId?: number
  deleted?: number
  createTime?: string
  updateTime?: string
}

export interface AdmissionMaterial {
  id: number
  applicationId: number
  materialType: string
  fileName: string
  fileUrl: string
  fileSize?: number
  mimeType?: string
  reviewStatus: AdmissionMaterialReviewStatus | string
  reviewOpinion?: string
  deleted?: number
  createTime?: string
  updateTime?: string
}

export interface AdmissionReviewRecord {
  id: number
  applicationId: number
  actionType: string
  fromStatus?: string
  toStatus?: string
  opinion?: string
  operatorId?: number
  operatorName?: string
  deleted?: number
  createTime?: string
  updateTime?: string
}

export interface AdmissionNotice {
  id: number
  applicationId: number
  noticeNo: string
  fileName: string
  filePath: string
  issueDate?: string
  issuerId?: number
  issuerName?: string
  downloadCount: number
  deleted?: number
  createTime?: string
  updateTime?: string
}

export interface AdmissionApplicationDetail {
  application?: AdmissionApplication
  materials: AdmissionMaterial[]
  reviews: AdmissionReviewRecord[]
  notice?: AdmissionNotice
}

export interface AdmissionReviewForm {
  opinion?: string
}

export interface AdmissionMaterialReviewForm {
  reviewStatus: string
  reviewOpinion?: string
}
