import type { RouteRecordRaw } from 'vue-router'

export const servicesRoutes: RouteRecordRaw[] = [
  {
    path: 'services',
    name: 'services',
    component: () => import('@/features/services/pages/ServicesListPage.vue'),
    meta: { title: 'Quản lý dịch vụ', section: 'services', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'services/create',
    name: 'service-create',
    component: () => import('@/features/services/pages/ServiceFormPage.vue'),
    meta: { title: 'Tạo dịch vụ', section: 'services', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'services/:id/edit',
    name: 'service-edit',
    component: () => import('@/features/services/pages/ServiceFormPage.vue'),
    meta: { title: 'Chỉnh sửa dịch vụ', section: 'services', requiresAuth: true, layout: 'admin' },
  },
]
