<template>
  <PageShell>
    <PageHeader :title="isEdit ? 'Chỉnh sửa dịch vụ' : 'Tạo dịch vụ mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <DataCard class="p-6">
      <form class="space-y-5" @submit.prevent="handleSubmit">
      <FormField>
        <FormLabel>Tên dịch vụ <span class="text-rose-600">*</span></FormLabel>
        <Input v-model="form.title" placeholder="Nhập tên dịch vụ" required @input="onTitleChange" />
      </FormField>

      <FormField>
        <FormLabel>Icon / URL</FormLabel>
        <Input v-model="form.icon" placeholder="Đường dẫn icon hoặc URL" />
      </FormField>

      <FormField>
        <FormLabel>Ảnh bìa</FormLabel>
        <div class="flex items-start gap-2">
          <Input v-model="form.coverImagePath" placeholder="Đường dẫn ảnh bìa" />
          <Button type="button" variant="secondary" class="flex-shrink-0" :disabled="coverUpload.uploading.value" @click="coverUpload.openPicker()">
            {{ coverUpload.uploading.value ? `${coverUpload.progress.value || 0}%` : 'Chọn ảnh' }}
          </Button>
        </div>
        <input :ref="assignCoverInput" type="file" accept="image/*" class="hidden" @change="coverUpload.handleFileSelected" />
        <img v-if="form.coverImagePath" :src="mediaUrl(form.coverImagePath)" width="320" height="160" loading="lazy" decoding="async" class="mt-2 h-40 w-full max-w-md rounded border border-gray-200 object-contain" />
      </FormField>

      <FormField>
        <div class="mb-3 flex items-center justify-between gap-3">
          <FormLabel>Thư viện ảnh</FormLabel>
          <Button type="button" variant="secondary" class="flex-shrink-0" :disabled="galleryUpload.uploading.value" @click="galleryUpload.openPicker()">
            {{ galleryUpload.uploading.value ? `Đang tải ${galleryUpload.progress.value || 0}%` : '+ Thêm ảnh' }}
          </Button>
        </div>
        <input :ref="assignGalleryInput" type="file" accept="image/*" multiple class="hidden" @change="handleGalleryUpload" />
        <div v-if="form.images.length" class="mt-3 grid gap-3 md:grid-cols-2 xl:grid-cols-3">
          <div v-for="(image, index) in form.images" :key="`${image.imagePath}-${index}`" class="rounded-lg border border-gray-200 bg-white p-3">
            <div class="overflow-hidden rounded-lg border border-gray-100">
              <img :src="mediaUrl(image.imagePath)" width="320" height="144" loading="lazy" decoding="async" class="h-36 w-full object-cover" />
            </div>
            <div class="mt-3 flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Ảnh {{ index + 1 }}</div>
              <div class="flex gap-2">
                <ActionButton :disabled="index === 0" @click="moveImage(index, -1)">↑</ActionButton>
                <ActionButton :disabled="index === form.images.length - 1" @click="moveImage(index, 1)">↓</ActionButton>
                <ActionButton variant="destructive" @click="removeImage(index)"><Trash2 class="h-4 w-4" /></ActionButton>
              </div>
            </div>
            <div class="mt-3 space-y-3">
              <FormField>
                <FormLabel>Alt text</FormLabel>
                <Input v-model="image.altText" placeholder="Mô tả ngắn cho ảnh" />
              </FormField>
              <FormField>
                <FormLabel>Chú thích</FormLabel>
                <Input v-model="image.caption" placeholder="Chú thích hiển thị nếu cần" />
              </FormField>
            </div>
          </div>
        </div>
        <div v-else class="mt-3 rounded-lg border border-dashed border-gray-300 bg-gray-50 px-4 py-6 text-center text-sm text-slate-500">
          Chưa có ảnh trong thư viện dịch vụ này.
        </div>
      </FormField>

      <FormField>
        <FormLabel>Tổng quan</FormLabel>
        <Textarea v-model="form.overview" placeholder="Mô tả tổng quan" />
      </FormField>

      <FormField>
        <div class="mb-3 flex items-center justify-between gap-3">
          <FormLabel>Tài liệu PDF</FormLabel>
          <Button type="button" variant="secondary" @click="addDocument">+ Thêm PDF</Button>
        </div>

        <div v-if="form.documents.length" class="space-y-3">
          <div v-for="(item, index) in form.documents" :key="`service-document-${index}`" class="rounded-lg border border-gray-200 p-4">
            <div class="flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Tài liệu {{ index + 1 }}</div>
              <div class="flex gap-2">
                <ActionButton :disabled="index === 0" @click="moveDocument(index, -1)">↑</ActionButton>
                <ActionButton :disabled="index === form.documents.length - 1" @click="moveDocument(index, 1)">↓</ActionButton>
                <ActionButton variant="destructive" @click="removeDocument(index)"><Trash2 class="h-4 w-4" /></ActionButton>
              </div>
            </div>

            <div class="mt-3 grid gap-4 md:grid-cols-2">
              <FormField>
                <FormLabel>Tên tài liệu</FormLabel>
                <Input v-model="item.title" placeholder="Ví dụ: Hồ sơ năng lực khảo sát" />
              </FormField>
              <FormField>
                <FormLabel>Thứ tự</FormLabel>
                <Input :model-value="item.sortOrder + 1" disabled />
              </FormField>
            </div>

            <FormField>
              <FormLabel>File PDF</FormLabel>
              <div class="relative">
                <Input v-model="item.filePath" class="pr-56" placeholder="uploads/documents/xxxx.pdf" />
                <div class="absolute right-1 top-1 flex gap-2">
                  <a v-if="item.filePath" :href="mediaUrl(item.filePath)" target="_blank" rel="noopener noreferrer" class="input-mini-action-btn">Xem</a>
                  <button type="button" class="input-mini-action-btn" :disabled="documentUploadBusy && activeDocumentInputIndex === index" @click="openDocumentPicker(index)">
                    {{ documentUploadBusy && activeDocumentInputIndex === index ? `Đang tải ${documentUploadProgress}%` : 'Chọn PDF' }}
                  </button>
                </div>
              </div>
            </FormField>
          </div>
        </div>

        <div v-else class="rounded-lg border border-dashed border-gray-300 bg-gray-50 px-4 py-6 text-center text-sm text-slate-500">
          Chưa có tài liệu PDF cho dịch vụ này.
        </div>

        <input ref="documentPdfInputRef" type="file" accept="application/pdf,.pdf" class="hidden" @change="handleDocumentSelected" />
      </FormField>

      <FormField>
        <div class="mb-2 flex items-center justify-between gap-3">
          <FormLabel>Nội dung</FormLabel>
          <Button type="button" variant="secondary" :disabled="contentImageUpload.uploading.value" @click="contentImageUpload.openPicker()">
            {{ contentImageUpload.uploading.value ? `Đang tải ${contentImageUpload.progress.value || 0}%` : 'Tải ảnh vào nội dung' }}
          </Button>
        </div>
        <div class="flex items-center gap-2">
          <button type="button" class="editor-tab-btn" :class="{ active: contentEditorMode === 'visual' }" @click="contentEditorMode = 'visual'">Soạn thảo</button>
          <button type="button" class="editor-tab-btn" :class="{ active: contentEditorMode === 'html' }" @click="contentEditorMode = 'html'">HTML</button>
        </div>
        <div class="intro-quill-wrap mt-2 overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm">
          <QuillEditor
            v-if="contentEditorMode === 'visual'"
            ref="contentQuillRef"
            v-model:content="form.content"
            content-type="html"
            theme="snow"
            :toolbar="contentQuillToolbar"
            placeholder="Nhập nội dung chi tiết..."
            class="intro-quill-editor"
            @focus="isUserTyping = true"
            @blur="isUserTyping = false"
          />
          <div v-else class="p-4">
            <Textarea
              v-model="form.content"
              class="font-mono text-sm"
              rows="14"
              placeholder="<p>Nhập nội dung chi tiết...</p>"
              @focus="isUserTyping = true"
              @blur="isUserTyping = false"
            />
          </div>
        </div>
        <div class="mt-3 rounded-xl border border-slate-200 bg-slate-50 p-4">
          <div class="text-xs font-medium uppercase tracking-[0.18em] text-slate-500">Xem trước</div>
          <div class="prose prose-slate mt-3 max-w-none text-sm" v-html="form.content"></div>
        </div>
        <input :ref="assignContentImageInput" type="file" accept="image/*" class="hidden" @change="handleContentImageSelected" />
      </FormField>

      <FormField class="max-w-[240px]">
        <FormLabel>Trạng thái</FormLabel>
        <Select v-model="form.status">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </Select>
      </FormField>

      <div class="mt-4 flex gap-3 border-t border-gray-100 pt-4">
        <Button type="submit" :disabled="saving">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</Button>
        <Button as="router-link" to="/services" variant="secondary">Quay lại</Button>
      </div>
      </form>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { computed, defineAsyncComponent, onMounted, reactive, ref, watch } from 'vue'
import { Trash2 } from 'lucide-vue-next'
import { useRoute, useRouter } from 'vue-router'
import ActionButton from '@/components/shared/ActionButton.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { useImageUpload } from '@/composables/useImageUpload'
import { mediaApi } from '@/features/media/api/mediaApi'
import { resolveMediaUrl } from '@/lib/media'
import { useToastsStore } from '@/stores/toasts'
import { useServicesStore } from '@/stores/services'
import type { ServiceDocumentPayload, ServiceEntity, ServiceImageEntity, ServiceImagePayload, ServicePayload } from '@/types'
import type { ApiErrorLike, QuillEditorComponent } from '@/types/api'
import { extractApiError, validateSelectedFile } from '@/utils/files'
import { toSlug } from '@/utils/slug'

const route = useRoute()
const router = useRouter()
const store = useServicesStore()
const QuillEditor = defineAsyncComponent(async () => {
  await import('@vueup/vue-quill/dist/vue-quill.snow.css')
  return (await import('@vueup/vue-quill')).QuillEditor
})
const saving = ref(false)
const error = ref('')
const slugManuallyEdited = ref(false)
const toasts = useToastsStore()
const isEdit = computed(() => !!route.params.id)
const form = reactive<ServicePayload>({
  title: '',
  slug: '',
  overview: '',
  content: '',
  icon: '',
  coverImagePath: '',
  galleryJson: '',
  documents: [],
  images: [],
  status: 'DRAFT',
})

const coverUpload = useImageUpload((path) => { form.coverImagePath = path }, { successMessage: 'Đã tải ảnh bìa' })
const galleryUpload = useImageUpload(() => {}, { successMessage: 'Đã tải ảnh thư viện' })
const contentEditorMode = ref<'visual' | 'html'>('visual')
const contentQuillRef = ref<QuillEditorComponent | null>(null)
const contentImageUpload = useImageUpload(() => {}, { successMessage: 'Đã tải ảnh nội dung' })
const isUserTyping = ref(false)
const documentPdfInputRef = ref<HTMLInputElement | null>(null)
const activeDocumentInputIndex = ref<number | null>(null)
const documentUploadBusy = ref(false)
const documentUploadProgress = ref(0)
function createDocument(): ServiceDocumentPayload {
  return {
    title: '',
    filePath: '',
    sortOrder: form.documents.length,
  }
}

function createImage(path = ''): ServiceImagePayload {
  return {
    imagePath: path,
    altText: '',
    caption: '',
    sortOrder: form.images.length,
  }
}

function assignCoverInput(el: unknown) {
  coverUpload.fileInputRef.value = el as HTMLInputElement | null
}

function assignGalleryInput(el: unknown) {
  galleryUpload.fileInputRef.value = el as HTMLInputElement | null
}

function assignContentImageInput(el: unknown) {
  contentImageUpload.fileInputRef.value = el as HTMLInputElement | null
}

function normalizeDocumentOrders() {
  form.documents.forEach((item, index) => {
    item.sortOrder = index
  })
}

function addDocument() {
  form.documents.push(createDocument())
  normalizeDocumentOrders()
}

function removeDocument(index: number) {
  form.documents.splice(index, 1)
  normalizeDocumentOrders()
}

function moveDocument(index: number, direction: -1 | 1) {
  const targetIndex = index + direction
  if (targetIndex < 0 || targetIndex >= form.documents.length) return
  const [item] = form.documents.splice(index, 1)
  form.documents.splice(targetIndex, 0, item)
  normalizeDocumentOrders()
}

function openDocumentPicker(index: number) {
  activeDocumentInputIndex.value = index
  documentPdfInputRef.value?.click()
}

function normalizeImageOrders() {
  form.images.forEach((item, index) => {
    item.sortOrder = index
  })
  form.galleryJson = form.images.map((item) => item.imagePath.trim()).filter(Boolean).join('\n')
}

function removeImage(index: number) {
  form.images.splice(index, 1)
  normalizeImageOrders()
}

function moveImage(index: number, direction: -1 | 1) {
  const targetIndex = index + direction
  if (targetIndex < 0 || targetIndex >= form.images.length) return
  const [item] = form.images.splice(index, 1)
  form.images.splice(targetIndex, 0, item)
  normalizeImageOrders()
}

function detectContentType(value: string): 'html' | 'text' {
  if (!value) return 'text'
  const trimmed = value.trim()
  if (trimmed.startsWith('<') && /<[a-z][^>]*>/i.test(trimmed)) return 'html'
  return 'text'
}

watch(() => form.content, (newVal) => {
  if (isUserTyping.value) return
  if (!newVal) return
  const detected = detectContentType(newVal)
  if (contentEditorMode.value === 'visual' && detected === 'html' && !newVal.includes('ql-editor')) {
    contentEditorMode.value = 'html'
  } else if (contentEditorMode.value === 'html' && detected === 'text') {
    contentEditorMode.value = 'visual'
  }
}, { immediate: true })

function createQuillToolbar(imageHandler: () => void) {
  return {
    container: [
      [{ header: [1, 2, 3, false] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
      [{ align: [] }],
      ['blockquote', 'link', 'image'],
      ['clean'],
    ],
    handlers: { image: imageHandler },
  }
}

const contentQuillToolbar = createQuillToolbar(() => contentImageUpload.openPicker())

function insertImageToContent(url: string) {
  const quill = contentQuillRef.value?.getQuill?.()
  if (!quill) return
  const selection = quill.getSelection(true)
  const index = selection?.index ?? quill.getLength()
  quill.insertEmbed(index, 'image', url, 'user')
  quill.setSelection(index + 1, 0, 'user')
  form.content = quill.root.innerHTML || ''
}

async function handleContentImageSelected(event: Event) {
  const input = event.target as HTMLInputElement | null
  const file = input?.files?.[0]
  if (!file) return
  const validationError = validateSelectedFile(file, 'image')
  if (validationError) {
    error.value = validationError
    toasts.show(validationError, 'error')
    if (input) input.value = ''
    return
  }
  contentImageUpload.uploading.value = true
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name)
    insertImageToContent((() => {
      const p = media.storagePath
      if (/^https?:\/\//i.test(p)) return p
      const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
      return `${baseUrl}/${p.replace(/^\/+/, '')}`
    })())
    toasts.show(`Đã tải ảnh: ${file.name}`, 'success')
  } catch (e: unknown) {
    error.value = extractApiError(e, 'Tải ảnh thất bại')
    toasts.show(error.value, 'error')
  } finally {
    contentImageUpload.uploading.value = false
    if (input) input.value = ''
  }
}

async function handleDocumentSelected(event: Event) {
  const input = event.target as HTMLInputElement | null
  const file = input?.files?.[0]
  const targetIndex = activeDocumentInputIndex.value
  if (!file || targetIndex == null || !form.documents[targetIndex]) {
    activeDocumentInputIndex.value = null
    if (input) input.value = ''
    return
  }

  const validationError = validateSelectedFile(file, 'pdf')
  if (validationError) {
    error.value = validationError
    toasts.show(validationError, 'error')
    activeDocumentInputIndex.value = null
    if (input) input.value = ''
    return
  }

  documentUploadBusy.value = true
  documentUploadProgress.value = 0
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name, {
      onUploadProgress(progressEvent) {
        if (!progressEvent.total) return
        documentUploadProgress.value = Math.max(1, Math.round((progressEvent.loaded / progressEvent.total) * 100))
      },
    })
    form.documents[targetIndex].filePath = media.storagePath
    if (!form.documents[targetIndex].title.trim()) {
      form.documents[targetIndex].title = file.name.replace(/\.pdf$/i, '')
    }
    toasts.show(`Đã tải PDF: ${file.name}`, 'success')
  } catch (e: unknown) {
    error.value = extractApiError(e, 'Tải PDF thất bại')
    toasts.show(error.value, 'error')
  } finally {
    documentUploadBusy.value = false
    window.setTimeout(() => { documentUploadProgress.value = 0 }, 400)
    activeDocumentInputIndex.value = null
    if (input) input.value = ''
  }
}

async function handleGalleryUpload(event: Event) {
  const input = event.target as HTMLInputElement | null
  const files = input?.files
  if (!files?.length) return

  galleryUpload.uploading.value = true
  error.value = ''
  try {
    for (const file of Array.from(files)) {
      const validationError = validateSelectedFile(file, 'image')
      if (validationError) throw new Error(validationError)
      const media = await mediaApi.upload(file, file.name)
      form.images.push({
        ...createImage(media.storagePath),
        altText: file.name.replace(/\.[^.]+$/i, ''),
      })
    }
    normalizeImageOrders()
    toasts.show(`Đã tải ${files.length} ảnh`, 'success')
  } catch (e: unknown) {
    error.value = extractApiError(e, 'Tải ảnh thư viện thất bại')
    toasts.show(error.value, 'error')
  } finally {
    galleryUpload.uploading.value = false
    if (input) input.value = ''
  }
}

function mediaUrl(path: string) {
  return resolveMediaUrl(path)
}

function generateSlug() {
  form.slug = toSlug(form.title)
  slugManuallyEdited.value = false
}

function onTitleChange() {
  if (!slugManuallyEdited.value && !isEdit.value) {
    form.slug = toSlug(form.title)
  }
}

function buildPayload(): ServicePayload {
  return {
    title: form.title.trim(),
    slug: (form.slug || toSlug(form.title)).trim(),
    overview: form.overview?.trim() || '',
    content: form.content || '',
    icon: form.icon?.trim() || '',
    coverImagePath: form.coverImagePath?.trim() || '',
    galleryJson: form.images.map((item) => item.imagePath.trim()).filter(Boolean).join('\n'),
    documents: form.documents
      .map((item, index) => ({
        title: item.title.trim(),
        filePath: item.filePath.trim(),
        sortOrder: index,
      }))
      .filter((item) => item.title && item.filePath),
    images: form.images
      .map((item, index) => ({
        imagePath: item.imagePath.trim(),
        altText: item.altText?.trim() || '',
        caption: item.caption?.trim() || '',
        sortOrder: index,
      }))
      .filter((item) => item.imagePath),
    status: form.status,
  }
}

function extractErrorMessage(e: unknown) {
  const fieldErrors = (e as ApiErrorLike).response?.data?.error?.details
  if (Array.isArray(fieldErrors) && fieldErrors.length) {
    return fieldErrors.map((item: { field?: string, message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  }
  return extractApiError(e)
}

function parseGalleryJson(value: string | null | undefined): ServiceImagePayload[] {
  if (!value?.trim()) return []
  return value.split('\n')
    .map((item) => item.trim())
    .filter(Boolean)
    .map((path, index) => ({
      imagePath: path,
      altText: '',
      caption: '',
      sortOrder: index,
    }))
}

function resolveServiceImages(entity: ServiceEntity): ServiceImagePayload[] {
  const imageRows = Array.isArray(entity.images) ? entity.images : []
  const images = imageRows
    .map((item: ServiceImageEntity, index: number) => ({
      imagePath: item.imagePath || '',
      altText: item.altText || '',
      caption: item.caption || '',
      sortOrder: typeof item.sortOrder === 'number' ? item.sortOrder : index,
    }))
    .filter((item: ServiceImagePayload) => item.imagePath)

  if (images.length) return images
  return parseGalleryJson(entity.galleryJson)
}

onMounted(async () => {
  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
    Object.assign(form, entity, {
      galleryJson: entity.galleryJson || '',
      documents: (entity.documents || []).map((item, index) => ({
        title: item.title || '',
        filePath: item.filePath || '',
        sortOrder: typeof item.sortOrder === 'number' ? item.sortOrder : index,
      })),
      images: resolveServiceImages(entity),
    })
    normalizeDocumentOrders()
    normalizeImageOrders()
    slugManuallyEdited.value = true
  }
})

async function handleSubmit() {
  const payload = buildPayload()

  if (!payload.title) {
    error.value = 'Tên dịch vụ là bắt buộc'
    toasts.show(error.value, 'error')
    return
  }

  if (!payload.slug) {
    error.value = 'Slug không hợp lệ. Vui lòng nhập tên dịch vụ hoặc slug.'
    toasts.show(error.value, 'error')
    return
  }

  form.title = payload.title
  form.slug = payload.slug
  form.overview = payload.overview
  form.icon = payload.icon
  form.coverImagePath = payload.coverImagePath || ''
  form.galleryJson = payload.galleryJson
  form.documents = payload.documents.map((item) => ({ ...item }))
  form.images = payload.images.map((item) => ({ ...item }))

  saving.value = true
  error.value = ''
  try {
    if (isEdit.value) await store.update(Number(route.params.id), payload)
    else await store.create(payload)
    toasts.show(isEdit.value ? 'Cập nhật dịch vụ thành công' : 'Tạo dịch vụ thành công', 'success')
    router.push('/services')
  } catch (e: unknown) {
    error.value = extractErrorMessage(e)
    toasts.show(error.value, 'error')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.editor-tab-btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #475569;
  border-radius: 999px;
  padding: 0.45rem 0.9rem;
  font-size: 0.82rem;
  font-weight: 600;
}

.editor-tab-btn.active {
  background: #0f172a;
  border-color: #0f172a;
  color: #fff;
}
</style>
