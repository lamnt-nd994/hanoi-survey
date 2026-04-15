import axios from 'axios'
import router from '@/app/router'
import type { PaginatedResponse } from '@/types'

const TOKEN_KEY = 'hanoi-survey-admin-token'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 15000,
})

api.interceptors.request.use((config) => {
  const token = getStoredToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response?.status === 401 && router.currentRoute.value.path !== '/login') {
      clearStoredToken()
      router.push('/login')
    }
    return Promise.reject(error)
  },
)

export function getStoredToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setStoredToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearStoredToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

export function getData<T>(res: { data: { data: T } }): T {
  return res.data.data
}

export function getPaging<T>(res: { data: { data?: T[]; meta?: PaginatedResponse<T>['meta'] } }): PaginatedResponse<T> {
  return {
    data: res.data.data || [],
    meta: res.data.meta || { page: 1, size: 10, total: 0, totalPages: 0 },
  }
}
