import { api, getData, getPaging } from '@/lib/api-client'
import type { PaginatedResponse, ServiceEntity, ServicePayload } from '@/types'

export const servicesApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<ServiceEntity>(await api.get('/api/admin/v1/services', { params })) as PaginatedResponse<ServiceEntity>,
  create: async (payload: ServicePayload) =>
    getData<ServiceEntity>(await api.post('/api/admin/v1/services', payload)),
  update: async (id: number, payload: ServicePayload) =>
    getData<ServiceEntity>(await api.put(`/api/admin/v1/services/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/services/${id}`)),
  getById: async (id: number) =>
    getData<ServiceEntity>(await api.get(`/api/admin/v1/services/${id}`)),
}
