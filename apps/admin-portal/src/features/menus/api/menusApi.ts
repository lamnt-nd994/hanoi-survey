import { api, getData } from '@/lib/api-client'
import type { MenuEntity, MenuItemEntity, MenuItemPayload, MenuPayload } from '@/types'

export const menusApi = {
  list: async () => getData<MenuEntity[]>(await api.get('/api/admin/v1/menus')),
  create: async (payload: MenuPayload) => getData<MenuEntity>(await api.post('/api/admin/v1/menus', payload)),
  update: async (id: number, payload: MenuPayload) => getData<MenuEntity>(await api.put(`/api/admin/v1/menus/${id}`, payload)),
  items: async (code: string) => getData<MenuItemEntity[]>(await api.get(`/api/admin/v1/menus/${code}/items`)),
  createItem: async (payload: MenuItemPayload) => getData<MenuItemEntity>(await api.post('/api/admin/v1/menus/items', payload)),
  updateItem: async (id: number, payload: MenuItemPayload) => getData<MenuItemEntity>(await api.put(`/api/admin/v1/menus/items/${id}`, payload)),
  deleteItem: async (id: number) => getData<void>(await api.delete(`/api/admin/v1/menus/items/${id}`)),
}
