import { api, getData, getPaging } from '@/lib/api-client'
import type { AdminRoleEntity, AdminRolePayload, AdminUserEntity, AdminUserPayload, PaginatedResponse } from '@/types'

export const usersApi = {
  list: async (params: { page?: number; size?: number; keyword?: string; roleId?: number | null; active?: boolean | null } = {}) =>
    getPaging<AdminUserEntity>(await api.get('/api/admin/v1/users', { params: { page: params.page || 1, size: params.size || 20, ...params } })) as PaginatedResponse<AdminUserEntity>,
  getById: async (id: number) =>
    getData<AdminUserEntity>(await api.get(`/api/admin/v1/users/${id}`)),
  create: async (payload: AdminUserPayload) =>
    getData<AdminUserEntity>(await api.post('/api/admin/v1/users', payload)),
  update: async (id: number, payload: AdminUserPayload) =>
    getData<AdminUserEntity>(await api.put(`/api/admin/v1/users/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/users/${id}`)),
  resetPassword: async (id: number, password: string) =>
    getData<void>(await api.post(`/api/admin/v1/users/${id}/reset-password`, { password })),
  roles: async () =>
    getData<AdminRoleEntity[]>(await api.get('/api/admin/v1/roles')),
  createRole: async (payload: AdminRolePayload) =>
    getData<AdminRoleEntity>(await api.post('/api/admin/v1/roles', payload)),
  updateRole: async (id: number, payload: AdminRolePayload) =>
    getData<AdminRoleEntity>(await api.put(`/api/admin/v1/roles/${id}`, payload)),
  deleteRole: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/roles/${id}`)),
}
