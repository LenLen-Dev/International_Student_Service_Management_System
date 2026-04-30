import router from './index'
import { usePermissionStore } from '@/stores/permission'
import { useUserStore } from '@/stores/user'
import { getToken } from '@/utils/auth'

const whiteList = ['/login']

router.beforeEach(async (to, _from, next) => {
  const token = getToken()
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  if (!token) {
    if (whiteList.includes(to.path)) {
      next()
      return
    }
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
    return
  }

  if (to.path === '/login') {
    next('/dashboard')
    return
  }

  try {
    if (!userStore.userInfo) {
      await userStore.getUserInfo()
    }

    if (permissionStore.routes.length === 0) {
      const routes = permissionStore.generateRoutes(userStore.userInfo?.menus || [])
      routes.forEach((route) => {
        if (route.name && !router.hasRoute(route.name)) {
          router.addRoute(route)
        }
      })
      next({ ...to, replace: true })
      return
    }

    next()
  } catch {
    userStore.resetUser()
    permissionStore.resetRoutes()
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
  }
})
