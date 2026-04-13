import {createRouter, createWebHistory} from 'vue-router'

export const ROUTE_NAMES = {
  home: 'home',
  about: 'about',
  services: 'services',
  serviceDetail: 'service-detail',
  projects: 'projects',
  projectDetail: 'project-detail',
  news: 'news',
  newsDetail: 'news-detail',
  equipmentRedirect: 'equipment-redirect',
  equipment: 'equipment',
  equipmentDetail: 'equipment-detail',
  page: 'page',
  contact: 'contact',
} as const

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/PublicLayout.vue'),
      children: [
        { path: '', name: ROUTE_NAMES.home, component: () => import('../pages/HomePage.vue') },
        { path: 'gioi-thieu', name: ROUTE_NAMES.about, component: () => import('../pages/AboutPage.vue') },
        { path: 'linh-vuc', name: ROUTE_NAMES.services, component: () => import('../pages/GenericPage.vue') },
        { path: 'linh-vuc/:slug', name: ROUTE_NAMES.serviceDetail, component: () => import('../pages/DetailPage.vue'), meta: { type: 'service' } },
        { path: 'du-an', name: ROUTE_NAMES.projects, component: () => import('../pages/GenericPage.vue') },
        { path: 'du-an/:slug', name: ROUTE_NAMES.projectDetail, component: () => import('../pages/DetailPage.vue'), meta: { type: 'project' } },
        { path: 'tin-tuc', name: ROUTE_NAMES.news, component: () => import('../pages/GenericPage.vue') },
        { path: 'tin-tuc/:slug', name: ROUTE_NAMES.newsDetail, component: () => import('../pages/DetailPage.vue'), meta: { type: 'post' } },
        { path: 'may-moc-thiet-bi/:slug', name: ROUTE_NAMES.equipmentRedirect, redirect: (to) => ({ name: ROUTE_NAMES.equipmentDetail, params: { slug: to.params.slug } }) },
        { path: 'may-moc-thiet-bi', name: ROUTE_NAMES.equipment, component: () => import('../pages/GenericPage.vue') },
        { path: 'may-moc-thiet-bi/:slug', name: ROUTE_NAMES.equipmentDetail, component: () => import('../pages/DetailPage.vue'), meta: { type: 'equipment' } },
        { path: 'trang/:slug', name: ROUTE_NAMES.page, component: () => import('../pages/DetailPage.vue'), meta: { type: 'page' } },
        { path: 'lien-he', name: ROUTE_NAMES.contact, component: () => import('../pages/GenericPage.vue') },
      ],
    },
  ],
})

export default router
