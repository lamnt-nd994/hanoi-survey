<template>
  <div class="min-h-screen text-neutral-900">
    <SiteHeader
      :site-name="siteName"
      :company-name="companyName"
      :logo-path="logoPath"
      :address="mergedContactInfo.officeAddress"
      :email="mergedContactInfo.email"
      :phone="mergedContactInfo.phone"
      :zalo-url="mergedContactInfo.zaloUrl"
      :is-mobile-menu-open="isMobileMenuOpen"
      :nav-items="navItems"
      :services="services"
      :project-categories="projectCategories"
      :post-categories="postCategories"
      :equipment-categories="equipmentCategories"
      :get-nav-to="getNavTo"
      :is-active-nav-route="isActiveNavRoute"
      :is-external-url="isExternalUrl"
      :is-active-category-route="isActiveCategoryRoute"
      :is-active-service-route="isActiveServiceRoute"
      :get-route-name="getNavRouteNameSafe"
      :is-services-nav-item="isServicesNavItem"
      :is-projects-nav-item="isProjectsNavItem"
      :is-equipments-nav-item="isEquipmentsNavItem"
      :is-news-nav-item="isNewsNavItem"
      :is-loading="!layoutReady"
      :nav-ready="publicMenusStore.loaded"
      @toggle-mobile-menu="isMobileMenuOpen = !isMobileMenuOpen"
      @close-mobile-menu="isMobileMenuOpen = false"
    />

    <Breadcrumbs v-if="shouldShowBreadcrumb" :items="breadcrumbItems" :is-loading="!layoutReady" />

    <main>
      <router-view />
    </main>

    <SiteFooter
      :site-name="siteName"
      :short-name="shortName"
      :company-name="companyName"
      :logo-path="logoPath"
      :footer-text="footerText"
      :address="mergedContactInfo.officeAddress"
      :email="mergedContactInfo.email"
      :phone="mergedContactInfo.phone"
      :working-hours="mergedContactInfo.workingHours"
      :license-number="siteSettingsStore.settings?.licenseNumber || ''"
      :tax-code="siteSettingsStore.settings?.taxCode || ''"
      :license-issued-date="siteSettingsStore.settings?.licenseIssuedDate || ''"
      :facebook-url="mergedContactInfo.facebookUrl"
      :youtube-url="mergedContactInfo.youtubeUrl"
      :linkedin-url="mergedContactInfo.linkedinUrl"
      :is-loading="!layoutReady"
    />

    <FloatingCTA />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, type RouteLocationRaw } from 'vue-router'
import FloatingCTA from '../FloatingCTA.vue'
import SiteFooter from './SiteFooter.vue'
import SiteHeader from './SiteHeader.vue'
import Breadcrumbs from '../ui/Breadcrumbs.vue'
import { fetchEquipmentDetail, fetchPageBySlug, fetchPostDetail, fetchProjectDetail, fetchServiceDetail } from '../../lib/api'
import { usePublicContentStore } from '../../stores/publicContent'
import { usePublicMenusStore } from '../../stores/publicMenus'
import { useSiteSettingsStore } from '../../stores/siteSettings'
import { ROUTE_NAMES } from '../../router'
import type { PublicMenuItem } from '../../types/content'

const route = useRoute()
const isMobileMenuOpen = ref(false)
const publicContentStore = usePublicContentStore()
const siteSettingsStore = useSiteSettingsStore()
const publicMenusStore = usePublicMenusStore()
const { siteName, companyNameEn, shortName, logoPath, footerText, mergedContactInfo } = storeToRefs(siteSettingsStore)
const { mainMenuItems: navItems } = storeToRefs(publicMenusStore)
const { services, projectCategories, postCategories, equipmentCategories } = storeToRefs(publicContentStore)
const breadcrumbDetailLabel = ref('')
const breadcrumbLabelCache = new Map<string, string>()
const layoutReady = ref(false)

const companyName = computed(() => companyNameEn.value || shortName.value || 'Hanoi Construction Survey Consultant Joint Stock Company')
const shouldShowBreadcrumb = computed(() => route.name !== ROUTE_NAMES.home)

const breadcrumbItems = computed(() => {
  const items: Array<{ label: string; to?: RouteLocationRaw; current?: boolean }> = [
    { label: 'Trang chủ', to: { name: ROUTE_NAMES.home } },
  ]

  switch (route.name) {
    case ROUTE_NAMES.about:
      return [...items, { label: 'Giới thiệu', current: true }]
    case ROUTE_NAMES.services:
      return [...items, { label: 'Lĩnh vực', current: true }]
    case ROUTE_NAMES.serviceDetail:
      return [...items, { label: 'Lĩnh vực', to: { name: ROUTE_NAMES.services } }, { label: breadcrumbDetailLabel.value || formatSlugLabel(String(route.params.slug || '')) || 'Chi tiết', current: true }]
    case ROUTE_NAMES.projects:
    case ROUTE_NAMES.projectCategory:
      return [...items, { label: 'Dự án', current: true }]
    case ROUTE_NAMES.projectDetail:
      return [...items, { label: 'Dự án', to: { name: ROUTE_NAMES.projects } }, { label: breadcrumbDetailLabel.value || formatSlugLabel(String(route.params.slug || '')) || 'Chi tiết', current: true }]
    case ROUTE_NAMES.news:
    case ROUTE_NAMES.newsCategory:
      return [...items, { label: 'Tin tức', current: true }]
    case ROUTE_NAMES.newsDetail:
      return [...items, { label: 'Tin tức', to: { name: ROUTE_NAMES.news } }, { label: breadcrumbDetailLabel.value || formatSlugLabel(String(route.params.slug || '')) || 'Chi tiết', current: true }]
    case ROUTE_NAMES.equipment:
      return [...items, { label: 'Máy móc - thiết bị', current: true }]
    case ROUTE_NAMES.equipmentDetail:
      return [...items, { label: 'Máy móc - thiết bị', to: { name: ROUTE_NAMES.equipment } }, { label: breadcrumbDetailLabel.value || formatSlugLabel(String(route.params.slug || '')) || 'Chi tiết', current: true }]
    case ROUTE_NAMES.page:
      return [...items, { label: breadcrumbDetailLabel.value || formatSlugLabel(String(route.params.slug || '')) || 'Trang thông tin', current: true }]
    case ROUTE_NAMES.contact:
      return [...items, { label: 'Liên hệ', current: true }]
    default:
      return items
  }
})

const routeParents: Record<string, string[]> = {
  [ROUTE_NAMES.home]: [ROUTE_NAMES.home],
  [ROUTE_NAMES.about]: [ROUTE_NAMES.about],
  [ROUTE_NAMES.services]: [ROUTE_NAMES.services, ROUTE_NAMES.serviceDetail],
  [ROUTE_NAMES.projects]: [ROUTE_NAMES.projects, ROUTE_NAMES.projectCategory, ROUTE_NAMES.projectDetail],
  [ROUTE_NAMES.equipment]: [ROUTE_NAMES.equipment, ROUTE_NAMES.equipmentDetail],
  [ROUTE_NAMES.news]: [ROUTE_NAMES.news, ROUTE_NAMES.newsCategory, ROUTE_NAMES.newsDetail],
  [ROUTE_NAMES.contact]: [ROUTE_NAMES.contact],
}

const pathToName: Record<string, string> = {
  '/': ROUTE_NAMES.home,
  '/gioi-thieu': ROUTE_NAMES.about,
  '/linh-vuc': ROUTE_NAMES.services,
  '/du-an': ROUTE_NAMES.projects,
  '/tin-tuc': ROUTE_NAMES.news,
  '/thiet-bi': ROUTE_NAMES.equipment,
  '/may-moc-thiet-bi': ROUTE_NAMES.equipment,
  '/lien-he': ROUTE_NAMES.contact,
}

function getNavRouteName(item: PublicMenuItem): string | null {
  if (isServicesNavItem(item)) return ROUTE_NAMES.services
  if (isProjectsNavItem(item)) return ROUTE_NAMES.projects
  if (isNewsNavItem(item)) return ROUTE_NAMES.news
  if (item.resolvedUrl === '/thiet-bi' || item.resolvedUrl === '/may-moc' || item.resolvedUrl === '/may-moc-thiet-bi') return ROUTE_NAMES.equipment
  return pathToName[item.resolvedUrl] || null
}

function getNavRouteNameSafe(item: PublicMenuItem) {
  return getNavRouteName(item) || ROUTE_NAMES.home
}

function getNavTo(item: PublicMenuItem, categorySlug?: string): RouteLocationRaw {
  if (isExternalUrl(item.resolvedUrl)) return item.resolvedUrl

  const routeName = getNavRouteName(item)
  if (!routeName) return item.resolvedUrl
  if (categorySlug) {
    if (routeName === ROUTE_NAMES.equipment) {
      return { name: ROUTE_NAMES.equipmentDetail, params: { slug: categorySlug } }
    }
    if (routeName === ROUTE_NAMES.projects) {
      return { name: ROUTE_NAMES.projectCategory, params: { category: categorySlug } }
    }
    if (routeName === ROUTE_NAMES.news) {
      return { name: ROUTE_NAMES.newsCategory, params: { category: categorySlug } }
    }
    return { name: routeName, query: { category: categorySlug } }
  }
  return { name: routeName }
}

function isActiveNavRoute(item: PublicMenuItem): boolean {
  if (isExternalUrl(item.resolvedUrl)) return false

  const routeName = getNavRouteName(item)
  if (!routeName) return false
  const relatedNames = routeParents[routeName]
  return relatedNames ? relatedNames.includes(route.name as string) : false
}

function isExternalUrl(path: string) {
  return /^https?:\/\//i.test(path)
}

function isActiveCategoryRoute(routeName: string, slug: string) {
  if (routeName === ROUTE_NAMES.equipment) {
    return route.name === ROUTE_NAMES.equipmentDetail && route.params.slug === slug
  }
  if (routeName === ROUTE_NAMES.projects || routeName === ROUTE_NAMES.news) {
    const expectedRoute = routeName === ROUTE_NAMES.projects ? ROUTE_NAMES.projectCategory : ROUTE_NAMES.newsCategory
    return route.name === expectedRoute && route.params.category === slug
  }
  return route.name === routeName && route.query.category === slug
}

function isActiveServiceRoute(slug: string) {
  return route.name === ROUTE_NAMES.serviceDetail && route.params.slug === slug
}

function isServicesNavItem(item: PublicMenuItem) {
  return item.resolvedUrl === '/linh-vuc' || /dịch vụ|linh vực|lĩnh vực/i.test(item.title)
}

function isProjectsNavItem(item: PublicMenuItem) {
  return item.resolvedUrl === '/du-an' || /dự án|du an/i.test(item.title)
}

function isEquipmentsNavItem(item: PublicMenuItem) {
  return item.resolvedUrl === '/thiet-bi' || item.resolvedUrl === '/may-moc' || item.resolvedUrl === '/may-moc-thiet-bi' || /thiết bị|thiet bi/i.test(item.title)
}

function isNewsNavItem(item: PublicMenuItem) {
  return item.resolvedUrl === '/tin-tuc' || /tin tức|tin tuc|kiến thức|kien thuc/i.test(item.title)
}

void Promise.allSettled([
  siteSettingsStore.ensureLoaded(),
  publicMenusStore.loadMainMenu(),
  publicContentStore.loadServices(),
  publicContentStore.loadProjectCategories(),
  publicContentStore.loadPostCategories(),
  publicContentStore.loadEquipmentCategories(),
]).finally(() => {
  layoutReady.value = true
})

watch(() => route.fullPath, () => {
  void loadBreadcrumbDetailLabel()
}, { immediate: true })

async function loadBreadcrumbDetailLabel() {
  breadcrumbDetailLabel.value = ''

  const slug = typeof route.params.slug === 'string' ? route.params.slug : ''
  const category = typeof route.params.category === 'string' ? route.params.category : ''
  const cacheKey = `${String(route.name || '')}:${slug || category}`
  const cachedLabel = breadcrumbLabelCache.get(cacheKey)
  if (cachedLabel) {
    breadcrumbDetailLabel.value = cachedLabel
    return
  }

  if (route.name === ROUTE_NAMES.projectCategory) {
    const existing = projectCategories.value.find((item) => item.slug === category)
    breadcrumbDetailLabel.value = existing?.name || formatSlugLabel(category)
    breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
    return
  }

  if (route.name === ROUTE_NAMES.newsCategory) {
    const existing = postCategories.value.find((item) => item.slug === category)
    breadcrumbDetailLabel.value = existing?.name || formatSlugLabel(category)
    breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
    return
  }

  if (!slug) return

  try {
    if (route.name === ROUTE_NAMES.serviceDetail) {
      const existing = services.value.find((item) => item.slug === slug)
      breadcrumbDetailLabel.value = existing?.title || (await fetchServiceDetail(slug)).title
      breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
      return
    }

    if (route.name === ROUTE_NAMES.projectDetail) {
      breadcrumbDetailLabel.value = (await fetchProjectDetail(slug)).title
      breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
      return
    }

    if (route.name === ROUTE_NAMES.newsDetail) {
      breadcrumbDetailLabel.value = (await fetchPostDetail(slug)).title
      breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
      return
    }

    if (route.name === ROUTE_NAMES.equipmentDetail) {
      const existingCategory = equipmentCategories.value.find((item) => item.slug === slug)
      breadcrumbDetailLabel.value = existingCategory?.name || (await fetchEquipmentDetail(slug)).name
      breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
      return
    }

    if (route.name === ROUTE_NAMES.page) {
      breadcrumbDetailLabel.value = (await fetchPageBySlug(slug)).title
      breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
    }
  } catch {
    breadcrumbDetailLabel.value = formatSlugLabel(slug)
    breadcrumbLabelCache.set(cacheKey, breadcrumbDetailLabel.value)
  }
}

function formatSlugLabel(slug: string) {
  return slug
    .split('-')
    .filter(Boolean)
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join(' ')
}
</script>
