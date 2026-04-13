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
          <span class="cms-nav-icon">&#9673;</span>
          <span class="cms-sidebar-label">Trang chủ</span>
        </router-link>

        <div class="cms-section-title">Nội dung</div>
        <router-link to="/pages" class="cms-nav-item" :class="{ active: $route.path.startsWith('/pages') }">
          <span class="cms-nav-icon">&#9998;</span>
          <span class="cms-sidebar-label">Trang tĩnh</span>
        </router-link>
        <router-link to="/menus" class="cms-nav-item" :class="{ active: $route.path === '/menus' }">
          <span class="cms-nav-icon">&#9776;</span>
          <span class="cms-sidebar-label">Menu</span>
        </router-link>
        <router-link to="/posts" class="cms-nav-item" :class="{ active: $route.path.startsWith('/posts') }">
          <span class="cms-nav-icon">&#9997;</span>
          <span class="cms-sidebar-label">Bài viết</span>
        </router-link>
        <div>
          <button type="button" class="cms-nav-item cms-nav-toggle w-full text-left" :class="{ active: isServicesSection }" @click="toggleServicesMenu">
            <span class="cms-nav-icon">&#9672;</span>
            <span class="cms-sidebar-label">Dịch vụ</span>
            <span class="cms-nav-chevron" :class="{ open: servicesMenuOpen }">&#9662;</span>
          </button>
          <div v-show="servicesMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/services" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/services' || route.path === '/services/create' || /^\/services\/\d+\/edit$/.test(route.path) }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Quản lý dịch vụ</span>
            </router-link>
            <router-link to="/services/categories" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/services/categories' }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Danh mục dịch vụ</span>
            </router-link>
          </div>
        </div>
        <div>
          <button type="button" class="cms-nav-item cms-nav-toggle w-full text-left" :class="{ active: isProjectsSection }" @click="toggleProjectsMenu">
            <span class="cms-nav-icon">&#9638;</span>
            <span class="cms-sidebar-label">Dự án</span>
            <span class="cms-nav-chevron" :class="{ open: projectsMenuOpen }">&#9662;</span>
          </button>
          <div v-show="projectsMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/projects" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/projects' || route.path === '/projects/create' || /^\/projects\/\d+\/edit$/.test(route.path) }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Quản lý dự án</span>
            </router-link>
            <router-link to="/projects/categories" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/projects/categories' }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Danh mục dự án</span>
            </router-link>
          </div>
        </div>
        <div>
          <button type="button" class="cms-nav-item cms-nav-toggle w-full text-left" :class="{ active: isEquipmentsSection }" @click="toggleEquipmentsMenu">
            <span class="cms-nav-icon">&#9881;</span>
            <span class="cms-sidebar-label">Thiết bị</span>
            <span class="cms-nav-chevron" :class="{ open: equipmentsMenuOpen }">&#9662;</span>
          </button>
          <div v-show="equipmentsMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/equipments" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/equipments' || route.path === '/equipments/create' || /^\/equipments\/\d+\/edit$/.test(route.path) }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Quản lý thiết bị</span>
            </router-link>
            <router-link to="/equipments/categories" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/equipments/categories' }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Danh mục thiết bị</span>
            </router-link>
          </div>
        </div>

        <div class="cms-section-title">Khách hàng</div>
        <router-link to="/company-info" class="cms-nav-item" :class="{ active: $route.path === '/company-info' || $route.path === '/contacts' }">
          <span class="cms-nav-icon">&#9993;</span>
          <span class="cms-sidebar-label">Thông tin công ty</span>
        </router-link>
        <div class="cms-section-title">Hệ thống</div>
        <div v-if="authStore.isSuperAdmin">
          <button type="button" class="cms-nav-item cms-nav-toggle w-full text-left" :class="{ active: isUsersSection }" @click="toggleUsersMenu">
            <span class="cms-nav-icon">&#128101;</span>
            <span class="cms-sidebar-label">Người dùng</span>
            <span class="cms-nav-chevron" :class="{ open: usersMenuOpen }">&#9662;</span>
          </button>
          <div v-show="usersMenuOpen && !sidebarCollapsed" class="cms-subnav">
            <router-link to="/users" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/users' || route.path === '/users/create' || /^\/users\/\d+\/edit$/.test(route.path) }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Quản lý user</span>
            </router-link>
            <router-link to="/users/roles" class="cms-nav-item cms-subnav-item" :class="{ active: route.path === '/users/roles' }">
              <span class="cms-subnav-icon">&#8226;</span>
              <span class="cms-sidebar-label">Vai trò user</span>
            </router-link>
          </div>
        </div>
        <router-link to="/media" class="cms-nav-item" :class="{ active: $route.path.startsWith('/media') }">
          <span class="cms-nav-icon">&#9635;</span>
          <span class="cms-sidebar-label">Thư viện Media</span>
        </router-link>
        <router-link to="/settings" class="cms-nav-item" :class="{ active: $route.path === '/settings' }">
          <span class="cms-nav-icon">&#9881;</span>
          <span class="cms-sidebar-label">Cài đặt</span>
        </router-link>
      </nav>

      <div class="cms-user">
        <div class="cms-user-avatar">{{ authStore.userInitial }}</div>
        <div class="cms-user-info">
          <h4>{{ authStore.user?.fullName || authStore.user?.username || 'Admin' }}</h4>
          <span>{{ authStore.user?.email || authStore.userRoles }}</span>
        </div>
        <button @click="authStore.logout(); $router.push('/login')" class="cms-act-btn" title="Đăng xuất" style="flex-shrink:0">&#8682;</button>
      </div>
    </aside>

    <div class="cms-main" @click="mobileOpen = false">
      <header class="cms-topbar">
        <div class="cms-topbar-left">
          <button class="cms-topbar-toggle" @click.stop="toggleSidebar">&#9776;</button>
          <div class="cms-breadcrumb">
            <span class="bc-muted">CMS</span>
            <span class="bc-sep">&#9654;</span>
            <span class="bc-active">{{ pageTitle }}</span>
          </div>
        </div>
        <div style="display:flex;align-items:center;gap:8px">
          <router-link to="/login" class="cms-btn cms-btn-secondary cms-btn-sm">Đăng xuất</router-link>
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
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const authStore = useAuthStore()
const sidebarCollapsed = ref(false)
const mobileOpen = ref(false)
const servicesMenuOpen = ref(route.path.startsWith('/services'))
const usersMenuOpen = ref(route.path.startsWith('/users'))
const projectsMenuOpen = ref(route.path.startsWith('/projects'))
const equipmentsMenuOpen = ref(route.path.startsWith('/equipments'))

const pageTitles: Record<string, string> = {
  '/dashboard': 'Trang chủ',
  '/pages': 'Trang tĩnh',
  '/menus': 'Menu',
  '/posts': 'Bài viết',
  '/services': 'Dịch vụ',
  '/services/categories': 'Danh mục dịch vụ',
  '/users': 'Người dùng',
  '/users/roles': 'Vai trò người dùng',
  '/projects': 'Dự án',
  '/projects/categories': 'Danh mục dự án',
  '/equipments': 'Thiết bị',
  '/equipments/categories': 'Danh mục thiết bị',
  '/company-info': 'Thông tin công ty',
  '/contacts': 'Thông tin công ty',
  '/media': 'Thư viện Media',
  '/settings': 'Cài đặt',
}

const pageTitle = computed(() => {
  const path = route.path.replace(/\/\d+\/edit$/, '').replace(/\/create$/, '')
  return pageTitles[path] || 'Quản trị'
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
