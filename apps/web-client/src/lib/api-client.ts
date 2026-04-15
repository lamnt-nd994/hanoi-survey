import axios from 'axios'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
})

export type ApiResponse<T> = {
  success: boolean
  data: T
  meta?: Record<string, unknown> | null
  traceId?: string | null
}

export function unwrap<T>(response: { data: ApiResponse<T> }): T {
  return response.data?.data
}

export function unwrapResponse<T>(response: { data: ApiResponse<T> }): ApiResponse<T> {
  return response.data
}
