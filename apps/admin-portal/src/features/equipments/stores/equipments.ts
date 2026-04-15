import { defineStore } from 'pinia'
import { ref } from 'vue'
import { equipmentsApi } from '@/features/equipments/api/equipmentsApi'
import type { Category, CategoryPayload, EquipmentEntity, EquipmentPayload, PaginatedResponse } from '@/types'

export const useEquipmentsStore = defineStore('equipments', () => {
  const items = ref<EquipmentEntity[]>([])
  const categories = ref<Category[]>([])
  const meta = ref({ page: 1, size: 20, total: 0, totalPages: 0 })
  const loading = ref(false)

  async function fetchList(page = 1, size = 20) {
    loading.value = true
    try {
      const result: PaginatedResponse<EquipmentEntity> = await equipmentsApi.list({ page, size })
      items.value = result.data
      meta.value = result.meta
    } finally {
      loading.value = false
    }
  }

  async function create(payload: EquipmentPayload) { return await equipmentsApi.create(payload) }
  async function update(id: number, payload: EquipmentPayload) { return await equipmentsApi.update(id, payload) }
  async function remove(id: number) { await equipmentsApi.delete(id) }
  async function getById(id: number) { return await equipmentsApi.getById(id) }
  async function fetchCategories() { categories.value = await equipmentsApi.categories() }
  async function createCategory(payload: CategoryPayload) { const c = await equipmentsApi.createCategory(payload); await fetchCategories(); return c }
  async function updateCategory(id: number, payload: CategoryPayload) { const c = await equipmentsApi.updateCategory(id, payload); await fetchCategories(); return c }
  async function removeCategory(id: number) { await equipmentsApi.deleteCategory(id); await fetchCategories() }

  return { items, categories, meta, loading, fetchList, create, update, remove, getById, fetchCategories, createCategory, updateCategory, removeCategory }
})
