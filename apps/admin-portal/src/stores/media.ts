import { defineStore } from 'pinia'
import { ref } from 'vue'
import { mediaApi } from '@/lib/api'
import type { MediaEntity } from '@/types'

export const useMediaStore = defineStore('media', () => {
  const items = ref<MediaEntity[]>([])
  const loading = ref(false)

  async function fetchList() {
    loading.value = true
    try {
      items.value = await mediaApi.list()
    } finally {
      loading.value = false
    }
  }

  async function upload(file: File, altText = '') {
    return await mediaApi.upload(file, altText)
  }

  async function remove(id: number) {
    await mediaApi.delete(id)
  }

  return { items, loading, fetchList, upload, remove }
})
