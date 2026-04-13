import { ref } from 'vue'
import { mediaApi } from '@/lib/api'

export function useImageUpload(onUploaded: (storagePath: string) => void) {
  const uploading = ref(false)
  const fileInputRef = ref<HTMLInputElement | null>(null)

  function openPicker() {
    fileInputRef.value?.click()
  }

  async function handleFileSelected(event: Event) {
    const input = event.target as HTMLInputElement | null
    const file = input?.files?.[0]
    if (!file) return
    uploading.value = true
    try {
      const media = await mediaApi.upload(file, file.name)
      onUploaded(media.storagePath)
    } finally {
      uploading.value = false
      if (input) input.value = ''
    }
  }

  return { uploading, fileInputRef, openPicker, handleFileSelected }
}
