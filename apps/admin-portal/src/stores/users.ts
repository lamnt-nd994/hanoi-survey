import { defineStore } from 'pinia'
import { ref } from 'vue'
import { usersApi } from '@/lib/api'
import type { AdminRoleEntity, AdminRolePayload, AdminUserEntity, AdminUserPayload, PaginatedResponse } from '@/types'

export const useUsersStore = defineStore('users', () => {
  const items = ref<AdminUserEntity[]>([])
  const roles = ref<AdminRoleEntity[]>([])
  const meta = ref({ page: 1, size: 20, total: 0, totalPages: 0 })
  const loading = ref(false)

  async function fetchList(params: { page?: number; size?: number; keyword?: string; roleId?: number | null; active?: boolean | null } = {}) {
    loading.value = true
    try {
      const result: PaginatedResponse<AdminUserEntity> = await usersApi.list(params)
      items.value = result.data
      meta.value = result.meta
    } finally {
      loading.value = false
    }
  }

  async function fetchRoles() {
    roles.value = await usersApi.roles()
  }

  async function getById(id: number) {
    return await usersApi.getById(id)
  }

  async function create(payload: AdminUserPayload) {
    return await usersApi.create(payload)
  }

  async function update(id: number, payload: AdminUserPayload) {
    return await usersApi.update(id, payload)
  }

  async function remove(id: number) {
    await usersApi.delete(id)
  }

  async function resetPassword(id: number, password: string) {
    await usersApi.resetPassword(id, password)
  }

  async function createRole(payload: AdminRolePayload) {
    const role = await usersApi.createRole(payload)
    await fetchRoles()
    return role
  }

  async function updateRole(id: number, payload: AdminRolePayload) {
    const role = await usersApi.updateRole(id, payload)
    await fetchRoles()
    return role
  }

  async function removeRole(id: number) {
    await usersApi.deleteRole(id)
    await fetchRoles()
  }

  return { items, roles, meta, loading, fetchList, fetchRoles, getById, create, update, remove, resetPassword, createRole, updateRole, removeRole }
})
