import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import type { MenuItem } from '@/types/menu'
import { transformMenusToRoutes } from '@/utils/menu'

interface PermissionState {
  routes: RouteRecordRaw[]
  menus: MenuItem[]
}

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routes: [],
    menus: []
  }),
  actions: {
    generateRoutes(menus: MenuItem[]) {
      this.menus = menus || []
      this.routes = transformMenusToRoutes(this.menus)
      return this.routes
    },
    setRoutes(routes: RouteRecordRaw[]) {
      this.routes = routes
    },
    resetRoutes() {
      this.routes = []
      this.menus = []
    }
  }
})
