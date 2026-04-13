export interface SiteSettings {
  id: number
  siteName: string
  companyNameEn: string | null
  shortName: string | null
  slogan: string | null
  logoPath: string | null
  footerLogoPath: string | null
  heroBannerPath: string | null
  companyIntro: string | null
  taxCode: string | null
  licenseNumber: string | null
  businessLicensePath: string | null
  licenseIssuedBy: string | null
  licenseIssuedDate: string | null
  officeAddress: string | null
  hotline: string
  phone: string | null
  email: string
  address: string
  mapEmbed: string | null
  workingHours: string | null
  facebookUrl: string | null
  zaloUrl: string | null
  youtubeUrl: string | null
  linkedinUrl: string | null
  footerText: string | null
}

export interface SurveyService {
  id: number
  categoryId: number | null
  categoryName: string | null
  title: string
  slug: string
  overview: string
  content: string
  icon: string | null
  coverImagePath: string | null
  galleryJson: string | null
  documents: ServiceDocument[]
  images: ServiceImage[]
  status: string
  publishedAt: string | null
  createdAt: string
  updatedAt: string
}

export interface ServiceDocument {
  id: number
  title: string
  filePath: string
  sortOrder: number
  createdAt: string
  updatedAt: string
}

export interface ServiceImage {
  id: number | null
  imagePath: string
  altText: string | null
  caption: string | null
  sortOrder: number
  createdAt: string | null
  updatedAt: string | null
}

export interface ServiceCategory {
  id: number
  name: string
  slug: string
  parentId: number | null
  sortOrder: number
  active: boolean
}

export interface Project {
  id: number
  categoryId: number
  categoryName: string
  title: string
  slug: string
  clientName: string | null
  location: string | null
  startedAt: string | null
  completedAt: string | null
  scaleText: string | null
  coverImagePath: string | null
  galleryJson: string | null
  content: string
  status: string
  publishedAt: string | null
  createdAt: string
  updatedAt: string
}

export interface ProjectCategory {
  id: number
  name: string
  slug: string
  parentId: number | null
  sortOrder: number
  active: boolean
}

export interface Post {
  id: number
  categoryId: number
  categoryName: string
  title: string
  slug: string
  excerpt: string
  content: string
  coverImagePath: string | null
  status: string
  publishedAt: string | null
  createdAt: string
  updatedAt: string
}

export interface PostCategory {
  id: number
  name: string
  slug: string
  parentId: number | null
  sortOrder: number
  active: boolean
}

export interface Equipment {
  id: number
  categoryId: number
  categoryName: string
  name: string
  slug: string
  model: string | null
  manufacturer: string | null
  origin: string | null
  unit: string | null
  quantity: number | null
  productionYear: number | null
  description: string
  coverImagePath: string | null
  status: string
  sortOrder: number | null
  createdAt: string
  updatedAt: string
}

export interface EquipmentCategory {
  id: number
  name: string
  slug: string
  parentId: number | null
  icon: string | null
  sortOrder: number
  active: boolean
}

export interface SitePage {
  id: number
  title: string
  slug: string
  summary: string
  content: string
  templateCode: 'default' | 'about_company' | 'home_landing'
  contentJson: string | null
  status: string
  publishedAt: string | null
  createdAt: string
  updatedAt: string
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

export interface PublicMenuItem {
  id: number
  menuId: number
  parentId: number | null
  title: string
  itemType: string
  refId: number | null
  url: string | null
  resolvedUrl: string
  icon: string | null
  sortOrder: number
  visible: boolean
}

export interface ContactPayload {
  fullName: string
  companyName: string
  phone: string
  email: string
  subject: string
  message: string
}
