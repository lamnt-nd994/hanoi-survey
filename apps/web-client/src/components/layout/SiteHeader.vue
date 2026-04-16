<template>
  <header class="sticky top-0 z-50 border-b border-neutral-200 bg-white/95 backdrop-blur-md">
    <div class="container-shell">
      <div class="grid min-h-[5.5rem] grid-cols-[minmax(0,1fr)_auto] items-center gap-4 py-3 md:gap-6 lg:min-h-[6.5rem]">
        <router-link :to="{ name: 'home' }" class="flex min-w-0 cursor-pointer items-center">
          <div v-if="logoPath" class="flex h-16 w-[9.75rem] shrink-0 items-center justify-center md:h-20 md:w-[10.5rem]">
            <img :src="resolveMediaUrl(logoPath)" :alt="siteName" width="156" height="156" loading="eager" decoding="async" class="max-h-full w-full object-contain" />
          </div>
          <div v-else class="flex h-14 w-14 shrink-0 items-center justify-center rounded-lg bg-primary-navy text-sm font-bold text-white shadow-lg md:h-16 md:w-16">
            HS
          </div>
          <div class="hidden min-w-0 flex-1 md:block">
            <div class="min-h-[2.5rem] font-heading text-sm font-extrabold leading-tight text-primary-navy lg:text-xl sm:pt-3 lg:pt-0">
              {{ siteName }}
            </div>
            <div class="min-h-[1rem] text-[11px] font-semibold tracking-[0.02em] text-neutral-500 lg:text-xs">
              <span v-if="companyName">{{ companyName }}</span>
              <span v-else-if="isLoading" class="block h-3 w-48 rounded bg-neutral-200/80"></span>
            </div>
          </div>
        </router-link>

        <div class="hidden lg:flex lg:min-h-[3.75rem] lg:min-w-[15rem] lg:items-center lg:justify-end">
          <div class="w-[15rem] text-right">
            <div class="flex min-h-[1.25rem] items-center justify-end gap-2 text-sm text-neutral-500">
              <AppIcon icon="mail" class="h-4 w-4 text-neutral-400" />
              <a v-if="email" :href="`mailto:${email}`" class="cursor-pointer transition-colors hover:text-primary-navy">
                {{ email }}
              </a>
              <span v-else-if="isLoading" class="block h-3 w-40 rounded bg-neutral-200/80"></span>
            </div>
            <div class="mt-1 flex min-h-[2rem] items-center justify-end gap-3 text-primary-navy">
              <AppIcon icon="phone" class="h-6 w-6 animate-[phone-call_1.2s_ease-in-out_infinite]" :stroke-width="2.25" />
              <a v-if="phone" :href="`tel:${phone}`" class="cursor-pointer rounded-full px-2 py-1 text-lg font-extrabold transition-colors animate-[phone-highlight_1.4s_ease-in-out_infinite] hover:text-red-600 lg:text-xl">
                <span>{{ phone }}</span>
              </a>
              <span v-else-if="isLoading" class="block h-7 w-36 rounded-full bg-neutral-200/80"></span>
            </div>
          </div>
        </div>

        <button @click="emit('toggleMobileMenu')" class="flex h-11 w-11 shrink-0 cursor-pointer items-center justify-center rounded-xl border border-neutral-200 text-neutral-600 hover:bg-neutral-100 hover:text-primary-navy lg:hidden" aria-label="Toggle menu">
          <AppIcon :icon="isMobileMenuOpen ? 'close' : 'menu'" class="h-6 w-6" />
        </button>
      </div>
    </div>

    <SiteDesktopNav
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
      :get-route-name="getRouteName"
      :is-services-nav-item="isServicesNavItem"
      :is-projects-nav-item="isProjectsNavItem"
      :is-equipments-nav-item="isEquipmentsNavItem"
      :is-news-nav-item="isNewsNavItem"
      :is-loading="isLoading"
      :nav-ready="navReady"
    />

    <SiteMobileNav
      :is-open="isMobileMenuOpen"
      :nav-items="navItems"
      :phone="phone"
      :zalo-url="zaloUrl"
      :services="services"
      :project-categories="projectCategories"
      :post-categories="postCategories"
      :equipment-categories="equipmentCategories"
      :get-nav-to="getNavTo"
      :is-active-nav-route="isActiveNavRoute"
      :is-external-url="isExternalUrl"
      :is-active-category-route="isActiveCategoryRoute"
      :is-active-service-route="isActiveServiceRoute"
      :get-route-name="getRouteName"
      :is-services-nav-item="isServicesNavItem"
      :is-projects-nav-item="isProjectsNavItem"
      :is-equipments-nav-item="isEquipmentsNavItem"
      :is-news-nav-item="isNewsNavItem"
      :is-loading="isLoading"
      @close="emit('closeMobileMenu')"
    />
  </header>
</template>

<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'
import { resolveMediaUrl } from '../../lib/media'
import type { EquipmentCategory, PostCategory, ProjectCategory, PublicMenuItem, SurveyService } from '../../types/content'
import SiteDesktopNav from './SiteDesktopNav.vue'
import SiteMobileNav from './SiteMobileNav.vue'
import AppIcon from '../ui/AppIcon.vue'

defineProps<{
  siteName: string
  companyName: string
  logoPath: string
  address: string
  email: string
  phone: string
  zaloUrl: string
  isMobileMenuOpen: boolean
  navItems: PublicMenuItem[]
  services: SurveyService[]
  projectCategories: ProjectCategory[]
  postCategories: PostCategory[]
  equipmentCategories: EquipmentCategory[]
  getNavTo: (item: PublicMenuItem, categorySlug?: string) => RouteLocationRaw
  isActiveNavRoute: (item: PublicMenuItem) => boolean
  isExternalUrl: (path: string) => boolean
  isActiveCategoryRoute: (routeName: string, slug: string) => boolean
  isActiveServiceRoute: (slug: string) => boolean
  getRouteName: (item: PublicMenuItem) => string
  isServicesNavItem: (item: PublicMenuItem) => boolean
  isProjectsNavItem: (item: PublicMenuItem) => boolean
  isEquipmentsNavItem: (item: PublicMenuItem) => boolean
  isNewsNavItem: (item: PublicMenuItem) => boolean
  isLoading?: boolean
  navReady?: boolean
}>()

const emit = defineEmits<{
  toggleMobileMenu: []
  closeMobileMenu: []
}>()
</script>
