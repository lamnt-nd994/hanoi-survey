<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa dự án' : 'Tạo dự án mới'" />
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
          <label class="cms-form-label">Tên dự án <span class="required">*</span></label>
          <input v-model="form.title" @input="onTitleChange" class="cms-form-control" required />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Slug</label>
          <div class="flex gap-2">
            <input v-model="form.slug" @input="slugManuallyEdited = true" class="cms-form-control" placeholder="vd: du-an-abc" />
            <button type="button" class="cms-btn cms-btn-secondary cms-btn-sm whitespace-nowrap" style="height:38px" @click="generateSlug">Tự tạo</button>
          </div>
        </div>
      </div>
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Khách hàng</label>
          <input v-model="form.clientName" class="cms-form-control" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Địa điểm</label>
          <input v-model="form.location" class="cms-form-control" />
        </div>
      </div>
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Quy mô</label>
          <input v-model="form.scaleText" class="cms-form-control" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Ảnh bìa</label>
          <div class="flex items-start gap-2">
            <input v-model="form.coverImagePath" class="cms-form-control" placeholder="Đường dẫn ảnh bìa" />
            <button type="button" class="cms-btn cms-btn-secondary flex-shrink-0" :disabled="coverUpload.uploading.value" @click="coverUpload.openPicker()">
              {{ coverUpload.uploading.value ? `${coverUpload.progress.value || 0}%` : 'Chọn ảnh' }}
            </button>
          </div>
          <input :ref="(el: any) => { coverUpload.fileInputRef.value = el }" type="file" accept="image/*" class="hidden" @change="coverUpload.handleFileSelected" />
          <img v-if="form.coverImagePath" :src="mediaUrl(form.coverImagePath)" class="mt-2 max-h-40 rounded border border-gray-200 object-contain" />
        </div>
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Thư viện ảnh</label>
        <div class="flex items-start gap-2">
          <button type="button" class="cms-btn cms-btn-secondary flex-shrink-0" :disabled="galleryUpload.uploading.value" @click="galleryUpload.openPicker()">
            {{ galleryUpload.uploading.value ? `Đang tải ${galleryUpload.progress.value || 0}%` : '+ Thêm ảnh' }}
          </button>
          <input :ref="(el: any) => { galleryUpload.fileInputRef.value = el }" type="file" accept="image/*" multiple class="hidden" @change="handleGalleryUpload" />
        </div>
        <div v-if="galleryPaths.length" class="mt-2 grid grid-cols-3 gap-2">
          <div v-for="(path, i) in galleryPaths" :key="i" class="relative group">
            <img :src="mediaUrl(path)" class="h-24 w-full rounded border border-gray-200 object-cover" />
            <button type="button" class="absolute right-1 top-1 hidden rounded bg-red-500 px-1 text-xs text-white group-hover:block" @click="galleryPaths.splice(i, 1)">&times;</button>
          </div>
        </div>
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Nội dung</label>
        <textarea v-model="form.content" class="cms-form-control" style="min-height:200px" />
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
        <router-link to="/projects" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { useToastsStore } from '@/stores/toasts'
import { useProjectsStore } from '@/stores/projects'
import { useImageUpload } from '@/composables/useImageUpload'
import { mediaApi } from '@/lib/api'
import type { ProjectPayload } from '@/types'
import { extractApiError, validateSelectedFile } from '@/utils/files'
import { toSlug } from '@/utils/slug'

const route = useRoute()
const router = useRouter()
const store = useProjectsStore()
const saving = ref(false)
const error = ref('')
const slugManuallyEdited = ref(false)
const isEdit = computed(() => !!route.params.id)
const form = reactive<ProjectPayload>({ categoryId: 0, title: '', slug: '', clientName: '', location: '', startedAt: null, completedAt: null, scaleText: '', coverImagePath: '', galleryJson: '', content: '', status: 'DRAFT' })
const toasts = useToastsStore()

const coverUpload = useImageUpload((path) => { form.coverImagePath = path }, { successMessage: 'Đã tải ảnh bìa dự án' })
const galleryUpload = useImageUpload(() => {}, { successMessage: 'Đã tải ảnh thư viện dự án' })

const galleryPaths = ref<string[]>([])

watch(() => form.galleryJson, (val) => {
  galleryPaths.value = val ? val.split('\n').map(s => s.trim()).filter(Boolean) : []
}, { immediate: true })

watch(galleryPaths, (paths) => {
  form.galleryJson = paths.join('\n')
})

async function handleGalleryUpload(event: Event) {
  const input = event.target as HTMLInputElement | null
  const files = input?.files
  if (!files?.length) return
  galleryUpload.uploading.value = true
  try {
    for (const file of Array.from(files)) {
      const validationError = validateSelectedFile(file, 'image')
      if (validationError) throw new Error(validationError)
      const media = await mediaApi.upload(file, file.name)
      galleryPaths.value.push(media.storagePath)
    }
    toasts.show(`Đã tải ${files.length} ảnh`, 'success')
  } catch (uploadError: any) {
    const message = extractApiError(uploadError, 'Tải ảnh thư viện thất bại')
    error.value = message
    toasts.show(message, 'error')
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

onMounted(async () => {
  await store.fetchCategories()
  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
    Object.assign(form, entity)
    slugManuallyEdited.value = true
  }
})

function generateSlug() {
  form.slug = toSlug(form.title)
  slugManuallyEdited.value = false
}

function onTitleChange() {
  if (!slugManuallyEdited.value && !isEdit.value) {
    form.slug = toSlug(form.title)
  }
}

async function handleSubmit() {
  if (!form.title.trim()) {
    error.value = 'Tên dự án không được để trống'
    toasts.show(error.value, 'error')
    return
  }
  form.slug = (form.slug || toSlug(form.title)).trim()
  if (!form.slug) {
    error.value = 'Slug không hợp lệ'
    toasts.show(error.value, 'error')
    return
  }
  saving.value = true; error.value = ''
  try {
    if (isEdit.value) await store.update(Number(route.params.id), form)
    else await store.create(form)
    toasts.show(isEdit.value ? 'Cập nhật dự án thành công' : 'Tạo dự án thành công', 'success')
    router.push('/projects')
  } catch (e: any) {
    error.value = extractApiError(e)
    toasts.show(error.value, 'error')
  }
  finally { saving.value = false }
}
</script>
