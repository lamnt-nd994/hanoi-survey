export const MAX_UPLOAD_SIZE_BYTES = 100 * 1024 * 1024

export type FileKind = 'image' | 'pdf' | 'image-or-pdf'

export function formatFileSize(size: number) {
  if (size >= 1024 * 1024) return `${(size / (1024 * 1024)).toFixed(1)} MB`
  if (size >= 1024) return `${(size / 1024).toFixed(1)} KB`
  return `${size} B`
}

export function validateSelectedFile(file: File, kind: FileKind) {
  if (!file) return 'Chưa chọn file'
  if (file.size > MAX_UPLOAD_SIZE_BYTES) return 'File vượt quá 100MB'

  if (kind === 'image' && !file.type.startsWith('image/')) {
    return 'Chỉ chấp nhận file ảnh'
  }

  if (kind === 'pdf' && file.type !== 'application/pdf' && !file.name.toLowerCase().endsWith('.pdf')) {
    return 'Chỉ chấp nhận file PDF'
  }

  if (kind === 'image-or-pdf') {
    const isImage = file.type.startsWith('image/')
    const isPdf = file.type === 'application/pdf' || file.name.toLowerCase().endsWith('.pdf')
    if (!isImage && !isPdf) return 'Chỉ chấp nhận file ảnh hoặc PDF'
  }

  return ''
}

export function extractApiError(error: any, fallback = 'Thao tác thất bại') {
  const details = error?.response?.data?.error?.details
  if (Array.isArray(details) && details.length) {
    return details.map((item: { field?: string; message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  }

  return error?.response?.data?.error?.message || error?.message || fallback
}
