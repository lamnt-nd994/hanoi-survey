<template>
  <div>
    <section class="container-shell py-16 md:py-14">
      <div v-if="loading" class="grid gap-6">
        <Skeleton class="h-40 rounded-2xl" />
        <Skeleton class="h-64 rounded-2xl" />
      </div>

      <Card v-else-if="error" class="p-8 text-rose-600">{{ error }}</Card>

      <div v-else-if="detail">
        <article v-if="type === 'project'" class="space-y-10">
          <div v-if="detail.coverImagePath" class="overflow-hidden rounded-[2rem] border border-neutral-200 bg-white shadow-sm">
            <img
              :src="resolveMediaUrl(detail.coverImagePath)"
              :alt="detail.title"
              width="1200"
              height="720"
              loading="eager"
              fetchpriority="high"
              decoding="async"
              class="h-[22rem] w-full object-cover md:h-[30rem]"
            />
          </div>
          <div v-else class="relative overflow-hidden rounded-[2rem] border border-neutral-200 bg-gradient-to-br from-primary-navy via-primary-light to-primary-navy px-8 py-12 text-white shadow-sm md:px-12 md:py-16">
            <div class="absolute inset-0 bg-[radial-gradient(circle_at_top_right,rgba(255,255,255,0.14),transparent_26%),linear-gradient(135deg,rgba(255,255,255,0.06),transparent_40%)]" />
            <div class="relative max-w-3xl">
              <div class="eyebrow text-white/70">Du an</div>
              <h2 class="mt-4 font-heading text-3xl font-bold leading-tight md:text-5xl">{{ detail.title }}</h2>
              <p class="mt-4 max-w-2xl text-sm leading-7 text-white/80 md:text-base">
                Ho so cong trinh va pham vi cong viec duoc trinh bay chi tiet ben duoi. Hinh anh cover dang duoc cap nhat.
              </p>
            </div>
          </div>

          <div class="grid gap-8 lg:grid-cols-[0.72fr_1.28fr]">
            <Card as="aside" class="p-6">
              <div class="eyebrow">Thông tin dự án</div>
              <div class="mt-5 space-y-4">
                <div v-for="item in projectMetadata" :key="item.label" class="rounded-2xl border border-neutral-200 bg-neutral-50 px-4 py-4">
                  <div class="text-xs font-semibold uppercase tracking-[0.12em] text-neutral-400">{{ item.label }}</div>
                  <div class="mt-2 text-base font-semibold text-primary-navy">{{ item.value }}</div>
                </div>
              </div>
            </Card>

            <Card class="p-6 md:p-8">
              <div class="prose prose-slate max-w-none">
                <div class="text-base leading-8 text-neutral-700" v-html="bodyText"></div>
              </div>
            </Card>
          </div>

          <section v-if="projectGalleryImages.length" class="space-y-5">
            <div>
              <div class="eyebrow">Hình ảnh dự án</div>
              <h2 class="mt-2 font-heading text-3xl font-bold text-primary-navy">Thư viện công trình</h2>
            </div>

            <div class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
              <button
                v-for="(image, index) in projectGalleryImages"
                :key="`${image}-${index}`"
                type="button"
                class="overflow-hidden rounded-[1.75rem] border border-neutral-200 bg-white shadow-sm transition-transform duration-300 hover:-translate-y-1"
                @click="openProjectLightbox(index)"
              >
                <img :src="resolveMediaUrl(image)" :alt="`${detail.title} ${index + 1}`" width="480" height="256" loading="lazy" decoding="async" class="h-64 w-full object-cover transition-transform duration-500 hover:scale-105" />
              </button>
            </div>
          </section>
        </article>

        <Dialog v-if="isProjectLightboxOpen" :open="isProjectLightboxOpen" @update:open="handleLightboxOpenChange">
          <DialogContent class="max-w-6xl px-0 py-0" aria-describedby="project-lightbox-description">
          <DialogTitle class="sr-only">{{ detail.title }} gallery</DialogTitle>
          <p id="project-lightbox-description" class="sr-only">Project image gallery lightbox.</p>
          <Button type="button" variant="ghost" size="icon" class="absolute right-0 top-[-3.25rem] border border-white/20 bg-white/10 text-white hover:bg-white/20" @click="closeProjectLightbox">
            <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </Button>

          <Button
            v-if="projectGalleryImages.length > 1"
            type="button"
            variant="ghost"
            size="icon"
            class="absolute left-3 top-1/2 -translate-y-1/2 border border-white/20 bg-white/10 text-white hover:bg-white/20 md:left-6"
            @click.stop="showPrevProjectImage"
          >
            <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
            </svg>
          </Button>

          <div class="w-full max-w-6xl">
            <img
              :src="currentProjectLightboxImage"
              :alt="`${detail.title} ${activeProjectImageIndex + 1}`"
              width="1200"
              height="800"
              loading="lazy"
              decoding="async"
              class="max-h-[82vh] w-full rounded-[1.75rem] object-contain"
            />
            <div class="mt-4 text-center text-sm text-white/75">
              Anh {{ activeProjectImageIndex + 1 }} / {{ projectGalleryImages.length }}
            </div>
            <div v-if="projectGalleryImages.length > 1" class="mt-4 flex flex-wrap justify-center gap-3">
              <Button
                v-for="(image, index) in projectGalleryImages"
                :key="`thumb-${image}-${index}`"
                type="button"
                variant="ghost"
                class="h-auto min-h-0 min-w-0 overflow-hidden rounded-2xl border-2 p-0 transition-all"
                :class="index === activeProjectImageIndex ? 'border-white shadow-lg shadow-white/10' : 'border-white/10 opacity-70 hover:opacity-100'"
                @click.stop="activeProjectImageIndex = index"
              >
                <img :src="resolveMediaUrl(image)" :alt="`${detail.title} thumbnail ${index + 1}`" width="128" height="80" loading="lazy" decoding="async" class="h-16 w-24 object-cover md:h-20 md:w-32" />
              </Button>
            </div>
          </div>

          <Button
            v-if="projectGalleryImages.length > 1"
            type="button"
            variant="ghost"
            size="icon"
            class="absolute right-3 top-1/2 -translate-y-1/2 border border-white/20 bg-white/10 text-white hover:bg-white/20 md:right-6"
            @click.stop="showNextProjectImage"
          >
            <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </Button>
          </DialogContent>
        </Dialog>

        <article v-if="type === 'equipment' && isEquipmentCategory" class="space-y-10">
          <Card v-if="categoryEquipments.length === 0" class="p-8 text-center text-neutral-500">
            Chưa có thiết bị trong danh mục này.
          </Card>
          <div v-else class="overflow-x-auto rounded-xl border border-neutral-200 bg-white shadow-sm">
            <table class="w-full text-left text-sm">
              <thead>
                <tr class="bg-primary-navy text-white">
                  <th class="px-5 py-4 font-semibold text-center w-16">STT</th>
                  <th class="px-5 py-4 font-semibold">Thiết bị / Dụng cụ</th>
                  <th class="px-5 py-4 font-semibold">Xuất xứ</th>
                  <th class="px-5 py-4 font-semibold">Đơn vị</th>
                  <th class="px-5 py-4 font-semibold text-center w-24">Số lượng</th>
                  <th class="px-5 py-4 font-semibold text-center w-28">Hình ảnh</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in categoryEquipments" :key="item.id" class="border-b border-neutral-100 last:border-b-0 transition-colors hover:bg-neutral-50">
                  <td class="px-5 py-4 text-center text-neutral-500">{{ index + 1 }}</td>
                  <td class="px-5 py-4 font-semibold text-primary-navy">{{ item.name }}</td>
                  <td class="px-5 py-4 text-neutral-600">{{ item.origin || '—' }}</td>
                  <td class="px-5 py-4 text-neutral-600">{{ item.unit || '—' }}</td>
                  <td class="px-5 py-4 text-center text-neutral-600">{{ item.quantity ?? '—' }}</td>
                  <td class="px-5 py-4 text-center">
                    <img
                      v-if="item.coverImagePath"
                      :src="resolveMediaUrl(item.coverImagePath)"
                      :alt="item.name"
                      width="80"
                      height="56"
                      loading="lazy"
                      decoding="async"
                      class="mx-auto h-14 w-20 rounded-lg border border-neutral-200 object-cover"
                      @error="($event) => ($event.target as HTMLImageElement).style.display = 'none'"
                    />
                    <span v-else class="text-neutral-400 text-xs">—</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </article>

        <article v-else-if="type !== 'project'">
          <section v-if="type === 'service' && serviceDocuments.length" class="mb-12 space-y-5">
            <h2 class="font-heading text-2xl font-bold uppercase text-primary-navy">* TÀI LIỆU-QUYẾT ĐỊNH</h2>

            <div class="divide-y divide-neutral-200 border-y border-neutral-200">
              <a
                v-for="(item, index) in serviceDocuments"
                :key="`${item.id}-${item.filePath}-${index}`"
                :href="resolveMediaUrl(item.filePath)"
                target="_blank"
                rel="noopener noreferrer"
                class="group flex items-center justify-between gap-4 py-4 text-left text-primary-navy transition-colors hover:text-accent-green"
              >
                <span class="text-base font-semibold leading-7">- {{ item.title }}</span>
                <span class="shrink-0 text-sm font-semibold transition-transform group-hover:translate-x-1">Xem ngay</span>
              </a>
            </div>
          </section>
          <section v-if="type === 'service' && serviceGalleryImages.length" class="mb-12 space-y-5">
            <h2 class="font-heading text-2xl font-bold uppercase text-primary-navy">* MỘT SỐ HÌNH ẢNH CỦA DỰ ÁN</h2>

            <div class="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
              <div
                v-for="(image, index) in serviceGalleryImages"
                :key="`${image}-${index}`"
                class="overflow-hidden rounded-lg border border-neutral-100 bg-white"
              >
                <img :src="resolveMediaUrl(image)" :alt="`${detail?.title || 'service'} ${index + 1}`" width="480" height="256" loading="lazy" decoding="async" class="h-64 w-full object-cover" />
              </div>
            </div>
          </section>
          <section v-if="type === 'service' && serviceContent" class="space-y-5">
            <h2 class="font-heading text-2xl font-bold uppercase text-primary-navy">* NỘI DUNG</h2>
            <div class="prose prose-slate max-w-none">
              <div class="text-base leading-8 text-neutral-700" v-html="serviceContent"></div>
            </div>
          </section>
          <div v-else-if="type !== 'service'" class="prose prose-slate max-w-none">
            <div class="text-base leading-8 text-neutral-700" v-html="bodyText"></div>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Button } from '../components/ui/button'
import { Card } from '../components/ui/card'
import { Dialog, DialogContent, DialogTitle } from '../components/ui/dialog'
import { Skeleton } from '../components/ui/skeleton'
import { fetchEquipmentDetail, fetchEquipmentCategories, fetchEquipments, fetchPageBySlug, fetchPostDetail, fetchProjectDetail, fetchServiceDetail } from '../lib/api'
import { resolveMediaUrl } from '../lib/media'
import type { Equipment, EquipmentCategory, ServiceDocument, ServiceImage } from '../types/content'

type EquipmentCategoryDetail = Pick<EquipmentCategory, 'id' | 'name' | 'slug'>
interface PublicDetail extends Partial<EquipmentCategoryDetail> {
  id?: number
  categoryId?: number | null
  categoryName?: string | null
  title?: string
  name?: string
  slug?: string
  overview?: string
  description?: string
  excerpt?: string
  content?: string
  coverImagePath?: string | null
  galleryJson?: string | null
  documents?: ServiceDocument[]
  images?: ServiceImage[]
  clientName?: string | null
  location?: string | null
  startedAt?: string | null
  completedAt?: string | null
  model?: string | null
  manufacturer?: string | null
  origin?: string | null
  unit?: string | null
  quantity?: number | null
  productionYear?: number | null
  contentJson?: string | null
}
type GalleryItem = string | {
  path?: string
  url?: string
  filePath?: string
}

const route = useRoute()
const loading = ref(true)
const error = ref('')
const detail = ref<PublicDetail | null>(null)
const isProjectLightboxOpen = ref(false)
const activeProjectImageIndex = ref(0)

const type = computed(() => route.meta.type)
const slug = computed(() => typeof route.params.slug === 'string' ? route.params.slug : '')

const isEquipmentCategory = ref(false)
const categoryEquipments = ref<Equipment[]>([])

function parseGalleryPaths(gallery: unknown) {
  if (typeof gallery !== 'string' || !gallery.trim()) return []

  try {
    const parsed = JSON.parse(gallery)
    if (!Array.isArray(parsed)) return []
    return (parsed as GalleryItem[])
      .map((item) => {
        if (typeof item === 'string') return item
        if (isGalleryObject(item)) return item.path || item.url || item.filePath || ''
        return ''
      })
      .filter(Boolean)
  } catch {
    return gallery.split('\n').map(item => item.trim()).filter(Boolean)
  }
}

function isGalleryObject(item: GalleryItem): item is Exclude<GalleryItem, string> {
  return typeof item === 'object' && item !== null
}

function hasRenderableContent(value: unknown) {
  if (typeof value !== 'string') return false
  const trimmed = value.trim()
  if (!trimmed) return false
  if (/<(img|iframe|video|table|ul|ol)\b/i.test(trimmed)) return true
  return trimmed
    .replace(/<br\s*\/?>/gi, '')
    .replace(/<[^>]*>/g, '')
    .replace(/&nbsp;/gi, ' ')
    .trim().length > 0
}

const bodyText = computed(() => detail.value?.content || detail.value?.description || detail.value?.overview || detail.value?.excerpt || 'Chưa có nội dung chi tiết.')
const serviceContent = computed(() => {
  if (type.value !== 'service') return ''
  const content = detail.value?.content
  return hasRenderableContent(content) ? content : ''
})
const projectYear = computed(() => {
  const rawDate = detail.value?.completedAt || detail.value?.startedAt
  if (!rawDate) return 'Đang cập nhật'
  const year = new Date(rawDate).getFullYear()
  return Number.isNaN(year) ? 'Đang cập nhật' : String(year)
})
const projectMetadata = computed(() => {
  if (type.value !== 'project' || !detail.value) return []
  return [
    { label: 'Công trình', value: detail.value.title || 'Đang cập nhật' },
    { label: 'Địa điểm', value: detail.value.location || 'Đang cập nhật' },
    { label: 'Công việc', value: detail.value.categoryName || 'Đang cập nhật' },
    { label: 'Năm', value: projectYear.value },
  ]
})
const projectGalleryImages = computed(() => {
  if (type.value !== 'project') return []
  return parseGalleryPaths(detail.value?.galleryJson)
})
const currentProjectLightboxImage = computed(() => resolveMediaUrl(projectGalleryImages.value[activeProjectImageIndex.value] || ''))
const serviceDocuments = computed<ServiceDocument[]>(() => {
  if (type.value !== 'service') return []
  const documents = detail.value?.documents
  if (!Array.isArray(documents)) return []
  return [...documents].sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0))
})
const serviceGalleryImages = computed(() => {
  if (type.value !== 'service') return []
  const images = detail.value?.images
  if (Array.isArray(images) && images.length) {
    return [...images]
      .sort((a: ServiceImage, b: ServiceImage) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0))
      .map((image: ServiceImage) => image.imagePath)
      .filter(Boolean)
  }
  return parseGalleryPaths(detail.value?.galleryJson)
})

const metadata = computed(() => {
  if (!detail.value) return []
  if (type.value === 'equipment' && isEquipmentCategory.value) {
    return [
      { label: 'Danh mục', value: detail.value?.name || 'Thiết bị' },
    ]
  }
  return [
    { label: 'Thiết bị', value: detail.value.name || 'Đang cập nhật' },
    { label: 'Model', value: detail.value.model || 'Đang cập nhật' },
    { label: 'Hãng', value: detail.value.manufacturer || 'Đang cập nhật' },
    { label: 'Xuất xứ', value: detail.value.origin || 'Đang cập nhật' },
    { label: 'Đơn vị', value: detail.value.unit || 'Đang cập nhật' },
    { label: 'Số lượng', value: detail.value.quantity ?? 'Đang cập nhật' },
    { label: 'Năm sản xuất', value: detail.value.productionYear || 'Đang cập nhật' },
  ]
})

async function loadDetail() {
  loading.value = true
  error.value = ''
  closeProjectLightbox()
  isEquipmentCategory.value = false
  categoryEquipments.value = []

  if (!slug.value) {
    detail.value = null
    error.value = 'Không tải được nội dung chi tiết.'
    loading.value = false
    return
  }

  try {
    if (type.value === 'service') detail.value = await fetchServiceDetail(slug.value)
    else if (type.value === 'project') detail.value = await fetchProjectDetail(slug.value)
    else if (type.value === 'post') detail.value = await fetchPostDetail(slug.value)
     else if (type.value === 'equipment') {
       const categories = await fetchEquipmentCategories()
       const category = categories.find((item) => item.slug === slug.value)

       if (category) {
         isEquipmentCategory.value = true
         detail.value = { name: category.name }
         const all = await fetchEquipments({ categorySlug: slug.value, size: 100 })
         categoryEquipments.value = all.filter((item) => item.categoryId === category.id)
       } else {
         detail.value = await fetchEquipmentDetail(slug.value)
       }
     }
    else if (type.value === 'page') detail.value = await fetchPageBySlug(slug.value)
    else detail.value = null
  } catch {
    error.value = 'Không tải được nội dung chi tiết.'
  } finally {
    loading.value = false
  }
}

function openProjectLightbox(index: number) {
  activeProjectImageIndex.value = index
  isProjectLightboxOpen.value = true
}

function closeProjectLightbox() {
  isProjectLightboxOpen.value = false
  activeProjectImageIndex.value = 0
}

function handleLightboxOpenChange(open: boolean) {
  if (!open) {
    closeProjectLightbox()
    return
  }
  isProjectLightboxOpen.value = true
}

function showPrevProjectImage() {
  if (!projectGalleryImages.value.length) return
  activeProjectImageIndex.value = (activeProjectImageIndex.value - 1 + projectGalleryImages.value.length) % projectGalleryImages.value.length
}

function showNextProjectImage() {
  if (!projectGalleryImages.value.length) return
  activeProjectImageIndex.value = (activeProjectImageIndex.value + 1) % projectGalleryImages.value.length
}

function handleProjectLightboxKeydown(event: KeyboardEvent) {
  if (!isProjectLightboxOpen.value) return
  if (event.key === 'Escape') {
    closeProjectLightbox()
    return
  }
  if (event.key === 'ArrowLeft') {
    showPrevProjectImage()
    return
  }
  if (event.key === 'ArrowRight') {
    showNextProjectImage()
  }
}

watch(() => route.fullPath, loadDetail)
onMounted(() => {
  loadDetail()
  window.addEventListener('keydown', handleProjectLightboxKeydown)
})
onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleProjectLightboxKeydown)
})
</script>
