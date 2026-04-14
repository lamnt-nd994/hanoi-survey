import { api, getData } from '@/lib/api-client'
import type { AuthUser, LoginResponse } from '@/types'

export const authApi = {
  login: async (username: string, password: string) =>
    getData<LoginResponse>(await api.post('/api/auth/v1/login', { username, password })),
  me: async () =>
    getData<AuthUser>(await api.get('/api/auth/v1/me')),
}
