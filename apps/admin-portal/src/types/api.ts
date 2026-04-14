import type { AxiosProgressEvent } from 'axios'

export type UploadProgressEvent = AxiosProgressEvent

export interface ApiErrorDetail {
  field?: string
  message?: string
}

export interface ApiErrorPayload {
  error?: {
    message?: string
    details?: ApiErrorDetail[]
  }
}

export interface ApiErrorLike {
  message?: string
  response?: {
    data?: ApiErrorPayload
  }
}

export interface QuillLike {
  getSelection(force?: boolean): { index: number; length: number } | null
  getLength(): number
  insertEmbed(index: number, type: string, value: string, source?: string): void
  setSelection(index: number, length: number, source?: string): void
  root: HTMLElement
}

export interface QuillEditorComponent {
  getQuill?: () => QuillLike
}

export type QuillToolbar = string | unknown[] | Record<string, unknown>
