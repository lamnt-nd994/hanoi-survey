<template>
  <PageShell>
    <PageHeader :title="isEdit ? 'Chỉnh sửa bài viết' : 'Tạo bài viết mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <DataCard v-if="pageLoading" class="p-8 text-center text-gray-400">Đang tải...</DataCard>
    <DataCard v-else class="p-6">
      <form class="space-y-5" @submit.prevent="handleSubmit">
      <FormField>
        <FormLabel>Danh mục <span class="text-rose-600">*</span></FormLabel>
        <Select v-model.number="form.categoryId">
          <option :value="0" disabled>Chọn danh mục</option>
          <option v-for="cat in store.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </Select>
      </FormField>
      <div class="grid gap-5 md:grid-cols-2">
        <FormField>
          <FormLabel>Tiêu đề <span class="text-rose-600">*</span></FormLabel>
          <Input v-model="form.title" placeholder="Nhập tiêu đề" required @input="onTitleChange" />
        </FormField>
        <FormField>
          <FormLabel>Slug</FormLabel>
          <div class="flex gap-2">
            <Input v-model="form.slug" class="flex-1" placeholder="vd: bai-viet-mau" @input="slugManuallyEdited = true" />
            <Button type="button" variant="secondary" size="sm" class="h-10 whitespace-nowrap" @click="generateSlug">Tự tạo</Button>
          </div>
        </FormField>
      </div>
      <FormField>
        <FormLabel>Ảnh tiêu đề</FormLabel>
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
        <FormLabel>Tóm tắt</FormLabel>
        <Textarea v-model="form.excerpt" rows="3" placeholder="Nội dung tóm tắt" />
      </FormField>
      <FormField>
        <FormLabel>Nội dung</FormLabel>
        <QuillEditor
          v-model:content="form.content"
          content-type="html"
          :toolbar="quillToolbar"
          theme="snow"
          class="post-quill"
          style="min-height:300px"
        />
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
        <Button as="router-link" to="/posts" variant="secondary">Quay lại</Button>
      </div>
      </form>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { ref, reactive, computed, defineAsyncComponent, onMounted } from 'vue'
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
import { usePostsStore } from '@/stores/posts'
import { useImageUpload } from '@/composables/useImageUpload'
import { resolveMediaUrl } from '@/lib/media'
import type { PostEntity, PostPayload } from '@/types'
import type { ApiErrorLike } from '@/types/api'
import { extractApiError } from '@/utils/files'
import { toSlug } from '@/utils/slug'

const route = useRoute()
const router = useRouter()
const store = usePostsStore()
const QuillEditor = defineAsyncComponent(async () => {
  await import('@vueup/vue-quill/dist/vue-quill.snow.css')
  return (await import('@vueup/vue-quill')).QuillEditor
})
const saving = ref(false)
const error = ref('')
const pageLoading = ref(false)
const slugManuallyEdited = ref(false)
const isEdit = computed(() => !!route.params.id)
const toasts = useToastsStore()

const coverUpload = useImageUpload((path) => { form.coverImagePath = path }, { successMessage: 'Đã tải ảnh tiêu đề' })

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

function assignCoverInput(el: unknown) {
  coverUpload.fileInputRef.value = el as HTMLInputElement | null
}

function extractError(err: unknown): string {
  const resp = (err as ApiErrorLike).response?.data
  if (resp?.error?.details?.length) {
    return resp.error.details.map((d) => `${d.field}: ${d.message}`).join('; ')
  }
  if (resp?.error?.message) return resp.error.message
  return extractApiError(err)
}

function toPayload(entity: Partial<PostEntity> & Partial<PostPayload>): PostPayload {
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
    } catch (e: unknown) {
      error.value = extractError(e)
    } finally {
      pageLoading.value = false
    }
  }
})

async function handleSubmit() {
  if (form.categoryId <= 0) { error.value = 'Vui lòng chọn danh mục bài viết'; toasts.show(error.value, 'error'); return }
  if (!form.title.trim()) { error.value = 'Tiêu đề không được để trống'; toasts.show(error.value, 'error'); return }
  if (!form.slug.trim()) { error.value = 'Slug không được để trống'; toasts.show(error.value, 'error'); return }

  saving.value = true
  error.value = ''
  try {
    if (isEdit.value) await store.update(Number(route.params.id), toPayload(form))
    else await store.create(toPayload(form))
    toasts.show(isEdit.value ? 'Cập nhật bài viết thành công' : 'Tạo bài viết thành công', 'success')
    router.push('/posts')
  } catch (e: unknown) {
    error.value = extractError(e)
    toasts.show(error.value, 'error')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.post-quill :deep(.ql-toolbar) {
  border: 1px solid #e5e7eb;
  border-radius: 8px 8px 0 0;
  background: #f9fafb;
}
.post-quill :deep(.ql-container) {
  border: 1px solid #e5e7eb;
  border-top: none;
  border-radius: 0 0 8px 8px;
  font-size: 14px;
  min-height: 300px;
}
.post-quill :deep(.ql-editor) {
  min-height: 250px;
}
</style>
