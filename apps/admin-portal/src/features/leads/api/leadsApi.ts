import { api, getData, getPaging } from '@/lib/api-client'
import type { ContactEntity, PaginatedResponse } from '@/types'

export const leadsApi = {
  contacts: async (params = { page: 1, size: 20 }) =>
    getPaging<ContactEntity>(await api.get('/api/admin/v1/contacts', { params })) as PaginatedResponse<ContactEntity>,
  updateContactStatus: async (id: number, status: string) =>
    getData<void>(await api.put(`/api/admin/v1/contacts/${id}/status`, { status })),
}
