import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { OperationLog, OperationLogQuery } from '@/types/config'

export function getOperationLogPage(params: OperationLogQuery) {
  return request.get<PageResult<OperationLog>, PageResult<OperationLog>>('/audit/operation-logs', { params })
}

export function getOperationLogDetail(id: number) {
  return request.get<OperationLog, OperationLog>(`/audit/operation-logs/${id}`)
}

export function exportOperationLogs(params: OperationLogQuery) {
  return request.get<Blob, Blob>('/audit/operation-logs/export', { params, responseType: 'blob' })
}

export function deleteOperationLog(id: number) {
  return request.delete<void, void>(`/audit/operation-logs/${id}`)
}
