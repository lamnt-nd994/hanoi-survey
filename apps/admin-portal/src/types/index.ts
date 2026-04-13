export interface PaginatedResponse<T> {
  data: T[]
  meta: {
    page: number
    size: number
    total: number
    totalPages: number
  }
}

export interface BaseEntity {
  id: number
  createdAt?: string
  updatedAt?: string
}

export interface PageEntity extends BaseEntity {
  title: string
  slug: string
  summary: string
  content: string
  templateCode: 'default' | 'about_company' | 'home_landing'
  contentJson: string | null
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
}

export interface PagePayload {
  title: string
  slug: string
  summary: string
  content: string
  templateCode: 'default' | 'about_company' | 'home_landing'
  contentJson: string | null
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
}

export interface HomeHeroBadgeItem {
  label: string
  iconKey: string
}

export interface HomeStatItem {
  value: string
  label: string
}

export interface HomeSectionConfig {
  eyebrow: string
  title: string
  description: string
  mode: 'latest' | 'manual'
  limit: number
}

export interface HomeSelectedServiceItem {
  serviceId: number | null
  categoryId?: number | null
  icon: string
}

export interface HomeSelectedProjectItem {
  projectId: number | null
}

export interface HomeFinalCta {
  eyebrow: string
  title: string
  description: string
  items: string[]
  primaryButtonLabel: string
  primaryButtonUrl: string
  secondaryButtonLabel: string
  secondaryButtonUrl: string
  showPhoneButton: boolean
}

export interface HomeAboutSection {
  eyebrow: string
  title: string
  description: string
  imagePath: string
  highlights: string[]
  buttonLabel: string
  buttonUrl: string
}

export interface HomePageContent {
  hero: {
    title: string
    subtitle: string
    backgroundImagePath: string
    badges: HomeHeroBadgeItem[]
    primaryCtaLabel: string
    primaryCtaUrl: string
    secondaryCtaLabel: string
    secondaryCtaUrl: string
  }
  stats: {
    items: HomeStatItem[]
  }
  aboutSection: HomeAboutSection
  servicesSection: HomeSectionConfig & {
    selectedItems: HomeSelectedServiceItem[]
  }
  projectsSection: HomeSectionConfig & {
    selectedItems: HomeSelectedProjectItem[]
  }
  finalCta: HomeFinalCta
}

export interface AboutPageValueItem {
  title: string
  icon: string
  description: string
}

export interface AboutPageTimelineItem {
  year: string
  title: string
  description: string
  sortOrder: number
}

export interface AboutCapabilityItem {
  title: string
  pdfFilePath: string
  buttonLabel: string
}

export interface AboutPageContent {
  hero: {
    title: string
    backgroundImagePath: string
  }
  intro: {
    heading: string
    imagePath: string
    content: string
  }
  coreValues: {
    sectionTitle: string
    items: AboutPageValueItem[]
  }
  timeline: {
    sectionTitle: string
    items: AboutPageTimelineItem[]
  }
  organization: {
    sectionTitle: string
    heading: string
    description: string
    chartImagePath: string
    chartCaption: string
    departments: string[]
  }
  capability: {
    sectionTitle: string
    heading: string
    description: string
    imagePath: string
    items: AboutCapabilityItem[]
  }
}

export interface Category extends BaseEntity {
  name: string
  slug: string
  parentId: number | null
  icon?: string
  sortOrder: number
  active: boolean
}

export interface CategoryPayload {
  name: string
  slug: string
  parentId: number | null
  icon?: string
  sortOrder: number
  active: boolean
}

export interface PostEntity extends BaseEntity {
  categoryId: number
  title: string
  slug: string
  excerpt: string
  content: string
  coverImagePath: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
  categoryName?: string
  publishedAt?: string
}

export interface PostPayload {
  categoryId: number
  title: string
  slug: string
  excerpt: string
  content: string
  coverImagePath: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
}

export interface ServiceDocumentEntity extends BaseEntity {
  title: string
  filePath: string
  sortOrder: number
}

export interface ServiceDocumentPayload {
  title: string
  filePath: string
  sortOrder: number
}

export interface ServiceImageEntity extends BaseEntity {
  imagePath: string
  altText: string | null
  caption: string | null
  sortOrder: number
}

export interface ServiceImagePayload {
  imagePath: string
  altText: string
  caption: string
  sortOrder: number
}

export interface ServiceEntity extends BaseEntity {
  categoryId: number | null
  title: string
  slug: string
  overview: string
  content: string
  icon: string
  coverImagePath: string
  galleryJson: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
  documents: ServiceDocumentEntity[]
  images: ServiceImageEntity[]
  categoryName?: string
}

export interface ServicePayload {
  categoryId?: number | null
  title: string
  slug: string
  overview: string
  content: string
  icon: string
  coverImagePath?: string
  galleryJson: string
  documents: ServiceDocumentPayload[]
  images: ServiceImagePayload[]
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
}

export interface ProjectEntity extends BaseEntity {
  categoryId: number
  title: string
  slug: string
  clientName: string
  location: string
  startedAt: string | null
  completedAt: string | null
  scaleText: string
  coverImagePath: string
  galleryJson: string
  content: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
  categoryName?: string
  projectLocation?: string
}

export interface ProjectPayload {
  categoryId: number
  title: string
  slug: string
  clientName: string
  location: string
  startedAt: string | null
  completedAt: string | null
  scaleText: string
  coverImagePath?: string
  galleryJson?: string
  content: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
}

export interface EquipmentEntity extends BaseEntity {
  categoryId: number
  categoryName?: string
  name: string
  slug: string
  model: string
  manufacturer: string
  origin: string
  unit: string
  quantity: number | null
  productionYear: number | null
  description: string
  coverImagePath: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
  sortOrder: number
}

export interface EquipmentPayload {
  categoryId: number
  name: string
  slug: string
  model: string
  manufacturer: string
  origin: string
  unit: string
  quantity: number | null
  productionYear: number | null
  description: string
  coverImagePath?: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
  sortOrder?: number
}

export interface MenuEntity extends BaseEntity {
  code: string
  name: string
  position: string
}

export interface MenuPayload {
  code: string
  name: string
  position: string
}

export interface MenuItemEntity extends BaseEntity {
  menuId: number
  parentId: number | null
  title: string
  itemType: string
  refId: number | null
  url: string
  resolvedUrl?: string
  icon: string
  sortOrder: number
  visible: boolean
}

export interface MenuItemPayload {
  menuId: number
  parentId: number | null
  title: string
  itemType: string
  refId: number | null
  url: string
  icon: string
  sortOrder: number
  visible: boolean
}

export interface ContactEntity extends BaseEntity {
  fullName: string
  phone: string
  email?: string
  subject?: string
  message?: string
  status: 'NEW' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED'
}

export interface MediaEntity extends BaseEntity {
  fileName: string
  storagePath: string
  mimeType: string
  sizeBytes: number
  altText: string
}

export interface SiteSettings {
  id?: number
  siteName: string
  companyNameEn: string
  shortName: string
  slogan: string
  logoPath: string
  footerLogoPath: string
  heroBannerPath: string
  companyIntro: string
  taxCode: string
  licenseNumber: string
  businessLicensePath: string
  licenseIssuedBy: string
  licenseIssuedDate: string | null
  officeAddress: string
  branchAddress: string
  hotline: string
  phone: string
  email: string
  website: string
  address: string
  mapEmbed: string
  workingHours: string
  facebookUrl: string
  zaloUrl: string
  youtubeUrl: string
  linkedinUrl: string
  footerText: string
}

export interface AuthUser {
  id: number
  username: string
  fullName: string
  email: string
  roles: string[]
}

export interface AdminRoleEntity extends BaseEntity {
  code: string
  name: string
  adminAccess: boolean
}

export interface AdminRolePayload {
  code: string
  name: string
  adminAccess: boolean
}

export interface AdminUserEntity extends BaseEntity {
  username: string
  fullName: string
  email: string
  active: boolean
  roles: AdminRoleEntity[]
}

export interface AdminUserPayload {
  username: string
  password: string
  fullName: string
  email: string
  active: boolean
  roleIds: number[]
}

export interface LoginResponse {
  accessToken: string
  user: AuthUser
}

export type EntityStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
export type LeadStatus = 'NEW' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED'
