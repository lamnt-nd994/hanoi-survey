<template>
  <section class="section">
    <div class="container-shell">
      <div class="section-title-center">
<!--        <div v-if="eyebrow" class="eyebrow">{{ eyebrow }}</div>-->
        <h2>{{ title || 'Dự Án Tiêu Biểu' }}</h2>
        <p v-if="description" class="section-subtitle mx-auto mt-4 max-w-3xl">{{ description }}</p>
        <div class="line"></div>
      </div>

      <div class="carousel-wrapper">
        <div class="carousel-track" ref="trackRef">
          <div v-for="(project, index) in projectItems" :key="project.slug || index" class="carousel-slide">
            <div
              class="project-card group relative overflow-hidden rounded-xl"
              style="height: 320px;"
              @mouseenter="hoveredIndex = index"
              @mouseleave="hoveredIndex = null"
            >
              <img
                v-if="project.coverImagePath"
                :src="resolveMediaUrl(project.coverImagePath)"
                :alt="project.title"
                class="h-full w-full object-cover"
                @error="handleImageError"
              />
              <div v-else class="h-full w-full bg-gradient-to-br from-primary-navy to-primary-light" />
              <div class="absolute inset-0 bg-gradient-to-t from-primary-navy/70 via-primary-navy/20 to-transparent" />
              <div
                class="project-overlay absolute inset-0 z-20 flex flex-col items-center justify-center gap-4 p-6 text-white transition-opacity duration-300"
                :class="hoveredIndex === index ? 'opacity-100' : 'opacity-0'"
                :style="{ background: 'linear-gradient(to top, rgba(0, 51, 102, 0.96) 0%, rgba(0, 51, 102, 0.72) 42%, transparent 100%)' }"
              >
                <span class="mb-2 text-sm font-semibold uppercase tracking-wider text-green-400">{{ project.categoryName }}</span>
                <h3 class="text-center text-2xl font-bold text-white md:text-3xl">{{ project.title }}</h3>
                <p class="text-center text-sm text-white/80">{{ project.location || 'Dự án khảo sát và thí nghiệm công trình' }}</p>
                <router-link :to="project.slug ? { name: 'project-detail', params: { slug: project.slug } } : { name: 'projects' }" class="inline-flex items-center rounded-full bg-green-500 px-6 py-3 text-sm font-bold text-white transition-all hover:bg-green-600 hover:scale-105" style="min-height: 44px;">
                  Xem chi tiết
                  <svg class="ml-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                  </svg>
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <button v-if="projectItems.length > slidesVisible" class="carousel-btn carousel-prev" @click="prev" aria-label="Previous">
          <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
          </svg>
        </button>
        <button v-if="projectItems.length > slidesVisible" class="carousel-btn carousel-next" @click="next" aria-label="Next">
          <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </button>

        <div v-if="projectItems.length > slidesVisible" class="carousel-dots">
          <button
            v-for="(_, index) in maxIndex"
            :key="index"
            class="carousel-dot"
            :class="{ active: currentIndex === index }"
            @click="goTo(index)"
            :aria-label="`Go to slide ${index + 1}`"
          ></button>
        </div>
      </div>

      <div v-if="!projectItems.length" class="panel mt-6 p-8 text-center text-neutral-500">
        Chưa có dữ liệu dự án từ hệ thống.
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { resolveMediaUrl } from '../lib/media'
import { usePublicContentStore } from '../stores/publicContent'
import type { HomeSelectedProjectItem } from '../types/content'

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
  title: 'Dự Án Tiêu Biểu',
  description: '',
  mode: 'latest',
  limit: 6,
  selectedItems: () => [],
})
const trackRef = ref<HTMLDivElement | null>(null)
const currentIndex = ref(0)
const windowWidth = ref(window.innerWidth)
const hoveredIndex = ref<number | null>(null)

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

let interval: ReturnType<typeof setInterval> | null = null
let resizeHandler: (() => void) | null = null

const maxIndex = computed(() => {
  const max = projectItems.value.length - slidesVisible.value + 1
  return Math.max(0, max)
})

function updatePosition() {
  if (trackRef.value) {
    trackRef.value.style.transform = `translateX(-${currentIndex.value * (100 / slidesVisible.value)}%)`
  }
}

function next() {
  if (maxIndex.value <= 0) return
  currentIndex.value = currentIndex.value >= maxIndex.value - 1 ? 0 : currentIndex.value + 1
  updatePosition()
}

function prev() {
  if (maxIndex.value <= 0) return
  currentIndex.value = currentIndex.value <= 0 ? maxIndex.value - 1 : currentIndex.value - 1
  updatePosition()
}

function goTo(index: number) {
  if (index >= 0 && index < maxIndex.value) {
    currentIndex.value = index
    updatePosition()
  }
}

function handleImageError(event: Event) {
  const image = event.target as HTMLImageElement | null
  if (!image) return
  image.style.display = 'none'
}

watch([slidesVisible, projectItems], () => {
  currentIndex.value = 0
  nextTick(() => updatePosition())
})

onMounted(async () => {
  await publicContentStore.loadProjects()

  resizeHandler = () => {
    windowWidth.value = window.innerWidth
    updatePosition()
  }
  window.addEventListener('resize', resizeHandler)
  updatePosition()
  interval = setInterval(next, 5000)
})

onUnmounted(() => {
  if (interval) clearInterval(interval)
  if (resizeHandler) window.removeEventListener('resize', resizeHandler)
})
</script>
