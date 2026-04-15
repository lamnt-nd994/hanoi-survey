import type { RouteRecordRaw } from 'vue-router'

export const pagesRoutes: RouteRecordRaw[] = [
  {
    path: 'pages',
    name: 'pages',
    component: () => import('@/features/pages/pages/PagesListPage.vue'),
    meta: { title: 'Trang tĩnh', section: 'pages', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'pages/create',
    name: 'page-create',
    component: () => import('@/features/pages/pages/PageFormPage.vue'),
    meta: { title: 'Tạo trang', section: 'pages', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'pages/:id/edit',
    name: 'page-edit',
    component: () => import('@/features/pages/pages/PageFormPage.vue'),
    meta: { title: 'Chỉnh sửa trang', section: 'pages', requiresAuth: true, layout: 'admin' },
  },
]
