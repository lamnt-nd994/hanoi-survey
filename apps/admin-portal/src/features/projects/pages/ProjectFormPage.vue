<template>
  <PageShell>
    <PageHeader :title="isEdit ? 'Chỉnh sửa dự án' : 'Tạo dự án mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <DataCard class="p-6">
      <form class="space-y-5" @submit.prevent="handleSubmit">
      <FormField class="max-w-md">
        <FormLabel>Danh mục</FormLabel>
        <Select v-model.number="form.categoryId">
          <option :value="0">Chọn danh mục</option>
          <option v-for="cat in store.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </Select>
      </FormField>
      <div class="grid gap-5 md:grid-cols-2">
        <FormField>
          <FormLabel>Tên dự án <span class="text-rose-600">*</span></FormLabel>
          <Input v-model="form.title" required @input="onTitleChange" />
        </FormField>
        <FormField>
          <FormLabel>Slug</FormLabel>
          <div class="flex gap-2">
            <Input v-model="form.slug" placeholder="vd: du-an-abc" @input="slugManuallyEdited = true" />
            <Button type="button" variant="secondary" size="sm" class="h-10 whitespace-nowrap" @click="generateSlug">Tự tạo</Button>
          </div>
        </FormField>
      </div>
      <div class="grid gap-5 md:grid-cols-2">
        <FormField>
          <FormLabel>Khách hàng</FormLabel>
          <Input v-model="form.clientName" />
        </FormField>
        <FormField>
          <FormLabel>Địa điểm</FormLabel>
          <Input v-model="form.location" />
        </FormField>
      </div>
      <div class="grid gap-5 md:grid-cols-2">
        <FormField>
          <FormLabel>Quy mô</FormLabel>
          <Input v-model="form.scaleText" />
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
      </div>
      <FormField>
        <FormLabel>Thư viện ảnh</FormLabel>
        <div class="flex items-start gap-2">
          <Button type="button" variant="secondary" class="flex-shrink-0" :disabled="galleryUpload.uploading.value" @click="galleryUpload.openPicker()">
            {{ galleryUpload.uploading.value ? `Đang tải ${galleryUpload.progress.value || 0}%` : '+ Thêm ảnh' }}
          </Button>
          <input :ref="assignGalleryInput" type="file" accept="image/*" multiple class="hidden" @change="handleGalleryUpload" />
        </div>
        <div v-if="galleryPaths.length" class="mt-2 grid grid-cols-3 gap-2">
          <div v-for="(path, i) in galleryPaths" :key="i" class="relative group">
            <img :src="mediaUrl(path)" width="240" height="96" loading="lazy" decoding="async" class="h-24 w-full rounded border border-gray-200 object-cover" />
            <button type="button" class="absolute right-1 top-1 hidden rounded bg-red-500 px-1 text-xs text-white group-hover:block" @click="galleryPaths.splice(i, 1)">&times;</button>
          </div>
        </div>
      </FormField>
      <FormField>
        <FormLabel>Nội dung</FormLabel>
        <Textarea v-model="form.content" class="min-h-[200px]" />
      </FormField>
      <FormField class="max-w-[240px]">
        <FormLabel>Trạng thái</FormLabel>
        <Select v-model="form.status">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </Select>
      </FormField>
      <div class="flex gap-3 mt-4 pt-4 border-t border-gray-100">
        <Button type="submit" :disabled="saving">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</Button>
        <Button as="router-link" to="/projects" variant="secondary">Quay lại</Button>
      </div>
      </form>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
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
import { useToastsStore } from '@/stores/toasts'
import { useProjectsStore } from '@/stores/projects'
import { useImageUpload } from '@/composables/useImageUpload'
import { mediaApi } from '@/features/media/api/mediaApi'
import { resolveMediaUrl } from '@/lib/media'
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

function assignCoverInput(el: unknown) {
  coverUpload.fileInputRef.value = el instanceof HTMLInputElement ? el : null
}

function assignGalleryInput(el: unknown) {
  galleryUpload.fileInputRef.value = el instanceof HTMLInputElement ? el : null
}

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
  } catch (uploadError: unknown) {
    const message = extractApiError(uploadError, 'Tải ảnh thư viện thất bại')
    error.value = message
    toasts.show(message, 'error')
  } finally {
    galleryUpload.uploading.value = false
    if (input) input.value = ''
  }
}

function mediaUrl(path: string) {
  return resolveMediaUrl(path)
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
  } catch (e: unknown) {
    error.value = extractApiError(e)
    toasts.show(error.value, 'error')
  }
  finally { saving.value = false }
}
</script>
