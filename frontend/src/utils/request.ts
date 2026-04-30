import axios, { type AxiosError, type InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResult } from '@/types/api'
import { getToken, removeToken } from './auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

request.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

function redirectToLogin(): void {
  removeToken()
  const current = `${window.location.pathname}${window.location.search}`
  if (!current.startsWith('/login')) {
    window.location.href = `/login?redirect=${encodeURIComponent(current)}`
  }
}

request.interceptors.response.use(
  (response) => {
    const result = response.data as ApiResult
    if (typeof result?.code === 'number') {
      if (result.code === 200) {
        return result.data
      }

      if (result.code === 401) {
        ElMessage.error(result.message || '登录状态已失效，请重新登录')
        redirectToLogin()
      } else {
        ElMessage.error(result.message || '接口请求失败')
      }
      return Promise.reject(new Error(result.message || 'Request Error'))
    }

    return response.data
  },
  (error: AxiosError<ApiResult>) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '网络请求异常'

    if (status === 401) {
      ElMessage.error('登录状态已失效，请重新登录')
      redirectToLogin()
    } else if (status === 403) {
      ElMessage.error(message || '无访问权限')
    } else {
      ElMessage.error(message)
    }

    return Promise.reject(error)
  }
)

export default request
