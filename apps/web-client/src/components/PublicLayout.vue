<template>
  <div class="min-h-screen text-neutral-900">
    <header class="sticky top-0 z-50 border-b border-neutral-200 bg-white/95 backdrop-blur-md">
      <div class="container-shell">
        <div class="flex items-center justify-between gap-6">
          <router-link :to="{ name: 'home' }" class="flex min-w-0 items-center">
            <div v-if="logoPath" class="flex shrink-0 items-center justify-center">
              <img :src="resolveMediaUrl(logoPath)" :alt="siteName" class="max-h-24 object-contain" />
            </div>
            <div v-else class="flex h-14 w-14 shrink-0 items-center justify-center rounded-2xl bg-primary-navy text-sm font-bold text-white shadow-lg">
              HS
            </div>
            <div class="min-w-0 hidden md:block">
              <div class="line-clamp-2 font-heading text-sm font-bold uppercase leading-tight tracking-[0.03em] text-primary-navy lg:text-xl">
                {{ siteName }}
              </div>
              <div class="mt-1 text-[11px] font-semibold uppercase tracking-[0.08em] text-neutral-500 lg:text-xs">
                {{ companyNameEn || shortName || 'Hanoi Construction Survey Consultant Joint Stock Company' }}
              </div>
            </div>
          </router-link>

          <div class="hidden lg:flex lg:items-center">
            <div class="flex items-center gap-3 text-amber-500">
              <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
              </svg>
              <a
                v-if="mergedContactInfo.phone"
                :href="`tel:${mergedContactInfo.phone}`"
                class="text-lg font-bold tracking-[0.02em] transition-colors hover:text-amber-600 lg:text-xl"
              >
                <span>{{ mergedContactInfo.phone }}</span>
              </a>
            </div>
          </div>

          <button
            @click="isMobileMenuOpen = !isMobileMenuOpen"
            class="lg:hidden flex h-11 w-11 shrink-0 items-center justify-center rounded-xl border border-neutral-200 text-neutral-600 hover:bg-neutral-100 hover:text-primary-navy"
            aria-label="Toggle menu"
          >
            <svg v-if="!isMobileMenuOpen" class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <svg v-else class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>

      <div class="hidden border-t border-primary-light/20 lg:block bg-primary-navy text-white">
        <div class="container-shell">
          <nav class="flex items-center justify-center gap-2 py-3">
            <template v-for="item in navItems" :key="item.resolvedUrl">
            <div
              v-if="isServicesNavItem(item) || isProjectsNavItem(item) || isEquipmentsNavItem(item) || isNewsNavItem(item)"
              class="group relative text-white"
            >
              <router-link
                :to="getNavTo(item)"
                class="flex items-center gap-2 rounded-full px-4 py-2 text-sm font-semibold transition-all text-white"
                :class="isActiveNavRoute(item) ? 'bg-white/14 text-white' : 'text-white/90 hover:bg-white/10 hover:text-white'"
              >
                {{ item.title }}
                <svg class="h-4 w-4 transition-transform group-hover:rotate-180" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </router-link>

              <div class="invisible absolute left-0 top-full z-50 pt-2 opacity-0 transition-all duration-150 group-hover:visible group-hover:opacity-100">
                <div class="w-[18rem] overflow-hidden rounded-lg border border-neutral-300 bg-white shadow-[0_16px_32px_rgba(15,23,42,0.08)]">
                  <div v-if="isServicesNavItem(item) && serviceCategories.length" class="p-2">
                    <router-link
                      v-for="category in serviceCategories"
                      :key="category.id"
                      :to="{ name: 'services', query: { category: category.slug } }"
                      class="flex min-h-[2.5rem] items-center justify-between gap-3 rounded-md px-3 py-2 text-sm leading-5 transition-colors"
                      :class="isActiveCategoryRoute('services', category.slug) ? 'bg-neutral-100 text-primary-navy' : 'text-neutral-700 hover:bg-neutral-50 hover:text-primary-navy'"
                    >
                      <span>{{ category.name }}</span>
                    </router-link>
                  </div>

                  <div v-else-if="isProjectsNavItem(item) && projectCategories.length" class="p-2">
                    <router-link
                      v-for="category in projectCategories"
                      :key="category.id"
                      :to="{ name: 'projects', query: { category: category.slug } }"
                      class="flex min-h-[2.5rem] items-center justify-between gap-3 rounded-md px-3 py-2 text-sm leading-5 transition-colors"
                      :class="isActiveCategoryRoute('projects', category.slug) ? 'bg-neutral-100 text-primary-navy' : 'text-neutral-700 hover:bg-neutral-50 hover:text-primary-navy'"
                    >
                      <span>{{ category.name }}</span>
                    </router-link>
                  </div>

                  <div v-else-if="isEquipmentsNavItem(item) && equipmentCategories.length" class="p-2">
                    <router-link
                      v-for="category in equipmentCategories"
                      :key="category.id"
                      :to="{ name: 'equipment', query: { category: category.slug } }"
                      class="flex min-h-[2.5rem] items-center justify-between gap-3 rounded-md px-3 py-2 text-sm leading-5 transition-colors"
                      :class="isActiveCategoryRoute('equipment', category.slug) ? 'bg-neutral-100 text-primary-navy' : 'text-neutral-700 hover:bg-neutral-50 hover:text-primary-navy'"
                    >
                      <span>{{ category.name }}</span>
                    </router-link>
                  </div>

                  <div v-else-if="isNewsNavItem(item) && postCategories.length" class="p-2">
                    <router-link
                      v-for="category in postCategories"
                      :key="category.id"
                      :to="{ name: 'news', query: { category: category.slug } }"
                      class="flex min-h-[2.5rem] items-center justify-between gap-3 rounded-md px-3 py-2 text-sm leading-5 transition-colors"
                      :class="isActiveCategoryRoute('news', category.slug) ? 'bg-neutral-100 text-primary-navy' : 'text-neutral-700 hover:bg-neutral-50 hover:text-primary-navy'"
                    >
                      <span>{{ category.name }}</span>
                    </router-link>
                  </div>

                  <div v-else class="px-4 py-3 text-sm text-neutral-500">
                    Chưa có danh mục để hiển thị.
                  </div>
                </div>
              </div>
            </div>

            <component
              :is="isExternalUrl(item.resolvedUrl) ? 'a' : 'router-link'"
              v-else
              :to="!isExternalUrl(item.resolvedUrl) ? getNavTo(item) : undefined"
              :href="isExternalUrl(item.resolvedUrl) ? item.resolvedUrl : undefined"
              :target="isExternalUrl(item.resolvedUrl) ? '_blank' : undefined"
              :rel="isExternalUrl(item.resolvedUrl) ? 'noopener noreferrer' : undefined"
              class="rounded-full px-4 py-2 text-sm font-semibold text-white transition-all"
              :class="isActiveNavRoute(item) ? 'bg-white/14 text-white' : 'text-white/90 hover:bg-white/10 hover:text-white'"
            >
              {{ item.title }}
            </component>
            </template>
          </nav>
        </div>
      </div>

      <div
        v-show="isMobileMenuOpen"
        class="lg:hidden border-t border-neutral-200 bg-white px-4 py-6 shadow-lg"
      >
        <nav class="flex flex-col gap-2">
          <template v-for="item in navItems" :key="item.resolvedUrl">
            <div v-if="isServicesNavItem(item) || isProjectsNavItem(item) || isEquipmentsNavItem(item) || isNewsNavItem(item)" class="rounded-2xl border border-neutral-100 p-1">
              <router-link
                :to="getNavTo(item)"
                @click="isMobileMenuOpen = false"
                class="flex min-h-[44px] items-center rounded-xl px-4 py-3 text-base font-medium transition-all"
                :class="isActiveNavRoute(item) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-700 hover:bg-neutral-100'"
              >
                {{ item.title }}
              </router-link>
              <div class="mt-1 space-y-1 px-2 pb-2">
                <router-link
                  v-for="category in getMobileNavCategories(item)"
                  :key="`${item.resolvedUrl}-${category.slug}`"
                  :to="getNavTo(item, category.slug)"
                  @click="isMobileMenuOpen = false"
                  class="flex min-h-[40px] items-center rounded-xl px-3 py-2 text-sm transition-colors"
                  :class="isActiveCategoryRoute(getNavRouteName(item)!, category.slug) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-600 hover:bg-neutral-100 hover:text-primary-navy'"
                >
                  {{ category.name }}
                </router-link>
              </div>
            </div>

            <component
              :is="isExternalUrl(item.resolvedUrl) ? 'a' : 'router-link'"
              v-else
              :to="!isExternalUrl(item.resolvedUrl) ? getNavTo(item) : undefined"
              :href="isExternalUrl(item.resolvedUrl) ? item.resolvedUrl : undefined"
              :target="isExternalUrl(item.resolvedUrl) ? '_blank' : undefined"
              :rel="isExternalUrl(item.resolvedUrl) ? 'noopener noreferrer' : undefined"
              @click="isMobileMenuOpen = false"
              class="rounded-xl px-4 py-3 text-base font-medium transition-all min-h-[44px] flex items-center"
              :class="isActiveNavRoute(item) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-700 hover:bg-neutral-100'"
            >
              {{ item.title }}
            </component>
          </template>
        </nav>
        <div class="mt-6 space-y-3">
          <a
            v-if="mergedContactInfo.phone"
            :href="`tel:${mergedContactInfo.phone}`"
            class="btn-primary flex w-full items-center justify-center"
          >
            <svg class="mr-2 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
            </svg>
            Gọi ngay: {{ mergedContactInfo.phone }}
          </a>
          <a
            v-if="mergedContactInfo.zaloUrl"
            :href="mergedContactInfo.zaloUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="btn-outline flex w-full items-center justify-center"
          >
            <svg class="mr-2 h-5 w-5" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.48 2 2 6.48 2 12c0 1.82.5 3.53 1.36 5.02L2 22l5.04-1.34C8.5 21.53 10.21 22 12 22c5.52 0 10-4.48 10-10S17.52 2 12 2zm0 18c-1.56 0-3.07-.43-4.38-1.18l-.31-.18-3.22.86.86-3.14-.2-.32C3.63 14.74 3.2 13.21 3.2 11.6c0-4.84 3.96-8.8 8.8-8.8s8.8 3.96 8.8 8.8-3.96 8.8-8.8 8.8zm5.44-7.26c-.3-.15-1.77-.87-2.04-.97-.27-.1-.47-.15-.67.15-.2.3-.77.97-.94 1.17-.17.2-.34.22-.64.07-.3-.15-1.26-.46-2.4-1.48-.89-.79-1.49-1.77-1.67-2.07-.17-.3-.02-.46.13-.61.13-.13.3-.34.45-.51.15-.17.2-.3.3-.5.1-.2.05-.37-.02-.52-.07-.15-.67-1.62-.92-2.22-.25-.58-.5-.5-.67-.51-.17-.01-.37-.01-.57-.01-.2 0-.52.07-.79.37-.27.3-1.04 1.01-1.04 2.47 0 1.46 1.06 2.87 1.21 3.07.15.2 2.09 3.19 5.06 4.47.71.31 1.26.49 1.69.63.71.23 1.36.2 1.87.12.57-.09 1.77-.72 2.02-1.42.25-.7.25-1.3.17-1.42-.07-.13-.27-.2-.57-.35z" />
            </svg>
            Chat Zalo
          </a>
        </div>
      </div>
    </header>

    <main>
      <router-view />
    </main>

    <footer class="mt-20 border-t border-neutral-200 bg-primary-navy text-white">
      <div class="container-shell grid gap-12 py-12 md:grid-cols-3 lg:grid-cols-4">
        <div class="md:col-span-1 lg:col-span-1">
          <div class="flex items-center gap-3">
            <div v-if="logoPath" class="flex h-16 w-[11rem] items-center justify-center">
              <img :src="resolveMediaUrl(logoPath)" :alt="siteName" class="max-h-12 w-full object-contain" />
            </div>
            <div v-else class="flex h-12 w-12 items-center justify-center rounded-2xl bg-accent-green text-sm font-bold text-white">
              HS
            </div>
            <div>
              <div class="font-heading text-lg font-bold">{{ siteName }}</div>
              <div class="text-xs uppercase tracking-[0.2em] text-neutral-300">{{ companyNameEn || shortName || 'Geological Engineering' }}</div>
            </div>
          </div>
          <p class="mt-4 text-sm leading-6 text-neutral-300">
            {{ footerText || 'Đơn vị khảo sát địa chất, địa hình, thủy văn và thí nghiệm xây dựng uy tín tại Việt Nam.' }}
          </p>
          <div class="mt-6 flex gap-4">
            <a v-if="mergedContactInfo.facebookUrl" :href="mergedContactInfo.facebookUrl" target="_blank" rel="noopener noreferrer" class="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 text-white transition-all hover:bg-accent-green" aria-label="Facebook">
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M18.77 7.46H14.5v-1.9c0-.9.6-1.1 1-1.1h3V.5h-4.33C10.24.5 9.5 3.44 9.5 5.32v2.15h-3v4h3v12h5v-12h3.85l.42-4z" />
              </svg>
            </a>
            <a v-if="mergedContactInfo.youtubeUrl" :href="mergedContactInfo.youtubeUrl" target="_blank" rel="noopener noreferrer" class="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 text-white transition-all hover:bg-accent-green" aria-label="YouTube">
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M23.5 6.19c-.32-1.21-1.27-2.16-2.48-2.48C18.8 3.25 12 3.25 12 3.25s-6.8 0-9.02.46c-1.21.32-2.16 1.27-2.48 2.48C0 8.41 0 12 0 12s0 3.59.5 5.81c.32 1.21 1.27 2.16 2.48 2.48C5.2 20.75 12 20.75 12 20.75s6.8 0 9.02-.46c1.21-.32 2.16-1.27 2.48-2.48C24 15.59 24 12 24 12s0-3.59-.5-5.81zM9.55 15.57v-7.14l6.32 3.57-6.32 3.57z" />
              </svg>
            </a>
            <a v-if="mergedContactInfo.linkedinUrl" :href="mergedContactInfo.linkedinUrl" target="_blank" rel="noopener noreferrer" class="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 text-white transition-all hover:bg-accent-green" aria-label="LinkedIn">
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M20.45 20.45H16.89V14.88c0-1.33-.03-3.04-1.85-3.04-1.86 0-2.14 1.45-2.14 2.95v5.66H9.34V9h3.41v1.56h.05c.48-.9 1.65-1.85 3.37-1.85 3.6 0 4.27 2.37 4.27 5.46v6.28zM5.34 7.43c-1.1 0-1.99-.89-1.99-1.99 0-1.1.89-1.99 1.99-1.99 1.1 0 1.99.89 1.99 1.99 0 1.1-.89 1.99-1.99 1.99zm1.72 13.02H3.62V9h3.44v11.45zM22.22 0H1.77C.79 0 0 .77 0 1.73v20.54C0 23.23.79 24 1.77 24h20.45c.98 0 1.77-.77 1.77-1.73V1.73C24 .77 23.2 0 22.22 0z" />
              </svg>
            </a>
          </div>
        </div>

        <div>
          <h3 class="font-heading text-lg font-semibold">Dịch vụ chính</h3>
          <ul class="mt-4 space-y-3 text-sm text-neutral-300">
            <li><router-link :to="{ name: 'services' }" class="hover:text-accent-green transition-colors">Khảo sát địa chất công trình</router-link></li>
            <li><router-link :to="{ name: 'services' }" class="hover:text-accent-green transition-colors">Khảo sát địa hình</router-link></li>
            <li><router-link :to="{ name: 'services' }" class="hover:text-accent-green transition-colors">Khảo sát địa chất thủy văn</router-link></li>
            <li><router-link :to="{ name: 'services' }" class="hover:text-accent-green transition-colors">Thí nghiệm chuyên ngành</router-link></li>
            <li><router-link :to="{ name: 'services' }" class="hover:text-accent-green transition-colors">Quan trắc công trình</router-link></li>
          </ul>
        </div>

        <div>
          <h3 class="font-heading text-lg font-semibold">Liên kết nhanh</h3>
          <ul class="mt-4 space-y-3 text-sm text-neutral-300">
            <li><router-link :to="{ name: 'about' }" class="hover:text-accent-green transition-colors">Giới thiệu</router-link></li>
            <li><router-link :to="{ name: 'projects' }" class="hover:text-accent-green transition-colors">Dự án tiêu biểu</router-link></li>
            <li><router-link :to="{ name: 'news' }" class="hover:text-accent-green transition-colors">Tin tức & Kiến thức</router-link></li>
            <li><router-link :to="{ name: 'contact' }" class="hover:text-accent-green transition-colors">Liên hệ</router-link></li>
          </ul>
        </div>

        <div>
          <h3 class="font-heading text-lg font-semibold">Liên hệ</h3>
          <ul class="mt-4 space-y-3 text-sm text-neutral-300">
            <li class="flex items-start gap-3">
              <svg class="mt-0.5 h-5 w-5 shrink-0 text-accent-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              <span>{{ mergedContactInfo.address }}</span>
            </li>
            <li class="flex items-center gap-3">
              <svg class="h-5 w-5 shrink-0 text-accent-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              <a :href="`mailto:${mergedContactInfo.email}`" class="hover:text-accent-green transition-colors">{{ mergedContactInfo.email }}</a>
            </li>
            <li class="flex items-center gap-3">
              <svg class="h-5 w-5 shrink-0 text-accent-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
              </svg>
              <a :href="`tel:${mergedContactInfo.phone}`" class="hover:text-accent-green transition-colors">{{ mergedContactInfo.phone }}</a>
            </li>
            <li class="flex items-center gap-3">
              <svg class="h-5 w-5 shrink-0 text-accent-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span class="text-neutral-400">{{ mergedContactInfo.workingHours }}</span>
            </li>
          </ul>
        </div>
      </div>

      <div class="border-t border-white/10 px-4 py-6">
        <div class="container-shell flex flex-col items-center justify-between gap-4 text-center text-sm text-neutral-400 md:flex-row md:text-left">
          <p>&copy; 2024 {{ siteName }}. All rights reserved.</p>
          <div class="flex gap-6">
            <router-link to="#" class="hover:text-white transition-colors">Chính sách bảo mật</router-link>
            <router-link to="#" class="hover:text-white transition-colors">Điều khoản sử dụng</router-link>
          </div>
        </div>
      </div>
    </footer>

    <FloatingCTA />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, type RouteLocationRaw } from 'vue-router'
import { resolveMediaUrl } from '../lib/media'
import { usePublicContentStore } from '../stores/publicContent'
import { usePublicMenusStore } from '../stores/publicMenus'
import { useSiteSettingsStore } from '../stores/siteSettings'
import { ROUTE_NAMES } from '../router'
import FloatingCTA from './FloatingCTA.vue'
import type { PublicMenuItem } from '../types/content'

const $route = useRoute()
const isMobileMenuOpen = ref(false)
const publicContentStore = usePublicContentStore()
const siteSettingsStore = useSiteSettingsStore()
const publicMenusStore = usePublicMenusStore()
const { siteName, companyNameEn, shortName, slogan, logoPath, footerText, mergedContactInfo } = storeToRefs(siteSettingsStore)
const { mainMenuItems: navItems } = storeToRefs(publicMenusStore)
const { serviceCategories, projectCategories, postCategories, equipmentCategories } = storeToRefs(publicContentStore)

const ROUTE_PARENTS: Record<string, string[]> = {
  [ROUTE_NAMES.home]: [ROUTE_NAMES.home],
  [ROUTE_NAMES.about]: [ROUTE_NAMES.about],
  [ROUTE_NAMES.services]: [ROUTE_NAMES.services, ROUTE_NAMES.serviceDetail],
  [ROUTE_NAMES.projects]: [ROUTE_NAMES.projects, ROUTE_NAMES.projectDetail],
  [ROUTE_NAMES.equipment]: [ROUTE_NAMES.equipment, ROUTE_NAMES.equipmentDetail],
  [ROUTE_NAMES.news]: [ROUTE_NAMES.news, ROUTE_NAMES.newsDetail],
  [ROUTE_NAMES.contact]: [ROUTE_NAMES.contact],
}

const PATH_TO_NAME: Record<string, string> = {
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
  if (isProjectsNavItem(item)) return ROUTE_NAMES.projects
  if (isNewsNavItem(item)) return ROUTE_NAMES.news
  if (item.resolvedUrl === '/thiet-bi' || item.resolvedUrl === '/may-moc' || item.resolvedUrl === '/may-moc-thiet-bi') return ROUTE_NAMES.equipment
  return PATH_TO_NAME[item.resolvedUrl] || null
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
  const relatedNames = ROUTE_PARENTS[routeName]
  return relatedNames ? relatedNames.includes($route.name as string) : false
}

function isExternalUrl(path: string) {
  return /^https?:\/\//i.test(path)
}

function isActiveCategoryRoute(routeName: string, slug: string) {
  return $route.name === routeName && $route.query.category === slug
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

function getMobileNavCategories(item: PublicMenuItem) {
  if (isServicesNavItem(item)) return serviceCategories.value
  if (isProjectsNavItem(item)) return projectCategories.value
  if (isNewsNavItem(item)) return postCategories.value
  if (isEquipmentsNavItem(item)) return equipmentCategories.value
  return []
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
