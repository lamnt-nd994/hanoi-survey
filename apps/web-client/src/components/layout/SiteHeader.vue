<template>
  <header class="sticky top-0 z-50 border-b border-neutral-200 bg-white/95 backdrop-blur-md">
    <div class="container-shell">
      <div class="flex items-center justify-between gap-6">
        <router-link :to="{ name: 'home' }" class="flex min-w-0 cursor-pointer items-center">
          <div v-if="logoPath" class="flex shrink-0 items-center justify-center">
            <img :src="resolveMediaUrl(logoPath)" :alt="siteName" width="156" height="156" loading="eager" decoding="async" class="max-h-24 object-contain" />
          </div>
          <div v-else class="flex h-14 w-14 shrink-0 items-center justify-center rounded-2xl bg-primary-navy text-sm font-bold text-white shadow-lg">
            HS
          </div>
          <div class="hidden min-w-0 md:block">
            <div class="line-clamp-2 font-heading text-sm font-extrabold leading-tight text-primary-navy lg:text-xl">
              {{ siteName }}
            </div>
            <div class="mt-1 text-[11px] font-semibold tracking-[0.02em] text-neutral-500 lg:text-xs">
              {{ companyName }}
            </div>
<!--            <div v-if="address" class="mt-1 max-w-[28rem] text-xs leading-5 text-neutral-500">-->
<!--              {{ address }}-->
<!--            </div>-->
          </div>
        </router-link>

        <div class="hidden lg:flex lg:items-center">
          <div class="text-right">
            <div v-if="email" class="flex items-center justify-end gap-2 text-sm text-neutral-500">
              <AppIcon icon="mail" class="h-4 w-4 text-neutral-400" />
              <a :href="`mailto:${email}`" class="cursor-pointer transition-colors hover:text-primary-navy">
                {{ email }}
              </a>
            </div>
            <div class="mt-1 flex items-center justify-end gap-3 text-primary-navy">
              <AppIcon icon="phone" class="h-6 w-6 animate-[phone-call_1.2s_ease-in-out_infinite]" :stroke-width="2.25" />
              <a v-if="phone" :href="`tel:${phone}`" class="cursor-pointer rounded-full px-2 py-1 text-lg font-extrabold transition-colors animate-[phone-highlight_1.4s_ease-in-out_infinite] hover:text-red-600 lg:text-xl">
                <span>{{ phone }}</span>
              </a>
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
}>()

const emit = defineEmits<{
  toggleMobileMenu: []
  closeMobileMenu: []
}>()
</script>
