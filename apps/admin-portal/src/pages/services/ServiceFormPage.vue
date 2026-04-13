<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa dịch vụ' : 'Tạo dịch vụ mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <form @submit.prevent="handleSubmit" class="cms-card mt-6 p-6">
      <div class="cms-form-group">
        <label class="cms-form-label">Danh mục</label>
        <select v-model.number="form.categoryId" class="cms-form-control">
          <option :value="0">Chọn danh mục</option>
          <option v-for="cat in store.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
      </div>
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
        <label class="cms-form-label">Tổng quan</label>
        <textarea v-model="form.overview" class="cms-form-control" placeholder="Mô tả tổng quan" />
      </div>
      <div class="cms-form-group">
        <div class="flex items-center justify-between gap-3 mb-2">
          <label class="cms-form-label mb-0">Nội dung</label>
          <button type="button" class="cms-btn cms-btn-secondary" :disabled="contentImageUpload.uploading.value" @click="contentImageUpload.openPicker()">
            {{ contentImageUpload.uploading.value ? 'Đang tải ảnh...' : 'Tải ảnh vào nội dung' }}
          </button>
        </div>
        <div class="flex items-center gap-2">
          <button type="button" class="editor-tab-btn" :class="{ active: contentEditorMode === 'visual' }" @click="contentEditorMode = 'visual'">Soạn thảo</button>
          <button type="button" class="editor-tab-btn" :class="{ active: contentEditorMode === 'html' }" @click="contentEditorMode = 'html'">HTML</button>
        </div>
        <div class="overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm intro-quill-wrap mt-2">
          <QuillEditor
            v-if="contentEditorMode === 'visual'"
            v-model:content="form.content"
            content-type="html"
            theme="snow"
            :toolbar="contentQuillToolbar"
            placeholder="Nhập nội dung chi tiết..."
            class="intro-quill-editor"
            ref="contentQuillRef"
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
      <div class="flex gap-3 mt-4 pt-4 border-t border-gray-100">
        <button type="submit" :disabled="saving" class="cms-btn cms-btn-primary">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</button>
        <router-link to="/services" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { useServicesStore } from '@/stores/services'
import { useImageUpload } from '@/composables/useImageUpload'
import type { ServicePayload } from '@/types'

const route = useRoute()
const router = useRouter()
const store = useServicesStore()
const saving = ref(false)
const error = ref('')
const isEdit = computed(() => !!route.params.id)
const form = reactive<ServicePayload>({ categoryId: 0, title: '', slug: '', overview: '', content: '', icon: '', coverImagePath: '', status: 'DRAFT' })

const coverUpload = useImageUpload((path) => { form.coverImagePath = path })

const contentEditorMode = ref<'visual' | 'html'>('visual')
const contentQuillRef = ref<any>(null)
const contentImageUpload = useImageUpload(() => {})
const isUserTyping = ref(false)

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
    const media = await (await import('@/lib/api')).mediaApi.upload(file, file.name)
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
    categoryId: form.categoryId,
    title: form.title.trim(),
    slug: (form.slug || slugify(form.title)).trim(),
    overview: form.overview?.trim() || '',
    content: form.content || '',
    icon: form.icon?.trim() || '',
    coverImagePath: form.coverImagePath?.trim() || '',
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

onMounted(async () => {
  await store.fetchCategories()
  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
    Object.assign(form, entity)
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

  if (!payload.categoryId || payload.categoryId <= 0) {
    error.value = 'Vui lòng chọn danh mục dịch vụ'
    return
  }

  form.title = payload.title
  form.slug = payload.slug
  form.overview = payload.overview
  form.icon = payload.icon
  form.coverImagePath = payload.coverImagePath || ''

  saving.value = true; error.value = ''
  try {
    if (isEdit.value) await store.update(Number(route.params.id), payload)
    else await store.create(payload)
    router.push('/services')
  } catch (e: any) { error.value = extractErrorMessage(e) }
  finally { saving.value = false }
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
