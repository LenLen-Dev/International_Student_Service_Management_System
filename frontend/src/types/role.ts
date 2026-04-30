import type { PageQuery } from './api'

export interface RoleItem {
  id: number
  roleCode: string
  roleName: string
  description?: string
  status: number
  sort?: number
  createTime?: string
  updateTime?: string
  menuIds?: number[]
}

export interface RoleQuery extends PageQuery {
  roleName?: string
  roleCode?: string
}

export interface RoleForm {
  id?: number
  roleCode: string
  roleName: string
  description?: string
  status: number
  sort?: number
}
