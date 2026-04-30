import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/user'

const permissionAliases: Record<string, string[]> = {
  'user:list': ['system:user:list'],
  'user:add': ['system:user:add'],
  'user:update': ['system:user:edit'],
  'user:edit': ['system:user:edit'],
  'user:delete': ['system:user:delete'],
  'user:status': ['system:user:status'],
  'user:assign-role': ['system:user:roles'],
  'user:roles': ['system:user:roles'],
  'user:reset-password': ['system:user:edit'],
  'system:user:update': ['system:user:edit'],
  'system:user:assign-role': ['system:user:roles'],
  'system:user:reset-password': ['system:user:edit'],
  'role:list': ['system:role:list'],
  'role:add': ['system:role:add'],
  'role:update': ['system:role:edit'],
  'role:edit': ['system:role:edit'],
  'role:delete': ['system:role:delete'],
  'role:assign-menu': ['system:role:menus'],
  'role:menus': ['system:role:menus'],
  'system:role:update': ['system:role:edit'],
  'system:role:assign-menu': ['system:role:menus'],
  'menu:list': ['system:menu:list'],
  'menu:add': ['system:menu:add'],
  'menu:update': ['system:menu:edit'],
  'menu:edit': ['system:menu:edit'],
  'menu:delete': ['system:menu:delete'],
  'system:menu:update': ['system:menu:edit']
}

export function resolvePermissionCodes(code: string): string[] {
  return Array.from(new Set([code, ...(permissionAliases[code] || [])]))
}

export function hasPermission(permission?: string | string[]): boolean {
  if (!permission) {
    return true
  }

  const userStore = useUserStore()
  const { permissions } = storeToRefs(userStore)
  const required = Array.isArray(permission) ? permission : [permission]
  const owned = permissions.value || []

  return required.some((item) => resolvePermissionCodes(item).some((code) => owned.includes(code)))
}

export function hasRole(role?: string | string[]): boolean {
  if (!role) {
    return true
  }

  const userStore = useUserStore()
  const { roles } = storeToRefs(userStore)
  const required = Array.isArray(role) ? role : [role]
  return required.some((item) => roles.value.includes(item))
}
