export type AdminRouteMeta = {
  title: string
  section?: string
  requiresAuth?: boolean
  requiresSuperAdmin?: boolean
  layout?: 'admin' | 'blank'
}
