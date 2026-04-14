<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa trang' : 'Tạo trang mới'" :description="isEdit ? 'Cập nhật nội dung trang tĩnh' : 'Thêm trang tĩnh mới vào hệ thống'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <form @submit.prevent="handleSubmit" class="cms-card mt-6 p-6">
      <PageMetaSection :form="form" @title-input="onTitleChange" @slug-input="slugManuallyEdited = true" @generate-slug="generateSlug" />

      <div v-if="form.templateCode === 'default'" class="cms-form-group">
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
      </div>

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

      <div v-else class="space-y-6">
        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Mở đầu</h2>
            <div class="flex items-center gap-3">
              <span class="text-xs text-slate-500">Quản lý phần xuất hiện đầu tiên của trang giới thiệu</span>
              <button type="button" class="cms-btn cms-btn-secondary" :disabled="uploadingIntroImage" @click="openIntroImagePicker">
                {{ uploadingIntroImage ? `Đang tải ${introImageUploadProgress}%` : 'Tải ảnh vào nội dung' }}
              </button>
            </div>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề lớn</label>
              <input v-model="aboutContent.hero.title" class="cms-form-control" placeholder="Về Chúng Tôi" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh mở đầu</label>
              <div class="relative">
                <input v-model="aboutContent.intro.imagePath" class="cms-form-control pr-36" placeholder="media/about-intro.jpg" />
                <button type="button" class="input-action-btn" :disabled="uploadingIntroCoverImage" @click="introCoverImageInputRef?.click()">
                  {{ uploadingIntroCoverImage ? `Đang tải ${introCoverUploadProgress}%` : 'Chọn ảnh' }}
                </button>
              </div>
              <input ref="introCoverImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleIntroCoverImageSelected" />
            </div>
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Nội dung giới thiệu</label>
            <p class="text-xs text-slate-500">Nhập các đoạn mô tả bên trái. Bạn có thể định dạng chữ, danh sách, link và chèn ảnh trực tiếp nếu cần.</p>
          </div>
          <div class="flex items-center gap-2">
            <button type="button" class="editor-tab-btn" :class="{ active: introEditorMode === 'visual' }" @click="introEditorMode = 'visual'">Soạn thảo</button>
            <button type="button" class="editor-tab-btn" :class="{ active: introEditorMode === 'html' }" @click="introEditorMode = 'html'">HTML</button>
          </div>
          <div class="overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm intro-quill-wrap">
            <QuillEditor
              v-if="introEditorMode === 'visual'"
              v-model:content="aboutContent.intro.content"
              content-type="html"
              theme="snow"
              :toolbar="quillToolbar"
              placeholder="Nhập nội dung giới thiệu doanh nghiệp..."
              class="intro-quill-editor"
              ref="introQuillRef"
            />
            <div v-else class="p-4">
              <textarea
                v-model="aboutContent.intro.content"
                class="cms-form-control font-mono text-sm"
                rows="14"
                placeholder="<p>Nhập nội dung giới thiệu doanh nghiệp...</p>"
              />
            </div>
          </div>
          <div class="mt-3 rounded-xl border border-slate-200 bg-slate-50 p-4">
            <div class="text-xs font-medium uppercase tracking-[0.18em] text-slate-500">Xem trước bố cục mở đầu</div>
            <div class="mt-4 grid gap-6 rounded-xl bg-white p-4 shadow-sm lg:grid-cols-[minmax(0,1fr)_320px] lg:items-start">
              <div>
                <div class="flex items-start gap-4">
                  <div class="mt-1 h-14 w-1.5 shrink-0 bg-orange-500"></div>
                  <div>
                    <div class="text-3xl font-bold text-[#0b4a92]">{{ aboutContent.hero.title || 'Về Chúng Tôi' }}</div>
                  </div>
                </div>
                <div class="prose prose-slate mt-6 max-w-none text-sm" v-html="aboutContent.intro.content"></div>
              </div>
              <div class="overflow-hidden rounded-2xl bg-slate-100">
                <img v-if="aboutContent.intro.imagePath" :src="resolveUploadedMediaUrl(aboutContent.intro.imagePath)" class="h-64 w-full object-cover" />
                <div v-else class="flex h-64 items-center justify-center px-6 text-center text-sm text-slate-400">Ảnh mở đầu sẽ hiển thị ở đây</div>
              </div>
            </div>
          </div>
          <input ref="introImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleIntroImageSelected" />
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Sứ mệnh, tầm nhìn, giá trị cốt lõi</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.coreValues.items.push(createValueItem())">+ Thêm mục</button>
          </div>
          <div class="mt-4 cms-form-group">
            <label class="cms-form-label">Tiêu đề section</label>
            <input v-model="aboutContent.coreValues.sectionTitle" class="cms-form-control" placeholder="Sứ Mệnh & Tầm Nhìn" />
          </div>
          <div class="space-y-3">
            <div v-for="(item, index) in aboutContent.coreValues.items" :key="`value-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Mục {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.coreValues.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Tiêu đề</label>
                  <input v-model="item.title" class="cms-form-control" placeholder="Sứ Mệnh" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Icon key</label>
                  <input v-model="item.icon" class="cms-form-control" placeholder="bullseye, eye, gem" />
                </div>
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Mô tả</label>
                <textarea v-model="item.description" class="cms-form-control" rows="3" placeholder="Mô tả nội dung" />
              </div>
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Quá trình phát triển</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.timeline.items.push(createTimelineItem())">+ Thêm mốc</button>
          </div>
          <div class="mt-4 cms-form-group">
            <label class="cms-form-label">Tiêu đề section</label>
            <input v-model="aboutContent.timeline.sectionTitle" class="cms-form-control" placeholder="Quá Trình Phát Triển" />
          </div>
          <div class="space-y-3">
            <div v-for="(item, index) in aboutContent.timeline.items" :key="`timeline-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Mốc {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.timeline.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Năm / giai đoạn</label>
                  <input v-model="item.year" class="cms-form-control" placeholder="2013" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Thứ tự</label>
                  <input v-model.number="item.sortOrder" type="number" min="1" class="cms-form-control" />
                </div>
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Tiêu đề</label>
                <input v-model="item.title" class="cms-form-control" placeholder="Thành lập công ty" />
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Mô tả</label>
                <textarea v-model="item.description" class="cms-form-control" rows="3" placeholder="Mô tả cột mốc" />
              </div>
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Cơ cấu tổ chức</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.organization.departments.push('')">+ Thêm bộ phận</button>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề section</label>
              <input v-model="aboutContent.organization.sectionTitle" class="cms-form-control" placeholder="Cơ Cấu Tổ Chức" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Heading</label>
              <input v-model="aboutContent.organization.heading" class="cms-form-control" placeholder="Sự tổ chức khoa học và hiệu quả" />
            </div>
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Mô tả</label>
            <textarea v-model="aboutContent.organization.description" class="cms-form-control" rows="3" placeholder="Mô tả cơ cấu tổ chức" />
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh sơ đồ</label>
              <div class="relative">
                <input v-model="aboutContent.organization.chartImagePath" class="cms-form-control pr-36" placeholder="media/org-chart.png" />
                <button type="button" class="input-action-btn" :disabled="uploadingOrgChartImage" @click="orgChartImageInputRef?.click()">
                  {{ uploadingOrgChartImage ? `Đang tải ${orgChartUploadProgress}%` : 'Chọn ảnh' }}
                </button>
              </div>
              <input ref="orgChartImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleOrgChartImageSelected" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Chú thích ảnh</label>
              <input v-model="aboutContent.organization.chartCaption" class="cms-form-control" placeholder="Sơ đồ tổ chức nhân sự" />
            </div>
          </div>
          <div class="space-y-3">
            <div v-for="(department, index) in aboutContent.organization.departments" :key="`department-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Bộ phận {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.organization.departments, index)">&#10005;</button>
              </div>
              <input v-model="aboutContent.organization.departments[index]" class="cms-form-control mt-3" placeholder="Tên bộ phận" />
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Hồ sơ năng lực</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.capability.items.push(createCapabilityItem())">+ Thêm mục</button>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề section</label>
              <input v-model="aboutContent.capability.sectionTitle" class="cms-form-control" placeholder="Hồ Sơ Năng Lực" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Heading</label>
              <input v-model="aboutContent.capability.heading" class="cms-form-control" placeholder="Năng lực và chứng chỉ của chúng tôi" />
            </div>
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Mô tả</label>
            <textarea v-model="aboutContent.capability.description" class="cms-form-control" rows="3" placeholder="Mô tả hồ sơ năng lực" />
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh minh họa</label>
              <div class="relative">
                <input v-model="aboutContent.capability.imagePath" class="cms-form-control pr-36" placeholder="media/capability.jpg" />
                <button type="button" class="input-action-btn" :disabled="uploadingCapabilityImage" @click="capabilityImageInputRef?.click()">
                  {{ uploadingCapabilityImage ? `Đang tải ${capabilityImageUploadProgress}%` : 'Chọn ảnh' }}
                </button>
              </div>
              <input ref="capabilityImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleCapabilityImageSelected" />
            </div>
          </div>
          <div class="space-y-3">
            <div v-for="(item, index) in aboutContent.capability.items" :key="`capability-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Mục {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.capability.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Tên tài liệu</label>
                  <input v-model="item.title" class="cms-form-control" placeholder="Ví dụ: Chứng chỉ năng lực hoạt động khảo sát xây dựng" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Nhãn nút</label>
                  <input v-model="item.buttonLabel" class="cms-form-control" placeholder="Xem PDF" />
                </div>
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">File PDF</label>
                <div class="relative">
                  <input v-model="item.pdfFilePath" class="cms-form-control pr-56" placeholder="media/ho-so-nang-luc.pdf" />
                  <div class="absolute right-1 top-1 flex gap-2">
                    <a v-if="item.pdfFilePath" :href="resolveUploadedMediaUrl(item.pdfFilePath)" target="_blank" rel="noopener noreferrer" class="input-mini-action-btn">Xem</a>
                    <button type="button" class="input-mini-action-btn" :disabled="uploadingCapabilityItemIndex === index" @click="openCapabilityItemPdfPicker(index)">
                      {{ uploadingCapabilityItemIndex === index ? `Đang tải ${capabilityItemUploadProgress}%` : 'Chọn PDF' }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <input ref="capabilityItemPdfInputRef" type="file" accept="application/pdf,.pdf" class="hidden" @change="handleCapabilityItemPdfSelected" />
        </section>

        <section class="rounded-xl border border-dashed border-gray-300 bg-gray-50 p-4">
          <div class="text-sm font-medium text-slate-700">Preview contentJson</div>
          <textarea :value="serializedAboutContent" class="cms-form-control mt-3 font-mono text-xs" rows="12" readonly />
        </section>
      </div>

      <div class="mt-4 flex gap-3 border-t border-gray-100 pt-4">
        <button type="submit" :disabled="saving" class="cms-btn cms-btn-primary">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</button>
        <router-link to="/pages" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import DefaultContentSection from '@/features/pages/components/PageForm/DefaultContentSection.vue'
import HomeLandingSection from '@/features/pages/components/PageForm/HomeLandingSection.vue'
import PageMetaSection from '@/features/pages/components/PageForm/PageMetaSection.vue'
import { mediaApi } from '@/lib/api'
import { useToastsStore } from '@/stores/toasts'
import { usePagesStore } from '@/stores/pages'
import { useProjectsStore } from '@/stores/projects'
import { useServicesStore } from '@/stores/services'
import type {
  AboutCapabilityItem,
  AboutPageContent,
  AboutPageTimelineItem,
  AboutPageValueItem,
  HomePageContent,
  PagePayload,
  ProjectEntity,
  ServiceEntity,
} from '@/types'
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
const slugManuallyEdited = ref(false)
const introEditorMode = ref<'visual' | 'html'>('visual')
const uploadingIntroCoverImage = ref(false)
const introCoverUploadProgress = ref(0)
const introCoverImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingIntroImage = ref(false)
const introImageUploadProgress = ref(0)
const introImageInputRef = ref<HTMLInputElement | null>(null)
const introQuillRef = ref<any>(null)
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

const isEdit = computed(() => !!route.params.id)

const form = reactive<PagePayload>({
  title: '',
  slug: '',
  summary: '',
  content: '',
  templateCode: 'default',
  contentJson: null,
  status: 'DRAFT',
})

const aboutContent = reactive<AboutPageContent>(createEmptyAboutContent())
const homeContent = reactive<HomePageContent>(createEmptyHomeContent())

const serializedAboutContent = computed(() => JSON.stringify(normalizeAboutContent(aboutContent), null, 2))
const serializedHomeContent = computed(() => JSON.stringify(normalizeHomeContent(homeContent), null, 2))
const availableServices = computed<ServiceEntity[]>(() => servicesStore.items.filter((item) => item.status === 'PUBLISHED'))
const availableProjects = computed<ProjectEntity[]>(() => projectsStore.items.filter((item) => item.status === 'PUBLISHED'))

onMounted(async () => {
  await Promise.all([
    servicesStore.fetchList(1, 100),
    projectsStore.fetchList(1, 100),
  ])

  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
    Object.assign(form, {
      title: entity.title,
      slug: entity.slug,
      summary: entity.summary,
      content: entity.content,
      templateCode: entity.templateCode || 'default',
      contentJson: entity.contentJson,
      status: entity.status,
    })
    if (entity.templateCode === 'about_company') {
      Object.assign(aboutContent, parseAboutContent(entity.contentJson))
    }
    if (entity.templateCode === 'home_landing') {
      Object.assign(homeContent, parseHomeContent(entity.contentJson))
    }
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
    const payload: PagePayload = {
      ...form,
      content: form.templateCode === 'default' ? form.content : '',
      contentJson: form.templateCode === 'about_company'
        ? JSON.stringify(normalizeAboutContent(aboutContent))
        : form.templateCode === 'home_landing'
          ? JSON.stringify(normalizeHomeContent(homeContent))
          : null,
    }

    if (isEdit.value) {
      await store.update(Number(route.params.id), payload)
    } else {
      await store.create(payload)
    }
    toasts.show(isEdit.value ? 'Cập nhật trang thành công' : 'Tạo trang thành công', 'success')
    router.push('/pages')
  } catch (e: any) {
    error.value = extractApiError(e)
    toasts.show(error.value, 'error')
  } finally {
    saving.value = false
  }
}

function createValueItem(): AboutPageValueItem {
  return { title: '', icon: '', description: '' }
}

function createTimelineItem(): AboutPageTimelineItem {
  return { year: '', title: '', description: '', sortOrder: 1 }
}

function createCapabilityItem(): AboutCapabilityItem {
  return { title: '', pdfFilePath: '', buttonLabel: 'Xem PDF' }
}

function createEmptyHomeContent(): HomePageContent {
  return {
    hero: {
      title: 'DỮ LIỆU [TIN CẬY] - NỀN MÓNG [VỮNG BỀN]',
      subtitle: 'Đối tác tin cậy cung cấp giải pháp toàn diện cho Khảo sát Địa chất, Địa hình, Thí nghiệm và Xây lắp công trình.',
      backgroundImagePath: '',
      badges: [
        { label: 'Địa hình', iconKey: 'MapPlus' },
        { label: 'Địa chất', iconKey: 'Earth' },
        { label: 'Thí nghiệm', iconKey: 'FlaskConical' },
        { label: 'Quan trắc', iconKey: 'ChartLine' },
        { label: 'Xây lắp', iconKey: 'fas fa-tools' },
        { label: 'Giám sát', iconKey: 'Telescope' },
      ],
      primaryCtaLabel: 'Xem dự án',
      primaryCtaUrl: '/du-an',
      secondaryCtaLabel: 'Liên hệ',
      secondaryCtaUrl: '/lien-he',
    },
    stats: {
      items: [
        { value: '10+', label: 'Năm kinh nghiệm' },
        { value: '500+', label: 'Dự án hoàn thành' },
        { value: '50+', label: 'Kỹ sư chuyên môn' },
      ],
    },
    aboutSection: {
      eyebrow: 'Về chúng tôi',
      title: 'VỀ CHÚNG TÔI',
      description: 'CÔNG TY CỔ PHẦN KHẢO SÁT XÂY DỰNG HÀ NỘI tự hào là đơn vị hàng đầu trong lĩnh vực khảo sát xây dựng tại Việt Nam. Chúng tôi cam kết mang lại sự chính xác tuyệt đối trong từng số liệu.',
      imagePath: 'uploads/2026-04-13/b50a87be-0c01-4bb8-b469-3ee561b571f8.jpg',
      highlights: [
        'Hệ thống máy móc hiện đại bậc nhất.',
        'Đội ngũ kỹ sư giàu kinh nghiệm.',
        'Quy trình làm việc chuyên nghiệp, chuẩn ISO.',
      ],
      buttonLabel: 'Xem thêm',
      buttonUrl: '/gioi-thieu',
    },
    servicesSection: {
      eyebrow: 'Lĩnh vực',
      title: 'LĨNH VỰC HOẠT ĐỘNG',
      description: '',
      mode: 'manual',
      limit: 6,
      selectedItems: [
        { serviceId: 11, categoryId: 2, icon: 'fas fa-map-marked-alt' },
        { serviceId: 13, categoryId: 1, icon: 'fas fa-globe-asia' },
        { serviceId: 14, categoryId: 4, icon: 'fas fa-flask' },
        { serviceId: 16, categoryId: null, icon: 'fas fa-hard-hat' },
        { serviceId: 12, categoryId: null, icon: 'fa-solid fa-water' },
        { serviceId: 15, categoryId: null, icon: 'fas fa-chart-line' },
      ],
    },
    projectsSection: {
      eyebrow: 'Dự án',
      title: 'Dự Án Tiêu Biểu',
      description: '',
      mode: 'latest',
      limit: 6,
      selectedItems: [],
    },
    finalCta: {
      eyebrow: 'Tư vấn nhanh ngoài hiện trường',
      title: 'Sẵn sàng khảo sát cho dự án mới?',
      description: 'Đội kỹ sư của Hanoi CSC có thể hỗ trợ từ giai đoạn lập phương án khảo sát, báo giá, bố trí nhân lực đến bàn giao hồ sơ đúng tiến độ.',
      items: [],
      primaryButtonLabel: 'Xem dự án',
      primaryButtonUrl: '/du-an',
      secondaryButtonLabel: 'Liên hệ',
      secondaryButtonUrl: '/lien-he',
      showPhoneButton: true,
    },
  }
}

function createEmptyAboutContent(): AboutPageContent {
  return {
    hero: { title: 'Về chúng tôi', backgroundImagePath: '' },
    intro: {
      heading: 'Doi tac tin cay trong nganh khao sat xay dung',
      imagePath: '',
      content: '<p class="ql-align-justify"><strong>CÔNG TY CỔ PHẦN TƯ VẤN KHẢO SÁT XÂY DỰNG HÀ NỘI</strong>&nbsp;(HANOI CONTRUCTION SERVEY CONSULTANT JOIN STOCK COMPANY) là một doanh nghiệp độc lập, tự hào với bề dày kinh nghiệm trong lĩnh vực tư vấn xây dựng.</p><p class="ql-align-justify"><br></p><p class="ql-align-justify">Được thành lập từ năm 2006, chúng tôi cam kết mang lại những giải pháp tư vấn chất lượng cao, đáp ứng các tiêu chuẩn khắt khe nhất của ngành xây dựng Việt Nam.</p><p class="ql-align-justify"><br></p><p class="ql-align-justify">Với sở hữu Trung tâm thí nghiệm và kiểm định xây dựng đạt chuẩn, VietDelta tự tin là đối tác tin cậy cho mọi công trình.</p><p class="ql-align-justify"><br></p><p><strong style="color: rgb(0, 64, 128);">Người đại diện:</strong>&nbsp;Đỗ Xuân Dân – Giám đốc Công ty</p>',
    },
    coreValues: {
      sectionTitle: 'Sứ mệnh và tầm nhìn',
      items: [
        {
          title: 'Sứ mệnh',
          icon: 'bullseye',
          description: 'Cung cấp dữ liệu khảo sát và thí nghiệm chính xác, minh bạch, giúp tối ưu giải pháp nền móng và giảm thiểu rủi ro kỹ thuật cho mọi công trình.',
        },
        {
          title: 'Tầm nhìn',
          icon: 'eye',
          description: 'Trở thành đơn vị khảo sát và quan trắc được ưu tiên lựa chọn trong các dự án hạ tâng, công nghiệp và phát triển đô thị tại Việt Nam.',
        },
        {
          title: 'Giá trị cốt lõi',
          icon: 'gem',
          description: 'Chúng tôi coi chất lượng dữ liệu, tốc độ phối hợp và trách nhiệm nghề nghiệp là nền tảng của mọi dự án.',
        },
      ],
    },
    timeline: {
      sectionTitle: 'Quá trình phát triển',
      items: [
        {
          year: '2006',
          title: 'Khởi đầu',
          description: 'Thành lập công ty tại Định Công, Hoàng Mai, Hà Nội. Đặt nền móng đầu tiên cho hành trình tư vấn xây dựng.',
          sortOrder: 1,
        },
        {
          year: '2007',
          title: 'Mở rộng năng lực Lab',
          description: 'Thành lập Trung tâm thí nghiệm và kiểm định xây dựng.\n\nĐược cấp quyết định công nhận khả năng thực hiện các phép thử số 1232/QĐ-BXD.',
          sortOrder: 2,
        },
        {
          year: '2024 - Nay',
          title: 'Tăng trưởng bền vững',
          description: 'Tham gia nhieu du an trong diem ve giao thong, cong nghiep va phat trien do thi voi goi dich vu khao sat tron goi.',
          sortOrder: 4,
        },
      ],
    },
    organization: {
      sectionTitle: 'Cơ cấu tổ chức',
      heading: 'Tổ chức khoa học để triển khai nhanh và kiểm soát chất lượng',
      description: 'Mô hình vận hành được phân tách rõ giữa khối điều hành, khối kỹ thuật và các đội hiện trường, giúp doanh nghiệm duy trì tiến độ, bảo đảm chất lượng dữ liệu và tăng khả năng phối hợp trên công trường.',
      chartImagePath: '',
      chartCaption: 'Sơ đồ tổ chức nhân sự',
      departments: [
        'Ban Giám đốc và Hội đồng quản trị',
        'Phòng Kỹ thuật - Nghiên cứu và Phát triển',
        'Phòng Thí nghiệm và Kiểm định chất lượng',
        'Phòng Thiết kế và Xây lấp',
        'Phòng Hành chính - Nhân sự và Kế toán',
        'Các đội thi công hiện trường',
      ],
    },
    capability: {
      sectionTitle: 'Hồ sơ năng lực',
      heading: 'Năng lực và chứng chỉ của chúng tôi',
      description: 'Chúng tôi duy trì đầy đủ hồ sơ pháp lý, năng lục hành nghề và các tài liệu kỹ thuật cần thiết để tham gia nhiều loại hình dự án từ dân dụng đến hạ tầng kỹ thuật.',
      imagePath: 'uploads/2026-04-11/8ad682f4-890d-4bff-8c0f-7c57ffb69739.jpg',
      items: [
        {
          title: 'Giấy phép đăng ký kinh doanh',
          pdfFilePath: 'uploads/2026-04-11/04dcb4b3-65a6-4bd0-81dd-f71ec2f43cbd.pdf',
          buttonLabel: 'Xem PDF',
        },
        {
          title: 'Chứng chỉ năng lực hoạt động khảo sát xây dựng',
          pdfFilePath: 'uploads/2026-04-11/f3bea515-17aa-4dc3-b3e2-e4cc626536fa.pdf',
          buttonLabel: 'Xem PDF',
        },
        {
          title: 'Chứng nhận hệ thống quản lý chất lượng',
          pdfFilePath: 'uploads/2026-04-11/89427367-90ea-4c6d-96b5-30d9e5cb4f58.pdf',
          buttonLabel: 'Xem PDF',
        },
        {
          title: 'Hồ sơ kinh nghiệm các dự án tiêu biểu',
          pdfFilePath: '',
          buttonLabel: 'Xem PDF',
        },
      ],
    },
  }
}

function parseAboutContent(raw: string | null | undefined): AboutPageContent {
  const fallback = createEmptyAboutContent()
  if (!raw) return fallback

  try {
    return normalizeAboutContent(JSON.parse(raw))
  } catch {
    return fallback
  }
}

function normalizeAboutContent(input: Partial<AboutPageContent> | AboutPageContent): AboutPageContent {
  const hero = input.hero ?? { title: '', backgroundImagePath: '' }
  const intro = input.intro ?? { heading: '', imagePath: '', content: '<p></p>' }
  const coreValues = input.coreValues ?? { sectionTitle: 'Sứ Mệnh & Tầm Nhìn', items: [] }
  const timeline = input.timeline ?? { sectionTitle: 'Quá Trình Phát Triển', items: [] }
  const organization = input.organization ?? {
    sectionTitle: 'Cơ Cấu Tổ Chức',
    heading: '',
    description: '',
    chartImagePath: '',
    chartCaption: '',
    departments: [''],
  }
  const capability = input.capability ?? {
    sectionTitle: 'Hồ Sơ Năng Lực',
    heading: '',
    description: '',
    imagePath: '',
    items: [createCapabilityItem()],
  }

  return {
    hero: {
      title: hero.title || '',
      backgroundImagePath: hero.backgroundImagePath || '',
    },
    intro: {
      heading: intro.heading || '',
      imagePath: intro.imagePath || '',
      content: normalizeIntroContent(intro),
    },
    coreValues: {
      sectionTitle: coreValues.sectionTitle || 'Sứ Mệnh & Tầm Nhìn',
      items: (coreValues.items || []).map((item) => ({
        title: item.title || '',
        icon: item.icon || '',
        description: item.description || '',
      })),
    },
    timeline: {
      sectionTitle: timeline.sectionTitle || 'Quá Trình Phát Triển',
      items: (timeline.items || []).map((item, index) => ({
        year: item.year || '',
        title: item.title || '',
        description: item.description || '',
        sortOrder: Number(item.sortOrder) || index + 1,
      })),
    },
    organization: {
      sectionTitle: organization.sectionTitle || 'Cơ Cấu Tổ Chức',
      heading: organization.heading || '',
      description: organization.description || '',
      chartImagePath: organization.chartImagePath || '',
      chartCaption: organization.chartCaption || '',
      departments: (organization.departments || ['']).map((item) => item || ''),
    },
    capability: {
      sectionTitle: capability.sectionTitle || 'Hồ Sơ Năng Lực',
      heading: capability.heading || '',
      description: capability.description || '',
      imagePath: capability.imagePath || '',
      items: (capability.items || [createCapabilityItem()]).map((item) => ({
        title: item.title || '',
        pdfFilePath: item.pdfFilePath || '',
        buttonLabel: item.buttonLabel || 'Xem PDF',
      })),
    },
  }
}

function parseHomeContent(raw: string | null | undefined): HomePageContent {
  const fallback = createEmptyHomeContent()
  if (!raw) return fallback

  try {
    return normalizeHomeContent(JSON.parse(raw))
  } catch {
    return fallback
  }
}

function normalizeHomeContent(input: Partial<HomePageContent> | HomePageContent): HomePageContent {
  const fallback = createEmptyHomeContent()

  return {
    hero: {
      title: input.hero?.title || fallback.hero.title,
      subtitle: input.hero?.subtitle || fallback.hero.subtitle,
      backgroundImagePath: input.hero?.backgroundImagePath || fallback.hero.backgroundImagePath,
      badges: (input.hero?.badges || fallback.hero.badges).map((item) => ({
        label: item.label || '',
        iconKey: item.iconKey || 'terrain',
      })),
      primaryCtaLabel: input.hero?.primaryCtaLabel || fallback.hero.primaryCtaLabel,
      primaryCtaUrl: input.hero?.primaryCtaUrl || fallback.hero.primaryCtaUrl,
      secondaryCtaLabel: input.hero?.secondaryCtaLabel || fallback.hero.secondaryCtaLabel,
      secondaryCtaUrl: input.hero?.secondaryCtaUrl || fallback.hero.secondaryCtaUrl,
    },
    stats: {
      items: (input.stats?.items || fallback.stats.items).map((item) => ({
        value: item.value || '',
        label: item.label || '',
      })),
    },
    aboutSection: {
      eyebrow: input.aboutSection?.eyebrow || fallback.aboutSection.eyebrow,
      title: input.aboutSection?.title || fallback.aboutSection.title,
      description: input.aboutSection?.description || fallback.aboutSection.description,
      imagePath: input.aboutSection?.imagePath || fallback.aboutSection.imagePath,
      highlights: (input.aboutSection?.highlights || fallback.aboutSection.highlights).map((item) => item || ''),
      buttonLabel: input.aboutSection?.buttonLabel || fallback.aboutSection.buttonLabel,
      buttonUrl: input.aboutSection?.buttonUrl || fallback.aboutSection.buttonUrl,
    },
    servicesSection: {
      eyebrow: input.servicesSection?.eyebrow || fallback.servicesSection.eyebrow,
      title: input.servicesSection?.title || fallback.servicesSection.title,
      description: input.servicesSection?.description || fallback.servicesSection.description,
      mode: input.servicesSection?.mode === 'manual' ? 'manual' : fallback.servicesSection.mode,
      limit: Number(input.servicesSection?.limit) || fallback.servicesSection.limit,
      selectedItems: (input.servicesSection?.selectedItems || fallback.servicesSection.selectedItems).map((item) => ({
        serviceId: Number(item.serviceId) || null,
        categoryId: item.categoryId == null ? null : Number(item.categoryId) || null,
        icon: item.icon || '',
      })),
    },
    projectsSection: {
      eyebrow: input.projectsSection?.eyebrow || fallback.projectsSection.eyebrow,
      title: input.projectsSection?.title || fallback.projectsSection.title,
      description: input.projectsSection?.description || fallback.projectsSection.description,
      mode: input.projectsSection?.mode === 'manual' ? 'manual' : fallback.projectsSection.mode,
      limit: Number(input.projectsSection?.limit) || fallback.projectsSection.limit,
      selectedItems: (input.projectsSection?.selectedItems || fallback.projectsSection.selectedItems).map((item) => ({
        projectId: Number(item.projectId) || null,
      })),
    },
    finalCta: {
      eyebrow: input.finalCta?.eyebrow || fallback.finalCta.eyebrow,
      title: input.finalCta?.title || fallback.finalCta.title,
      description: input.finalCta?.description || fallback.finalCta.description,
      items: (input.finalCta?.items || fallback.finalCta.items).map((item) => item || ''),
      primaryButtonLabel: input.finalCta?.primaryButtonLabel || fallback.finalCta.primaryButtonLabel,
      primaryButtonUrl: input.finalCta?.primaryButtonUrl || fallback.finalCta.primaryButtonUrl,
      secondaryButtonLabel: input.finalCta?.secondaryButtonLabel || fallback.finalCta.secondaryButtonLabel,
      secondaryButtonUrl: input.finalCta?.secondaryButtonUrl || fallback.finalCta.secondaryButtonUrl,
      showPhoneButton: input.finalCta?.showPhoneButton ?? fallback.finalCta.showPhoneButton,
    },
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
  } catch (e: any) {
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
  const quill = introQuillRef.value?.getQuill?.()
  if (!quill) return

  const selection = quill.getSelection(true)
  const index = selection?.index ?? quill.getLength()
  quill.insertEmbed(index, 'image', url, 'user')
  quill.setSelection(index + 1, 0, 'user')
  aboutContent.intro.content = quill.root.innerHTML || '<p></p>'
}

function resolveUploadedMediaUrl(path: string) {
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
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
  } catch (e: any) {
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
  } catch (e: any) {
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
.cms-collapsible > summary {
  cursor: pointer;
  list-style: none;
}

.cms-collapsible > summary::-webkit-details-marker {
  display: none;
}

.collapse-icon {
  transition: transform 0.2s ease;
}

.cms-collapsible[open] .collapse-icon {
  transform: rotate(180deg);
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
