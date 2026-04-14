import type { RouteRecordRaw } from 'vue-router'

export const equipmentsRoutes: RouteRecordRaw[] = [
  {
    path: 'equipments',
    name: 'equipments',
    component: () => import('@/features/equipments/pages/EquipmentsListPage.vue'),
    meta: { title: 'Quản lý thiết bị', section: 'equipments', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'equipments/categories',
    name: 'equipment-categories',
    component: () => import('@/features/equipments/pages/EquipmentCategoriesPage.vue'),
    meta: { title: 'Danh mục thiết bị', section: 'equipments', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'equipments/create',
    name: 'equipment-create',
    component: () => import('@/features/equipments/pages/EquipmentFormPage.vue'),
    meta: { title: 'Tạo thiết bị', section: 'equipments', requiresAuth: true, layout: 'admin' },
  },
  {
    path: 'equipments/:id/edit',
    name: 'equipment-edit',
    component: () => import('@/features/equipments/pages/EquipmentFormPage.vue'),
    meta: { title: 'Chỉnh sửa thiết bị', section: 'equipments', requiresAuth: true, layout: 'admin' },
  },
]
