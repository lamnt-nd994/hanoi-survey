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

  <HomeAboutSection
    :site-name="siteName"
    :eyebrow="homeData.aboutSection.eyebrow"
    :title="homeData.aboutSection.title"
    :description="homeData.aboutSection.description || companyIntro || `${siteName} cung cấp dịch vụ khảo sát địa chất, địa hình, thủy văn, quan trắc và thí nghiệm xây dựng cho công trình dân dụng, công nghiệp và hạ tầng kỹ thuật.`"
    :image-path="aboutImagePath"
    :highlights="aboutHighlights"
    :button-label="homeData.aboutSection.buttonLabel"
    :button-url="homeData.aboutSection.buttonUrl"
  />

  <ServicesGrid
    :title="homeData.servicesSection.title"
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

  <FinalCtaSection
    :eyebrow="homeData.finalCta.eyebrow"
    :title="homeData.finalCta.title"
    :description="homeData.finalCta.description"
    :primary-button-label="homeData.finalCta.primaryButtonLabel"
    :primary-button-url="homeData.finalCta.primaryButtonUrl"
    :secondary-button-label="homeData.finalCta.secondaryButtonLabel"
    :secondary-button-url="homeData.finalCta.secondaryButtonUrl"
    :show-phone-button="homeData.finalCta.showPhoneButton"
    :phone="mergedContactInfo.phone"
  />
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import Hero from '../components/sections/HeroSection.vue'
import StatsBar from '../components/sections/StatsSection.vue'
import ServicesGrid from '../components/sections/ServicesSection.vue'
import ProjectsCarousel from '../components/sections/ProjectsSection.vue'
import HomeAboutSection from '../components/sections/HomeAboutSection.vue'
import FinalCtaSection from '../components/sections/FinalCtaSection.vue'
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
        { value: '24/7', label: 'Hỗ trợ khách hàng' },
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
</script>
