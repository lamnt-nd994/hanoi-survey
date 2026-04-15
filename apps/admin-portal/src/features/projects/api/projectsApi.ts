import { api, getData, getPaging } from '@/lib/api-client'
import type { Category, CategoryPayload, PaginatedResponse, ProjectEntity, ProjectPayload } from '@/types'

export const projectsApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<ProjectEntity>(await api.get('/api/admin/v1/projects', { params })) as PaginatedResponse<ProjectEntity>,
  create: async (payload: ProjectPayload) =>
    getData<ProjectEntity>(await api.post('/api/admin/v1/projects', payload)),
  update: async (id: number, payload: ProjectPayload) =>
    getData<ProjectEntity>(await api.put(`/api/admin/v1/projects/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/projects/${id}`)),
  getById: async (id: number) =>
    getData<ProjectEntity>(await api.get(`/api/admin/v1/projects/${id}`)),
  categories: async () =>
    getData<Category[]>(await api.get('/api/admin/v1/projects/categories')),
  createCategory: async (payload: CategoryPayload) =>
    getData<Category>(await api.post('/api/admin/v1/projects/categories', payload)),
  updateCategory: async (id: number, payload: CategoryPayload) =>
    getData<Category>(await api.put(`/api/admin/v1/projects/categories/${id}`, payload)),
  deleteCategory: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/projects/categories/${id}`)),
}
