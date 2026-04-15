import { defineStore } from 'pinia'
import { ref } from 'vue'
import { servicesApi } from '@/features/services/api/servicesApi'
import type { ServiceEntity, ServicePayload, PaginatedResponse } from '@/types'

export const useServicesStore = defineStore('services', () => {
  const items = ref<ServiceEntity[]>([])
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

  async function create(payload: ServicePayload) { return await servicesApi.create(payload) }
  async function update(id: number, payload: ServicePayload) { return await servicesApi.update(id, payload) }
  async function remove(id: number) { await servicesApi.delete(id) }

  async function getById(id: number) { return await servicesApi.getById(id) }

  return { items, meta, loading, fetchList, create, update, remove, getById }
})
