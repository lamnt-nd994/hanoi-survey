<template>
  <div>
    <section class="relative overflow-hidden py-20 text-white md:py-28">
      <div
        class="absolute inset-0 bg-primary-navy"
        :style="heroBackgroundStyle"
      />
      <div class="absolute inset-0 bg-[linear-gradient(135deg,rgba(0,0,0,0.45),rgba(0,28,56,0.78))]" />
      <div class="container-shell relative text-center">
<!--        <div class="eyebrow inline-block text-white/75">Giới thiệu</div>-->
        <h1 class="section-title mt-4 text-white">{{ aboutData.hero.title || page?.title || 'Về Chúng Tôi' }}</h1>
        <p class="section-subtitle mx-auto mt-4 max-w-3xl text-neutral-200">{{ page?.summary || '' }}</p>
      </div>
    </section>

    <section class="container-shell py-16 md:py-20">
      <div v-if="loadingState" class="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        <div v-for="index in 6" :key="index" class="panel h-40 animate-pulse bg-neutral-100"></div>
      </div>

      <div v-else-if="errorMessage" class="panel p-8 text-rose-600">{{ errorMessage }}</div>

      <template v-else>
        <section class="mx-auto max-w-4xl text-center">
          <h2 class="font-heading text-3xl font-bold text-primary-navy">{{ aboutData.intro.heading }}</h2>
          <div class="prose prose-slate mx-auto mt-6 max-w-none text-left text-base leading-8 text-neutral-600" v-html="aboutData.intro.content"></div>
        </section>

        <section class="mt-16 rounded-[2rem] bg-neutral-50 px-6 py-10 md:px-10 md:py-14">
          <div class="text-center">
            <h2 class="section-title text-primary-navy">{{ aboutData.coreValues.sectionTitle }}</h2>
            <div class="mx-auto mt-4 h-1 w-20 rounded-full bg-accent-green"></div>
          </div>
          <div class="mt-10 grid gap-6 md:grid-cols-3">
            <article v-for="(item, index) in valueItems" :key="`${item.title}-${index}`" class="panel border-t-4 border-primary-navy p-8 text-center shadow-sm">
              <div class="mx-auto flex h-16 w-16 items-center justify-center rounded-2xl bg-accent-green/10 text-accent-green">
                <span class="block h-8 w-8" v-html="resolveValueIcon(item.icon)"></span>
              </div>
              <h3 class="mt-6 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
              <p class="mt-4 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
            </article>
          </div>
        </section>

        <section class="mt-16">
          <div class="text-center">
            <h2 class="section-title text-primary-navy">{{ aboutData.timeline.sectionTitle }}</h2>
            <div class="mx-auto mt-4 h-1 w-20 rounded-full bg-accent-green"></div>
          </div>
          <div class="relative mx-auto mt-12 max-w-5xl">
            <div class="absolute bottom-0 left-1/2 top-0 hidden w-1 -translate-x-1/2 rounded-full bg-neutral-200 md:block"></div>
            <div class="space-y-6 md:space-y-0">
              <div
                v-for="(item, index) in timelineItems"
                :key="`${item.year}-${index}`"
                class="relative md:grid md:grid-cols-2 md:gap-10"
              >
                <div v-if="index % 2 === 0" class="md:pr-10">
                  <article class="panel p-6 md:text-right">
                    <div class="text-sm font-semibold uppercase tracking-[0.2em] text-accent-green">{{ item.year }}</div>
                    <h3 class="mt-3 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
                    <p class="mt-3 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
                  </article>
                </div>
                <div class="hidden md:block"></div>
                <div class="absolute left-1/2 top-8 hidden h-5 w-5 -translate-x-1/2 rounded-full border-4 border-accent-green bg-white md:block"></div>
                <div v-if="index % 2 === 1" class="md:col-start-2 md:pl-10">
                  <article class="panel p-6">
                    <div class="text-sm font-semibold uppercase tracking-[0.2em] text-accent-green">{{ item.year }}</div>
                    <h3 class="mt-3 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
                    <p class="mt-3 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
                  </article>
                </div>
                <article v-if="isMobile" class="panel mt-4 p-6 md:hidden">
                  <div class="text-sm font-semibold uppercase tracking-[0.2em] text-accent-green">{{ item.year }}</div>
                  <h3 class="mt-3 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
                  <p class="mt-3 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
                </article>
              </div>
            </div>
          </div>
        </section>

        <section class="mt-16 rounded-[2rem] bg-neutral-50 px-6 py-10 md:px-10 md:py-14">
          <div class="text-center">
            <h2 class="section-title text-primary-navy">{{ aboutData.organization.sectionTitle }}</h2>
            <div class="mx-auto mt-4 h-1 w-20 rounded-full bg-accent-green"></div>
          </div>
          <div class="mt-10 grid gap-8 lg:grid-cols-[1.1fr_0.9fr] lg:items-center">
            <div class="panel p-4">
              <img
                v-if="aboutData.organization.chartImagePath"
                :src="resolveMediaUrl(aboutData.organization.chartImagePath)"
                :alt="aboutData.organization.chartCaption || aboutData.organization.heading"
                class="h-full w-full rounded-2xl border border-neutral-200 object-cover"
              />
              <div v-else class="flex min-h-72 items-center justify-center rounded-2xl border border-dashed border-neutral-300 bg-white text-center text-sm text-neutral-400">
                Chưa có ảnh sơ đồ tổ chức
              </div>
              <p class="mt-4 text-center text-sm italic text-neutral-500">{{ aboutData.organization.chartCaption }}</p>
            </div>
            <div>
              <h3 class="font-heading text-3xl font-bold text-primary-navy">{{ aboutData.organization.heading }}</h3>
              <p class="mt-4 text-base leading-8 text-neutral-600">{{ aboutData.organization.description }}</p>
              <ul class="mt-8 space-y-4">
                <li v-for="(department, index) in departments" :key="`${department}-${index}`" class="flex items-start gap-3 rounded-2xl bg-white px-4 py-4 shadow-sm">
                  <span class="mt-1 flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-primary-navy text-xs font-bold text-white">{{ index + 1 }}</span>
                  <span class="text-sm font-medium leading-7 text-neutral-700">{{ department }}</span>
                </li>
              </ul>
            </div>
          </div>
        </section>

        <section class="mt-16">
<!--          <div class="text-center">-->
<!--            <h2 class="section-title text-primary-navy">{{ aboutData.capability.sectionTitle }}</h2>-->
<!--            <div class="mx-auto mt-4 h-1 w-20 rounded-full bg-accent-green"></div>-->
<!--          </div>-->
          <div class="mt-10 grid gap-8 lg:grid-cols-[0.95fr_1.05fr] lg:items-center">
            <div>
              <h3 class="font-heading text-3xl font-bold text-primary-navy">{{ aboutData.capability.heading }}</h3>
              <p class="mt-4 text-base leading-8 text-neutral-600">{{ aboutData.capability.description }}</p>
              <ul class="mt-8 space-y-4">
                <li v-for="(item, index) in capabilityItems" :key="`${item.title}-${index}`" class="rounded-2xl border border-neutral-200 bg-white px-5 py-4 shadow-sm">
                  <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                    <div class="flex items-start gap-3">
                      <span class="mt-1 text-accent-green">✓</span>
                      <span class="text-sm font-medium leading-7 text-neutral-700">{{ item.title }}</span>
                    </div>
                    <a
                      v-if="item.pdfFilePath"
                      :href="resolveMediaUrl(item.pdfFilePath)"
                      target="_blank"
                      rel="noopener noreferrer"
                      class="inline-flex items-center justify-center rounded-full border border-primary-navy px-4 py-2 text-sm font-semibold text-primary-navy transition hover:bg-primary-navy hover:text-white"
                    >
                      {{ item.buttonLabel || 'Xem PDF' }}
                    </a>
                  </div>
                </li>
              </ul>
            </div>
            <div class="panel p-4">
              <img
                v-if="aboutData.capability.imagePath"
                :src="resolveMediaUrl(aboutData.capability.imagePath)"
                :alt="aboutData.capability.heading || aboutData.capability.sectionTitle"
                class="h-full w-full rounded-2xl border border-neutral-200 object-cover"
              />
              <div v-else class="flex min-h-72 items-center justify-center rounded-2xl border border-dashed border-neutral-300 bg-white text-center text-sm text-neutral-400">
                Chưa có ảnh hồ sơ năng lực
              </div>
            </div>
          </div>
        </section>
      </template>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { usePublicContentStore } from '../stores/publicContent'
import { resolveMediaUrl } from '../lib/media'
import type { AboutCapabilityItem, AboutPageContent, AboutPageTimelineItem, AboutPageValueItem } from '../types/content'

const publicContentStore = usePublicContentStore()
const { introPage: page, loading, errors } = storeToRefs(publicContentStore)

const loadingState = computed(() => loading.value.introPage)
const errorMessage = computed(() => errors.value.introPage)

const aboutData = computed<AboutPageContent>(() => parseAboutContent(page.value?.contentJson))
const valueItems = computed<AboutPageValueItem[]>(() => aboutData.value.coreValues.items.filter((item) => item.title.trim() || item.description.trim()))
const timelineItems = computed<AboutPageTimelineItem[]>(() => [...aboutData.value.timeline.items]
  .filter((item) => item.year.trim() || item.title.trim() || item.description.trim())
  .sort((first, second) => first.sortOrder - second.sortOrder))
const departments = computed(() => aboutData.value.organization.departments.filter((item) => item.trim()))
const capabilityItems = computed<AboutCapabilityItem[]>(() => aboutData.value.capability.items.filter((item) => item.title.trim()))
const isMobile = computed(() => typeof window !== 'undefined' && window.innerWidth < 768)

const heroBackgroundStyle = computed(() => {
  const path = aboutData.value.hero.backgroundImagePath
  return path ? { backgroundImage: `url(${resolveMediaUrl(path)})`, backgroundSize: 'cover', backgroundPosition: 'center' } : undefined
})

onMounted(async () => {
  await publicContentStore.loadIntroPage()
})

function parseAboutContent(raw: string | null | undefined): AboutPageContent {
  const fallback = createEmptyAboutContent()
  if (!raw) return fallback

  try {
    return normalizeAboutContent(JSON.parse(raw))
  } catch {
    return fallback
  }
}

function createEmptyAboutContent(): AboutPageContent {
  return {
    hero: { title: '', backgroundImagePath: '' },
    intro: { heading: '', content: '<p></p>' },
    coreValues: { sectionTitle: 'Sứ Mệnh & Tầm Nhìn', items: [] },
    timeline: { sectionTitle: 'Quá Trình Phát Triển', items: [] },
    organization: {
      sectionTitle: 'Cơ Cấu Tổ Chức',
      heading: '',
      description: '',
      chartImagePath: '',
      chartCaption: '',
      departments: [],
    },
    capability: {
      sectionTitle: 'Hồ Sơ Năng Lực',
      heading: '',
      description: '',
      imagePath: '',
      items: [],
    },
  }
}

function normalizeAboutContent(input: Partial<AboutPageContent> | AboutPageContent): AboutPageContent {
  const fallback = createEmptyAboutContent()
  return {
    hero: {
      title: input.hero?.title || fallback.hero.title,
      backgroundImagePath: input.hero?.backgroundImagePath || fallback.hero.backgroundImagePath,
    },
    intro: {
      heading: input.intro?.heading || fallback.intro.heading,
      content: normalizeIntroContent(input.intro),
    },
    coreValues: {
      sectionTitle: input.coreValues?.sectionTitle || fallback.coreValues.sectionTitle,
      items: (input.coreValues?.items || fallback.coreValues.items).map((item) => ({
        title: item.title || '',
        icon: item.icon || '',
        description: item.description || '',
      })),
    },
    timeline: {
      sectionTitle: input.timeline?.sectionTitle || fallback.timeline.sectionTitle,
      items: (input.timeline?.items || fallback.timeline.items).map((item, index) => ({
        year: item.year || '',
        title: item.title || '',
        description: item.description || '',
        sortOrder: Number(item.sortOrder) || index + 1,
      })),
    },
    organization: {
      sectionTitle: input.organization?.sectionTitle || fallback.organization.sectionTitle,
      heading: input.organization?.heading || fallback.organization.heading,
      description: input.organization?.description || fallback.organization.description,
      chartImagePath: input.organization?.chartImagePath || fallback.organization.chartImagePath,
      chartCaption: input.organization?.chartCaption || fallback.organization.chartCaption,
      departments: (input.organization?.departments || fallback.organization.departments).map((item) => item || ''),
    },
    capability: {
      sectionTitle: input.capability?.sectionTitle || fallback.capability.sectionTitle,
      heading: input.capability?.heading || fallback.capability.heading,
      description: input.capability?.description || fallback.capability.description,
      imagePath: input.capability?.imagePath || fallback.capability.imagePath,
      items: (input.capability?.items || fallback.capability.items).map((item) => ({
        title: item.title || '',
        pdfFilePath: item.pdfFilePath || '',
        buttonLabel: item.buttonLabel || 'Xem PDF',
      })),
    },
  }
}

const iconMap = {
  bullseye: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="9" /><circle cx="12" cy="12" r="5" /><circle cx="12" cy="12" r="1.5" /></svg>',
  eye: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 12s3.5-6 10-6 10 6 10 6-3.5 6-10 6S2 12 2 12Z" /><circle cx="12" cy="12" r="3" /></svg>',
  gem: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m6 3-4 6 10 12L22 9l-4-6H6Z" /><path d="M2 9h20" /><path d="m8 3 4 18 4-18" /></svg>',
  default: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 6v6l4 2" /><circle cx="12" cy="12" r="9" /></svg>',
}

function resolveValueIcon(iconKey: string) {
  return iconMap[iconKey as keyof typeof iconMap] || iconMap.default
}

function normalizeIntroContent(intro?: { content?: string; paragraphs?: string[] }) {
  const html = intro?.content?.trim()
  if (html) return html

  const paragraphs = (intro?.paragraphs || []).map((item) => item.trim()).filter(Boolean)
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
</script>
