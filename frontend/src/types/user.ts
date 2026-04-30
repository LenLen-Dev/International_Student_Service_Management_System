import type { PageQuery } from './api'
import type { MenuItem } from './menu'

export interface LoginParams {
  username: string
  password: string
  captcha?: string
}

export interface LoginResult {
  token: string
  tokenType: string
  expiresIn: number
  userInfo: UserInfo
}

export interface UserInfo {
  id: number
  username: string
  realName: string
  userType: string
  roles: string[]
  permissions: string[]
  menus: MenuItem[]
}

export interface UserItem {
  id: number
  username: string
  realName: string
  email?: string
  phone?: string
  userType: string
  status: number
  createTime?: string
  updateTime?: string
  roles?: string[]
  roleIds?: number[]
}

export interface UserQuery extends PageQuery {
  username?: string
  realName?: string
  userType?: string
}

export interface UserForm {
  id?: number
  username: string
  password?: string
  realName: string
  userType: string
  email?: string
  phone?: string
  status: number
  roleIds?: number[]
}
