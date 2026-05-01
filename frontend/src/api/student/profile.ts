import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type {
  StudentContact,
  StudentContactForm,
  StudentDocument,
  StudentDocumentForm,
  StudentEducation,
  StudentEducationForm,
  StudentProfileDetail,
  StudentProfileForm,
  StudentProfileListItem,
  StudentProfileQuery,
  StudentStatusLog,
  StudentStatusUpdateForm
} from '@/types/student'

export function getStudentProfilePage(params: StudentProfileQuery) {
  return request.get<PageResult<StudentProfileListItem>, PageResult<StudentProfileListItem>>('/student/profiles', { params })
}

export function createStudentProfile(data: StudentProfileForm) {
  return request.post<number, number>('/student/profiles', data)
}

export function getStudentProfileDetail(id: number) {
  return request.get<StudentProfileDetail, StudentProfileDetail>(`/student/profiles/${id}`)
}

export function updateStudentProfile(id: number, data: StudentProfileForm) {
  return request.put<void, void>(`/student/profiles/${id}`, data)
}

export function deleteStudentProfile(id: number) {
  return request.delete<void, void>(`/student/profiles/${id}`)
}

export function updateStudentProfileStatus(id: number, data: StudentStatusUpdateForm) {
  return request.put<void, void>(`/student/profiles/${id}/status`, data)
}

export function getMyStudentProfile() {
  return request.get<StudentProfileDetail, StudentProfileDetail>('/student/profiles/me')
}

export function updateMyStudentProfile(data: Partial<StudentProfileForm>) {
  return request.put<void, void>('/student/profiles/me', data)
}

export function getStudentContacts(studentId: number) {
  return request.get<StudentContact[], StudentContact[]>(`/student/profiles/${studentId}/contacts`)
}

export function createStudentContact(studentId: number, data: StudentContactForm) {
  return request.post<number, number>(`/student/profiles/${studentId}/contacts`, data)
}

export function updateStudentContact(id: number, data: StudentContactForm) {
  return request.put<void, void>(`/student/contacts/${id}`, data)
}

export function deleteStudentContact(id: number) {
  return request.delete<void, void>(`/student/contacts/${id}`)
}

export function getStudentEducations(studentId: number) {
  return request.get<StudentEducation[], StudentEducation[]>(`/student/profiles/${studentId}/educations`)
}

export function createStudentEducation(studentId: number, data: StudentEducationForm) {
  return request.post<number, number>(`/student/profiles/${studentId}/educations`, data)
}

export function updateStudentEducation(id: number, data: StudentEducationForm) {
  return request.put<void, void>(`/student/educations/${id}`, data)
}

export function deleteStudentEducation(id: number) {
  return request.delete<void, void>(`/student/educations/${id}`)
}

export function getStudentDocuments(studentId: number) {
  return request.get<StudentDocument[], StudentDocument[]>(`/student/profiles/${studentId}/documents`)
}

export function createStudentDocument(studentId: number, data: StudentDocumentForm) {
  return request.post<number, number>(`/student/profiles/${studentId}/documents`, data)
}

export function updateStudentDocument(id: number, data: StudentDocumentForm) {
  return request.put<void, void>(`/student/documents/${id}`, data)
}

export function deleteStudentDocument(id: number) {
  return request.delete<void, void>(`/student/documents/${id}`)
}

export function getStudentStatusLogs(studentId: number) {
  return request.get<StudentStatusLog[], StudentStatusLog[]>(`/student/profiles/${studentId}/status-logs`)
}
