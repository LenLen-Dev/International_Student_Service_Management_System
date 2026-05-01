import type { Component } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'
import type { MenuItem, MenuType } from '@/types/menu'

const viewModules = import.meta.glob('../views/**/*.vue')

const pathMap: Record<string, string> = {
  '/system/users': '/system/user',
  '/system/roles': '/system/role',
  '/system/menus': '/system/menu',
  '/student/profile': '/student/profiles',
  '/admission/applications/me': '/admission/my-application',
  '/admission/applications': '/admission/review',
  '/visa/records': '/visa/record',
  '/visa/residence-permits': '/visa/permit',
  '/visa/renewals': '/visa/renewal',
  '/visa/alerts': '/visa/alert',
  '/visa/me': '/visa/my'
}

const componentMap: Record<string, string> = {
  'system/users/index': 'system/user/index',
  'system/roles/index': 'system/role/index',
  'system/menus/index': 'system/menu/index',
  'student/profiles/index': 'student/profile/index',
  'admission/applications/me/index': 'admission/my-application/index',
  'admission/my-application/index': 'admission/my-application/index',
  'admission/applications/index': 'admission/review/index',
  'admission/review/index': 'admission/review/index',
  'visa/records/index': 'visa/record/index',
  'visa/record/index': 'visa/record/index',
  'visa/residence-permits/index': 'visa/permit/index',
  'visa/permit/index': 'visa/permit/index',
  'visa/renewals/index': 'visa/renewal/index',
  'visa/renewal/index': 'visa/renewal/index',
  'visa/alerts/index': 'visa/alert/index',
  'visa/alert/index': 'visa/alert/index',
  'visa/my/index': 'visa/my/index'
}

export const iconMap: Record<string, string> = {
  system: 'Setting',
  config: 'Setting',
  setting: 'Setting',
  user: 'User',
  role: 'Key',
  key: 'Key',
  menu: 'Menu',
  flow: 'Share',
  share: 'Share',
  log: 'Document',
  backup: 'Folder',
  folder: 'Folder',
  dashboard: 'House',
  home: 'House',
  admission: 'Tickets',
  apply: 'EditPen',
  review: 'Checked',
  tickets: 'Tickets',
  checked: 'Checked',
  visa: 'OfficeBuilding',
  officebuilding: 'OfficeBuilding',
  permit: 'Folder',
  renewal: 'Edit',
  edit: 'Edit',
  warning: 'Warning',
  alert: 'Warning',
  document: 'Document',
  school: 'School',
  office: 'OfficeBuilding'
}

export function normalizeMenuType(type: MenuType): 'CATALOG' | 'MENU' | 'BUTTON' {
  if (type === 1 || type === '1' || type === 'CATALOG') {
    return 'CATALOG'
  }
  if (type === 2 || type === '2' || type === 'MENU') {
    return 'MENU'
  }
  return 'BUTTON'
}

export function normalizeRoutePath(path?: string): string {
  if (!path) {
    return ''
  }

  const normalized = path.startsWith('/') ? path : `/${path}`
  return pathMap[normalized] || normalized
}

export function normalizeComponentPath(component?: string): string {
  if (!component) {
    return ''
  }

  if (component === 'Layout') {
    return component
  }

  const noPrefix = component.replace(/^\/?src\/views\//, '').replace(/^\/?views\//, '').replace(/\.vue$/, '')
  return componentMap[noPrefix] || noPrefix
}

export function getIconName(icon?: string): string {
  if (!icon) {
    return 'Menu'
  }

  return iconMap[icon] || iconMap[icon.toLowerCase()] || icon
}

export function getVisibleMenuChildren(menu?: MenuItem): MenuItem[] {
  return (menu?.children || []).filter((item) => {
    const type = normalizeMenuType(item.menuType)
    return type !== 'BUTTON' && item.visible !== 0 && item.status !== 0
  })
}

function routeNameFromPath(path: string, id: number): string {
  const name = path
    .split('/')
    .filter(Boolean)
    .map((item) => item.replace(/(^|-)([a-z])/g, (_, _dash, letter: string) => letter.toUpperCase()))
    .join('')
  return name || `Menu${id}`
}

function getRouteComponent(menu: MenuItem): Component | (() => Promise<unknown>) {
  const component = normalizeComponentPath(menu.component)
  if (component === 'Layout') {
    return Layout
  }

  const moduleKey = `../views/${component || 'dashboard/index'}.vue`
  return viewModules[moduleKey] || (() => import('@/views/dashboard/index.vue'))
}

function toChildRoutePath(path: string, parentPath: string): string {
  if (!parentPath) {
    return path
  }

  if (path.startsWith(`${parentPath}/`)) {
    return path.slice(parentPath.length + 1)
  }

  return path.replace(/^\//, '')
}

function transformMenu(menu: MenuItem, parentPath = ''): RouteRecordRaw | null {
  const type = normalizeMenuType(menu.menuType)
  if (type === 'BUTTON' || menu.status === 0) {
    return null
  }

  const fullPath = normalizeRoutePath(menu.path) || parentPath || '/'
  const routePath = toChildRoutePath(fullPath, parentPath)
  const visibleChildren = getVisibleMenuChildren(menu)
  const childrenRoutes = visibleChildren
    .map((child) => transformMenu(child, fullPath))
    .filter(Boolean) as RouteRecordRaw[]
  const redirect =
    type === 'CATALOG' && childrenRoutes.length
      ? `${fullPath}/${String(childrenRoutes[0].path).replace(/^\//, '')}`.replace(/\/+/g, '/')
      : undefined

  return {
    path: routePath,
    name: routeNameFromPath(fullPath, menu.id),
    component: type === 'CATALOG' ? Layout : getRouteComponent(menu),
    meta: {
      id: menu.id,
      title: menu.menuName,
      icon: getIconName(menu.icon),
      permission: menu.permissionCode,
      hidden: menu.visible === 0
    },
    ...(childrenRoutes.length ? { children: childrenRoutes } : {}),
    ...(redirect ? { redirect } : {})
  } as RouteRecordRaw
}

export function transformMenusToRoutes(menus: MenuItem[]): RouteRecordRaw[] {
  return menus
    .filter((item) => normalizeMenuType(item.menuType) !== 'BUTTON')
    .map((item) => transformMenu(item))
    .filter(Boolean) as RouteRecordRaw[]
}
