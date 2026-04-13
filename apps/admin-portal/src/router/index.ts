import { createRouter, createWebHistory } from 'vue-router'
import { getStoredToken, clearStoredToken } from '@/lib/api'
import { useAuthStore } from '@/stores/auth'
import AdminLayout from '@/components/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: () => import('@/pages/LoginPage.vue') },
    {
      path: '/',
      component: AdminLayout,
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', name: 'dashboard', component: () => import('@/pages/dashboard/DashboardPage.vue') },
        { path: 'pages', name: 'pages', component: () => import('@/pages/pages/PagesListPage.vue') },
        { path: 'pages/create', name: 'page-create', component: () => import('@/pages/pages/PageFormPage.vue') },
        { path: 'pages/:id/edit', name: 'page-edit', component: () => import('@/pages/pages/PageFormPage.vue') },
        { path: 'posts', name: 'posts', component: () => import('@/pages/posts/PostsListPage.vue') },
        { path: 'posts/create', name: 'post-create', component: () => import('@/pages/posts/PostFormPage.vue') },
        { path: 'posts/:id/edit', name: 'post-edit', component: () => import('@/pages/posts/PostFormPage.vue') },
        { path: 'services', name: 'services', component: () => import('@/pages/services/ServicesListPage.vue') },
        { path: 'services/categories', name: 'service-categories', component: () => import('@/pages/services/ServiceCategoriesPage.vue') },
        { path: 'services/create', name: 'service-create', component: () => import('@/pages/services/ServiceFormPage.vue') },
        { path: 'services/:id/edit', name: 'service-edit', component: () => import('@/pages/services/ServiceFormPage.vue') },
        { path: 'users', name: 'users', component: () => import('@/pages/users/UsersListPage.vue') },
        { path: 'users/roles', name: 'user-roles', component: () => import('@/pages/users/RolesPage.vue') },
        { path: 'users/create', name: 'user-create', component: () => import('@/pages/users/UserFormPage.vue') },
        { path: 'users/:id/edit', name: 'user-edit', component: () => import('@/pages/users/UserFormPage.vue') },
        { path: 'projects', name: 'projects', component: () => import('@/pages/projects/ProjectsListPage.vue') },
        { path: 'projects/categories', name: 'project-categories', component: () => import('@/pages/projects/ProjectCategoriesPage.vue') },
        { path: 'projects/create', name: 'project-create', component: () => import('@/pages/projects/ProjectFormPage.vue') },
        { path: 'projects/:id/edit', name: 'project-edit', component: () => import('@/pages/projects/ProjectFormPage.vue') },
        { path: 'equipments', name: 'equipments', component: () => import('@/pages/equipments/EquipmentsListPage.vue') },
        { path: 'equipments/categories', name: 'equipment-categories', component: () => import('@/pages/equipments/EquipmentCategoriesPage.vue') },
        { path: 'equipments/create', name: 'equipment-create', component: () => import('@/pages/equipments/EquipmentFormPage.vue') },
        { path: 'equipments/:id/edit', name: 'equipment-edit', component: () => import('@/pages/equipments/EquipmentFormPage.vue') },
        { path: 'menus', name: 'menus', component: () => import('@/pages/MenusPage.vue') },
        { path: 'leads', redirect: '/company-info' },
        { path: 'contacts', redirect: '/company-info' },
        { path: 'company-info', name: 'company-info', component: () => import('@/pages/SettingsPage.vue') },
        { path: 'media', name: 'media', component: () => import('@/pages/media/MediaListPage.vue') },
        { path: 'media/upload', name: 'media-upload', component: () => import('@/pages/media/MediaFormPage.vue') },
        { path: 'settings', name: 'settings', component: () => import('@/pages/SettingsPage.vue') },
      ],
    },
  ],
})

let userFetched = false

router.beforeEach(async (to) => {
  const token = getStoredToken()
  if (to.path !== '/login' && !token) {
    return '/login'
  }
  if (token && !userFetched) {
    userFetched = true
    try {
      const authStore = useAuthStore()
      if (!authStore.user) await authStore.fetchUser()
    } catch {
      userFetched = false
      clearStoredToken()
      return '/login'
    }
  }

  if (to.path.startsWith('/users')) {
    const authStore = useAuthStore()
    if (!authStore.isSuperAdmin) {
      return '/dashboard'
    }
  }
})

export default router
