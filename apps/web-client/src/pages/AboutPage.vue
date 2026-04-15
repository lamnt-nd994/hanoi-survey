<template>
  <div>
    <section class="container-shell py-16 md:py-20">
      <div v-if="loadingState" class="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        <Skeleton v-for="index in 6" :key="index" class="h-40 rounded-2xl" />
      </div>

      <Card v-else-if="errorMessage" class="p-8 text-rose-600">{{ errorMessage }}</Card>

      <template v-else>
        <section class="py-2 md:py-4">
          <div class="grid gap-10 lg:grid-cols-[minmax(0,1fr)_minmax(420px,0.92fr)] lg:items-start lg:gap-16">
            <div class="max-w-4xl pt-2">
              <div class="flex items-center gap-6">
                <div class="h-14 w-2 shrink-0 bg-orange-500 md:h-18"></div>
                <div>
                  <h1 class="font-heading font-extrabold tracking-tight section-title text-primary-navy">
                    {{ introTitle }}
                  </h1>
                </div>
              </div>
              <div class="about-intro-copy prose prose-slate mt-4 max-w-none text-left text-[1.15rem] leading-[1.95] text-neutral-600" v-html="cleanedIntroContent"></div>
            </div>

            <div class="lg:pt-[4.6rem]">
              <div class="overflow-hidden rounded-[1.75rem] bg-neutral-100 shadow-[0_20px_50px_rgba(15,23,42,0.14)]">
                <img
                  v-if="introImagePath"
                  :src="resolveMediaUrl(introImagePath)"
                  :alt="introTitle"
                  width="720"
                  height="470"
                  loading="eager"
                  fetchpriority="high"
                  decoding="async"
                  class="h-[320px] w-full object-cover md:h-[430px] lg:h-[470px]"
                />
                <div v-else class="flex h-[320px] items-center justify-center bg-[linear-gradient(135deg,#214c79_0%,#3f6a94_55%,#d8e0e8_55%,#eef3f7_100%)] px-10 text-center text-white md:h-[430px] lg:h-[470px]">
                  <div>
                    <div class="text-xs font-semibold uppercase tracking-[0.24em] text-white/70">Hanoi Survey</div>
                    <div class="mt-4 font-heading text-3xl font-bold leading-tight">Hình ảnh công trình và hoạt động khảo sát</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section class="mt-16 border-t border-neutral-200 pt-14">
          <div class="max-w-5xl">
            <h2 class="section-title text-primary-navy">Thông tin doanh nghiệp</h2>
            <div class="mt-4 h-1 w-20 rounded-full bg-accent-green"></div>
            <div class="mt-10 space-y-10 text-[1.05rem] leading-8 text-neutral-600 md:text-[1.08rem]">
              <div>
                <h3 class="text-sm font-semibold uppercase tracking-[0.18em] text-primary-navy">Tên nhà thầu</h3>
                <div class="mt-4 space-y-3">
                  <p><span class="font-semibold text-neutral-900">Tên công ty:</span> {{ companySettings.siteName }}</p>
                  <p v-if="companySettings.companyTradeName || companySettings.companyNameEn"><span class="font-semibold text-neutral-900">Tên giao dịch:</span> {{ companySettings.companyTradeName || companySettings.companyNameEn }}</p>
                </div>
              </div>

              <div>
                <h3 class="text-sm font-semibold uppercase tracking-[0.18em] text-primary-navy">Trụ sở chính</h3>
                <div class="mt-4 space-y-3">
                  <p v-if="companySettings.officeAddress"><span class="font-semibold text-neutral-900">Địa chỉ:</span> {{ companySettings.officeAddress }}</p>
                  <p v-if="companySettings.phone"><span class="font-semibold text-neutral-900">Điện thoại:</span> {{ companySettings.phone }}</p>
                  <p v-if="companySettings.email"><span class="font-semibold text-neutral-900">Email:</span> {{ companySettings.email }}</p>
                  <p v-if="companySettings.taxCode"><span class="font-semibold text-neutral-900">Mã số thuế:</span> {{ companySettings.taxCode }}</p>
                  <p v-if="companySettings.website"><span class="font-semibold text-neutral-900">Website:</span> {{ companySettings.website }}</p>
                  <p v-if="representativeLine"><span class="font-semibold text-neutral-900">Người đại diện:</span> <span class="font-semibold text-neutral-900">{{ representativeLine }}</span> <span> - {{representativeTitle}}</span></p>
                </div>
              </div>

              <div v-if="companySettings.establishmentInfo">
                <h3 class="text-sm font-semibold uppercase tracking-[0.18em] text-primary-navy">Nơi và năm thành lập</h3>
                <p class="mt-4 whitespace-pre-line">{{ companySettings.establishmentInfo }}</p>
              </div>
            </div>
          </div>
        </section>

        <section class="mt-16 rounded-[2rem] bg-neutral-50 px-6 py-10 md:px-10 md:py-14">
          <div class="text-center">
            <h2 class="section-title text-primary-navy">{{ aboutData.coreValues.sectionTitle }}</h2>
            <div class="mx-auto mt-4 h-1 w-20 rounded-full bg-accent-green"></div>
          </div>
          <div class="mt-10 grid gap-6 md:grid-cols-3">
            <Card v-for="(item, index) in valueItems" :key="`${item.title}-${index}`" as="article" class="border-t-4 border-primary-navy p-8 text-center shadow-sm">
              <div class="mx-auto flex h-16 w-16 items-center justify-center rounded-2xl bg-accent-green/10 text-accent-green">
                <span class="block h-8 w-8" v-html="resolveValueIcon(item.icon)"></span>
              </div>
              <h3 class="mt-6 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
              <p class="mt-4 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
            </Card>
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
                  <Card as="article" class="p-6 md:text-right">
                    <div class="text-sm font-semibold uppercase tracking-[0.2em] text-accent-green">{{ item.year }}</div>
                    <h3 class="mt-3 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
                    <p class="mt-3 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
                  </Card>
                </div>
                <div class="hidden md:block"></div>
                <div class="absolute left-1/2 top-8 hidden h-5 w-5 -translate-x-1/2 rounded-full border-4 border-accent-green bg-white md:block"></div>
                <div v-if="index % 2 === 1" class="md:col-start-2 md:pl-10">
                  <Card as="article" class="p-6">
                    <div class="text-sm font-semibold uppercase tracking-[0.2em] text-accent-green">{{ item.year }}</div>
                    <h3 class="mt-3 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
                    <p class="mt-3 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
                  </Card>
                </div>
                <Card v-if="isMobile" as="article" class="mt-4 p-6 md:hidden">
                  <div class="text-sm font-semibold uppercase tracking-[0.2em] text-accent-green">{{ item.year }}</div>
                  <h3 class="mt-3 font-heading text-2xl font-bold text-primary-navy">{{ item.title }}</h3>
                  <p class="mt-3 text-sm leading-7 text-neutral-600">{{ item.description }}</p>
                </Card>
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
            <Card class="p-4">
              <img
                v-if="aboutData.organization.chartImagePath"
                :src="resolveMediaUrl(aboutData.organization.chartImagePath)"
                :alt="aboutData.organization.chartCaption || aboutData.organization.heading"
                width="720"
                height="480"
                loading="lazy"
                decoding="async"
                class="h-full w-full rounded-2xl border border-neutral-200 object-cover"
              />
              <div v-else class="flex min-h-72 items-center justify-center rounded-2xl border border-dashed border-neutral-300 bg-white text-center text-sm text-neutral-400">
                Chưa có ảnh sơ đồ tổ chức
              </div>
              <p class="mt-4 text-center text-sm italic text-neutral-500">{{ aboutData.organization.chartCaption }}</p>
            </Card>
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
            <div>
              <img
                v-if="aboutData.capability.imagePath"
                :src="resolveMediaUrl(aboutData.capability.imagePath)"
                :alt="aboutData.capability.heading || aboutData.capability.sectionTitle"
                width="720"
                height="480"
                loading="lazy"
                decoding="async"
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
import { useSiteSettingsStore } from '../stores/siteSettings'
import { resolveMediaUrl } from '../lib/media'
import { Card } from '../components/ui/card'
import { Skeleton } from '../components/ui/skeleton'
import type { AboutCapabilityItem, AboutPageContent, AboutPageTimelineItem, AboutPageValueItem } from '../types/content'

const publicContentStore = usePublicContentStore()
const siteSettingsStore = useSiteSettingsStore()
const { introPage: page, loading, errors } = storeToRefs(publicContentStore)
const companySettings = computed(() => siteSettingsStore.settings || {
  siteName: '',
  companyNameEn: '',
  companyTradeName: '',
  officeAddress: '',
  phone: '',
  email: '',
  taxCode: '',
  website: '',
  representativeName: '',
  representativeTitle: '',
  establishmentInfo: '',
})

const loadingState = computed(() => loading.value.introPage)
const errorMessage = computed(() => errors.value.introPage)

const aboutData = computed<AboutPageContent>(() => parseAboutContent(page.value?.contentJson))
const introTitle = computed(() => aboutData.value.hero.title || page.value?.title || 'Về Chúng Tôi')
const cleanedIntroContent = computed(() => stripRepresentativeParagraph(aboutData.value.intro.content))
const representativeLine = computed(() => {
  return [companySettings.value.representativeName]
    .filter(Boolean)
    .join(' - ')
    .trim()
})

const representativeTitle = computed(() => { return companySettings.value.representativeTitle })

const introImagePath = computed(() => {
  return aboutData.value.intro.imagePath || ''
})
const valueItems = computed<AboutPageValueItem[]>(() => aboutData.value.coreValues.items.filter((item) => item.title.trim() || item.description.trim()))
const timelineItems = computed<AboutPageTimelineItem[]>(() => [...aboutData.value.timeline.items]
  .filter((item) => item.year.trim() || item.title.trim() || item.description.trim())
  .sort((first, second) => first.sortOrder - second.sortOrder))
const departments = computed(() => aboutData.value.organization.departments.filter((item) => item.trim()))
const capabilityItems = computed<AboutCapabilityItem[]>(() => aboutData.value.capability.items.filter((item) => item.title.trim()))
const isMobile = computed(() => typeof window !== 'undefined' && window.innerWidth < 768)

onMounted(async () => {
  await Promise.all([
    publicContentStore.loadIntroPage(),
    siteSettingsStore.ensureLoaded(),
  ])
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

function stripRepresentativeParagraph(html: string) {
  if (!html) return ''
  return html
    .replace(/<p\b[^>]*>(?:(?!<\/p>).)*Người\s*đại\s*diện:(?:(?!<\/p>).)*<\/p>/giu, '')
    .replace(/<p\b[^>]*>(?:\s|&nbsp;|<br\s*\/?>)*<\/p>/giu, '')
    .trim()
}

function createEmptyAboutContent(): AboutPageContent {
  return {
    hero: { title: 'Về chúng tôi', backgroundImagePath: '' },
    intro: {
      heading: 'Doi tac tin cay trong nganh khao sat xay dung',
      imagePath: '',
      content: '<p class="ql-align-justify"><strong>CÔNG TY CỔ PHẦN TƯ VẤN KHẢO SÁT XÂY DỰNG HÀ NỘI</strong>&nbsp;(HANOI CONTRUCTION SERVEY CONSULTANT JOIN STOCK COMPANY) là một doanh nghiệp độc lập, tự hào với bề dày kinh nghiệm trong lĩnh vực tư vấn xây dựng.</p><p class="ql-align-justify"><br></p><p class="ql-align-justify">Được thành lập từ năm 2006, chúng tôi cam kết mang lại những giải pháp tư vấn chất lượng cao, đáp ứng các tiêu chuẩn khắt khe nhất của ngành xây dựng Việt Nam.</p><p class="ql-align-justify"><br></p><p class="ql-align-justify">Với sở hữu Trung tâm thí nghiệm và kiểm định xây dựng đạt chuẩn, VietDelta tự tin là đối tác tin cậy cho mọi công trình.</p>',
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

function normalizeAboutContent(input: Partial<AboutPageContent> | AboutPageContent): AboutPageContent {
  const fallback = createEmptyAboutContent()
  return {
    hero: {
      title: input.hero?.title || fallback.hero.title,
      backgroundImagePath: input.hero?.backgroundImagePath || fallback.hero.backgroundImagePath,
    },
    intro: {
      heading: input.intro?.heading || fallback.intro.heading,
      imagePath: input.intro?.imagePath || fallback.intro.imagePath,
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
