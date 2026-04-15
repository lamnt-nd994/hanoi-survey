<template>
  <div class="flex min-h-screen bg-slate-50 text-slate-900">
    <aside :class="sidebarClass">
        <div :class="['flex h-16 shrink-0 items-center border-b border-slate-200 px-4', sidebarCollapsed ? 'justify-center' : 'gap-3']">
          <img v-if="companyLogoUrl" :src="companyLogoUrl" alt="Company logo" class="h-9 w-9 shrink-0 rounded-md object-contain" />
          <div v-else class="flex h-9 w-9 shrink-0 items-center justify-center rounded-md bg-slate-900 text-sm font-bold text-white">{{ companyInitial }}</div>
          <div v-if="!sidebarCollapsed" class="min-w-0">
            <h1 class="text-sm font-bold leading-tight text-slate-900">{{ companyShortName }}</h1>
            <span class="text-[10px] font-medium uppercase tracking-wider text-slate-400">Hệ thống CMS</span>
          </div>
        </div>

      <nav class="flex-1 space-y-1 overflow-y-auto px-2 py-3">
        <div v-if="!sidebarCollapsed" class="px-3 pb-1 pt-4 text-[10px] font-semibold uppercase tracking-wider text-slate-400">Nội dung</div>
        <router-link to="/pages" :class="navItemClass(route.path.startsWith('/pages'))">
          <FileText :class="iconClass" />
          <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate">Trang tĩnh</span>
        </router-link>
        <router-link to="/menus" :class="navItemClass(route.path === '/menus')">
          <Menu :class="iconClass" />
          <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate">Menu</span>
        </router-link>
        <router-link to="/posts" :class="navItemClass(route.path.startsWith('/posts'))">
          <Newspaper :class="iconClass" />
          <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate">Bài viết</span>
        </router-link>

        <div>
          <Button type="button" variant="ghost" :class="navItemClass(isServicesSection)" @click="toggleServicesMenu">
            <BriefcaseBusiness :class="iconClass" />
            <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate text-left">Dịch vụ</span>
            <ChevronDown v-if="!sidebarCollapsed" class="h-4 w-4 text-slate-400 transition-transform" :class="{ 'rotate-180': servicesMenuOpen }" />
          </Button>
          <div v-show="servicesMenuOpen && !sidebarCollapsed" class="ml-9 mt-1 grid gap-1">
            <router-link to="/services" :class="navItemClass(route.path === '/services' || route.path === '/services/create' || /^\/services\/\d+\/edit$/.test(route.path), true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Quản lý dịch vụ</span>
            </router-link>
          </div>
        </div>

        <div>
          <Button type="button" variant="ghost" :class="navItemClass(isProjectsSection)" @click="toggleProjectsMenu">
            <FolderKanban :class="iconClass" />
            <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate text-left">Dự án</span>
            <ChevronDown v-if="!sidebarCollapsed" class="h-4 w-4 text-slate-400 transition-transform" :class="{ 'rotate-180': projectsMenuOpen }" />
          </Button>
          <div v-show="projectsMenuOpen && !sidebarCollapsed" class="ml-9 mt-1 grid gap-1">
            <router-link to="/projects" :class="navItemClass(route.path === '/projects' || route.path === '/projects/create' || /^\/projects\/\d+\/edit$/.test(route.path), true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Quản lý dự án</span>
            </router-link>
            <router-link to="/projects/categories" :class="navItemClass(route.path === '/projects/categories', true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Danh mục dự án</span>
            </router-link>
          </div>
        </div>

        <div>
          <Button type="button" variant="ghost" :class="navItemClass(isEquipmentsSection)" @click="toggleEquipmentsMenu">
            <Wrench :class="iconClass" />
            <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate text-left">Thiết bị</span>
            <ChevronDown v-if="!sidebarCollapsed" class="h-4 w-4 text-slate-400 transition-transform" :class="{ 'rotate-180': equipmentsMenuOpen }" />
          </Button>
          <div v-show="equipmentsMenuOpen && !sidebarCollapsed" class="ml-9 mt-1 grid gap-1">
            <router-link to="/equipments" :class="navItemClass(route.path === '/equipments' || route.path === '/equipments/create' || /^\/equipments\/\d+\/edit$/.test(route.path), true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Quản lý thiết bị</span>
            </router-link>
            <router-link to="/equipments/categories" :class="navItemClass(route.path === '/equipments/categories', true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Danh mục thiết bị</span>
            </router-link>
          </div>
        </div>

        <div v-if="!sidebarCollapsed" class="px-3 pb-1 pt-4 text-[10px] font-semibold uppercase tracking-wider text-slate-400">Khách hàng</div>
        <router-link to="/company-info" :class="navItemClass(route.path === '/company-info' || route.path === '/contacts')">
          <Building2 :class="iconClass" />
          <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate">Thông tin công ty</span>
        </router-link>

        <div v-if="!sidebarCollapsed" class="px-3 pb-1 pt-4 text-[10px] font-semibold uppercase tracking-wider text-slate-400">Hệ thống</div>
        <div v-if="authStore.isSuperAdmin">
          <Button type="button" variant="ghost" :class="navItemClass(isUsersSection)" @click="toggleUsersMenu">
            <Users :class="iconClass" />
            <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate text-left">Người dùng</span>
            <ChevronDown v-if="!sidebarCollapsed" class="h-4 w-4 text-slate-400 transition-transform" :class="{ 'rotate-180': usersMenuOpen }" />
          </Button>
          <div v-show="usersMenuOpen && !sidebarCollapsed" class="ml-9 mt-1 grid gap-1">
            <router-link to="/users" :class="navItemClass(route.path === '/users' || route.path === '/users/create' || /^\/users\/\d+\/edit$/.test(route.path), true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Quản lý user</span>
            </router-link>
            <router-link to="/users/roles" :class="navItemClass(route.path === '/users/roles', true)">
              <Dot class="h-4 w-4 shrink-0 text-slate-400" />
              <span class="min-w-0 flex-1 truncate">Vai trò user</span>
            </router-link>
          </div>
        </div>

        <router-link to="/media" :class="navItemClass(route.path.startsWith('/media'))">
          <ImageIcon :class="iconClass" />
          <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate">Thư viện Media</span>
        </router-link>
        <router-link to="/settings" :class="navItemClass(route.path === '/settings')">
          <Settings :class="iconClass" />
          <span v-if="!sidebarCollapsed" class="min-w-0 flex-1 truncate">Cài đặt</span>
        </router-link>
      </nav>

      <div class="flex min-h-[72px] items-center gap-3 border-t border-slate-200 p-3">
        <div class="flex h-9 w-9 shrink-0 items-center justify-center rounded-md bg-slate-100 text-sm font-bold text-slate-700">{{ authStore.userInitial }}</div>
        <div v-if="!sidebarCollapsed" class="min-w-0 flex-1">
          <h4 class="truncate text-xs font-semibold text-slate-900">{{ authStore.user?.fullName || authStore.user?.username || 'Admin' }}</h4>
          <span class="block truncate text-[10px] text-slate-400">{{ authStore.user?.email || authStore.userRoles }}</span>
        </div>
        <Button variant="ghost" size="icon" class="shrink-0" title="Đăng xuất" @click="authStore.logout(); $router.push('/login')">
          <LogOut class="h-4 w-4" />
        </Button>
      </div>
    </aside>

    <div class="flex min-w-0 flex-1 flex-col overflow-hidden bg-slate-50" @click="mobileOpen = false">
      <ToastViewport />
      <header class="flex h-16 shrink-0 items-center justify-between border-b border-slate-200 bg-white px-6">
        <div class="flex items-center gap-3">
          <Button class="border border-transparent hover:border-slate-200" variant="ghost" size="icon" @click.stop="toggleSidebar">
            <PanelLeft class="h-5 w-5" />
          </Button>
          <div class="flex items-center gap-1.5 text-sm">
            <span class="text-slate-400">CMS</span>
            <template v-for="(item, index) in breadcrumbItems" :key="`${item.label}-${index}`">
              <ChevronRight class="h-4 w-4 text-slate-300" />
              <span :class="index === breadcrumbItems.length - 1 ? 'font-semibold text-slate-900' : 'text-slate-500'">{{ item.label }}</span>
            </template>
          </div>
        </div>
          <Button as="button" variant="secondary" size="sm" @click="authStore.logout(); $router.push('/login')">Đăng xuất</Button>
        </header>
      <main class="min-w-0 flex-1 overflow-y-auto p-6">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { BriefcaseBusiness, Building2, ChevronDown, ChevronRight, Dot, FileText, FolderKanban, ImageIcon, LogOut, Menu, Newspaper, PanelLeft, Settings, Users, Wrench } from 'lucide-vue-next'
import ToastViewport from '@/components/shared/ToastViewport.vue'
import { Button } from '@/components/ui/button'
import { resolveMediaUrl } from '@/lib/media'
import { useAuthStore } from '@/stores/auth'
import { useSettingsStore } from '@/stores/settings'

const route = useRoute()
const authStore = useAuthStore()
const settingsStore = useSettingsStore()
const sidebarCollapsed = ref(false)
const mobileOpen = ref(false)
const servicesMenuOpen = ref(route.path.startsWith('/services'))
const usersMenuOpen = ref(route.path.startsWith('/users'))
const projectsMenuOpen = ref(route.path.startsWith('/projects'))
const equipmentsMenuOpen = ref(route.path.startsWith('/equipments'))

const sidebarClass = computed(() => [
  'fixed inset-y-0 left-0 z-40 flex h-screen shrink-0 flex-col border-r border-slate-200 bg-white shadow-xl transition-all duration-200 md:sticky md:top-0 md:translate-x-0 md:shadow-none',
  sidebarCollapsed.value ? 'w-16' : 'w-64',
  mobileOpen.value ? 'translate-x-0' : '-translate-x-full',
])

const iconClass = computed(() => [
  'h-4 w-4 shrink-0',
  sidebarCollapsed.value ? '' : 'mr-2.5',
])

const pageTitle = computed(() => {
  return (route.meta.title as string) || 'Quản trị'
})
const companyShortName = computed(() => settingsStore.settings?.shortName || 'Hanoi Survey')
const companyLogoUrl = computed(() => settingsStore.settings?.logoPath ? resolveMediaUrl(settingsStore.settings.logoPath) : '')
const companyInitial = computed(() => companyShortName.value.trim().charAt(0).toUpperCase() || 'H')
const sectionLabelMap: Record<string, string> = {
  dashboard: 'Trang chủ',
  pages: 'Trang tĩnh',
  posts: 'Bài viết',
  services: 'Dịch vụ',
  projects: 'Dự án',
  equipments: 'Thiết bị',
  menus: 'Menu',
  settings: 'Cài đặt',
  media: 'Thư viện Media',
  users: 'Người dùng',
}
const breadcrumbItems = computed(() => {
  const section = String(route.meta.section || '')
  const parentLabel = sectionLabelMap[section]
  const currentTitle = pageTitle.value
  if (!parentLabel) return [{ label: currentTitle }]
  if (currentTitle === parentLabel) return [{ label: parentLabel }]
  return [{ label: parentLabel }, { label: currentTitle }]
})

const isServicesSection = computed(() => route.path.startsWith('/services'))
const isUsersSection = computed(() => route.path.startsWith('/users'))
const isProjectsSection = computed(() => route.path.startsWith('/projects'))
const isEquipmentsSection = computed(() => route.path.startsWith('/equipments'))

watch(() => route.path, (path) => {
  if (path.startsWith('/services')) {
    servicesMenuOpen.value = true
  }
  if (path.startsWith('/users')) {
    usersMenuOpen.value = true
  }
  if (path.startsWith('/projects')) {
    projectsMenuOpen.value = true
  }
  if (path.startsWith('/equipments')) {
    equipmentsMenuOpen.value = true
  }
})

onMounted(async () => {
  if (!settingsStore.settings) {
    await settingsStore.fetch()
  }
})

function navItemClass(active: boolean, nested = false) {
  return [
    'flex w-full items-center rounded-md text-sm font-medium text-slate-600 transition-colors hover:bg-slate-100 hover:text-slate-900',
    nested ? 'min-h-10 px-3 text-xs' : 'min-h-11 px-3',
    active ? 'bg-slate-200 font-semibold text-slate-900' : '',
    sidebarCollapsed.value ? 'justify-center px-0' : '',
  ]
}

function toggleSidebar() {
  if (window.innerWidth <= 768) mobileOpen.value = !mobileOpen.value
  else sidebarCollapsed.value = !sidebarCollapsed.value
}

function toggleProjectsMenu() {
  projectsMenuOpen.value = !projectsMenuOpen.value
}

function toggleUsersMenu() {
  usersMenuOpen.value = !usersMenuOpen.value
}

function toggleServicesMenu() {
  servicesMenuOpen.value = !servicesMenuOpen.value
}

function toggleEquipmentsMenu() {
  equipmentsMenuOpen.value = !equipmentsMenuOpen.value
}
</script>
