import request from '@/utils/request'
import type { LoginParams, LoginResult, UserInfo } from '@/types/user'

export function login(data: LoginParams) {
  return request.post<LoginResult, LoginResult>('/auth/login', data)
}

export function getAuthInfo() {
  return request.get<UserInfo, UserInfo>('/auth/info')
}

export function logout() {
  return request.post<void, void>('/auth/logout')
}
