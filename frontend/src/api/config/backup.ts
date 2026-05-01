import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { BackupCreateForm, BackupQuery, DataBackup } from '@/types/config'

export function getBackupPage(params: BackupQuery) {
  return request.get<PageResult<DataBackup>, PageResult<DataBackup>>('/config/backups', { params })
}

export function createBackup(data: BackupCreateForm) {
  return request.post<DataBackup, DataBackup>('/config/backups', data)
}

export function downloadBackup(id: number) {
  return request.get<Blob, Blob>(`/config/backups/${id}/download`, { responseType: 'blob' })
}

export function deleteBackup(id: number) {
  return request.delete<void, void>(`/config/backups/${id}`)
}
