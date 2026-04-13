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
      :service-categories="serviceCategories"
      :project-categories="projectCategories"
      :post-categories="postCategories"
      :equipment-categories="equipmentCategories"
      :get-nav-to="getNavTo"
      :is-active-nav-route="isActiveNavRoute"
      :is-external-url="isExternalUrl"
      :is-active-category-route="isActiveCategoryRoute"
      :get-route-name="getNavRouteNameSafe"
      :is-services-nav-item="isServicesNavItem"
      :is-projects-nav-item="isProjectsNavItem"
      :is-equipments-nav-item="isEquipmentsNavItem"
      :is-news-nav-item="isNewsNavItem"
      @toggle-mobile-menu="isMobileMenuOpen = !isMobileMenuOpen"
      @close-mobile-menu="isMobileMenuOpen = false"
    />

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
    />

    <FloatingCTA />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, type RouteLocationRaw } from 'vue-router'
import FloatingCTA from '../FloatingCTA.vue'
import SiteFooter from './SiteFooter.vue'
import SiteHeader from './SiteHeader.vue'
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
const { serviceCategories, projectCategories, postCategories, equipmentCategories } = storeToRefs(publicContentStore)

const companyName = computed(() => companyNameEn.value || shortName.value || 'Hanoi Construction Survey Consultant Joint Stock Company')

const routeParents: Record<string, string[]> = {
  [ROUTE_NAMES.home]: [ROUTE_NAMES.home],
  [ROUTE_NAMES.about]: [ROUTE_NAMES.about],
  [ROUTE_NAMES.services]: [ROUTE_NAMES.services, ROUTE_NAMES.serviceDetail],
  [ROUTE_NAMES.projects]: [ROUTE_NAMES.projects, ROUTE_NAMES.projectDetail],
  [ROUTE_NAMES.equipment]: [ROUTE_NAMES.equipment, ROUTE_NAMES.equipmentDetail],
  [ROUTE_NAMES.news]: [ROUTE_NAMES.news, ROUTE_NAMES.newsDetail],
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
  if (categorySlug) return { name: routeName, query: { category: categorySlug } }
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
  return route.name === routeName && route.query.category === slug
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

onMounted(async () => {
  await Promise.all([
    siteSettingsStore.ensureLoaded(),
    publicMenusStore.loadMainMenu(),
    publicContentStore.loadServiceCategories(),
    publicContentStore.loadProjectCategories(),
    publicContentStore.loadPostCategories(),
    publicContentStore.loadEquipmentCategories(),
  ])
})
</script>
