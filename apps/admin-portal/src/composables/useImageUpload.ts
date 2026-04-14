import { ref } from 'vue'
import { mediaApi } from '@/lib/api'
import { useToastsStore } from '@/stores/toasts'
import { extractApiError, validateSelectedFile } from '@/utils/files'

export function useImageUpload(onUploaded: (storagePath: string) => void, options?: { successMessage?: string }) {
  const uploading = ref(false)
  const progress = ref(0)
  const fileInputRef = ref<HTMLInputElement | null>(null)
  const toasts = useToastsStore()

  function openPicker() {
    fileInputRef.value?.click()
  }

  async function handleFileSelected(event: Event) {
    const input = event.target as HTMLInputElement | null
    const file = input?.files?.[0]
    if (!file) return

    const validationError = validateSelectedFile(file, 'image')
    if (validationError) {
      toasts.show(validationError, 'error')
      if (input) input.value = ''
      return
    }

    uploading.value = true
    progress.value = 0
    try {
      const media = await mediaApi.upload(file, file.name, {
        onUploadProgress(progressEvent) {
          if (!progressEvent.total) return
          progress.value = Math.max(1, Math.round((progressEvent.loaded / progressEvent.total) * 100))
        },
      })
      onUploaded(media.storagePath)
      progress.value = 100
      toasts.show(options?.successMessage || `Đã tải ảnh: ${file.name}`, 'success')
    } catch (error: any) {
      toasts.show(extractApiError(error, 'Tải ảnh thất bại'), 'error')
    } finally {
      uploading.value = false
      window.setTimeout(() => { progress.value = 0 }, 400)
      if (input) input.value = ''
    }
  }

  return { uploading, progress, fileInputRef, openPicker, handleFileSelected }
}
