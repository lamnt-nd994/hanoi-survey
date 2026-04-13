import { defineStore } from 'pinia'
import { ref } from 'vue'
import { leadsApi } from '@/lib/api'
import type { ContactEntity } from '@/types'

export const useLeadsStore = defineStore('leads', () => {
  const contacts = ref<ContactEntity[]>([])
  const loading = ref(false)

  async function fetchContacts() {
    loading.value = true
    try {
      const contactsRes = await leadsApi.contacts()
      contacts.value = contactsRes.data
    } finally {
      loading.value = false
    }
  }

  async function updateContactStatus(id: number, status: string) {
    await leadsApi.updateContactStatus(id, status)
  }

  return { contacts, loading, fetchContacts, updateContactStatus }
})
