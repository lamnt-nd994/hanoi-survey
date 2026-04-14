import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/features/auth/api/authApi'
import { getStoredToken, setStoredToken, clearStoredToken } from '@/lib/api-client'
import type { AuthUser } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<AuthUser | null>(null)
  const loading = ref(false)

  const isLoggedIn = computed(() => !!getStoredToken())
  const userInitial = computed(() => user.value?.username?.charAt(0).toUpperCase() || 'A')
  const userRoles = computed(() => user.value ? user.value.roles.join(', ') : 'Quản trị viên')
  const isSuperAdmin = computed(() => !!user.value?.roles.includes('ROLE_ADMIN'))

  async function login(username: string, password: string) {
    loading.value = true
    try {
      clearStoredToken()
      const data = await authApi.login(username, password)
      setStoredToken(data.accessToken)
      user.value = data.user
      return data
    } finally {
      loading.value = false
    }
  }

  async function fetchUser() {
    try {
      user.value = await authApi.me()
    } catch {
      user.value = null
    }
  }

  function logout() {
    user.value = null
    clearStoredToken()
  }

  async function updateMyProfile(payload: { fullName: string; email: string }) {
    user.value = await authApi.updateMyProfile(payload)
    return user.value
  }

  async function changeMyPassword(payload: { currentPassword: string; newPassword: string }) {
    return await authApi.changeMyPassword(payload)
  }

  return { user, loading, isLoggedIn, userInitial, userRoles, isSuperAdmin, login, fetchUser, logout, updateMyProfile, changeMyPassword }
})
