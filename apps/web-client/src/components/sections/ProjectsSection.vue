<template>
  <section class="section">
    <div class="container-shell">
      <SectionHeader :eyebrow="eyebrow" :title="title || 'Dự án tiêu biểu'" :description="description" centered show-divider />

      <div class="carousel-wrapper mt-20">
        <div class="carousel-track" :style="trackStyle">
          <div v-for="(project, index) in projectItems" :key="project.slug || index" class="carousel-slide">
            <router-link
              :to="project.slug ? { name: 'project-detail', params: { slug: project.slug } } : { name: 'projects' }"
              class="group relative block h-[360px] overflow-hidden rounded-lg bg-primary-navy shadow-lg transition-transform duration-300 hover:-translate-y-1 focus:outline-none focus:ring-2 focus:ring-accent-green focus:ring-offset-2"
            >
              <div class="absolute inset-0 bg-gradient-to-br from-primary-navy to-primary-light">
                <img
                  v-if="project.coverImagePath"
                  :src="resolveMediaUrl(project.coverImagePath)"
                  :alt="project.title"
                  width="420"
                  height="220"
                  loading="lazy"
                  decoding="async"
                  class="h-full w-full object-cover transition-transform duration-700 group-hover:scale-105"
                  @error="handleImageError"
                />
                <div v-else class="h-full w-full bg-gradient-to-br from-primary-navy to-primary-light" />
              </div>

              <div class="absolute inset-0 bg-gradient-to-t from-primary-navy via-primary-navy/45 to-transparent" />
              <div class="absolute left-5 right-5 top-5 flex items-center justify-between gap-3">
                <span v-if="project.categoryName" class="rounded-full border border-white/25 bg-white/15 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.14em] text-slate-700 backdrop-blur-sm">{{ project.categoryName }}</span>
                <span class="ml-auto rounded-full bg-accent-green px-3 py-1 text-xs font-bold text-white">0{{ index + 1 }}</span>
              </div>

              <div class="absolute inset-x-0 bottom-0 p-5 text-white">
                <h3 class="font-heading text-2xl font-bold leading-tight drop-shadow-md transition-colors">{{ project.title }}</h3>
                <p class="mt-3 line-clamp-2 text-sm leading-6 text-white/85">{{ project.location || 'Dự án khảo sát và thí nghiệm công trình' }}</p>
                <span class="mt-5 inline-flex items-center gap-2 text-sm font-semibold text-white transition-colors">
                  Xem dự án
                  <AppIcon icon="arrowRight" class="h-4 w-4 transition-transform group-hover:translate-x-1" />
                </span>
              </div>
            </router-link>
          </div>
        </div>

        <Button v-if="shouldShowControls" variant="secondary" size="icon" class="absolute left-4 top-1/2 z-10 h-12 w-12 -translate-y-1/2 rounded-full border border-gray-300 bg-white text-primary-navy shadow-lg hover:border-accent-green hover:bg-accent-green hover:text-white" @click="prev" aria-label="Previous">
          <AppIcon icon="chevronLeft" class="h-6 w-6" />
        </Button>
        <Button v-if="shouldShowControls" variant="secondary" size="icon" class="absolute right-4 top-1/2 z-10 h-12 w-12 -translate-y-1/2 rounded-full border border-gray-300 bg-white text-primary-navy shadow-lg hover:border-accent-green hover:bg-accent-green hover:text-white" @click="next" aria-label="Next">
          <AppIcon icon="chevronRight" class="h-6 w-6" />
        </Button>

        <div v-if="shouldShowControls" class="mt-6 flex justify-center gap-3">
          <Button v-for="(_, index) in maxIndex" :key="index" variant="ghost" class="h-3 min-h-0 min-w-0 rounded-full p-0 transition-all" :class="currentIndex === index ? 'w-8 bg-accent-green hover:bg-accent-green' : 'w-3 bg-gray-300 hover:bg-accent-green/70'" @click="goTo(index)" :aria-label="`Go to slide ${index + 1}`" />
        </div>
      </div>

      <Card v-if="!projectItems.length" class="mt-6 p-8 text-center text-neutral-500">Chưa có dữ liệu dự án từ hệ thống.</Card>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { Button } from '../ui/button'
import { Card } from '../ui/card'
import AppIcon from '../ui/AppIcon.vue'
import SectionHeader from '../ui/SectionHeader.vue'
import { resolveMediaUrl } from '../../lib/media'
import { usePublicContentStore } from '../../stores/publicContent'
import type { HomeSelectedProjectItem } from '../../types/content'

const publicContentStore = usePublicContentStore()
const props = withDefaults(defineProps<{
  eyebrow?: string
  title?: string
  description?: string
  mode?: 'latest' | 'manual'
  limit?: number
  selectedItems?: HomeSelectedProjectItem[]
}>(), {
  eyebrow: '',
  title: 'Dự án tiêu biểu',
  description: '',
  mode: 'latest',
  limit: 6,
  selectedItems: () => [],
})

const currentIndex = ref(0)
const windowWidth = ref(window.innerWidth)
const documentVisible = ref(typeof document === 'undefined' ? true : !document.hidden)

const projectItems = computed(() => {
  if (props.mode !== 'manual') {
    return publicContentStore.projects.slice(0, props.limit)
  }

  return props.selectedItems
    .map((selected) => publicContentStore.projects.find((item) => item.id === selected.projectId) || null)
    .filter((project): project is NonNullable<typeof project> => project !== null)
})

const slidesVisible = computed(() => {
  if (windowWidth.value < 768) return 1
  if (windowWidth.value < 1024) return 2
  return 3
})

const maxIndex = computed(() => {
  const max = projectItems.value.length - slidesVisible.value + 1
  return Math.max(0, max)
})

const shouldShowControls = computed(() => projectItems.value.length > slidesVisible.value)

const trackStyle = computed(() => ({
  transform: `translateX(-${currentIndex.value * (100 / slidesVisible.value)}%)`,
}))

let interval: ReturnType<typeof setInterval> | null = null
let resizeHandler: (() => void) | null = null
let visibilityHandler: (() => void) | null = null

function next() {
  if (maxIndex.value <= 0) return
  currentIndex.value = currentIndex.value >= maxIndex.value - 1 ? 0 : currentIndex.value + 1
}

function prev() {
  if (maxIndex.value <= 0) return
  currentIndex.value = currentIndex.value <= 0 ? maxIndex.value - 1 : currentIndex.value - 1
}

function goTo(index: number) {
  if (index >= 0 && index < maxIndex.value) currentIndex.value = index
}

function handleImageError(event: Event) {
  const image = event.target as HTMLImageElement | null
  if (!image) return
  image.style.display = 'none'
}

function startAutoplay() {
  if (interval || !shouldShowControls.value || !documentVisible.value) return
  interval = setInterval(next, 5000)
}

function stopAutoplay() {
  if (!interval) return
  clearInterval(interval)
  interval = null
}

watch([slidesVisible, projectItems], () => {
  currentIndex.value = 0
})

watch([shouldShowControls, documentVisible], () => {
  stopAutoplay()
  startAutoplay()
}, { immediate: true })

onMounted(async () => {
  await publicContentStore.loadProjects()

  resizeHandler = () => {
    windowWidth.value = window.innerWidth
  }

  visibilityHandler = () => {
    documentVisible.value = !document.hidden
  }

  window.addEventListener('resize', resizeHandler)
  document.addEventListener('visibilitychange', visibilityHandler)
})

onUnmounted(() => {
  stopAutoplay()
  if (resizeHandler) window.removeEventListener('resize', resizeHandler)
  if (visibilityHandler) document.removeEventListener('visibilitychange', visibilityHandler)
})
</script>
