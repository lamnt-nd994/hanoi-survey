import type { RouteRecordRaw } from 'vue-router'

export const postsRoutes: RouteRecordRaw[] = [
  {
    path: 'posts',
    name: 'posts',
    component: () => import('@/pages/posts/PostsListPage.vue'),
    meta: { title: 'Bài viết', section: 'posts', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'posts/create',
    name: 'post-create',
    component: () => import('@/pages/posts/PostFormPage.vue'),
    meta: { title: 'Tạo bài viết', section: 'posts', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'posts/:id/edit',
    name: 'post-edit',
    component: () => import('@/pages/posts/PostFormPage.vue'),
    meta: { title: 'Chỉnh sửa bài viết', section: 'posts', requiresAuth: true, layout: 'admin' },
  },
]
