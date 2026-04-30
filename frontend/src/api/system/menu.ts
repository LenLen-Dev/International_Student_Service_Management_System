import request from '@/utils/request'
import type { MenuForm, MenuTree } from '@/types/menu'

export function getMenuTree(params: { keyword?: string; status?: number | string } = {}) {
  return request.get<MenuTree, MenuTree>('/system/menus/tree', { params })
}

export function createMenu(data: MenuForm) {
  return request.post<number, number>('/system/menus', data)
}

export function updateMenu(id: number, data: MenuForm) {
  const { id: _id, ...payload } = data
  return request.put<void, void>(`/system/menus/${id}`, payload)
}

export function deleteMenu(id: number) {
  return request.delete<void, void>(`/system/menus/${id}`)
}
