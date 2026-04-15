import { defineStore } from 'pinia'
import { ref } from 'vue'
import { settingsApi } from '@/features/settings/api/settingsApi'
import type { SiteSettings } from '@/types'

export const useSettingsStore = defineStore('settings', () => {
  const settings = ref<SiteSettings | null>(null)
  const loading = ref(false)

  async function fetch() {
    loading.value = true
    try {
      settings.value = await settingsApi.get()
    } finally {
      loading.value = false
    }
  }

  async function update(payload: SiteSettings) {
    settings.value = await settingsApi.update(payload)
  }

  return { settings, loading, fetch, update }
})
