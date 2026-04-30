import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { RoleForm, RoleItem, RoleQuery } from '@/types/role'

type RoleListResult = RoleItem[] | PageResult<RoleItem>

export function getRoleList(params: RoleQuery = {}) {
  return request.get<RoleListResult, RoleListResult>('/system/roles', { params })
}

export function createRole(data: RoleForm) {
  return request.post<number, number>('/system/roles', data)
}

export function updateRole(id: number, data: RoleForm) {
  const { id: _id, ...payload } = data
  return request.put<void, void>(`/system/roles/${id}`, payload)
}

export function deleteRole(id: number) {
  return request.delete<void, void>(`/system/roles/${id}`)
}

export function assignRoleMenus(id: number, menuIds: number[]) {
  return request.put<void, void>(`/system/roles/${id}/menus`, { menuIds })
}
