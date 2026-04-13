<template>
  <Hero
    :title="homeData.hero.title"
    :subtitle="homeData.hero.subtitle"
    :background-image-path="homeData.hero.backgroundImagePath"
    :badges="homeData.hero.badges"
    :primary-cta-label="homeData.hero.primaryCtaLabel"
    :primary-cta-url="homeData.hero.primaryCtaUrl"
    :secondary-cta-label="homeData.hero.secondaryCtaLabel"
    :secondary-cta-url="homeData.hero.secondaryCtaUrl"
  />

  <StatsBar :items="homeData.stats.items" />

  <section class="py-16 md:py-20">
    <div class="container-shell">
      <div class="grid gap-8 lg:grid-cols-[1.05fr_0.95fr] lg:items-center">
        <div class="overflow-hidden rounded-[2rem] border border-neutral-200 bg-neutral-100 shadow-sm">
          <img
            v-if="aboutImagePath"
            :src="resolveMediaUrl(aboutImagePath)"
            :alt="siteName"
            class="h-[320px] w-full object-cover"
          />
          <div v-else class="flex h-[320px] items-center justify-center bg-[linear-gradient(135deg,#16365f_0%,#264d7e_48%,#d6dde6_48%,#eef2f6_100%)] px-10 text-center text-white">
            <div>
              <div class="text-xs font-semibold uppercase tracking-[0.22em] text-white/70">Hanoi Survey</div>
              <div class="mt-4 font-heading text-3xl font-bold leading-tight">Khảo sát chính xác cho nền móng và hạ tầng</div>
            </div>
          </div>
        </div>

        <div class="max-w-2xl">
          <div class="text-xs font-semibold uppercase tracking-[0.18em] text-neutral-500">{{ homeData.aboutSection.eyebrow }}</div>
          <h2 class="mt-4 font-heading text-4xl font-bold leading-tight text-primary-navy">{{ homeData.aboutSection.title }}</h2>
          <p class="mt-5 text-base leading-8 text-neutral-600">
            {{ homeData.aboutSection.description || companyIntro || `${siteName} cung cấp dịch vụ khảo sát địa chất, địa hình, thủy văn, quan trắc và thí nghiệm xây dựng cho công trình dân dụng, công nghiệp và hạ tầng kỹ thuật.` }}
          </p>

          <div class="mt-7 space-y-3">
            <div v-for="item in aboutHighlights" :key="item" class="flex items-start gap-3 text-[1.05rem] font-medium text-primary-navy">
              <span class="mt-1.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-amber-400 text-[11px] font-bold text-white">•</span>
              <span>{{ item }}</span>
            </div>
          </div>

          <router-link :to="homeData.aboutSection.buttonUrl" class="mt-8 inline-flex items-center gap-2 text-lg font-semibold text-primary-navy underline-offset-4 transition-colors hover:text-accent-green hover:underline">
            {{ homeData.aboutSection.buttonLabel }}
            <span aria-hidden="true">→</span>
          </router-link>
        </div>
      </div>
    </div>
  </section>

  <ServicesGrid
    :eyebrow="homeData.servicesSection.eyebrow"
    :title="homeData.servicesSection.title"
    :description="homeData.servicesSection.description"
    :mode="homeData.servicesSection.mode"
    :limit="homeData.servicesSection.limit"
    :selected-items="homeData.servicesSection.selectedItems"
  />

<!--  <TrustSection />-->

<!--  <ProcessSection />-->

  <ProjectsCarousel
    :eyebrow="homeData.projectsSection.eyebrow"
    :title="homeData.projectsSection.title"
    :description="homeData.projectsSection.description"
    :mode="homeData.projectsSection.mode"
    :limit="homeData.projectsSection.limit"
    :selected-items="homeData.projectsSection.selectedItems"
  />

  <section class="py-16 md:py-24">
    <div class="container-shell">
      <div class="panel rounded-[32px] p-8 md:p-10 lg:p-12 text-center">
        <div class="mx-auto max-w-3xl">
          <div class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500 md:text-sm">{{ homeData.finalCta.eyebrow }}</div>
          <h2 class="section-title mt-4 mx-auto max-w-3xl">{{ homeData.finalCta.title }}</h2>
          <p class="section-subtitle mt-4 mx-auto max-w-2xl">{{ homeData.finalCta.description }}</p>

          <div class="mt-8 flex flex-wrap items-center justify-center gap-4">
            <router-link :to="homeData.finalCta.primaryButtonUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition-colors hover:bg-slate-700">
              {{ homeData.finalCta.primaryButtonLabel }}
            </router-link>
            <router-link :to="homeData.finalCta.secondaryButtonUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full border border-slate-300 px-6 py-3 text-sm font-semibold text-slate-700 transition-colors hover:bg-slate-50">
              {{ homeData.finalCta.secondaryButtonLabel }}
            </router-link>
            <a v-if="homeData.finalCta.showPhoneButton && mergedContactInfo.phone" :href="`tel:${mergedContactInfo.phone}`" class="btn-outline">
              Gọi ngay: {{ mergedContactInfo.phone }}
            </a>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import Hero from '../components/Hero.vue'
import StatsBar from '../components/StatsBar.vue'
import ServicesGrid from '../components/ServicesGrid.vue'
import ProjectsCarousel from '../components/ProjectsCarousel.vue'
import { resolveMediaUrl } from '../lib/media'
import { usePublicContentStore } from '../stores/publicContent'
import { useSiteSettingsStore } from '../stores/siteSettings'
import type { HomePageContent } from '../types/content'

const siteSettingsStore = useSiteSettingsStore()
const publicContentStore = usePublicContentStore()
const { mergedContactInfo, siteName, companyIntro, heroBannerPath } = storeToRefs(siteSettingsStore)
const { homePage } = storeToRefs(publicContentStore)

const homeData = computed<HomePageContent>(() => parseHomeContent(homePage.value?.contentJson))
const aboutHighlights = computed(() => homeData.value.aboutSection.highlights.filter((item) => item.trim()))
const aboutImagePath = computed(() => homeData.value.aboutSection.imagePath || heroBannerPath.value)

onMounted(async () => {
  await Promise.all([
    siteSettingsStore.ensureLoaded(),
    publicContentStore.loadHomePage(),
  ])
})

function createEmptyHomeContent(): HomePageContent {
  return {
    hero: {
      title: '',
      subtitle: '',
      backgroundImagePath: '',
      badges: [
        { label: 'Dia hinh', iconKey: 'terrain' },
        { label: 'Dia chat', iconKey: 'geology' },
        { label: 'Thi nghiem', iconKey: 'testing' },
        { label: 'Quan trac', iconKey: 'monitoring' },
        { label: 'Giam sat', iconKey: 'supervision' },
        { label: 'Xay lap', iconKey: 'construction' },
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
        { value: '24/7', label: 'Hỗ trợ khách hàng' },
      ],
    },
    aboutSection: {
      eyebrow: 'Về chúng tôi',
      title: 'Đơn vị khảo sát xây dựng tập trung vào độ chính xác và tiến độ thực địa',
      description: '',
      imagePath: '',
      highlights: [
        'Thiết bị khảo sát và đo đạc được bố trí theo yêu cầu từng công trình.',
        'Đội ngũ kỹ sư bám sát hiện trường, phối hợp nhanh với chủ đầu tư và tư vấn thiết kế.',
        'Quy trình triển khai gọn, hồ sơ bàn giao rõ ràng, phù hợp tiến độ thi công.',
      ],
      buttonLabel: 'Xem hồ sơ năng lực',
      buttonUrl: '/gioi-thieu',
    },
    servicesSection: {
      eyebrow: '',
      title: 'Các Dịch Vụ Chính',
      description: '',
      mode: 'latest',
      limit: 6,
      selectedItems: [],
    },
    projectsSection: {
      eyebrow: '',
      title: 'Dự Án Tiêu Biểu',
      description: '',
      mode: 'latest',
      limit: 6,
      selectedItems: [],
    },
    finalCta: {
      eyebrow: 'Tư vấn nhanh ngoài hiện trường',
      title: 'Sẵn sàng khảo sát cho dự án mới?',
      description: 'Đội kỹ sư của Hanoi Survey có thể hỗ trợ từ giai đoạn lập phương án khảo sát, báo giá, bố trí nhân lực đến bàn giao hồ sơ đúng tiến độ.',
      items: [
        'Trong ngày',
        'Miền Bắc',
        'Nhà xưởng, khu công nghiệp, giao thông, hạ tầng kỹ thuật, dân dụng cao tầng, kiểm định vật liệu và quan trắc công trình.',
      ],
      primaryButtonLabel: 'Xem dự án',
      primaryButtonUrl: '/du-an',
      secondaryButtonLabel: 'Liên hệ',
      secondaryButtonUrl: '/lien-he',
      showPhoneButton: true,
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
        categoryId: Number(item.categoryId) || null,
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
</script>
