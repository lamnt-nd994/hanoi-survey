import type { RouteRecordRaw } from 'vue-router'

export const mediaRoutes: RouteRecordRaw[] = [
  {
    path: 'media',
    name: 'media',
    component: () => import('@/pages/media/MediaListPage.vue'),
    meta: { title: 'Thư viện Media', section: 'media', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'media/upload',
    name: 'media-upload',
    component: () => import('@/pages/media/MediaFormPage.vue'),
    meta: { title: 'Tải lên Media', section: 'media', requiresAuth: true, layout: 'admin' },
  },
]
