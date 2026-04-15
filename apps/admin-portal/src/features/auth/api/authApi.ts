import { api, getData } from '@/lib/api-client'
import type { AuthUser, LoginResponse } from '@/types'

export const authApi = {
  login: async (username: string, password: string) =>
    getData<LoginResponse>(await api.post('/api/auth/v1/login', { username, password })),
  me: async () =>
    getData<AuthUser>(await api.get('/api/auth/v1/me')),
  updateMyProfile: async (payload: { fullName: string; email: string }) =>
    getData<AuthUser>(await api.put('/api/admin/v1/users/me', payload)),
  changeMyPassword: async (payload: { currentPassword: string; newPassword: string }) =>
    getData<string>(await api.post('/api/admin/v1/users/change-password', payload)),
}
