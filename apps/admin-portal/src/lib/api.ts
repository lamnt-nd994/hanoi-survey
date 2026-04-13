import axios from 'axios'
import router from '@/router'
import type {
  PaginatedResponse,
  PageEntity, PagePayload,
  PostEntity, PostPayload, Category, CategoryPayload,
  ServiceEntity, ServicePayload,
  ProjectEntity, ProjectPayload,
  EquipmentEntity, EquipmentPayload,
  MenuEntity, MenuPayload, MenuItemEntity, MenuItemPayload,
  ContactEntity,
  MediaEntity, SiteSettings,
  AuthUser, LoginResponse,
  AdminUserEntity, AdminUserPayload,
  AdminRoleEntity, AdminRolePayload,
} from '@/types'

const TOKEN_KEY = 'hanoi-survey-admin-token'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 15000,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response?.status === 401 && router.currentRoute.value.path !== '/login') {
      clearStoredToken()
      router.push('/login')
    }
    return Promise.reject(error)
  },
)

export function getStoredToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setStoredToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearStoredToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

function getData<T>(res: { data: { data: T } }): T {
  return res.data.data
}

function getPaging<T>(res: { data: { data: T[]; meta: any } }): PaginatedResponse<T> {
  return {
    data: res.data.data || [],
    meta: res.data.meta || { page: 1, size: 10, total: 0, totalPages: 0 },
  }
}

export const authApi = {
  login: async (username: string, password: string) =>
    getData<LoginResponse>(await api.post('/api/auth/v1/login', { username, password })),
  me: async () =>
    getData<AuthUser>(await api.get('/api/auth/v1/me')),
}

export const usersApi = {
  list: async (params: { page?: number; size?: number; keyword?: string; roleId?: number | null; active?: boolean | null } = {}) =>
    getPaging<AdminUserEntity>(await api.get('/api/admin/v1/users', { params: { page: params.page || 1, size: params.size || 20, ...params } })),
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

export const pagesApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<PageEntity>(await api.get('/api/admin/v1/pages', { params })),
  create: async (payload: PagePayload) =>
    getData<PageEntity>(await api.post('/api/admin/v1/pages', payload)),
  update: async (id: number, payload: PagePayload) =>
    getData<PageEntity>(await api.put(`/api/admin/v1/pages/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/pages/${id}`)),
  getById: async (id: number) =>
    getData<PageEntity>(await api.get(`/api/admin/v1/pages/${id}`)),
}

export const postsApi = {
  list: async (params: { page?: number; size?: number; keyword?: string; categoryId?: number | null } = {}) =>
    getPaging<PostEntity>(await api.get('/api/admin/v1/posts', { params: { page: params.page || 1, size: params.size || 20, ...params } })),
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

export const servicesApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<ServiceEntity>(await api.get('/api/admin/v1/services', { params })),
  create: async (payload: ServicePayload) =>
    getData<ServiceEntity>(await api.post('/api/admin/v1/services', payload)),
  update: async (id: number, payload: ServicePayload) =>
    getData<ServiceEntity>(await api.put(`/api/admin/v1/services/${id}`, payload)),
  delete: async (id: number) =>
    getData<void>(await api.delete(`/api/admin/v1/services/${id}`)),
  getById: async (id: number) =>
    getData<ServiceEntity>(await api.get(`/api/admin/v1/services/${id}`)),
}

export const projectsApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<ProjectEntity>(await api.get('/api/admin/v1/projects', { params })),
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

export const equipmentsApi = {
  list: async (params = { page: 1, size: 20 }) =>
    getPaging<EquipmentEntity>(await api.get('/api/admin/v1/equipments', { params })),
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

export const menusApi = {
  list: async () => getData<MenuEntity[]>(await api.get('/api/admin/v1/menus')),
  create: async (payload: MenuPayload) => getData<MenuEntity>(await api.post('/api/admin/v1/menus', payload)),
  update: async (id: number, payload: MenuPayload) => getData<MenuEntity>(await api.put(`/api/admin/v1/menus/${id}`, payload)),
  items: async (code: string) => getData<MenuItemEntity[]>(await api.get(`/api/admin/v1/menus/${code}/items`)),
  createItem: async (payload: MenuItemPayload) => getData<MenuItemEntity>(await api.post('/api/admin/v1/menus/items', payload)),
  updateItem: async (id: number, payload: MenuItemPayload) => getData<MenuItemEntity>(await api.put(`/api/admin/v1/menus/items/${id}`, payload)),
  deleteItem: async (id: number) => getData<void>(await api.delete(`/api/admin/v1/menus/items/${id}`)),
}

export const leadsApi = {
  contacts: async (params = { page: 1, size: 20 }) =>
    getPaging<ContactEntity>(await api.get('/api/admin/v1/contacts', { params })),
  updateContactStatus: async (id: number, status: string) =>
    getData<void>(await api.put(`/api/admin/v1/contacts/${id}/status`, { status })),
}

export const mediaApi = {
  list: async () => getData<MediaEntity[]>(await api.get('/api/admin/v1/media')),
  upload: async (file: File, altText = '') => {
    const formData = new FormData()
    formData.append('file', file)
    const params: Record<string, string> = {}
    if (altText) params.altText = altText
    return getData<MediaEntity>(await api.post('/api/admin/v1/media/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      params,
    }))
  },
  delete: async (id: number) => getData<void>(await api.delete(`/api/admin/v1/media/${id}`)),
}

export const settingsApi = {
  get: async () => getData<SiteSettings>(await api.get('/api/admin/v1/site-settings')),
  update: async (payload: SiteSettings) => getData<SiteSettings>(await api.put('/api/admin/v1/site-settings', payload)),
}
