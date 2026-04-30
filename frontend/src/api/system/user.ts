import request from '@/utils/request'
import type { PageResult } from '@/types/api'
import type { UserForm, UserItem, UserQuery } from '@/types/user'

type UserListResult = UserItem[] | PageResult<UserItem>

export function getUserList(params: UserQuery) {
  return request.get<UserListResult, UserListResult>('/system/users', { params })
}

export function createUser(data: UserForm) {
  return request.post<number, number>('/system/users', data)
}

export function updateUser(id: number, data: UserForm) {
  const { username: _username, roleIds: _roleIds, id: _id, ...payload } = data
  return request.put<void, void>(`/system/users/${id}`, payload)
}

export function deleteUser(id: number) {
  return request.delete<void, void>(`/system/users/${id}`)
}

export function updateUserStatus(id: number, status: number) {
  return request.put<void, void>(`/system/users/${id}/status`, { status })
}

export function assignUserRoles(id: number, roleIds: number[]) {
  return request.put<void, void>(`/system/users/${id}/roles`, { roleIds })
}

export function resetUserPassword(id: number, user: UserItem, password: string) {
  return updateUser(id, {
    id,
    username: user.username,
    realName: user.realName,
    email: user.email,
    phone: user.phone,
    userType: user.userType,
    status: user.status,
    password
  })
}
