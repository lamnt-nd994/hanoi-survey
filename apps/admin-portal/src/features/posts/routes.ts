import type { RouteRecordRaw } from 'vue-router'

export const postsRoutes: RouteRecordRaw[] = [
  {
    path: 'posts',
    name: 'posts',
    component: () => import('@/features/posts/pages/PostsListPage.vue'),
    meta: { title: 'Bài viết', section: 'posts', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'posts/create',
    name: 'post-create',
    component: () => import('@/features/posts/pages/PostFormPage.vue'),
    meta: { title: 'Tạo bài viết', section: 'posts', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'posts/:id/edit',
    name: 'post-edit',
    component: () => import('@/features/posts/pages/PostFormPage.vue'),
    meta: { title: 'Chỉnh sửa bài viết', section: 'posts', requiresAuth: true, layout: 'admin' },
  },
]
