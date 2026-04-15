<template>
  <div class="hidden border-t border-primary-light/20 bg-primary-navy text-white lg:block">
    <div class="container-shell">
      <nav class="flex min-h-[4rem] items-center justify-center gap-2 py-3">
        <template v-if="!navReady || isLoading">
          <span
            v-for="slot in navSlots"
            :key="slot.key"
            class="inline-flex min-h-[2.75rem] items-center rounded-full bg-white/10 px-4 py-2"
            :class="slot.widthClass"
            aria-hidden="true"
          >
            <span class="block h-3 w-full rounded bg-white/20"></span>
          </span>
        </template>

        <template v-else>
          <template v-for="(item, index) in navItems" :key="item.resolvedUrl">
            <div v-if="hasDropdown(item)" class="group relative text-white">
              <router-link
                :to="getNavTo(item)"
                class="relative inline-flex min-h-[2.75rem] min-w-[5.5rem] items-center justify-center gap-2 rounded-full px-3 py-2 text-sm font-semibold text-white transition-colors after:absolute after:bottom-0 after:left-4 after:right-4 after:h-0.5 after:rounded-full after:bg-white after:opacity-0 after:transition-opacity xl:px-4"
                :class="[isActiveNavRoute(item) ? 'bg-white/14 text-white after:opacity-100' : 'text-white/90 hover:bg-white/10 hover:text-white hover:after:opacity-70']"
              >
                <span class="text-center whitespace-nowrap">{{ item.title }}</span>
                <AppIcon icon="chevronDown" class="h-4 w-4 shrink-0 transition-transform group-hover:rotate-180" />
              </router-link>

              <div class="invisible absolute left-0 top-full z-50 pt-2 opacity-0 transition-all duration-150 group-hover:visible group-hover:opacity-100">
                <div class="w-[18rem] overflow-hidden rounded-lg border border-neutral-300 bg-white shadow-[0_16px_32px_rgba(15,23,42,0.08)]">
                  <div v-if="props.isServicesNavItem(item) && services.length" class="p-2">
                    <router-link
                      v-for="service in services"
                      :key="service.id"
                      :to="{ name: 'service-detail', params: { slug: service.slug } }"
                      class="flex min-h-[2.75rem] items-center justify-between gap-3 rounded-md px-3 py-2 text-sm leading-5 transition-colors"
                      :class="isActiveServiceRoute(service.slug) ? 'bg-neutral-100 text-primary-navy' : 'text-neutral-700 hover:bg-neutral-50 hover:text-primary-navy'"
                    >
                      <span>{{ service.title }}</span>
                    </router-link>
                  </div>
                  <div v-else-if="getCategories(item).length" class="p-2">
                    <router-link
                      v-for="category in getCategories(item)"
                      :key="category.id"
                      :to="getNavTo(item, category.slug)"
                      class="flex min-h-[2.5rem] items-center justify-between gap-3 rounded-md px-3 py-2 text-sm leading-5 transition-colors"
                      :class="isActiveCategoryRoute(getRouteName(item), category.slug) ? 'bg-neutral-100 text-primary-navy' : 'text-neutral-700 hover:bg-neutral-50 hover:text-primary-navy'"
                    >
                      <span>{{ category.name }}</span>
                    </router-link>
                  </div>
                  <div v-else class="px-4 py-3 text-sm text-neutral-500">
                    Chưa có dữ liệu để hiển thị.
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
              class="relative inline-flex min-h-[2.75rem] min-w-[5.5rem] items-center justify-center rounded-full px-3 py-2 text-sm font-semibold text-white transition-colors after:absolute after:bottom-0 after:left-4 after:right-4 after:h-0.5 after:rounded-full after:bg-white after:opacity-0 after:transition-opacity xl:px-4"
              :class="[isActiveNavRoute(item) ? 'bg-white/14 text-white after:opacity-100' : 'text-white/90 hover:bg-white/10 hover:text-white hover:after:opacity-70']"
            >
              <span class="text-center whitespace-nowrap">{{ item.title }}</span>
            </component>
          </template>
        </template>
      </nav>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { RouteLocationRaw } from 'vue-router'
import type { EquipmentCategory, PostCategory, ProjectCategory, PublicMenuItem, SurveyService } from '../../types/content'
import AppIcon from '../ui/AppIcon.vue'

type NavCategory = ProjectCategory | PostCategory | EquipmentCategory

type NavSlot = {
  key: string
  widthClass: string
}

const DEFAULT_SLOT_WIDTHS = ['w-[5.5rem]', 'w-[7.5rem]', 'w-[6.5rem]', 'w-[6rem]', 'w-[6rem]', 'w-[5.5rem]']

const props = defineProps<{
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

const navSlots = computed<NavSlot[]>(() => DEFAULT_SLOT_WIDTHS.map((widthClass, index) => ({
  key: `slot-${index}`,
  widthClass,
})))

function hasDropdown(item: PublicMenuItem) {
  return props.isServicesNavItem(item) || props.isProjectsNavItem(item) || props.isEquipmentsNavItem(item) || props.isNewsNavItem(item)
}

function getCategories(item: PublicMenuItem): NavCategory[] {
  if (props.isProjectsNavItem(item)) return props.projectCategories
  if (props.isEquipmentsNavItem(item)) return props.equipmentCategories
  if (props.isNewsNavItem(item)) return props.postCategories
  return []
}

</script>
