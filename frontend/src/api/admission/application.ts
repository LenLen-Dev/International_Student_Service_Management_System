import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  AdmissionApplication,
  AdmissionApplicationDetail,
  AdmissionApplicationForm,
  AdmissionApplicationQuery,
  AdmissionMaterial,
  AdmissionMaterialReviewForm,
  AdmissionReviewForm
} from '@/types/admission'

export function getMyAdmissionApplication() {
  return request.get<AdmissionApplicationDetail | null, AdmissionApplicationDetail | null>('/admission/applications/me')
}

export function createMyAdmissionApplication(data: AdmissionApplicationForm) {
  return request.post<number, number>('/admission/applications/me', data)
}

export function updateMyAdmissionApplication(data: AdmissionApplicationForm) {
  return request.put<void, void>('/admission/applications/me', data)
}

export function submitMyAdmissionApplication() {
  return request.post<void, void>('/admission/applications/me/submit')
}

export function uploadMyAdmissionMaterial(materialType: string, file: File) {
  const formData = new FormData()
  formData.append('materialType', materialType)
  formData.append('file', file)
  return request.post<AdmissionMaterial, AdmissionMaterial>('/admission/applications/me/materials', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function deleteMyAdmissionMaterial(id: number) {
  return request.delete<void, void>(`/admission/applications/me/materials/${id}`)
}

export function downloadMyAdmissionNotice() {
  return request.get<Blob, Blob>('/admission/applications/me/notice/download', { responseType: 'blob' })
}

export function getAdmissionApplicationPage(params: AdmissionApplicationQuery) {
  return request.get<PageResult<AdmissionApplication>, PageResult<AdmissionApplication>>('/admission/applications', { params })
}

export function getAdmissionApplicationDetail(id: number) {
  return request.get<AdmissionApplicationDetail, AdmissionApplicationDetail>(`/admission/applications/${id}`)
}

export function returnAdmissionApplication(id: number, data: AdmissionReviewForm) {
  return request.put<void, void>(`/admission/applications/${id}/return`, data)
}

export function rejectAdmissionApplication(id: number, data: AdmissionReviewForm) {
  return request.put<void, void>(`/admission/applications/${id}/reject`, data)
}

export function admitAdmissionApplication(id: number, data: AdmissionReviewForm) {
  return request.put<void, void>(`/admission/applications/${id}/admit`, data)
}

export function reviewAdmissionMaterial(id: number, data: AdmissionMaterialReviewForm) {
  return request.put<void, void>(`/admission/materials/${id}/review`, data)
}

export function generateAdmissionNotice(id: number) {
  return request.post<void, void>(`/admission/applications/${id}/notice`)
}

export function downloadAdmissionNotice(id: number) {
  return request.get<Blob, Blob>(`/admission/applications/${id}/notice/download`, { responseType: 'blob' })
}
