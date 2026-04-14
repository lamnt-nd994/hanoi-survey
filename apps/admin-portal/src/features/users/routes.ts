import type { RouteRecordRaw } from 'vue-router'

export const usersRoutes: RouteRecordRaw[] = [
  {
    path: 'users',
    name: 'users',
    component: () => import('@/features/users/pages/UsersListPage.vue'),
    meta: { title: 'Người dùng', section: 'users', requiresAuth: true, requiresSuperAdmin: true, layout: 'admin' },
  },
  {
    path: 'users/roles',
    name: 'user-roles',
    component: () => import('@/features/users/pages/RolesPage.vue'),
    meta: { title: 'Vai trò người dùng', section: 'users', requiresAuth: true, requiresSuperAdmin: true, layout: 'admin' },
  },
  {
    path: 'users/create',
    name: 'user-create',
    component: () => import('@/features/users/pages/UserFormPage.vue'),
    meta: { title: 'Tạo người dùng', section: 'users', requiresAuth: true, requiresSuperAdmin: true, layout: 'admin' },
  },
  {
    path: 'users/:id/edit',
    name: 'user-edit',
    component: () => import('@/features/users/pages/UserFormPage.vue'),
    meta: { title: 'Chỉnh sửa người dùng', section: 'users', requiresAuth: true, requiresSuperAdmin: true, layout: 'admin' },
  },
]
