import type { RouteRecordRaw } from 'vue-router'

export const dashboardRoutes: RouteRecordRaw[] = [
  {
    path: 'dashboard',
    name: 'dashboard',
    component: () => import('@/pages/dashboard/DashboardPage.vue'),
    meta: { title: 'Trang chủ', section: 'dashboard', requiresAuth: true, layout: 'admin' },
  },
]
