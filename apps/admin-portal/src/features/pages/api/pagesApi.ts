import { api, getData, getPaging } from '@/lib/api-client'
import type { PageEntity, PagePayload, PaginatedResponse } from '@/types'

export const pagesApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<PageEntity>(await api.get('/api/admin/v1/pages', { params })) as PaginatedResponse<PageEntity>,
  create: async (payload: PagePayload) =>
    getData<PageEntity>(await api.post('/api/admin/v1/pages', payload)),
  update: async (id: number, payload: PagePayload) =>
    getData<PageEntity>(await api.put(`/api/admin/v1/pages/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/pages/${id}`)),
  getById: async (id: number) =>
    getData<PageEntity>(await api.get(`/api/admin/v1/pages/${id}`)),
}
