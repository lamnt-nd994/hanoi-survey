import { defineStore } from 'pinia'
import { ref } from 'vue'
import { pagesApi } from '@/lib/api'
import type { PageEntity, PagePayload, PaginatedResponse } from '@/types'

export const usePagesStore = defineStore('pages', () => {
  const items = ref<PageEntity[]>([])
  const meta = ref({ page: 1, size: 20, total: 0, totalPages: 0 })
  const loading = ref(false)

  async function fetchList(page = 1, size = 20) {
    loading.value = true
    try {
      const result: PaginatedResponse<PageEntity> = await pagesApi.list({ page, size })
      items.value = result.data
      meta.value = result.meta
    } finally {
      loading.value = false
    }
  }

  async function create(payload: PagePayload): Promise<PageEntity> {
    const entity = await pagesApi.create(payload)
    return entity
  }

  async function update(id: number, payload: PagePayload): Promise<PageEntity> {
    const entity = await pagesApi.update(id, payload)
    return entity
  }

  async function remove(id: number): Promise<void> {
    await pagesApi.delete(id)
  }

  async function getById(id: number): Promise<PageEntity> {
    return await pagesApi.getById(id)
  }

  return { items, meta, loading, fetchList, create, update, remove, getById }
})
