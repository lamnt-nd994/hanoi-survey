import { api, unwrap, unwrapResponse } from './api-client'
import type {
  ContactPayload,
  Equipment,
  EquipmentCategory,
  Post,
  PostCategory,
  PublicMenuItem,
  Project,
  ProjectCategory,
  SitePage,
  SiteSettings,
  SurveyService,
} from '../types/content'

export async function fetchSiteSettings(): Promise<SiteSettings> {
  return unwrap(await api.get('/api/public/v1/site-settings'))
}

export async function fetchPageBySlug(slug: string): Promise<SitePage> {
  return unwrap(await api.get(`/api/public/v1/pages/${slug}`))
}

export async function fetchPublicMenu(code: string): Promise<PublicMenuItem[]> {
  return unwrap(await api.get(`/api/public/v1/menus/${code}`))
}

export async function fetchServices(params: Record<string, unknown> = {}): Promise<SurveyService[]> {
  return unwrap(await api.get('/api/public/v1/services', { params }))
}

export async function fetchServiceDetail(slug: string): Promise<SurveyService> {
  return unwrap(await api.get(`/api/public/v1/services/${slug}`))
}

export async function fetchProjects(params: Record<string, unknown> = {}): Promise<Project[]> {
  return unwrap(await api.get('/api/public/v1/projects', { params }))
}

export async function fetchProjectDetail(slug: string): Promise<Project> {
  return unwrap(await api.get(`/api/public/v1/projects/${slug}`))
}

export async function fetchProjectCategories(): Promise<ProjectCategory[]> {
  return unwrap(await api.get('/api/public/v1/projects/categories'))
}

export interface PaginationMeta {
  page: number
  size: number
  total: number
  totalPages: number
}

export async function fetchPosts(params: Record<string, unknown> = {}): Promise<{ data: Post[]; meta: PaginationMeta }> {
  const res = unwrapResponse<Post[]>(await api.get('/api/public/v1/posts', { params }))
  return {
    data: res.data || [],
    meta: {
      page: Number(res.meta?.page || 1),
      size: Number(res.meta?.size || 10),
      total: Number(res.meta?.total || 0),
      totalPages: Number(res.meta?.totalPages || 1),
    },
  }
}

export async function fetchPostDetail(slug: string): Promise<Post> {
  return unwrap(await api.get(`/api/public/v1/posts/${slug}`))
}

export async function fetchPostCategories(): Promise<PostCategory[]> {
  return unwrap(await api.get('/api/public/v1/posts/categories'))
}

export async function fetchEquipments(params: Record<string, unknown> = {}): Promise<Equipment[]> {
  return unwrap(await api.get('/api/public/v1/equipments', { params }))
}

export async function fetchEquipmentDetail(slug: string): Promise<Equipment> {
  return unwrap(await api.get(`/api/public/v1/equipments/${slug}`))
}

export async function fetchEquipmentCategories(): Promise<EquipmentCategory[]> {
  return unwrap(await api.get('/api/public/v1/equipments/categories'))
}

export async function createContact(payload: ContactPayload): Promise<unknown> {
  return unwrap(await api.post('/api/public/v1/contacts', payload))
}
