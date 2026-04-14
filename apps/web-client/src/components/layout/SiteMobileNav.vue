<template>
  <div
    v-show="isOpen"
    class="max-h-[calc(100dvh-6rem)] overflow-y-auto overscroll-contain border-t border-neutral-200 bg-white px-4 py-6 shadow-lg lg:hidden"
  >
    <nav class="flex flex-col gap-2">
      <template v-for="item in navItems" :key="item.resolvedUrl">
        <div v-if="hasDropdown(item)" class="rounded-2xl border border-neutral-100 p-1">
          <router-link
            :to="getNavTo(item)"
            @click="emit('close')"
            class="flex min-h-[44px] cursor-pointer items-center rounded-xl px-4 py-3 text-base font-medium transition-all"
            :class="isActiveNavRoute(item) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-700 hover:bg-neutral-100'"
          >
            {{ item.title }}
          </router-link>
          <div v-if="isServicesNavItem(item)" class="mt-1 space-y-1 px-2 pb-2">
            <router-link
              v-for="service in services"
              :key="`${item.resolvedUrl}-${service.slug}`"
              :to="{ name: 'service-detail', params: { slug: service.slug } }"
              @click="emit('close')"
              class="flex min-h-[40px] cursor-pointer items-center rounded-xl px-3 py-2 text-sm transition-colors"
              :class="isActiveServiceRoute(service.slug) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-600 hover:bg-neutral-100 hover:text-primary-navy'"
            >
              {{ service.title }}
            </router-link>
            <div v-if="!services.length" class="px-3 py-2 text-sm text-neutral-500">Chưa có dịch vụ để hiển thị.</div>
          </div>
          <div v-else class="mt-1 space-y-1 px-2 pb-2">
            <router-link
              v-for="category in getMobileNavCategories(item)"
              :key="`${item.resolvedUrl}-${category.slug}`"
              :to="getNavTo(item, category.slug)"
              @click="emit('close')"
              class="flex min-h-[40px] cursor-pointer items-center rounded-xl px-3 py-2 text-sm transition-colors"
              :class="isActiveCategoryRoute(getRouteName(item), category.slug) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-600 hover:bg-neutral-100 hover:text-primary-navy'"
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
          @click="emit('close')"
          class="flex min-h-[44px] cursor-pointer items-center rounded-xl px-4 py-3 text-base font-medium transition-all"
          :class="isActiveNavRoute(item) ? 'bg-accent-green/10 text-accent-green' : 'text-neutral-700 hover:bg-neutral-100'"
        >
          {{ item.title }}
        </component>
      </template>
    </nav>

    <div class="mt-6 space-y-3">
      <Button v-if="phone" as="a" :href="`tel:${phone}`" class="flex w-full items-center justify-center">
        <AppIcon icon="phone" class="mr-2 h-5 w-5" />
        Gọi ngay: {{ phone }}
      </Button>
      <Button v-if="zaloUrl" as="a" :href="zaloUrl" target="_blank" rel="noopener noreferrer" variant="outline" class="flex w-full items-center justify-center">
        <AppIcon icon="zalo" class="mr-2 h-5 w-5" />
        Chat Zalo
      </Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'
import type { EquipmentCategory, PostCategory, ProjectCategory, PublicMenuItem, SurveyService } from '../../types/content'
import AppIcon from '../ui/AppIcon.vue'
import { Button } from '../ui/button'

type NavCategory = ProjectCategory | PostCategory | EquipmentCategory

const props = defineProps<{
  isOpen: boolean
  navItems: PublicMenuItem[]
  phone: string
  zaloUrl: string
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
  close: []
}>()

function hasDropdown(item: PublicMenuItem) {
  return props.isServicesNavItem(item) || props.isProjectsNavItem(item) || props.isEquipmentsNavItem(item) || props.isNewsNavItem(item)
}

function getMobileNavCategories(item: PublicMenuItem): NavCategory[] {
  if (props.isProjectsNavItem(item)) return props.projectCategories
  if (props.isNewsNavItem(item)) return props.postCategories
  if (props.isEquipmentsNavItem(item)) return props.equipmentCategories
  return []
}
</script>
