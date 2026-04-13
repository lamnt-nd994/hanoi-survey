import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  fetchServiceCategories,
  fetchEquipments,
  fetchEquipmentCategories,
  fetchPostCategories,
  fetchPageBySlug,
  fetchProjectCategories,
  fetchPosts,
  fetchProjects,
  fetchServices,
  type PaginationMeta,
} from '../lib/api'
import type { Equipment, EquipmentCategory, Post, PostCategory, Project, ProjectCategory, ServiceCategory, SitePage, SurveyService } from '../types/content'

type ResourceKey = 'services' | 'serviceCategories' | 'projects' | 'projectCategories' | 'posts' | 'postCategories' | 'equipments' | 'equipmentCategories' | 'introPage' | 'homePage'

export const usePublicContentStore = defineStore('public-content', () => {
  const services = ref<SurveyService[]>([])
  const serviceCategories = ref<ServiceCategory[]>([])
  const projects = ref<Project[]>([])
  const projectCategories = ref<ProjectCategory[]>([])
  const posts = ref<Post[]>([])
  const postCategories = ref<PostCategory[]>([])
  const postsMeta = ref<PaginationMeta>({ page: 1, size: 9, total: 0, totalPages: 1 })
  const equipments = ref<Equipment[]>([])
  const equipmentCategories = ref<EquipmentCategory[]>([])
  const introPage = ref<SitePage | null>(null)
  const homePage = ref<SitePage | null>(null)

  const loading = reactive<Record<ResourceKey, boolean>>({
    services: false,
    serviceCategories: false,
    projects: false,
    projectCategories: false,
    posts: false,
    postCategories: false,
    equipments: false,
    equipmentCategories: false,
    introPage: false,
    homePage: false,
  })

  const errors = reactive<Record<ResourceKey, string>>({
    services: '',
    serviceCategories: '',
    projects: '',
    projectCategories: '',
    posts: '',
    postCategories: '',
    equipments: '',
    equipmentCategories: '',
    introPage: '',
    homePage: '',
  })

  async function loadServices(force = false) {
    if (loading.services) return services.value
    if (services.value.length && !force) return services.value

    loading.services = true
    errors.services = ''
    try {
      services.value = await fetchServices({ page: 1, size: 12 })
      return services.value
    } catch {
      errors.services = 'Không tải được danh sách dịch vụ.'
      services.value = []
      return services.value
    } finally {
      loading.services = false
    }
  }

  async function loadServiceCategories(force = false) {
    if (loading.serviceCategories) return serviceCategories.value
    if (serviceCategories.value.length && !force) return serviceCategories.value

    loading.serviceCategories = true
    errors.serviceCategories = ''
    try {
      serviceCategories.value = await fetchServiceCategories()
      return serviceCategories.value
    } catch {
      errors.serviceCategories = 'Không tải được danh mục dịch vụ.'
      serviceCategories.value = []
      return serviceCategories.value
    } finally {
      loading.serviceCategories = false
    }
  }

  async function loadProjects(force = false) {
    if (loading.projects) return projects.value
    if (projects.value.length && !force) return projects.value

    loading.projects = true
    errors.projects = ''
    try {
      projects.value = await fetchProjects({ page: 1, size: 12 })
      return projects.value
    } catch {
      errors.projects = 'Không tải được danh sách dự án.'
      projects.value = []
      return projects.value
    } finally {
      loading.projects = false
    }
  }

  async function loadProjectCategories(force = false) {
    if (loading.projectCategories) return projectCategories.value
    if (projectCategories.value.length && !force) return projectCategories.value

    loading.projectCategories = true
    errors.projectCategories = ''
    try {
      projectCategories.value = await fetchProjectCategories()
      return projectCategories.value
    } catch {
      errors.projectCategories = 'Không tải được danh mục dự án.'
      projectCategories.value = []
      return projectCategories.value
    } finally {
      loading.projectCategories = false
    }
  }

  async function loadPosts(page = 1, force = false, categorySlug = '') {
    if (loading.posts) return posts.value
    if (posts.value.length && !force && page === 1 && !categorySlug) return posts.value

    loading.posts = true
    errors.posts = ''
    try {
      const result = await fetchPosts({ page, size: 9, ...(categorySlug ? { categorySlug } : {}) })
      posts.value = result.data
      postsMeta.value = result.meta
      return posts.value
    } catch {
      errors.posts = 'Không tải được danh sách bài viết.'
      posts.value = []
      return posts.value
    } finally {
      loading.posts = false
    }
  }

  async function loadPostCategories(force = false) {
    if (loading.postCategories) return postCategories.value
    if (postCategories.value.length && !force) return postCategories.value

    loading.postCategories = true
    errors.postCategories = ''
    try {
      postCategories.value = await fetchPostCategories()
      return postCategories.value
    } catch {
      errors.postCategories = 'Không tải được danh mục bài viết.'
      postCategories.value = []
      return postCategories.value
    } finally {
      loading.postCategories = false
    }
  }

  async function loadEquipments(force = false) {
    if (loading.equipments) return equipments.value
    if (equipments.value.length && !force) return equipments.value

    loading.equipments = true
    errors.equipments = ''
    try {
      equipments.value = await fetchEquipments({ page: 1, size: 12 })
      return equipments.value
    } catch {
      errors.equipments = 'Không tải được danh sách thiết bị.'
      equipments.value = []
      return equipments.value
    } finally {
      loading.equipments = false
    }
  }

  async function loadEquipmentCategories(force = false) {
    if (loading.equipmentCategories) return equipmentCategories.value
    if (equipmentCategories.value.length && !force) return equipmentCategories.value

    loading.equipmentCategories = true
    errors.equipmentCategories = ''
    try {
      equipmentCategories.value = await fetchEquipmentCategories()
      return equipmentCategories.value
    } catch {
      errors.equipmentCategories = 'Không tải được danh mục thiết bị.'
      equipmentCategories.value = []
      return equipmentCategories.value
    } finally {
      loading.equipmentCategories = false
    }
  }

  async function loadIntroPage(force = false) {
    if (loading.introPage) return introPage.value
    if (introPage.value && !force) return introPage.value

    loading.introPage = true
    errors.introPage = ''
    try {
      introPage.value = await fetchPageBySlug('gioi-thieu')
      return introPage.value
    } catch {
      errors.introPage = 'Không tải được nội dung giới thiệu.'
      introPage.value = null
      return introPage.value
    } finally {
      loading.introPage = false
    }
  }

  async function loadHomePage(force = false) {
    if (loading.homePage) return homePage.value
    if (homePage.value && !force) return homePage.value

    loading.homePage = true
    errors.homePage = ''
    try {
      homePage.value = await fetchPageBySlug('trang-chu')
      return homePage.value
    } catch {
      errors.homePage = 'Không tải được nội dung trang chủ.'
      homePage.value = null
      return homePage.value
    } finally {
      loading.homePage = false
    }
  }

  return {
    services,
    serviceCategories,
    projects,
    projectCategories,
    posts,
    postCategories,
    postsMeta,
    equipments,
    equipmentCategories,
    introPage,
    homePage,
    loading,
    errors,
    loadServices,
    loadServiceCategories,
    loadProjects,
    loadProjectCategories,
    loadPosts,
    loadPostCategories,
    loadEquipments,
    loadEquipmentCategories,
    loadIntroPage,
    loadHomePage,
  }
})
