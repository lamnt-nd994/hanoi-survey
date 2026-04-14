import type { RouteRecordRaw } from 'vue-router'

export const projectsRoutes: RouteRecordRaw[] = [
  {
    path: 'projects',
    name: 'projects',
    component: () => import('@/pages/projects/ProjectsListPage.vue'),
    meta: { title: 'Dự án', section: 'projects', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'projects/categories',
    name: 'project-categories',
    component: () => import('@/pages/projects/ProjectCategoriesPage.vue'),
    meta: { title: 'Danh mục dự án', section: 'projects', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'projects/create',
    name: 'project-create',
    component: () => import('@/pages/projects/ProjectFormPage.vue'),
    meta: { title: 'Tạo dự án', section: 'projects', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'projects/:id/edit',
    name: 'project-edit',
    component: () => import('@/pages/projects/ProjectFormPage.vue'),
    meta: { title: 'Chỉnh sửa dự án', section: 'projects', requiresAuth: true, layout: 'admin' },
  },
]
