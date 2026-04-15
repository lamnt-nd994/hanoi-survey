import type { Router } from 'vue-router'
import { clearStoredToken, getStoredToken } from '@/lib/api-client'
import { useAuthStore } from '@/stores/auth'

let userFetched = false

export function registerRouterGuards(router: Router) {
  router.beforeEach(async (to) => {
    const token = getStoredToken()
    const requiresAuth = to.matched.some((record) => record.meta?.requiresAuth)
    const requiresSuperAdmin = to.matched.some((record) => record.meta?.requiresSuperAdmin)

    if (to.path === '/login' && token) {
      return '/dashboard'
    }

    if (requiresAuth && !token) {
      return '/login'
    }

    if (token && !userFetched) {
      userFetched = true
      try {
        const authStore = useAuthStore()
        if (!authStore.user) await authStore.fetchUser()
      } catch {
        userFetched = false
        clearStoredToken()
        return '/login'
      }
    }

    if (requiresSuperAdmin) {
      const authStore = useAuthStore()
      if (!authStore.isSuperAdmin) {
        return '/dashboard'
      }
    }

    return true
  })
}
