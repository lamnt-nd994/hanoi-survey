const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
const MEDIA_BASE_URL = (import.meta.env.VITE_MEDIA_BASE_URL || '').replace(/\/$/, '')

export function resolveMediaUrl(path?: string | null): string {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path

  const normalizedPath = path.replace(/^\//, '')

  if (MEDIA_BASE_URL) {
    return `${MEDIA_BASE_URL}/${normalizedPath}`
  }

  if (typeof window !== 'undefined' && window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
    return `${window.location.origin}/${normalizedPath}`
  }

  return `${API_BASE_URL}/${normalizedPath}`
}
