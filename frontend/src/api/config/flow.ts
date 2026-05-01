import request from '@/utils/request'
import type { FlowDetail, FlowNode, FlowTemplate, FlowTemplateForm } from '@/types/config'

export function getFlowList(params?: { keyword?: string; status?: number | string }) {
  return request.get<FlowTemplate[], FlowTemplate[]>('/config/flows', { params })
}

export function createFlow(data: FlowTemplateForm) {
  return request.post<number, number>('/config/flows', data)
}

export function getFlowDetail(id: number) {
  return request.get<FlowDetail, FlowDetail>(`/config/flows/${id}`)
}

export function updateFlow(id: number, data: FlowTemplateForm) {
  return request.put<void, void>(`/config/flows/${id}`, data)
}

export function deleteFlow(id: number) {
  return request.delete<void, void>(`/config/flows/${id}`)
}

export function updateFlowStatus(id: number, status: number) {
  return request.put<void, void>(`/config/flows/${id}/status`, { status })
}

export function getFlowNodes(id: number) {
  return request.get<FlowNode[], FlowNode[]>(`/config/flows/${id}/nodes`)
}

export function saveFlowNodes(id: number, nodes: FlowNode[]) {
  return request.put<void, void>(`/config/flows/${id}/nodes`, nodes)
}
