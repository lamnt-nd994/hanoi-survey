import { api, getData, getPaging } from '@/lib/api-client'
import type { Category, CategoryPayload, PaginatedResponse, PostEntity, PostPayload } from '@/types'

export const postsApi = {
  list: async (params: { page?: number; size?: number; keyword?: string; categoryId?: number | null } = {}) =>
    getPaging<PostEntity>(await api.get('/api/admin/v1/posts', { params: { page: params.page || 1, size: params.size || 20, ...params } })) as PaginatedResponse<PostEntity>,
  create: async (payload: PostPayload) =>
    getData<PostEntity>(await api.post('/api/admin/v1/posts', payload)),
  update: async (id: number, payload: PostPayload) =>
    getData<PostEntity>(await api.put(`/api/admin/v1/posts/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/posts/${id}`)),
  getById: async (id: number) =>
    getData<PostEntity>(await api.get(`/api/admin/v1/posts/${id}`)),
  categories: async () =>
    getData<Category[]>(await api.get('/api/admin/v1/posts/categories')),
  createCategory: async (payload: CategoryPayload) =>
    getData<Category>(await api.post('/api/admin/v1/posts/categories', payload)),
  updateCategory: async (id: number, payload: CategoryPayload) =>
    getData<Category>(await api.put(`/api/admin/v1/posts/categories/${id}`, payload)),
  deleteCategory: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/posts/categories/${id}`)),
}
