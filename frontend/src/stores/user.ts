import { defineStore } from 'pinia'
import { getAuthInfo, login as loginApi, logout as logoutApi } from '@/api/system/auth'
import { getToken, removeToken, setToken } from '@/utils/auth'
import type { LoginParams, UserInfo } from '@/types/user'

interface UserState {
  token: string
  userInfo: UserInfo | null
  roles: string[]
  permissions: string[]
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken(),
    userInfo: null,
    roles: [],
    permissions: []
  }),
  actions: {
    async login(params: LoginParams) {
      const result = await loginApi(params)
      this.token = result.token
      setToken(result.token)
      if (result.userInfo) {
        this.userInfo = result.userInfo
        this.roles = result.userInfo.roles || []
        this.permissions = result.userInfo.permissions || []
      }
      return result
    },
    async getUserInfo() {
      const info = await getAuthInfo()
      this.userInfo = info
      this.roles = info.roles || []
      this.permissions = info.permissions || []
      return info
    },
    async logout() {
      try {
        await logoutApi()
      } finally {
        this.resetUser()
      }
    },
    resetUser() {
      this.token = ''
      this.userInfo = null
      this.roles = []
      this.permissions = []
      removeToken()
    }
  }
})
