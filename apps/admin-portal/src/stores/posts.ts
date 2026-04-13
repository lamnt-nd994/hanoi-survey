import { defineStore } from 'pinia'
import { ref } from 'vue'
import { postsApi } from '@/lib/api'
import type { PostEntity, PostPayload, Category, CategoryPayload, PaginatedResponse } from '@/types'

export const usePostsStore = defineStore('posts', () => {
  const items = ref<PostEntity[]>([])
  const categories = ref<Category[]>([])
  const meta = ref({ page: 1, size: 10, total: 0, totalPages: 0 })
  const loading = ref(false)
  const filters = ref<{ keyword: string; categoryId: number | null }>({ keyword: '', categoryId: null })

  async function fetchList(page?: number) {
    loading.value = true
    try {
      const p = page || meta.value.page
      const result: PaginatedResponse<PostEntity> = await postsApi.list({
        page: p,
        size: meta.value.size,
        keyword: filters.value.keyword || undefined,
        categoryId: filters.value.categoryId || undefined,
      })
      items.value = result.data
      meta.value = result.meta
    } finally {
      loading.value = false
    }
  }

  function setFilters(keyword: string, categoryId: number | null) {
    filters.value = { keyword, categoryId }
  }

  async function fetchCategories() {
    categories.value = await postsApi.categories()
  }

  async function create(payload: PostPayload): Promise<PostEntity> {
    return await postsApi.create(payload)
  }

  async function update(id: number, payload: PostPayload): Promise<PostEntity> {
    return await postsApi.update(id, payload)
  }

  async function remove(id: number): Promise<void> {
    await postsApi.delete(id)
  }

  async function createCategory(payload: CategoryPayload): Promise<Category> {
    const cat = await postsApi.createCategory(payload)
    await fetchCategories()
    return cat
  }

  async function updateCategory(id: number, payload: CategoryPayload): Promise<Category> {
    const cat = await postsApi.updateCategory(id, payload)
    await fetchCategories()
    return cat
  }

  async function removeCategory(id: number): Promise<void> {
    await postsApi.deleteCategory(id)
    await fetchCategories()
  }

  async function getById(id: number): Promise<PostEntity> {
    return await postsApi.getById(id)
  }

  return { items, categories, meta, loading, filters, fetchList, setFilters, fetchCategories, create, update, remove, getById, createCategory, updateCategory, removeCategory }
})
