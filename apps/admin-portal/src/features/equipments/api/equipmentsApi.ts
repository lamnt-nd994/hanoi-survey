import { api, getData, getPaging } from '@/lib/api-client'
import type { Category, CategoryPayload, EquipmentEntity, EquipmentPayload, PaginatedResponse } from '@/types'

export const equipmentsApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<EquipmentEntity>(await api.get('/api/admin/v1/equipments', { params })) as PaginatedResponse<EquipmentEntity>,
  create: async (payload: EquipmentPayload) =>
    getData<EquipmentEntity>(await api.post('/api/admin/v1/equipments', payload)),
  update: async (id: number, payload: EquipmentPayload) =>
    getData<EquipmentEntity>(await api.put(`/api/admin/v1/equipments/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/equipments/${id}`)),
  getById: async (id: number) =>
    getData<EquipmentEntity>(await api.get(`/api/admin/v1/equipments/${id}`)),
  categories: async () =>
    getData<Category[]>(await api.get('/api/admin/v1/equipments/categories')),
  createCategory: async (payload: CategoryPayload) =>
    getData<Category>(await api.post('/api/admin/v1/equipments/categories', payload)),
  updateCategory: async (id: number, payload: CategoryPayload) =>
    getData<Category>(await api.put(`/api/admin/v1/equipments/categories/${id}`, payload)),
  deleteCategory: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/equipments/categories/${id}`)),
}
