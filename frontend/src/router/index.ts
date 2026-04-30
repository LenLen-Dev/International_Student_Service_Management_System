import { createRouter, createWebHistory } from 'vue-router'
import { staticRoutes } from './staticRoutes'

const router = createRouter({
  history: createWebHistory(),
  routes: staticRoutes,
  scrollBehavior: () => ({ left: 0, top: 0 })
})

export default router
