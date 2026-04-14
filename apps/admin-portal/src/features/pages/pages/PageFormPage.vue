<template>
  <PageShell>
    <PageHeader :title="isEdit ? 'Chỉnh sửa trang' : 'Tạo trang mới'" :description="isEdit ? 'Cập nhật nội dung trang tĩnh' : 'Thêm trang tĩnh mới vào hệ thống'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <DataCard class="p-6">
      <form class="space-y-6" @submit.prevent="handleSubmit">
      <PageMetaSection :form="form" @title-input="onTitleChange" @slug-input="slugManuallyEdited = true" @generate-slug="generateSlug" />

      <FormField v-if="form.templateCode === 'default'">
        <DefaultContentSection
          ref="defaultContentSectionRef"
          v-model:content="form.content"
          v-model:editor-mode="defaultEditorMode"
          :toolbar="defaultQuillToolbar"
          :uploading="uploadingDefaultImage"
          :upload-progress="defaultImageUploadProgress"
          @trigger-image-upload="openDefaultImagePicker"
        />
        <input ref="defaultImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleDefaultImageSelected" />
      </FormField>

      <template v-else-if="form.templateCode === 'home_landing'">
        <HomeLandingSection
          :home-content="homeContent"
          :available-services="availableServices"
          :available-projects="availableProjects"
          :serialized-home-content="serializedHomeContent"
          :uploading-home-hero-image="uploadingHomeHeroImage"
          :home-hero-upload-progress="homeHeroUploadProgress"
          :uploading-home-about-image="uploadingHomeAboutImage"
          :home-about-upload-progress="homeAboutUploadProgress"
          @pick-home-hero="homeHeroImageInputRef?.click()"
          @pick-home-about="homeAboutImageInputRef?.click()"
        />
        <input ref="homeHeroImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleHomeHeroImageSelected" />
        <input ref="homeAboutImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleHomeAboutImageSelected" />
      </template>

      <template v-else>
        <AboutCompanySection
          ref="aboutCompanySectionRef"
          :about-content="aboutContent"
          v-model:intro-editor-mode="introEditorMode"
          :toolbar="quillToolbar"
          :resolve-uploaded-media-url="resolveUploadedMediaUrl"
          :serialized-about-content="serializedAboutContent"
          :uploading-intro-image="uploadingIntroImage"
          :intro-image-upload-progress="introImageUploadProgress"
          :uploading-intro-cover-image="uploadingIntroCoverImage"
          :intro-cover-upload-progress="introCoverUploadProgress"
          :uploading-org-chart-image="uploadingOrgChartImage"
          :org-chart-upload-progress="orgChartUploadProgress"
          :uploading-capability-image="uploadingCapabilityImage"
          :capability-image-upload-progress="capabilityImageUploadProgress"
          :uploading-capability-item-index="uploadingCapabilityItemIndex"
          :capability-item-upload-progress="capabilityItemUploadProgress"
          @pick-intro-image="openIntroImagePicker"
          @pick-intro-cover="introCoverImageInputRef?.click()"
          @pick-org-chart="orgChartImageInputRef?.click()"
          @pick-capability-image="capabilityImageInputRef?.click()"
          @pick-capability-item-pdf="openCapabilityItemPdfPicker"
        />
        <input ref="introImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleIntroImageSelected" />
        <input ref="introCoverImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleIntroCoverImageSelected" />
        <input ref="orgChartImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleOrgChartImageSelected" />
        <input ref="capabilityImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleCapabilityImageSelected" />
        <input ref="capabilityItemPdfInputRef" type="file" accept="application/pdf,.pdf" class="hidden" @change="handleCapabilityItemPdfSelected" />
      </template>

      <div class="mt-4 flex gap-3 border-t border-gray-100 pt-4">
        <Button type="submit" :disabled="saving">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</Button>
        <Button as="router-link" to="/pages" variant="secondary">Quay lại</Button>
      </div>
      </form>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AlertBox from '@/components/shared/AlertBox.vue'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Button } from '@/components/ui/button'
import AboutCompanySection from '@/features/pages/components/PageForm/AboutCompanySection.vue'
import { usePageForm } from '@/features/pages/composables/usePageForm'
import DefaultContentSection from '@/features/pages/components/PageForm/DefaultContentSection.vue'
import HomeLandingSection from '@/features/pages/components/PageForm/HomeLandingSection.vue'
import PageMetaSection from '@/features/pages/components/PageForm/PageMetaSection.vue'
import { mediaApi } from '@/features/media/api/mediaApi'
import { resolveMediaUrl } from '@/lib/media'
import { useToastsStore } from '@/stores/toasts'
import { usePagesStore } from '@/stores/pages'
import { useProjectsStore } from '@/stores/projects'
import { useServicesStore } from '@/stores/services'
import type { ProjectEntity, ServiceEntity } from '@/types'
import { extractApiError, validateSelectedFile } from '@/utils/files'
import { toSlug } from '@/utils/slug'

const route = useRoute()
const router = useRouter()
const store = usePagesStore()
const servicesStore = useServicesStore()
const projectsStore = useProjectsStore()
const toasts = useToastsStore()
const saving = ref(false)
const error = ref('')
const {
  form,
  aboutContent,
  homeContent,
  slugManuallyEdited,
  serializedAboutContent,
  serializedHomeContent,
  isEdit,
  generateSlug,
  onTitleChange,
  applyEntity,
  buildPayload,
} = usePageForm(computed(() => route.params.id))
const introEditorMode = ref<'visual' | 'html'>('visual')
const uploadingIntroCoverImage = ref(false)
const introCoverUploadProgress = ref(0)
const introCoverImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingIntroImage = ref(false)
const introImageUploadProgress = ref(0)
const introImageInputRef = ref<HTMLInputElement | null>(null)
const aboutCompanySectionRef = ref<{ insertImage: (url: string) => void } | null>(null)
const uploadingOrgChartImage = ref(false)
const orgChartUploadProgress = ref(0)
const orgChartImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingCapabilityImage = ref(false)
const capabilityImageUploadProgress = ref(0)
const capabilityImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingHomeHeroImage = ref(false)
const homeHeroUploadProgress = ref(0)
const homeHeroImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingHomeAboutImage = ref(false)
const homeAboutUploadProgress = ref(0)
const homeAboutImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingCapabilityItemIndex = ref<number | null>(null)
const capabilityItemPdfInputRef = ref<HTMLInputElement | null>(null)
const capabilityItemUploadIndex = ref<number | null>(null)
const capabilityItemUploadProgress = ref(0)

const defaultEditorMode = ref<'visual' | 'html'>('visual')
const uploadingDefaultImage = ref(false)
const defaultImageUploadProgress = ref(0)
const defaultImageInputRef = ref<HTMLInputElement | null>(null)
const defaultContentSectionRef = ref<{ insertImage: (url: string) => void } | null>(null)

function createQuillToolbar(imageHandler: () => void) {
  return {
    container: [
      [{ header: [1, 2, 3, false] }],
      [{ font: [] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ color: [] }, { background: [] }],
      [{ script: 'super' }, { script: 'sub' }],
      [{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
      [{ align: [] }],
      ['blockquote', 'link', 'image'],
      ['clean'],
    ],
    handlers: {
      image: imageHandler,
    },
  }
}

const quillToolbar = createQuillToolbar(() => openIntroImagePicker())

const defaultQuillToolbar = createQuillToolbar(() => openDefaultImagePicker())

const availableServices = computed<ServiceEntity[]>(() => servicesStore.items.filter((item) => item.status === 'PUBLISHED'))
const availableProjects = computed<ProjectEntity[]>(() => projectsStore.items.filter((item) => item.status === 'PUBLISHED'))

onMounted(async () => {
  await Promise.all([
    servicesStore.fetchList(1, 100),
    projectsStore.fetchList(1, 100),
  ])

  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
    applyEntity(entity)
  }
})

async function handleSubmit() {
  if (!form.title.trim()) {
    error.value = 'Tiêu đề không được để trống'
    toasts.show(error.value, 'error')
    return
  }

  form.slug = (form.slug || toSlug(form.title)).trim()
  if (!form.slug) {
    error.value = 'Slug không hợp lệ'
    toasts.show(error.value, 'error')
    return
  }

  saving.value = true
  error.value = ''
  try {
    const payload = buildPayload()

    if (isEdit.value) {
      await store.update(Number(route.params.id), payload)
    } else {
      await store.create(payload)
    }
    toasts.show(isEdit.value ? 'Cập nhật trang thành công' : 'Tạo trang thành công', 'success')
    router.push('/pages')
  } catch (e: unknown) {
    error.value = extractApiError(e)
    toasts.show(error.value, 'error')
  } finally {
    saving.value = false
  }
}

function removeArrayItem<T>(items: T[], index: number) {
  if (items.length <= 1) return
  items.splice(index, 1)
}

function removeAnyArrayItem<T>(items: T[], index: number) {
  items.splice(index, 1)
}

function normalizeIntroContent(intro: { heading?: string; content?: string; paragraphs?: string[] }) {
  const html = intro.content?.trim()
  if (html) return html

  const paragraphs = (intro.paragraphs || []).map((item) => item.trim()).filter(Boolean)
  if (!paragraphs.length) return '<p></p>'
  return paragraphs.map((item) => `<p>${escapeHtml(item)}</p>`).join('')
}

function escapeHtml(value: string) {
  return value
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')
}

function openIntroImagePicker() {
  introImageInputRef.value?.click()
}

async function handleIntroCoverImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingIntroCoverImage, 'Tải ảnh mở đầu thất bại', introCoverUploadProgress)
  if (storagePath) {
    aboutContent.intro.imagePath = storagePath
  }
}

async function handleIntroImageSelected(event: Event) {
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

  uploadingIntroImage.value = true
  introImageUploadProgress.value = 0
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name, {
      onUploadProgress(progressEvent) {
        if (!progressEvent.total) return
        introImageUploadProgress.value = Math.max(1, Math.round((progressEvent.loaded / progressEvent.total) * 100))
      },
    })
    insertImageToIntro(resolveUploadedMediaUrl(media.storagePath))
    introImageUploadProgress.value = 100
    toasts.show(`Đã tải ảnh: ${file.name}`, 'success')
  } catch (e: unknown) {
    error.value = extractApiError(e, 'Tải ảnh vào nội dung thất bại')
    toasts.show(error.value, 'error')
  } finally {
    uploadingIntroImage.value = false
    window.setTimeout(() => { introImageUploadProgress.value = 0 }, 400)
    if (input) input.value = ''
  }
}

async function handleOrgChartImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingOrgChartImage, 'Tải ảnh sơ đồ thất bại', orgChartUploadProgress)
  if (storagePath) {
    aboutContent.organization.chartImagePath = storagePath
  }
}

async function handleCapabilityImageSelected(event: Event) {
  const storagePath = await uploadFileFromInput(event, uploadingCapabilityImage, 'Tải ảnh hồ sơ năng lực thất bại', capabilityImageUploadProgress)
  if (storagePath) {
    aboutContent.capability.imagePath = storagePath
  }
}

async function handleHomeHeroImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingHomeHeroImage, 'Tải ảnh hero trang chủ thất bại', homeHeroUploadProgress)
  if (storagePath) {
    homeContent.hero.backgroundImagePath = storagePath
  }
}

async function handleHomeAboutImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingHomeAboutImage, 'Tải ảnh phần giới thiệu thất bại', homeAboutUploadProgress)
  if (storagePath) {
    homeContent.aboutSection.imagePath = storagePath
  }
}

function openCapabilityItemPdfPicker(index: number) {
  capabilityItemUploadIndex.value = index
  capabilityItemPdfInputRef.value?.click()
}

async function handleCapabilityItemPdfSelected(event: Event) {
  const targetIndex = capabilityItemUploadIndex.value
  if (targetIndex === null) return

  const loadingProxy = {
    get value() {
      return uploadingCapabilityItemIndex.value !== null
    },
    set value(next: boolean) {
      uploadingCapabilityItemIndex.value = next ? targetIndex : null
    },
  }

  const storagePath = await uploadFileFromInput(event, loadingProxy, 'Tải file PDF thất bại', capabilityItemUploadProgress)
  if (storagePath && aboutContent.capability.items[targetIndex]) {
    aboutContent.capability.items[targetIndex].pdfFilePath = storagePath
  }
  capabilityItemUploadIndex.value = null
}

function insertImageToIntro(url: string) {
  aboutCompanySectionRef.value?.insertImage(url)
}

function resolveUploadedMediaUrl(path: string) {
  return resolveMediaUrl(path)
}

function openDefaultImagePicker() {
  defaultImageInputRef.value?.click()
}

async function handleDefaultImageSelected(event: Event) {
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

  uploadingDefaultImage.value = true
  defaultImageUploadProgress.value = 0
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name, {
      onUploadProgress(progressEvent) {
        if (!progressEvent.total) return
        defaultImageUploadProgress.value = Math.max(1, Math.round((progressEvent.loaded / progressEvent.total) * 100))
      },
    })
    insertImageToDefault(resolveUploadedMediaUrl(media.storagePath))
    defaultImageUploadProgress.value = 100
    toasts.show(`Đã tải ảnh: ${file.name}`, 'success')
  } catch (e: unknown) {
    error.value = extractApiError(e, 'Tải ảnh vào nội dung thất bại')
    toasts.show(error.value, 'error')
  } finally {
    uploadingDefaultImage.value = false
    window.setTimeout(() => { defaultImageUploadProgress.value = 0 }, 400)
    if (input) input.value = ''
  }
}

function insertImageToDefault(url: string) {
  defaultContentSectionRef.value?.insertImage(url)
}

async function uploadImageFromInput(event: Event, loadingState: { value: boolean }, fallbackMessage: string, progressState?: { value: number }) {
  return uploadFileFromInput(event, loadingState, fallbackMessage, progressState)
}

async function uploadFileFromInput(event: Event, loadingState: { value: boolean }, fallbackMessage: string, progressState?: { value: number }) {
  const input = event.target as HTMLInputElement | null
  const file = input?.files?.[0]
  if (!file) return ''

  const fileKind: 'pdf' | 'image' = /pdf/i.test(file.type) || file.name.toLowerCase().endsWith('.pdf') ? 'pdf' : 'image'
  const validationError = validateSelectedFile(file, fileKind)
  if (validationError) {
    error.value = validationError
    toasts.show(validationError, 'error')
    if (input) input.value = ''
    return ''
  }

  loadingState.value = true
  if (progressState) progressState.value = 0
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name, {
      onUploadProgress(progressEvent) {
        if (!progressState || !progressEvent.total) return
        progressState.value = Math.max(1, Math.round((progressEvent.loaded / progressEvent.total) * 100))
      },
    })
    if (progressState) progressState.value = 100
    toasts.show(`Đã tải file: ${file.name}`, 'success')
    return media.storagePath
  } catch (e: unknown) {
    error.value = extractApiError(e, fallbackMessage)
    toasts.show(error.value, 'error')
    return ''
  } finally {
    loadingState.value = false
    if (progressState) window.setTimeout(() => { progressState.value = 0 }, 400)
    if (input) input.value = ''
  }
}
</script>

<style scoped>
.collapse-icon {
  transition: transform 0.2s ease;
}

.input-action-btn {
  position: absolute;
  right: 0.4rem;
  top: 50%;
  transform: translateY(-50%);
  border: 1px solid #cbd5e1;
  background: #f8fafc;
  color: #334155;
  border-radius: 0.7rem;
  height: calc(100% - 0.8rem);
  min-width: 7.75rem;
  padding: 0 0.85rem;
  font-size: 0.8rem;
  font-weight: 600;
}

.input-action-btn:hover:not(:disabled) {
  background: #f1f5f9;
}

.input-action-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

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

.intro-quill-wrap :deep(.ql-toolbar.ql-snow) {
  border: 0;
  border-bottom: 1px solid #e2e8f0;
  background: #f8fafc;
}

.intro-quill-wrap :deep(.ql-container.ql-snow) {
  border: 0;
  font-size: 0.95rem;
  min-height: 320px;
}

.intro-quill-editor :deep(.ql-editor) {
  min-height: 320px;
  color: #334155;
  line-height: 1.8;
  padding: 1rem 1.125rem;
}

.intro-quill-editor :deep(.ql-editor.ql-blank::before) {
  color: #94a3b8;
  font-style: normal;
}
</style>
