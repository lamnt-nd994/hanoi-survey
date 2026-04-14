<template>
  <div class="cms-layout">
    <aside class="cms-sidebar" :class="{ collapsed: sidebarCollapsed, 'mobile-open': mobileOpen }">
      <div class="cms-sidebar-logo">
        <div class="cms-sidebar-logo-icon">H</div>
        <div class="cms-sidebar-logo-text">
          <h1>Hanoi Survey</h1>
          <span>Hệ thống CMS</span>
        </div>
      </div>

      <nav class="cms-sidebar-nav">
        <div class="cms-section-title">Tổng quan</div>
        <router-link to="/dashboard" class="cms-nav-item" :class="{ active: $route.path === '/dashboard' }">
          <LayoutDashboard class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Trang chủ</span>
        </router-link>

        <div class="cms-section-title">Nội dung</div>
        <router-link to="/pages" class="cms-nav-item" :class="{ active: $route.path.startsWith('/pages') }">
          <FileText class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Trang tĩnh</span>
        </router-link>
        <router-link to="/menus" class="cms-nav-item" :class="{ active: $route.path === '/menus' }">
          <Menu class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Menu</span>
        </router-link>
        <router-link to="/posts" class="cms-nav-item" :class="{ active: $route.path.startsWith('/posts') }">
          <Newspaper class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Bài viết</span>
        </router-link>
        <div>
          <Button type="button" variant="ghost" class="cms-nav-item cms-nav-toggle w-full justify-start text-left" :class="{ active: isServicesSection }" @click="toggleServicesMenu">
            <BriefcaseBusiness class="cms-nav-icon h-4 w-4" />
            <span class="cms-sidebar-label">Dịch vụ</span>
            <ChevronDown class="cms-nav-chevron h-4 w-4" :class="{ open: servicesMenuOpen }" />
          </Button>
          <div v-show="servicesMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/services" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/services' || route.path === '/services/create' || /^\/services\/\d+\/edit$/.test(route.path) }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Quản lý dịch vụ</span>
            </router-link>
          </div>
        </div>
        <div>
          <Button type="button" variant="ghost" class="cms-nav-item cms-nav-toggle w-full justify-start text-left" :class="{ active: isProjectsSection }" @click="toggleProjectsMenu">
            <FolderKanban class="cms-nav-icon h-4 w-4" />
            <span class="cms-sidebar-label">Dự án</span>
            <ChevronDown class="cms-nav-chevron h-4 w-4" :class="{ open: projectsMenuOpen }" />
          </Button>
          <div v-show="projectsMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/projects" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/projects' || route.path === '/projects/create' || /^\/projects\/\d+\/edit$/.test(route.path) }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Quản lý dự án</span>
            </router-link>
            <router-link to="/projects/categories" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/projects/categories' }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Danh mục dự án</span>
            </router-link>
          </div>
        </div>
        <div>
          <Button type="button" variant="ghost" class="cms-nav-item cms-nav-toggle w-full justify-start text-left" :class="{ active: isEquipmentsSection }" @click="toggleEquipmentsMenu">
            <Wrench class="cms-nav-icon h-4 w-4" />
            <span class="cms-sidebar-label">Thiết bị</span>
            <ChevronDown class="cms-nav-chevron h-4 w-4" :class="{ open: equipmentsMenuOpen }" />
          </Button>
          <div v-show="equipmentsMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/equipments" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/equipments' || route.path === '/equipments/create' || /^\/equipments\/\d+\/edit$/.test(route.path) }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Quản lý thiết bị</span>
            </router-link>
            <router-link to="/equipments/categories" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/equipments/categories' }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Danh mục thiết bị</span>
            </router-link>
          </div>
        </div>

        <div class="cms-section-title">Khách hàng</div>
        <router-link to="/company-info" class="cms-nav-item" :class="{ active: $route.path === '/company-info' || $route.path === '/contacts' }">
          <Building2 class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Thông tin công ty</span>
        </router-link>
        <div class="cms-section-title">Hệ thống</div>
        <div v-if="authStore.isSuperAdmin">
          <Button type="button" variant="ghost" class="cms-nav-item cms-nav-toggle w-full justify-start text-left" :class="{ active: isUsersSection }" @click="toggleUsersMenu">
            <Users class="cms-nav-icon h-4 w-4" />
            <span class="cms-sidebar-label">Người dùng</span>
            <ChevronDown class="cms-nav-chevron h-4 w-4" :class="{ open: usersMenuOpen }" />
          </Button>
          <div v-show="usersMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/users" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/users' || route.path === '/users/create' || /^\/users\/\d+\/edit$/.test(route.path) }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Quản lý user</span>
            </router-link>
            <router-link to="/users/roles" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/users/roles' }">
              <Dot class="cms-subnav-icon h-4 w-4" />
              <span class="cms-sidebar-label">Vai trò user</span>
            </router-link>
          </div>
        </div>
        <router-link to="/media" class="cms-nav-item" :class="{ active: $route.path.startsWith('/media') }">
          <ImageIcon class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Thư viện Media</span>
        </router-link>
        <router-link to="/settings" class="cms-nav-item" :class="{ active: $route.path === '/settings' }">
          <Settings class="cms-nav-icon h-4 w-4" />
          <span class="cms-sidebar-label">Cài đặt</span>
        </router-link>
      </nav>

      <div class="cms-user">
        <div class="cms-user-avatar">{{ authStore.userInitial }}</div>
        <div class="cms-user-info">
          <h4>{{ authStore.user?.fullName || authStore.user?.username || 'Admin' }}</h4>
          <span>{{ authStore.user?.email || authStore.userRoles }}</span>
        </div>
        <Button @click="authStore.logout(); $router.push('/login')" variant="ghost" size="icon" class="cms-act-btn" title="Đăng xuất" style="flex-shrink:0">
          <LogOut class="h-4 w-4" />
        </Button>
      </div>
    </aside>

    <div class="cms-main" @click="mobileOpen = false">
      <ToastViewport />
      <header class="cms-topbar">
        <div class="cms-topbar-left">
          <Button class="cms-topbar-toggle" variant="ghost" size="icon" @click.stop="toggleSidebar"><PanelLeft class="h-5 w-5" /></Button>
          <div class="cms-breadcrumb">
            <span class="bc-muted">CMS</span>
            <ChevronRight class="bc-sep h-4 w-4" />
            <span class="bc-active">{{ pageTitle }}</span>
          </div>
        </div>
        <div style="display:flex;align-items:center;gap:8px">
          <Button as="button" variant="secondary" size="sm" @click="authStore.logout(); $router.push('/login')">Đăng xuất</Button>
        </div>
      </header>
      <main class="cms-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { BriefcaseBusiness, Building2, ChevronDown, ChevronRight, Dot, FileText, FolderKanban, ImageIcon, LayoutDashboard, LogOut, Menu, Newspaper, PanelLeft, Settings, Users, Wrench } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { useAuthStore } from '@/stores/auth'
import ToastViewport from '@/components/shared/ToastViewport.vue'

const route = useRoute()
const authStore = useAuthStore()
const sidebarCollapsed = ref(false)
const mobileOpen = ref(false)
const servicesMenuOpen = ref(route.path.startsWith('/services'))
const usersMenuOpen = ref(route.path.startsWith('/users'))
const projectsMenuOpen = ref(route.path.startsWith('/projects'))
const equipmentsMenuOpen = ref(route.path.startsWith('/equipments'))

const pageTitle = computed(() => {
  return (route.meta.title as string) || 'Quản trị'
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
