import { api, getData } from '@/lib/api-client'
import type { SiteSettings } from '@/types'

export const settingsApi = {
  get: async () => getData<SiteSettings>(await api.get('/api/admin/v1/site-settings')),
  update: async (payload: SiteSettings) => getData<SiteSettings>(await api.put('/api/admin/v1/site-settings', payload)),
}
