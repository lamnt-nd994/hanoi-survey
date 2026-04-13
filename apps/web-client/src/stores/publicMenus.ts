import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { fetchPublicMenu } from '../lib/api'
import type { PublicMenuItem } from '../types/content'

const FALLBACK_MAIN_MENU: PublicMenuItem[] = [
  { id: 1, menuId: 0, parentId: null, title: 'Trang chủ', itemType: 'URL', refId: null, url: '/', resolvedUrl: '/', icon: null, sortOrder: 1, visible: true },
  { id: 2, menuId: 0, parentId: null, title: 'Giới thiệu', itemType: 'URL', refId: null, url: '/gioi-thieu', resolvedUrl: '/gioi-thieu', icon: null, sortOrder: 2, visible: true },
  { id: 3, menuId: 0, parentId: null, title: 'Lĩnh vực', itemType: 'URL', refId: null, url: '/linh-vuc', resolvedUrl: '/linh-vuc', icon: null, sortOrder: 3, visible: true },
  { id: 4, menuId: 0, parentId: null, title: 'Dự án', itemType: 'URL', refId: null, url: '/du-an', resolvedUrl: '/du-an', icon: null, sortOrder: 4, visible: true },
  { id: 5, menuId: 0, parentId: null, title: 'Tin tức', itemType: 'URL', refId: null, url: '/tin-tuc', resolvedUrl: '/tin-tuc', icon: null, sortOrder: 5, visible: true },
  { id: 6, menuId: 0, parentId: null, title: 'Liên hệ', itemType: 'URL', refId: null, url: '/lien-he', resolvedUrl: '/lien-he', icon: null, sortOrder: 6, visible: true },
]

export const usePublicMenusStore = defineStore('public-menus', () => {
  const mainMenu = ref<PublicMenuItem[]>([])
  const loading = ref(false)
  const loaded = ref(false)

  const mainMenuItems = computed(() => mainMenu.value.length ? mainMenu.value : FALLBACK_MAIN_MENU)

  async function loadMainMenu(force = false) {
    if (loading.value) return
    if (loaded.value && !force) return

    loading.value = true
    try {
      const headerMain = await fetchPublicMenu('HEADER_MAIN')
      mainMenu.value = headerMain.length ? headerMain : FALLBACK_MAIN_MENU
    } catch {
      try {
        const alternateMain = await fetchPublicMenu('main')
        mainMenu.value = alternateMain.length ? alternateMain : FALLBACK_MAIN_MENU
      } catch {
        mainMenu.value = FALLBACK_MAIN_MENU
      }
    } finally {
      loading.value = false
      loaded.value = true
    }
  }

  return {
    mainMenu,
    mainMenuItems,
    loading,
    loaded,
    loadMainMenu,
  }
})
