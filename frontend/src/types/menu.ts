export type MenuType = 'CATALOG' | 'MENU' | 'BUTTON' | 1 | 2 | 3 | '1' | '2' | '3'

export interface MenuItem {
  id: number
  parentId: number
  menuName: string
  menuType: MenuType
  permissionCode?: string
  path?: string
  component?: string
  icon?: string
  visible: number
  status: number
  sort?: number
  createTime?: string
  updateTime?: string
  children?: MenuItem[]
}

export interface MenuForm {
  id?: number
  parentId: number
  menuName: string
  menuType: 'CATALOG' | 'MENU' | 'BUTTON'
  permissionCode?: string
  path?: string
  component?: string
  icon?: string
  visible: number
  status: number
  sort?: number
}

export type MenuTree = MenuItem[]
