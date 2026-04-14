import { api, getData } from '@/lib/api-client'
import type { MediaEntity } from '@/types'
import type { UploadProgressEvent } from '@/types/api'

export const mediaApi = {
  list: async () => getData<MediaEntity[]>(await api.get('/api/admin/v1/media')),
  upload: async (file: File, altText = '', options?: { timeout?: number; onUploadProgress?: (progressEvent: UploadProgressEvent) => void }) => {
    const formData = new FormData()
    formData.append('file', file)
    const params: Record<string, string> = {}
    if (altText) params.altText = altText
    return getData<MediaEntity>(await api.post('/api/admin/v1/media/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      params,
      timeout: options?.timeout ?? 300000,
      onUploadProgress: options?.onUploadProgress,
    }))
  },
  delete: async (id: number) => getData<void>(await api.delete(`/api/admin/v1/media/${id}`)),
}
