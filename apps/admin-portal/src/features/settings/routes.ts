import type { RouteRecordRaw } from 'vue-router'

export const settingsRoutes: RouteRecordRaw[] = [
  {
    path: 'company-info',
    name: 'company-info',
    component: () => import('@/pages/SettingsPage.vue'),
    meta: { title: 'Thông tin công ty', section: 'settings', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'settings',
    name: 'settings',
    component: () => import('@/pages/SettingsPage.vue'),
    meta: { title: 'Cài đặt', section: 'settings', requiresAuth: true, layout: 'admin' },
  },
]
