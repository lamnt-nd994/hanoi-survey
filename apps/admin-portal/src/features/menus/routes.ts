import type { RouteRecordRaw } from 'vue-router'

export const menusRoutes: RouteRecordRaw[] = [
  {
    path: 'menus',
    name: 'menus',
    component: () => import('@/features/menus/pages/MenusPage.vue'),
    meta: { title: 'Menu', section: 'menus', requiresAuth: true, layout: 'admin' },
  },
]
