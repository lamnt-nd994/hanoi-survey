<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa dịch vụ' : 'Tạo dịch vụ mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <form @submit.prevent="handleSubmit" class="cms-card mt-6 p-6">
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Tên dịch vụ <span class="required">*</span></label>
          <input v-model="form.title" class="cms-form-control" placeholder="Nhập tên dịch vụ" required />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Slug</label>
          <input v-model="form.slug" class="cms-form-control" placeholder="vd: khao-sat-duc-dat" />
        </div>
      </div>

      <div class="cms-form-group">
        <label class="cms-form-label">Icon / URL</label>
        <input v-model="form.icon" class="cms-form-control" placeholder="Đường dẫn icon hoặc URL" />
      </div>

      <div class="cms-form-group">
        <label class="cms-form-label">Ảnh bìa</label>
        <div class="flex items-start gap-2">
          <input v-model="form.coverImagePath" class="cms-form-control" placeholder="Đường dẫn ảnh bìa" />
          <button type="button" class="cms-btn cms-btn-secondary flex-shrink-0" :disabled="coverUpload.uploading.value" @click="coverUpload.openPicker()">
            {{ coverUpload.uploading.value ? '...' : 'Upload' }}
          </button>
        </div>
        <input :ref="(el: any) => { coverUpload.fileInputRef.value = el }" type="file" accept="image/*" class="hidden" @change="coverUpload.handleFileSelected" />
        <img v-if="form.coverImagePath" :src="mediaUrl(form.coverImagePath)" class="mt-2 max-h-40 rounded border border-gray-200 object-contain" />
      </div>

      <div class="cms-form-group">
        <div class="mb-3 flex items-center justify-between gap-3">
          <label class="cms-form-label mb-0">Thư viện ảnh</label>
          <button type="button" class="cms-btn cms-btn-secondary flex-shrink-0" :disabled="galleryUpload.uploading.value" @click="galleryUpload.openPicker()">
            {{ galleryUpload.uploading.value ? 'Đang tải...' : '+ Thêm ảnh' }}
          </button>
        </div>
        <input :ref="(el: any) => { galleryUpload.fileInputRef.value = el }" type="file" accept="image/*" multiple class="hidden" @change="handleGalleryUpload" />
        <div v-if="form.images.length" class="mt-3 grid gap-3 md:grid-cols-2 xl:grid-cols-3">
          <div v-for="(image, index) in form.images" :key="`${image.imagePath}-${index}`" class="rounded-lg border border-gray-200 bg-white p-3">
            <div class="overflow-hidden rounded-lg border border-gray-100">
              <img :src="mediaUrl(image.imagePath)" class="h-36 w-full object-cover" />
            </div>
            <div class="mt-3 flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Ảnh {{ index + 1 }}</div>
              <div class="flex gap-2">
                <button type="button" class="cms-act-btn" :disabled="index === 0" @click="moveImage(index, -1)">&#8593;</button>
                <button type="button" class="cms-act-btn" :disabled="index === form.images.length - 1" @click="moveImage(index, 1)">&#8595;</button>
                <button type="button" class="cms-act-btn delete" @click="removeImage(index)">&#10005;</button>
              </div>
            </div>
            <div class="mt-3 space-y-3">
              <div class="cms-form-group">
                <label class="cms-form-label">Alt text</label>
                <input v-model="image.altText" class="cms-form-control" placeholder="Mô tả ngắn cho ảnh" />
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Chú thích</label>
                <input v-model="image.caption" class="cms-form-control" placeholder="Chú thích hiển thị nếu cần" />
              </div>
            </div>
          </div>
        </div>
        <div v-else class="mt-3 rounded-lg border border-dashed border-gray-300 bg-gray-50 px-4 py-6 text-center text-sm text-slate-500">
          Chưa có ảnh trong thư viện dịch vụ này.
        </div>
      </div>

      <div class="cms-form-group">
        <label class="cms-form-label">Tổng quan</label>
        <textarea v-model="form.overview" class="cms-form-control" placeholder="Mô tả tổng quan" />
      </div>

      <div class="cms-form-group">
        <div class="mb-3 flex items-center justify-between gap-3">
          <label class="cms-form-label mb-0">Tài liệu PDF</label>
          <button type="button" class="cms-btn cms-btn-secondary" @click="addDocument">+ Thêm PDF</button>
        </div>

        <div v-if="form.documents.length" class="space-y-3">
          <div v-for="(item, index) in form.documents" :key="`service-document-${index}`" class="rounded-lg border border-gray-200 p-4">
            <div class="flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Tài liệu {{ index + 1 }}</div>
              <div class="flex gap-2">
                <button type="button" class="cms-act-btn" :disabled="index === 0" @click="moveDocument(index, -1)">&#8593;</button>
                <button type="button" class="cms-act-btn" :disabled="index === form.documents.length - 1" @click="moveDocument(index, 1)">&#8595;</button>
                <button type="button" class="cms-act-btn delete" @click="removeDocument(index)">&#10005;</button>
              </div>
            </div>

            <div class="mt-3 grid gap-4 md:grid-cols-2">
              <div class="cms-form-group">
                <label class="cms-form-label">Tên tài liệu</label>
                <input v-model="item.title" class="cms-form-control" placeholder="Ví dụ: Hồ sơ năng lực khảo sát" />
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Thứ tự</label>
                <input :value="item.sortOrder + 1" class="cms-form-control" disabled />
              </div>
            </div>

            <div class="cms-form-group">
              <label class="cms-form-label">File PDF</label>
              <div class="relative">
                <input v-model="item.filePath" class="cms-form-control pr-56" placeholder="uploads/documents/xxxx.pdf" />
                <div class="absolute right-1 top-1 flex gap-2">
                  <a v-if="item.filePath" :href="mediaUrl(item.filePath)" target="_blank" rel="noopener noreferrer" class="input-mini-action-btn">Xem</a>
                  <button type="button" class="input-mini-action-btn" :disabled="documentUploadBusy && activeDocumentInputIndex === index" @click="openDocumentPicker(index)">
                    {{ documentUploadBusy && activeDocumentInputIndex === index ? 'Đang tải...' : 'Từ máy tính' }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="rounded-lg border border-dashed border-gray-300 bg-gray-50 px-4 py-6 text-center text-sm text-slate-500">
          Chưa có tài liệu PDF cho dịch vụ này.
        </div>

        <input ref="documentPdfInputRef" type="file" accept="application/pdf,.pdf" class="hidden" @change="handleDocumentSelected" />
      </div>

      <div class="cms-form-group">
        <div class="mb-2 flex items-center justify-between gap-3">
          <label class="cms-form-label mb-0">Nội dung</label>
          <button type="button" class="cms-btn cms-btn-secondary" :disabled="contentImageUpload.uploading.value" @click="contentImageUpload.openPicker()">
            {{ contentImageUpload.uploading.value ? 'Đang tải ảnh...' : 'Tải ảnh vào nội dung' }}
          </button>
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
            <textarea
              v-model="form.content"
              class="cms-form-control font-mono text-sm"
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
        <input :ref="(el: any) => { contentImageUpload.fileInputRef.value = el }" type="file" accept="image/*" class="hidden" @change="handleContentImageSelected" />
      </div>

      <div class="cms-form-group" style="max-width:240px">
        <label class="cms-form-label">Trạng thái</label>
        <select v-model="form.status" class="cms-form-control">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </select>
      </div>

      <div class="mt-4 flex gap-3 border-t border-gray-100 pt-4">
        <button type="submit" :disabled="saving" class="cms-btn cms-btn-primary">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</button>
        <router-link to="/services" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { useRoute, useRouter } from 'vue-router'
import AlertBox from '@/components/shared/AlertBox.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import { useImageUpload } from '@/composables/useImageUpload'
import { mediaApi } from '@/lib/api'
import { useServicesStore } from '@/stores/services'
import type { ServiceDocumentPayload, ServiceImagePayload, ServicePayload } from '@/types'

const route = useRoute()
const router = useRouter()
const store = useServicesStore()
const saving = ref(false)
const error = ref('')
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

const coverUpload = useImageUpload((path) => { form.coverImagePath = path })
const galleryUpload = useImageUpload(() => {})
const contentEditorMode = ref<'visual' | 'html'>('visual')
const contentQuillRef = ref<any>(null)
const contentImageUpload = useImageUpload(() => {})
const isUserTyping = ref(false)
const documentPdfInputRef = ref<HTMLInputElement | null>(null)
const activeDocumentInputIndex = ref<number | null>(null)
const documentUploadBusy = ref(false)
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
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Tải ảnh thất bại'
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

  documentUploadBusy.value = true
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name)
    form.documents[targetIndex].filePath = media.storagePath
    if (!form.documents[targetIndex].title.trim()) {
      form.documents[targetIndex].title = file.name.replace(/\.pdf$/i, '')
    }
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Tải PDF thất bại'
  } finally {
    documentUploadBusy.value = false
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
      const media = await mediaApi.upload(file, file.name)
      form.images.push({
        ...createImage(media.storagePath),
        altText: file.name.replace(/\.[^.]+$/i, ''),
      })
    }
    normalizeImageOrders()
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Tải ảnh thư viện thất bại'
  } finally {
    galleryUpload.uploading.value = false
    if (input) input.value = ''
  }
}

function mediaUrl(path: string) {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
}

function slugify(value: string) {
  return value
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '')
}

function buildPayload(): ServicePayload {
  return {
    title: form.title.trim(),
    slug: (form.slug || slugify(form.title)).trim(),
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

function extractErrorMessage(e: any) {
  const fieldErrors = e.response?.data?.error?.details
  if (Array.isArray(fieldErrors) && fieldErrors.length) {
    return fieldErrors.map((item: { field?: string, message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  }
  return e.response?.data?.error?.message || 'Thao tác thất bại'
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

function resolveServiceImages(entity: any): ServiceImagePayload[] {
  const imageRows = Array.isArray(entity.images) ? entity.images : []
  const images = imageRows
    .map((item: any, index: number) => ({
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
  }
})

async function handleSubmit() {
  const payload = buildPayload()

  if (!payload.title) {
    error.value = 'Tên dịch vụ là bắt buộc'
    return
  }

  if (!payload.slug) {
    error.value = 'Slug không hợp lệ. Vui lòng nhập tên dịch vụ hoặc slug.'
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
    router.push('/services')
  } catch (e: any) {
    error.value = extractErrorMessage(e)
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
