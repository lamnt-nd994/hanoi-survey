import { defineStore } from 'pinia'
import { ref } from 'vue'
import { projectsApi } from '@/features/projects/api/projectsApi'
import type { ProjectEntity, ProjectPayload, Category, CategoryPayload, PaginatedResponse } from '@/types'

export const useProjectsStore = defineStore('projects', () => {
  const items = ref<ProjectEntity[]>([])
  const categories = ref<Category[]>([])
  const meta = ref({ page: 1, size: 20, total: 0, totalPages: 0 })
  const loading = ref(false)

  async function fetchList(page = 1, size = 20) {
    loading.value = true
    try {
      const result: PaginatedResponse<ProjectEntity> = await projectsApi.list({ page, size })
      items.value = result.data
      meta.value = result.meta
    } finally {
      loading.value = false
    }
  }

  async function fetchCategories() { categories.value = await projectsApi.categories() }
  async function create(payload: ProjectPayload) { return await projectsApi.create(payload) }
  async function update(id: number, payload: ProjectPayload) { return await projectsApi.update(id, payload) }
  async function remove(id: number) { await projectsApi.delete(id) }
  async function createCategory(payload: CategoryPayload) { const c = await projectsApi.createCategory(payload); await fetchCategories(); return c }
  async function updateCategory(id: number, payload: CategoryPayload) { const c = await projectsApi.updateCategory(id, payload); await fetchCategories(); return c }
  async function removeCategory(id: number) { await projectsApi.deleteCategory(id); await fetchCategories() }

  async function getById(id: number) { return await projectsApi.getById(id) }

  return { items, categories, meta, loading, fetchList, fetchCategories, create, update, remove, getById, createCategory, updateCategory, removeCategory }
})
