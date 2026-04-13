import { defineStore } from 'pinia'
import { ref } from 'vue'
import { servicesApi } from '@/lib/api'
import type { ServiceEntity, ServicePayload, Category, CategoryPayload, PaginatedResponse } from '@/types'

export const useServicesStore = defineStore('services', () => {
  const items = ref<ServiceEntity[]>([])
  const categories = ref<Category[]>([])
  const meta = ref({ page: 1, size: 20, total: 0, totalPages: 0 })
  const loading = ref(false)

  async function fetchList(page = 1, size = 20) {
    loading.value = true
    try {
      const result: PaginatedResponse<ServiceEntity> = await servicesApi.list({ page, size })
      items.value = result.data
      meta.value = result.meta
    } finally {
      loading.value = false
    }
  }

  async function fetchCategories() { categories.value = await servicesApi.categories() }
  async function create(payload: ServicePayload) { return await servicesApi.create(payload) }
  async function update(id: number, payload: ServicePayload) { return await servicesApi.update(id, payload) }
  async function remove(id: number) { await servicesApi.delete(id) }
  async function createCategory(payload: CategoryPayload) { const c = await servicesApi.createCategory(payload); await fetchCategories(); return c }
  async function updateCategory(id: number, payload: CategoryPayload) { const c = await servicesApi.updateCategory(id, payload); await fetchCategories(); return c }
  async function removeCategory(id: number) { await servicesApi.deleteCategory(id); await fetchCategories() }

  async function getById(id: number) { return await servicesApi.getById(id) }

  return { items, categories, meta, loading, fetchList, fetchCategories, create, update, remove, getById, createCategory, updateCategory, removeCategory }
})
