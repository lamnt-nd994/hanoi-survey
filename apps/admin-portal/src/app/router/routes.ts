import type { RouteRecordRaw } from 'vue-router'
import AdminLayout from '@/app/layouts/AdminLayout.vue'
import { authRoutes } from '@/features/auth/routes'
import { dashboardRoutes } from '@/features/dashboard/routes'
import { equipmentsRoutes } from '@/features/equipments/routes'
import { mediaRoutes } from '@/features/media/routes'
import { menusRoutes } from '@/features/menus/routes'
import { pagesRoutes } from '@/features/pages/routes'
import { postsRoutes } from '@/features/posts/routes'
import { projectsRoutes } from '@/features/projects/routes'
import { servicesRoutes } from '@/features/services/routes'
import { settingsRoutes } from '@/features/settings/routes'
import { usersRoutes } from '@/features/users/routes'

const adminChildRoutes: RouteRecordRaw[] = [
  { path: '', redirect: '/company-info' },
  ...dashboardRoutes,
  ...pagesRoutes,
  ...postsRoutes,
  ...servicesRoutes,
  ...projectsRoutes,
  ...equipmentsRoutes,
  ...menusRoutes,
  { path: 'leads', redirect: '/company-info' },
  { path: 'contacts', redirect: '/company-info' },
  ...settingsRoutes,
  ...mediaRoutes,
  ...usersRoutes,
]

export const appRoutes: RouteRecordRaw[] = [
  ...authRoutes,
  {
    path: '/',
    component: AdminLayout,
    meta: { requiresAuth: true, layout: 'admin' },
    children: adminChildRoutes,
  },
]
