<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa bài viết' : 'Tạo bài viết mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <div v-if="pageLoading" class="cms-card mt-6 p-8 text-center text-gray-400">Đang tải...</div>
    <form v-else @submit.prevent="handleSubmit" class="cms-card mt-6 p-6">
      <div class="cms-form-group">
        <label class="cms-form-label">Danh mục <span class="required">*</span></label>
        <select v-model.number="form.categoryId" class="cms-form-control">
          <option :value="0" disabled>Chọn danh mục</option>
          <option v-for="cat in store.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
      </div>
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Tiêu đề <span class="required">*</span></label>
          <input v-model="form.title" @input="onTitleChange" class="cms-form-control" placeholder="Nhập tiêu đề" required />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Slug</label>
          <div class="flex gap-2">
            <input v-model="form.slug" class="cms-form-control flex-1" placeholder="vd: bai-viet-mau" />
            <button v-if="!slugManuallyEdited" type="button" @click="generateSlug" class="cms-btn cms-btn-secondary cms-btn-sm whitespace-nowrap" style="height:38px">Tự tạo</button>
          </div>
        </div>
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Ảnh tiêu đề</label>
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
        <label class="cms-form-label">Tóm tắt</label>
        <textarea v-model="form.excerpt" class="cms-form-control" rows="3" placeholder="Nội dung tóm tắt" />
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Nội dung</label>
        <QuillEditor
          v-model:content="form.content"
          content-type="html"
          :toolbar="quillToolbar"
          theme="snow"
          class="cms-quill"
          style="min-height:300px"
        />
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
        <router-link to="/posts" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { usePostsStore } from '@/stores/posts'
import { useImageUpload } from '@/composables/useImageUpload'
import type { PostPayload } from '@/types'

const route = useRoute()
const router = useRouter()
const store = usePostsStore()
const saving = ref(false)
const error = ref('')
const pageLoading = ref(false)
const slugManuallyEdited = ref(false)
const isEdit = computed(() => !!route.params.id)

const coverUpload = useImageUpload((path) => { form.coverImagePath = path })

const quillToolbar = [
  [{ header: [1, 2, 3, false] }],
  ['bold', 'italic', 'underline', 'strike'],
  [{ list: 'ordered' }, { list: 'bullet' }],
  [{ color: [] }, { background: [] }],
  [{ align: [] }],
  ['link', 'image'],
  ['clean'],
]

const form = reactive<PostPayload>({ categoryId: 0, title: '', slug: '', excerpt: '', content: '', coverImagePath: '', status: 'DRAFT' })

function mediaUrl(path: string) {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
}

function toSlug(text: string): string {
  return text
    .toLowerCase()
    .trim()
    .replace(/[\s\-.]+/g, '-')
    .replace(/[^\w\-u00c0-\u024f\u1e00-\u1eff]+/g, '')
    .replace(/-+/g, '-')
    .replace(/^-+|-+$/g, '')
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

function extractError(err: any): string {
  const resp = err?.response?.data
  if (resp?.error?.details?.length) {
    return resp.error.details.map((d: any) => `${d.field}: ${d.message}`).join('; ')
  }
  if (resp?.error?.message) return resp.error.message
  return err?.message || 'Thao tác thất bại'
}

function toPayload(entity: any): PostPayload {
  return {
    categoryId: entity.categoryId ?? 0,
    title: entity.title ?? '',
    slug: entity.slug ?? '',
    excerpt: entity.excerpt ?? '',
    content: entity.content ?? '',
    coverImagePath: entity.coverImagePath ?? '',
    status: entity.status ?? 'DRAFT',
  }
}

onMounted(async () => {
  await store.fetchCategories()
  if (route.params.id) {
    pageLoading.value = true
    try {
      const entity = await store.getById(Number(route.params.id))
      const payload = toPayload(entity)
      Object.assign(form, payload)
      slugManuallyEdited.value = true
    } catch (e: any) {
      error.value = extractError(e)
    } finally {
      pageLoading.value = false
    }
  }
})

async function handleSubmit() {
  if (form.categoryId <= 0) { error.value = 'Vui lòng chọn danh mục bài viết'; return }
  if (!form.title.trim()) { error.value = 'Tiêu đề không được để trống'; return }
  if (!form.slug.trim()) { error.value = 'Slug không được để trống'; return }

  saving.value = true
  error.value = ''
  try {
    if (isEdit.value) await store.update(Number(route.params.id), toPayload(form))
    else await store.create(toPayload(form))
    router.push('/posts')
  } catch (e: any) {
    error.value = extractError(e)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.cms-quill :deep(.ql-toolbar) {
  border: 1px solid #e5e7eb;
  border-radius: 8px 8px 0 0;
  background: #f9fafb;
}
.cms-quill :deep(.ql-container) {
  border: 1px solid #e5e7eb;
  border-top: none;
  border-radius: 0 0 8px 8px;
  font-size: 14px;
  min-height: 300px;
}
.cms-quill :deep(.ql-editor) {
  min-height: 250px;
}
</style>
