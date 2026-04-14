import type { RouteRecordRaw } from 'vue-router'

export const pagesRoutes: RouteRecordRaw[] = [
  {
    path: 'pages',
    name: 'pages',
    component: () => import('@/pages/pages/PagesListPage.vue'),
    meta: { title: 'Trang tĩnh', section: 'pages', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'pages/create',
    name: 'page-create',
    component: () => import('@/pages/pages/PageFormPage.vue'),
    meta: { title: 'Tạo trang', section: 'pages', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'pages/:id/edit',
    name: 'page-edit',
    component: () => import('@/pages/pages/PageFormPage.vue'),
    meta: { title: 'Chỉnh sửa trang', section: 'pages', requiresAuth: true, layout: 'admin' },
  },
]
