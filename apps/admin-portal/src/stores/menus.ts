import { defineStore } from 'pinia'
import { ref } from 'vue'
import { menusApi } from '@/lib/api'
import type { MenuEntity, MenuPayload, MenuItemEntity, MenuItemPayload } from '@/types'

export const useMenusStore = defineStore('menus', () => {
  const menus = ref<MenuEntity[]>([])
  const menuItems = ref<MenuItemEntity[]>([])
  const loading = ref(false)

  async function fetchMenus() {
    loading.value = true
    try {
      menus.value = await menusApi.list()
      if (menus.value.length) {
        menuItems.value = await menusApi.items(menus.value[0].code)
      } else {
        menuItems.value = []
      }
    } finally {
      loading.value = false
    }
  }

  async function fetchMenuItems(code: string) {
    menuItems.value = await menusApi.items(code)
  }

  async function createMenu(payload: MenuPayload) { return await menusApi.create(payload) }
  async function updateMenu(id: number, payload: MenuPayload) { return await menusApi.update(id, payload) }
  async function createMenuItem(payload: MenuItemPayload) { return await menusApi.createItem(payload) }
  async function updateMenuItem(id: number, payload: MenuItemPayload) { return await menusApi.updateItem(id, payload) }
  async function removeMenuItem(id: number) { await menusApi.deleteItem(id) }

  return { menus, menuItems, loading, fetchMenus, fetchMenuItems, createMenu, updateMenu, createMenuItem, updateMenuItem, removeMenuItem }
})
