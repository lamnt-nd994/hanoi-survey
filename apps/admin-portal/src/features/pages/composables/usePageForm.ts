import { computed, reactive, ref, type Ref } from 'vue'
import type {
  AboutCapabilityItem,
  AboutPageContent,
  AboutPageTimelineItem,
  AboutPageValueItem,
  HomePageContent,
  PageEntity,
  PagePayload,
} from '@/types'
import { toSlug } from '@/utils/slug'

export function usePageForm(routeId: Ref<unknown>) {
  const slugManuallyEdited = ref(false)

  const form = reactive<PagePayload>({
    title: '',
    slug: '',
    summary: '',
    content: '',
    templateCode: 'default',
    contentJson: null,
    status: 'DRAFT',
  })

  const aboutContent = reactive<AboutPageContent>(createEmptyAboutContent())
  const homeContent = reactive<HomePageContent>(createEmptyHomeContent())

  const serializedAboutContent = computed(() => JSON.stringify(normalizeAboutContent(aboutContent), null, 2))
  const serializedHomeContent = computed(() => JSON.stringify(normalizeHomeContent(homeContent), null, 2))
  const isEdit = computed(() => !!routeId.value)

  function generateSlug() {
    form.slug = toSlug(form.title)
    slugManuallyEdited.value = false
  }

  function onTitleChange() {
    if (!slugManuallyEdited.value && !isEdit.value) {
      form.slug = toSlug(form.title)
    }
  }

  function applyEntity(entity: PageEntity) {
    Object.assign(form, {
      title: entity.title,
      slug: entity.slug,
      summary: entity.summary,
      content: entity.content,
      templateCode: entity.templateCode || 'default',
      contentJson: entity.contentJson,
      status: entity.status,
    })

    if (entity.templateCode === 'about_company') {
      Object.assign(aboutContent, parseAboutContent(entity.contentJson))
    }
    if (entity.templateCode === 'home_landing') {
      Object.assign(homeContent, parseHomeContent(entity.contentJson))
    }
    slugManuallyEdited.value = true
  }

  function buildPayload(): PagePayload {
    return {
      ...form,
      content: form.templateCode === 'default' ? form.content : '',
      contentJson: form.templateCode === 'about_company'
        ? JSON.stringify(normalizeAboutContent(aboutContent))
        : form.templateCode === 'home_landing'
          ? JSON.stringify(normalizeHomeContent(homeContent))
          : null,
    }
  }

  return {
    form,
    aboutContent,
    homeContent,
    slugManuallyEdited,
    serializedAboutContent,
    serializedHomeContent,
    isEdit,
    generateSlug,
    onTitleChange,
    applyEntity,
    buildPayload,
  }
}

function createValueItem(): AboutPageValueItem {
  return { title: '', icon: '', description: '' }
}

function createCapabilityItem(): AboutCapabilityItem {
  return { title: '', pdfFilePath: '', buttonLabel: 'Xem PDF' }
}

function createEmptyHomeContent(): HomePageContent {
  return {
    hero: {
      title: 'DỮ LIỆU [TIN CẬY] - NỀN MÓNG [VỮNG BỀN]',
      subtitle: 'Đối tác tin cậy cung cấp giải pháp toàn diện cho Khảo sát Địa chất, Địa hình, Thí nghiệm và Xây lắp công trình.',
      backgroundImagePath: '',
      badges: [
        { label: 'Địa hình', iconKey: 'MapPlus' },
        { label: 'Địa chất', iconKey: 'Earth' },
        { label: 'Thí nghiệm', iconKey: 'FlaskConical' },
        { label: 'Quan trắc', iconKey: 'ChartLine' },
        { label: 'Xây lắp', iconKey: 'fas fa-tools' },
        { label: 'Giám sát', iconKey: 'Telescope' },
      ],
      primaryCtaLabel: 'Xem dự án',
      primaryCtaUrl: '/du-an',
      secondaryCtaLabel: 'Liên hệ',
      secondaryCtaUrl: '/lien-he',
    },
    stats: {
      items: [
        { value: '10+', label: 'Năm kinh nghiệm' },
        { value: '500+', label: 'Dự án hoàn thành' },
        { value: '50+', label: 'Kỹ sư chuyên môn' },
      ],
    },
    aboutSection: {
      eyebrow: 'Về chúng tôi',
      title: 'VỀ CHÚNG TÔI',
      description: 'CÔNG TY CỔ PHẦN KHẢO SÁT XÂY DỰNG HÀ NỘI tự hào là đơn vị hàng đầu trong lĩnh vực khảo sát xây dựng tại Việt Nam. Chúng tôi cam kết mang lại sự chính xác tuyệt đối trong từng số liệu.',
      imagePath: 'uploads/2026-04-13/b50a87be-0c01-4bb8-b469-3ee561b571f8.jpg',
      highlights: [
        'Hệ thống máy móc hiện đại bậc nhất.',
        'Đội ngũ kỹ sư giàu kinh nghiệm.',
        'Quy trình làm việc chuyên nghiệp, chuẩn ISO.',
      ],
      buttonLabel: 'Xem thêm',
      buttonUrl: '/gioi-thieu',
    },
    servicesSection: {
      eyebrow: 'Lĩnh vực',
      title: 'LĨNH VỰC HOẠT ĐỘNG',
      description: '',
      mode: 'manual',
      limit: 6,
      selectedItems: [
        { serviceId: 11, categoryId: 2, icon: 'fas fa-map-marked-alt' },
        { serviceId: 13, categoryId: 1, icon: 'fas fa-globe-asia' },
        { serviceId: 14, categoryId: 4, icon: 'fas fa-flask' },
        { serviceId: 16, categoryId: null, icon: 'fas fa-hard-hat' },
        { serviceId: 12, categoryId: null, icon: 'fa-solid fa-water' },
        { serviceId: 15, categoryId: null, icon: 'fas fa-chart-line' },
      ],
    },
    projectsSection: {
      eyebrow: 'Dự án',
      title: 'Dự Án Tiêu Biểu',
      description: '',
      mode: 'latest',
      limit: 6,
      selectedItems: [],
    },
    finalCta: {
      eyebrow: 'Tư vấn nhanh ngoài hiện trường',
      title: 'Sẵn sàng khảo sát cho dự án mới?',
      description: 'Đội kỹ sư của Hanoi CSC có thể hỗ trợ từ giai đoạn lập phương án khảo sát, báo giá, bố trí nhân lực đến bàn giao hồ sơ đúng tiến độ.',
      items: [],
      primaryButtonLabel: 'Xem dự án',
      primaryButtonUrl: '/du-an',
      secondaryButtonLabel: 'Liên hệ',
      secondaryButtonUrl: '/lien-he',
      showPhoneButton: true,
    },
  }
}

function createEmptyAboutContent(): AboutPageContent {
  return {
    hero: { title: 'Về chúng tôi', backgroundImagePath: '' },
    intro: {
      heading: 'Doi tac tin cay trong nganh khao sat xay dung',
      imagePath: '',
      content: '<p class="ql-align-justify"><strong>CÔNG TY CỔ PHẦN TƯ VẤN KHẢO SÁT XÂY DỰNG HÀ NỘI</strong>&nbsp;(HANOI CONTRUCTION SERVEY CONSULTANT JOIN STOCK COMPANY) là một doanh nghiệp độc lập, tự hào với bề dày kinh nghiệm trong lĩnh vực tư vấn xây dựng.</p><p class="ql-align-justify"><br></p><p class="ql-align-justify">Được thành lập từ năm 2006, chúng tôi cam kết mang lại những giải pháp tư vấn chất lượng cao, đáp ứng các tiêu chuẩn khắt khe nhất của ngành xây dựng Việt Nam.</p><p class="ql-align-justify"><br></p><p class="ql-align-justify">Với sở hữu Trung tâm thí nghiệm và kiểm định xây dựng đạt chuẩn, VietDelta tự tin là đối tác tin cậy cho mọi công trình.</p><p class="ql-align-justify"><br></p><p><strong style="color: rgb(0, 64, 128);">Người đại diện:</strong>&nbsp;Đỗ Xuân Dân – Giám đốc Công ty</p>',
    },
    coreValues: {
      sectionTitle: 'Sứ mệnh và tầm nhìn',
      items: [
        { title: 'Sứ mệnh', icon: 'bullseye', description: 'Cung cấp dữ liệu khảo sát và thí nghiệm chính xác, minh bạch, giúp tối ưu giải pháp nền móng và giảm thiểu rủi ro kỹ thuật cho mọi công trình.' },
        { title: 'Tầm nhìn', icon: 'eye', description: 'Trở thành đơn vị khảo sát và quan trắc được ưu tiên lựa chọn trong các dự án hạ tâng, công nghiệp và phát triển đô thị tại Việt Nam.' },
        { title: 'Giá trị cốt lõi', icon: 'gem', description: 'Chúng tôi coi chất lượng dữ liệu, tốc độ phối hợp và trách nhiệm nghề nghiệp là nền tảng của mọi dự án.' },
      ],
    },
    timeline: {
      sectionTitle: 'Quá trình phát triển',
      items: [
        { year: '2006', title: 'Khởi đầu', description: 'Thành lập công ty tại Định Công, Hoàng Mai, Hà Nội. Đặt nền móng đầu tiên cho hành trình tư vấn xây dựng.', sortOrder: 1 },
        { year: '2007', title: 'Mở rộng năng lực Lab', description: 'Thành lập Trung tâm thí nghiệm và kiểm định xây dựng.\n\nĐược cấp quyết định công nhận khả năng thực hiện các phép thử số 1232/QĐ-BXD.', sortOrder: 2 },
        { year: '2024 - Nay', title: 'Tăng trưởng bền vững', description: 'Tham gia nhieu du an trong diem ve giao thong, cong nghiep va phat trien do thi voi goi dich vu khao sat tron goi.', sortOrder: 4 },
      ],
    },
    organization: {
      sectionTitle: 'Cơ cấu tổ chức',
      heading: 'Tổ chức khoa học để triển khai nhanh và kiểm soát chất lượng',
      description: 'Mô hình vận hành được phân tách rõ giữa khối điều hành, khối kỹ thuật và các đội hiện trường, giúp doanh nghiệm duy trì tiến độ, bảo đảm chất lượng dữ liệu và tăng khả năng phối hợp trên công trường.',
      chartImagePath: '',
      chartCaption: 'Sơ đồ tổ chức nhân sự',
      departments: ['Ban Giám đốc và Hội đồng quản trị', 'Phòng Kỹ thuật - Nghiên cứu và Phát triển', 'Phòng Thí nghiệm và Kiểm định chất lượng', 'Phòng Thiết kế và Xây lấp', 'Phòng Hành chính - Nhân sự và Kế toán', 'Các đội thi công hiện trường'],
    },
    capability: {
      sectionTitle: 'Hồ sơ năng lực',
      heading: 'Năng lực và chứng chỉ của chúng tôi',
      description: 'Chúng tôi duy trì đầy đủ hồ sơ pháp lý, năng lục hành nghề và các tài liệu kỹ thuật cần thiết để tham gia nhiều loại hình dự án từ dân dụng đến hạ tầng kỹ thuật.',
      imagePath: 'uploads/2026-04-11/8ad682f4-890d-4bff-8c0f-7c57ffb69739.jpg',
      items: [
        { title: 'Giấy phép đăng ký kinh doanh', pdfFilePath: 'uploads/2026-04-11/04dcb4b3-65a6-4bd0-81dd-f71ec2f43cbd.pdf', buttonLabel: 'Xem PDF' },
        { title: 'Chứng chỉ năng lực hoạt động khảo sát xây dựng', pdfFilePath: 'uploads/2026-04-11/f3bea515-17aa-4dc3-b3e2-e4cc626536fa.pdf', buttonLabel: 'Xem PDF' },
        { title: 'Chứng nhận hệ thống quản lý chất lượng', pdfFilePath: 'uploads/2026-04-11/89427367-90ea-4c6d-96b5-30d9e5cb4f58.pdf', buttonLabel: 'Xem PDF' },
        { title: 'Hồ sơ kinh nghiệm các dự án tiêu biểu', pdfFilePath: '', buttonLabel: 'Xem PDF' },
      ],
    },
  }
}

function parseHomeContent(raw: string | null | undefined): HomePageContent {
  const fallback = createEmptyHomeContent()
  if (!raw) return fallback
  try {
    return normalizeHomeContent(JSON.parse(raw))
  } catch {
    return fallback
  }
}

function normalizeHomeContent(input: Partial<HomePageContent> | HomePageContent): HomePageContent {
  const fallback = createEmptyHomeContent()
  return {
    hero: {
      title: input.hero?.title || fallback.hero.title,
      subtitle: input.hero?.subtitle || fallback.hero.subtitle,
      backgroundImagePath: input.hero?.backgroundImagePath || fallback.hero.backgroundImagePath,
      badges: (input.hero?.badges || fallback.hero.badges).map((item) => ({ label: item.label || '', iconKey: item.iconKey || '' })),
      primaryCtaLabel: input.hero?.primaryCtaLabel || fallback.hero.primaryCtaLabel,
      primaryCtaUrl: input.hero?.primaryCtaUrl || fallback.hero.primaryCtaUrl,
      secondaryCtaLabel: input.hero?.secondaryCtaLabel || fallback.hero.secondaryCtaLabel,
      secondaryCtaUrl: input.hero?.secondaryCtaUrl || fallback.hero.secondaryCtaUrl,
    },
    stats: { items: (input.stats?.items || fallback.stats.items).map((item) => ({ value: item.value || '', label: item.label || '' })) },
    aboutSection: {
      eyebrow: input.aboutSection?.eyebrow || fallback.aboutSection.eyebrow,
      title: input.aboutSection?.title || fallback.aboutSection.title,
      description: input.aboutSection?.description || fallback.aboutSection.description,
      imagePath: input.aboutSection?.imagePath || fallback.aboutSection.imagePath,
      highlights: [...(input.aboutSection?.highlights || fallback.aboutSection.highlights)],
      buttonLabel: input.aboutSection?.buttonLabel || fallback.aboutSection.buttonLabel,
      buttonUrl: input.aboutSection?.buttonUrl || fallback.aboutSection.buttonUrl,
    },
    servicesSection: {
      eyebrow: input.servicesSection?.eyebrow || fallback.servicesSection.eyebrow,
      title: input.servicesSection?.title || fallback.servicesSection.title,
      description: input.servicesSection?.description || fallback.servicesSection.description,
      mode: input.servicesSection?.mode || fallback.servicesSection.mode,
      limit: Number(input.servicesSection?.limit) || fallback.servicesSection.limit,
      selectedItems: (input.servicesSection?.selectedItems || fallback.servicesSection.selectedItems).map((item) => ({ serviceId: item.serviceId ?? null, categoryId: item.categoryId ?? null, icon: item.icon || '' })),
    },
    projectsSection: {
      eyebrow: input.projectsSection?.eyebrow || fallback.projectsSection.eyebrow,
      title: input.projectsSection?.title || fallback.projectsSection.title,
      description: input.projectsSection?.description || fallback.projectsSection.description,
      mode: input.projectsSection?.mode || fallback.projectsSection.mode,
      limit: Number(input.projectsSection?.limit) || fallback.projectsSection.limit,
      selectedItems: (input.projectsSection?.selectedItems || fallback.projectsSection.selectedItems).map((item) => ({ projectId: item.projectId ?? null })),
    },
    finalCta: {
      eyebrow: input.finalCta?.eyebrow || fallback.finalCta.eyebrow,
      title: input.finalCta?.title || fallback.finalCta.title,
      description: input.finalCta?.description || fallback.finalCta.description,
      items: [...(input.finalCta?.items || fallback.finalCta.items)],
      primaryButtonLabel: input.finalCta?.primaryButtonLabel || fallback.finalCta.primaryButtonLabel,
      primaryButtonUrl: input.finalCta?.primaryButtonUrl || fallback.finalCta.primaryButtonUrl,
      secondaryButtonLabel: input.finalCta?.secondaryButtonLabel || fallback.finalCta.secondaryButtonLabel,
      secondaryButtonUrl: input.finalCta?.secondaryButtonUrl || fallback.finalCta.secondaryButtonUrl,
      showPhoneButton: input.finalCta?.showPhoneButton ?? fallback.finalCta.showPhoneButton,
    },
  }
}

function normalizeIntroContent(input: { content?: string }) {
  return input.content || '<p></p>'
}

function parseAboutContent(raw: string | null | undefined): AboutPageContent {
  const fallback = createEmptyAboutContent()
  if (!raw) return fallback
  try {
    return normalizeAboutContent(JSON.parse(raw))
  } catch {
    return fallback
  }
}

function normalizeAboutContent(input: Partial<AboutPageContent> | AboutPageContent): AboutPageContent {
  const hero = input.hero ?? { title: '', backgroundImagePath: '' }
  const intro = input.intro ?? { heading: '', imagePath: '', content: '<p></p>' }
  const coreValues = input.coreValues ?? { sectionTitle: 'Sứ Mệnh & Tầm Nhìn', items: [] }
  const timeline = input.timeline ?? { sectionTitle: 'Quá Trình Phát Triển', items: [] }
  const organization = input.organization ?? { sectionTitle: 'Cơ Cấu Tổ Chức', heading: '', description: '', chartImagePath: '', chartCaption: '', departments: [''] }
  const capability = input.capability ?? { sectionTitle: 'Hồ Sơ Năng Lực', heading: '', description: '', imagePath: '', items: [createCapabilityItem()] }
  return {
    hero: { title: hero.title || '', backgroundImagePath: hero.backgroundImagePath || '' },
    intro: { heading: intro.heading || '', imagePath: intro.imagePath || '', content: normalizeIntroContent(intro) },
    coreValues: { sectionTitle: coreValues.sectionTitle || 'Sứ Mệnh & Tầm Nhìn', items: (coreValues.items || []).map((item) => ({ title: item.title || '', icon: item.icon || '', description: item.description || '' })) },
    timeline: { sectionTitle: timeline.sectionTitle || 'Quá Trình Phát Triển', items: (timeline.items || []).map((item, index) => ({ year: item.year || '', title: item.title || '', description: item.description || '', sortOrder: Number(item.sortOrder) || index + 1 })) },
    organization: { sectionTitle: organization.sectionTitle || 'Cơ Cấu Tổ Chức', heading: organization.heading || '', description: organization.description || '', chartImagePath: organization.chartImagePath || '', chartCaption: organization.chartCaption || '', departments: [...(organization.departments || [''])] },
    capability: { sectionTitle: capability.sectionTitle || 'Hồ Sơ Năng Lực', heading: capability.heading || '', description: capability.description || '', imagePath: capability.imagePath || '', items: (capability.items || []).map((item) => ({ title: item.title || '', pdfFilePath: item.pdfFilePath || '', buttonLabel: item.buttonLabel || 'Xem PDF' })) },
  }
}
