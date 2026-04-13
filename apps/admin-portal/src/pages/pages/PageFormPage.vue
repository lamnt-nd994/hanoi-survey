<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa trang' : 'Tạo trang mới'" :description="isEdit ? 'Cập nhật nội dung trang tĩnh' : 'Thêm trang tĩnh mới vào hệ thống'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <form @submit.prevent="handleSubmit" class="cms-card mt-6 p-6">
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Tiêu đề <span class="required">*</span></label>
          <input v-model="form.title" class="cms-form-control" placeholder="Nhập tiêu đề trang" required />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Slug</label>
          <input v-model="form.slug" class="cms-form-control" placeholder="vd: gioi-thieu" />
        </div>
      </div>

      <div class="cms-form-group">
        <label class="cms-form-label">Tóm tắt</label>
        <textarea v-model="form.summary" class="cms-form-control" placeholder="Mô tả ngắn gọn" />
      </div>

      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Template</label>
          <select v-model="form.templateCode" class="cms-form-control">
            <option value="default">Mặc định</option>
            <option value="home_landing">Trang chủ</option>
            <option value="about_company">Giới thiệu công ty</option>
          </select>
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Trạng thái</label>
          <select v-model="form.status" class="cms-form-control">
            <option value="DRAFT">Nháp</option>
            <option value="PUBLISHED">Đã xuất bản</option>
            <option value="ARCHIVED">Lưu trữ</option>
          </select>
        </div>
      </div>

      <div v-if="form.templateCode === 'default'" class="cms-form-group">
        <div class="flex items-center justify-between gap-3 mb-2">
          <label class="cms-form-label mb-0">Nội dung</label>
          <div class="flex items-center gap-3">
            <button type="button" class="cms-btn cms-btn-secondary" :disabled="uploadingDefaultImage" @click="openDefaultImagePicker">
              {{ uploadingDefaultImage ? 'Đang tải ảnh...' : 'Tải ảnh vào nội dung' }}
            </button>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <button type="button" class="editor-tab-btn" :class="{ active: defaultEditorMode === 'visual' }" @click="defaultEditorMode = 'visual'">Soạn thảo</button>
          <button type="button" class="editor-tab-btn" :class="{ active: defaultEditorMode === 'html' }" @click="defaultEditorMode = 'html'">HTML</button>
        </div>
        <div class="overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm intro-quill-wrap mt-2">
          <QuillEditor
            v-if="defaultEditorMode === 'visual'"
            v-model:content="form.content"
            content-type="html"
            theme="snow"
            :toolbar="defaultQuillToolbar"
            placeholder="Nhập nội dung chi tiết..."
            class="intro-quill-editor"
            ref="defaultQuillRef"
          />
          <div v-else class="p-4">
            <textarea
              v-model="form.content"
              class="cms-form-control font-mono text-sm"
              rows="14"
              placeholder="<p>Nhập nội dung chi tiết...</p>"
            />
          </div>
        </div>
        <div class="mt-3 rounded-xl border border-slate-200 bg-slate-50 p-4">
          <div class="text-xs font-medium uppercase tracking-[0.18em] text-slate-500">Xem trước</div>
          <div class="prose prose-slate mt-3 max-w-none text-sm" v-html="form.content"></div>
        </div>
              <input ref="defaultImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleDefaultImageSelected" />
      </div>

      <div v-else-if="form.templateCode === 'home_landing'" class="space-y-6">
        <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
          <summary class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Hero</h2>
            <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
          </summary>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group md:col-span-2">
              <label class="cms-form-label">Tiêu đề</label>
              <input v-model="homeContent.hero.title" class="cms-form-control" placeholder="Khảo sát chính xác - Nền móng vững chắc" />
              <div class="mt-1 text-xs text-slate-500">Dùng <code>[ ]</code> để tô nổi bật, ví dụ: <code>Dữ liệu [tin cậy] - nền móng [vững bền]</code></div>
            </div>
            <div class="cms-form-group md:col-span-2">
              <label class="cms-form-label">Phụ đề</label>
              <input v-model="homeContent.hero.subtitle" class="cms-form-control" placeholder="Ha Noi Construction Survey Consultant J.S.C" />
            </div>
            <div class="cms-form-group md:col-span-2">
              <label class="cms-form-label">Ảnh nền</label>
              <div class="relative">
                <input v-model="homeContent.hero.backgroundImagePath" class="cms-form-control pr-36" placeholder="media/home-hero.jpg" />
                <button type="button" class="input-action-btn" :disabled="uploadingHomeHeroImage" @click="homeHeroImageInputRef?.click()">
                  {{ uploadingHomeHeroImage ? 'Đang tải...' : 'Từ máy tính' }}
                </button>
              </div>
              <input ref="homeHeroImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleHomeHeroImageSelected" />
            </div>
          </div>

          <div class="mt-4">
            <div class="flex items-center justify-between gap-3">
              <h3 class="text-sm font-semibold text-slate-800">Badge dịch vụ</h3>
              <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.hero.badges.push(createHomeBadgeItem())">+ Thêm badge</button>
            </div>
            <div class="mt-3 space-y-3">
              <div v-for="(item, index) in homeContent.hero.badges" :key="`hero-badge-${index}`" class="rounded-lg border border-gray-200 p-3">
                <div class="flex items-center justify-between gap-3">
                  <div class="text-sm font-medium text-slate-700">Badge {{ index + 1 }}</div>
                  <button type="button" class="cms-act-btn delete" @click="removeArrayItem(homeContent.hero.badges, index)">&#10005;</button>
                </div>
                <div class="mt-3 grid gap-4 md:grid-cols-2">
                  <div class="cms-form-group">
                    <label class="cms-form-label">Nhãn</label>
                    <input v-model="item.label" class="cms-form-control" placeholder="Địa hình" />
                  </div>
                  <div class="cms-form-group">
                    <label class="cms-form-label">Icon</label>
                    <input v-model="item.iconKey" class="cms-form-control" placeholder="fas fa-map-marked-alt hoặc map-pinned" />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Nút chính - nhãn</label>
              <input v-model="homeContent.hero.primaryCtaLabel" class="cms-form-control" placeholder="Xem dự án" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Nút chính - URL</label>
              <input v-model="homeContent.hero.primaryCtaUrl" class="cms-form-control" placeholder="/du-an" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Nút phụ - nhãn</label>
              <input v-model="homeContent.hero.secondaryCtaLabel" class="cms-form-control" placeholder="Liên hệ" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Nút phụ - URL</label>
              <input v-model="homeContent.hero.secondaryCtaUrl" class="cms-form-control" placeholder="/lien-he" />
            </div>
          </div>
        </details>

        <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
          <summary class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Số liệu nổi bật</h2>
            <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
          </summary>
          <div class="mt-4 flex justify-end">
            <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.stats.items.push(createHomeStatItem())">+ Thêm số liệu</button>
          </div>
          <div class="mt-4 space-y-3">
            <div v-for="(item, index) in homeContent.stats.items" :key="`stat-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Số liệu {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeAnyArrayItem(homeContent.stats.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Giá trị</label>
                  <input v-model="item.value" class="cms-form-control" placeholder="10+" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Nhãn</label>
                  <input v-model="item.label" class="cms-form-control" placeholder="Năm kinh nghiệm" />
                </div>
              </div>
            </div>
          </div>
        </details>

        <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
          <summary class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Về chúng tôi</h2>
            <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
          </summary>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Eyebrow</label>
              <input v-model="homeContent.aboutSection.eyebrow" class="cms-form-control" placeholder="Về chúng tôi" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh minh họa</label>
              <div class="relative">
                <input v-model="homeContent.aboutSection.imagePath" class="cms-form-control pr-36" placeholder="media/home-about.jpg" />
                <button type="button" class="input-action-btn" :disabled="uploadingHomeAboutImage" @click="homeAboutImageInputRef?.click()">
                  {{ uploadingHomeAboutImage ? 'Đang tải...' : 'Từ máy tính' }}
                </button>
              </div>
              <input ref="homeAboutImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleHomeAboutImageSelected" />
            </div>
            <div class="cms-form-group md:col-span-2">
              <label class="cms-form-label">Tiêu đề</label>
              <input v-model="homeContent.aboutSection.title" class="cms-form-control" placeholder="Đơn vị khảo sát xây dựng tập trung vào độ chính xác và tiến độ thực địa" />
            </div>
            <div class="cms-form-group md:col-span-2">
              <label class="cms-form-label">Mô tả</label>
              <textarea v-model="homeContent.aboutSection.description" class="cms-form-control" rows="4" placeholder="Mô tả ngắn về doanh nghiệp" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Nhãn nút</label>
              <input v-model="homeContent.aboutSection.buttonLabel" class="cms-form-control" placeholder="Xem hồ sơ năng lực" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">URL nút</label>
              <input v-model="homeContent.aboutSection.buttonUrl" class="cms-form-control" placeholder="/gioi-thieu" />
            </div>
          </div>

          <div class="mt-4">
            <div class="flex items-center justify-between gap-3">
              <h3 class="text-sm font-semibold text-slate-800">Điểm nhấn</h3>
              <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.aboutSection.highlights.push('')">+ Thêm ý</button>
            </div>
            <div class="mt-3 space-y-3">
              <div v-for="(item, index) in homeContent.aboutSection.highlights" :key="`about-highlight-${index}`" class="rounded-lg border border-gray-200 p-3">
                <div class="flex items-center justify-between gap-3">
                  <div class="text-sm font-medium text-slate-700">Ý {{ index + 1 }}</div>
                  <button type="button" class="cms-act-btn delete" @click="removeAnyArrayItem(homeContent.aboutSection.highlights, index)">&#10005;</button>
                </div>
                <textarea v-model="homeContent.aboutSection.highlights[index]" class="cms-form-control mt-3" rows="3" placeholder="Nội dung điểm nhấn" />
              </div>
            </div>
          </div>
        </details>

        <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
          <summary class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Khối dịch vụ</h2>
            <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
          </summary>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.servicesSection.eyebrow" class="cms-form-control" placeholder="Lĩnh vực" /></div>
            <div class="cms-form-group">
              <label class="cms-form-label">Chế độ hiển thị</label>
              <select v-model="homeContent.servicesSection.mode" class="cms-form-control">
                <option value="latest">Mới nhất theo số lượng</option>
                <option value="manual">Chọn thủ công</option>
              </select>
            </div>
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.servicesSection.title" class="cms-form-control" placeholder="Các Dịch Vụ Chính" /></div>
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.servicesSection.description" class="cms-form-control" rows="3" placeholder="Mô tả ngắn section dịch vụ" /></div>
          </div>

          <div v-if="homeContent.servicesSection.mode === 'latest'" class="mt-4 cms-form-group" style="max-width: 20rem;">
            <label class="cms-form-label">Số lượng hiển thị</label>
            <input v-model.number="homeContent.servicesSection.limit" type="number" min="1" class="cms-form-control" />
          </div>

          <div v-else class="mt-4">
            <div class="flex items-center justify-between gap-3">
              <h3 class="text-sm font-semibold text-slate-800">Dịch vụ được chọn</h3>
              <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.servicesSection.selectedItems.push(createSelectedServiceItem())">+ Thêm dịch vụ</button>
            </div>
            <div class="mt-3 space-y-3">
              <div v-for="(item, index) in homeContent.servicesSection.selectedItems" :key="`selected-service-${index}`" class="rounded-lg border border-gray-200 p-3">
                <div class="flex items-center justify-between gap-3">
                  <div class="text-sm font-medium text-slate-700">Dịch vụ {{ index + 1 }}</div>
                  <button type="button" class="cms-act-btn delete" @click="removeAnyArrayItem(homeContent.servicesSection.selectedItems, index)">&#10005;</button>
                </div>
                <div class="mt-3 grid gap-4 md:grid-cols-2">
                  <div class="cms-form-group">
                    <label class="cms-form-label">Chọn dịch vụ</label>
                    <select v-model.number="item.serviceId" class="cms-form-control">
                      <option :value="null">-- Chọn dịch vụ --</option>
                      <option v-for="service in availableServices" :key="service.id" :value="service.id">{{ service.title }}</option>
                    </select>
                  </div>
                  <div class="cms-form-group">
                    <label class="cms-form-label">Icon</label>
                    <input v-model="item.icon" class="cms-form-control" placeholder="fas fa-map-marked-alt hoặc map-pinned" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </details>

        <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
          <summary class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Khối dự án</h2>
            <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
          </summary>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.projectsSection.eyebrow" class="cms-form-control" placeholder="Dự án" /></div>
            <div class="cms-form-group">
              <label class="cms-form-label">Chế độ hiển thị</label>
              <select v-model="homeContent.projectsSection.mode" class="cms-form-control">
                <option value="latest">Mới nhất theo số lượng</option>
                <option value="manual">Chọn thủ công</option>
              </select>
            </div>
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.projectsSection.title" class="cms-form-control" placeholder="Dự Án Tiêu Biểu" /></div>
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.projectsSection.description" class="cms-form-control" rows="3" placeholder="Mô tả ngắn section dự án" /></div>
          </div>

          <div v-if="homeContent.projectsSection.mode === 'latest'" class="mt-4 cms-form-group" style="max-width: 20rem;">
            <label class="cms-form-label">Số lượng hiển thị</label>
            <input v-model.number="homeContent.projectsSection.limit" type="number" min="1" class="cms-form-control" />
          </div>

          <div v-else class="mt-4">
            <div class="flex items-center justify-between gap-3">
              <h3 class="text-sm font-semibold text-slate-800">Dự án được chọn</h3>
              <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.projectsSection.selectedItems.push(createSelectedProjectItem())">+ Thêm dự án</button>
            </div>
            <div class="mt-3 space-y-3">
              <div v-for="(item, index) in homeContent.projectsSection.selectedItems" :key="`selected-project-${index}`" class="rounded-lg border border-gray-200 p-3">
                <div class="flex items-center justify-between gap-3">
                  <div class="text-sm font-medium text-slate-700">Dự án {{ index + 1 }}</div>
                  <button type="button" class="cms-act-btn delete" @click="removeAnyArrayItem(homeContent.projectsSection.selectedItems, index)">&#10005;</button>
                </div>
                <div class="mt-3 cms-form-group">
                  <label class="cms-form-label">Chọn dự án</label>
                  <select v-model.number="item.projectId" class="cms-form-control">
                    <option :value="null">-- Chọn dự án --</option>
                    <option v-for="project in availableProjects" :key="project.id" :value="project.id">{{ project.title }}</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </details>

        <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
          <summary class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">CTA cuối trang</h2>
            <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
          </summary>
          <div class="mt-4 flex justify-end">
            <label class="flex items-center gap-2 text-sm text-slate-700">
              <input v-model="homeContent.finalCta.showPhoneButton" type="checkbox" />
              Hiện nút gọi điện
            </label>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.finalCta.eyebrow" class="cms-form-control" placeholder="Tư vấn nhanh ngoài hiện trường" /></div>
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.finalCta.title" class="cms-form-control" placeholder="Sẵn sàng khảo sát cho dự án mới?" /></div>
            <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.finalCta.description" class="cms-form-control" rows="3" placeholder="Mô tả CTA cuối trang" /></div>
            <div class="cms-form-group"><label class="cms-form-label">Nút chính - nhãn</label><input v-model="homeContent.finalCta.primaryButtonLabel" class="cms-form-control" placeholder="Xem dự án" /></div>
            <div class="cms-form-group"><label class="cms-form-label">Nút chính - URL</label><input v-model="homeContent.finalCta.primaryButtonUrl" class="cms-form-control" placeholder="/du-an" /></div>
            <div class="cms-form-group"><label class="cms-form-label">Nút phụ - nhãn</label><input v-model="homeContent.finalCta.secondaryButtonLabel" class="cms-form-control" placeholder="Liên hệ" /></div>
            <div class="cms-form-group"><label class="cms-form-label">Nút phụ - URL</label><input v-model="homeContent.finalCta.secondaryButtonUrl" class="cms-form-control" placeholder="/lien-he" /></div>
          </div>
        </details>

        <section class="rounded-xl border border-dashed border-gray-300 bg-gray-50 p-4">
          <div class="text-sm font-medium text-slate-700">Preview contentJson</div>
          <textarea :value="serializedHomeContent" class="cms-form-control mt-3 font-mono text-xs" rows="12" readonly />
        </section>
      </div>

      <div v-else class="space-y-6">
        <section class="rounded-xl border border-gray-200 p-4">
          <h2 class="text-base font-semibold text-slate-900">Hero</h2>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề hero</label>
              <input v-model="aboutContent.hero.title" class="cms-form-control" placeholder="Về Chúng Tôi" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh nền</label>
              <div class="relative">
                <input v-model="aboutContent.hero.backgroundImagePath" class="cms-form-control pr-36" placeholder="media/about-hero.jpg" />
                <button type="button" class="input-action-btn" :disabled="uploadingHeroImage" @click="heroImageInputRef?.click()">
                  {{ uploadingHeroImage ? 'Đang tải...' : 'Từ máy tính' }}
                </button>
              </div>
              <input ref="heroImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleHeroImageSelected" />
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Mở đầu</h2>
            <div class="flex items-center gap-3">
              <span class="text-xs text-slate-500">Khung soạn thảo nội dung giới thiệu</span>
              <button type="button" class="cms-btn cms-btn-secondary" :disabled="uploadingIntroImage" @click="openIntroImagePicker">
                {{ uploadingIntroImage ? 'Đang tải ảnh...' : 'Tải ảnh vào nội dung' }}
              </button>
            </div>
          </div>
          <div class="mt-4 cms-form-group">
            <label class="cms-form-label">Heading</label>
            <input v-model="aboutContent.intro.heading" class="cms-form-control" placeholder="Đối tác tin cậy trong ngành khảo sát xây dựng" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Nội dung giới thiệu</label>
            <p class="text-xs text-slate-500">Nhập phần mô tả chi tiết của doanh nghiệp tại đây. Bạn có thể định dạng chữ, danh sách, link và chèn ảnh trực tiếp.</p>
          </div>
          <div class="flex items-center gap-2">
            <button type="button" class="editor-tab-btn" :class="{ active: introEditorMode === 'visual' }" @click="introEditorMode = 'visual'">Soạn thảo</button>
            <button type="button" class="editor-tab-btn" :class="{ active: introEditorMode === 'html' }" @click="introEditorMode = 'html'">HTML</button>
          </div>
          <div class="overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm intro-quill-wrap">
            <QuillEditor
              v-if="introEditorMode === 'visual'"
              v-model:content="aboutContent.intro.content"
              content-type="html"
              theme="snow"
              :toolbar="quillToolbar"
              placeholder="Nhập nội dung giới thiệu doanh nghiệp..."
              class="intro-quill-editor"
              ref="introQuillRef"
            />
            <div v-else class="p-4">
              <textarea
                v-model="aboutContent.intro.content"
                class="cms-form-control font-mono text-sm"
                rows="14"
                placeholder="<p>Nhập nội dung giới thiệu doanh nghiệp...</p>"
              />
            </div>
          </div>
          <div class="mt-3 rounded-xl border border-slate-200 bg-slate-50 p-4">
            <div class="text-xs font-medium uppercase tracking-[0.18em] text-slate-500">Xem trước</div>
            <div class="prose prose-slate mt-3 max-w-none text-sm" v-html="aboutContent.intro.content"></div>
          </div>
          <input ref="introImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleIntroImageSelected" />
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Sứ mệnh, tầm nhìn, giá trị cốt lõi</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.coreValues.items.push(createValueItem())">+ Thêm mục</button>
          </div>
          <div class="mt-4 cms-form-group">
            <label class="cms-form-label">Tiêu đề section</label>
            <input v-model="aboutContent.coreValues.sectionTitle" class="cms-form-control" placeholder="Sứ Mệnh & Tầm Nhìn" />
          </div>
          <div class="space-y-3">
            <div v-for="(item, index) in aboutContent.coreValues.items" :key="`value-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Mục {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.coreValues.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Tiêu đề</label>
                  <input v-model="item.title" class="cms-form-control" placeholder="Sứ Mệnh" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Icon key</label>
                  <input v-model="item.icon" class="cms-form-control" placeholder="bullseye, eye, gem" />
                </div>
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Mô tả</label>
                <textarea v-model="item.description" class="cms-form-control" rows="3" placeholder="Mô tả nội dung" />
              </div>
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Quá trình phát triển</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.timeline.items.push(createTimelineItem())">+ Thêm mốc</button>
          </div>
          <div class="mt-4 cms-form-group">
            <label class="cms-form-label">Tiêu đề section</label>
            <input v-model="aboutContent.timeline.sectionTitle" class="cms-form-control" placeholder="Quá Trình Phát Triển" />
          </div>
          <div class="space-y-3">
            <div v-for="(item, index) in aboutContent.timeline.items" :key="`timeline-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Mốc {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.timeline.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Năm / giai đoạn</label>
                  <input v-model="item.year" class="cms-form-control" placeholder="2013" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Thứ tự</label>
                  <input v-model.number="item.sortOrder" type="number" min="1" class="cms-form-control" />
                </div>
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Tiêu đề</label>
                <input v-model="item.title" class="cms-form-control" placeholder="Thành lập công ty" />
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Mô tả</label>
                <textarea v-model="item.description" class="cms-form-control" rows="3" placeholder="Mô tả cột mốc" />
              </div>
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Cơ cấu tổ chức</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.organization.departments.push('')">+ Thêm bộ phận</button>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề section</label>
              <input v-model="aboutContent.organization.sectionTitle" class="cms-form-control" placeholder="Cơ Cấu Tổ Chức" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Heading</label>
              <input v-model="aboutContent.organization.heading" class="cms-form-control" placeholder="Sự tổ chức khoa học và hiệu quả" />
            </div>
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Mô tả</label>
            <textarea v-model="aboutContent.organization.description" class="cms-form-control" rows="3" placeholder="Mô tả cơ cấu tổ chức" />
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh sơ đồ</label>
              <div class="relative">
                <input v-model="aboutContent.organization.chartImagePath" class="cms-form-control pr-36" placeholder="media/org-chart.png" />
                <button type="button" class="input-action-btn" :disabled="uploadingOrgChartImage" @click="orgChartImageInputRef?.click()">
                  {{ uploadingOrgChartImage ? 'Đang tải...' : 'Từ máy tính' }}
                </button>
              </div>
              <input ref="orgChartImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleOrgChartImageSelected" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Chú thích ảnh</label>
              <input v-model="aboutContent.organization.chartCaption" class="cms-form-control" placeholder="Sơ đồ tổ chức nhân sự" />
            </div>
          </div>
          <div class="space-y-3">
            <div v-for="(department, index) in aboutContent.organization.departments" :key="`department-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Bộ phận {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.organization.departments, index)">&#10005;</button>
              </div>
              <input v-model="aboutContent.organization.departments[index]" class="cms-form-control mt-3" placeholder="Tên bộ phận" />
            </div>
          </div>
        </section>

        <section class="rounded-xl border border-gray-200 p-4">
          <div class="flex items-center justify-between gap-3">
            <h2 class="text-base font-semibold text-slate-900">Hồ sơ năng lực</h2>
            <button type="button" class="cms-btn cms-btn-secondary" @click="aboutContent.capability.items.push(createCapabilityItem())">+ Thêm mục</button>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề section</label>
              <input v-model="aboutContent.capability.sectionTitle" class="cms-form-control" placeholder="Hồ Sơ Năng Lực" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Heading</label>
              <input v-model="aboutContent.capability.heading" class="cms-form-control" placeholder="Năng lực và chứng chỉ của chúng tôi" />
            </div>
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Mô tả</label>
            <textarea v-model="aboutContent.capability.description" class="cms-form-control" rows="3" placeholder="Mô tả hồ sơ năng lực" />
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <div class="cms-form-group">
              <label class="cms-form-label">Ảnh minh họa</label>
              <div class="relative">
                <input v-model="aboutContent.capability.imagePath" class="cms-form-control pr-36" placeholder="media/capability.jpg" />
                <button type="button" class="input-action-btn" :disabled="uploadingCapabilityImage" @click="capabilityImageInputRef?.click()">
                  {{ uploadingCapabilityImage ? 'Đang tải...' : 'Từ máy tính' }}
                </button>
              </div>
              <input ref="capabilityImageInputRef" type="file" accept=".png,.jpg,.jpeg" class="hidden" @change="handleCapabilityImageSelected" />
            </div>
          </div>
          <div class="space-y-3">
            <div v-for="(item, index) in aboutContent.capability.items" :key="`capability-${index}`" class="rounded-lg border border-gray-200 p-3">
              <div class="flex items-center justify-between gap-3">
                <div class="text-sm font-medium text-slate-700">Mục {{ index + 1 }}</div>
                <button type="button" class="cms-act-btn delete" @click="removeArrayItem(aboutContent.capability.items, index)">&#10005;</button>
              </div>
              <div class="mt-3 grid gap-4 md:grid-cols-2">
                <div class="cms-form-group">
                  <label class="cms-form-label">Tên tài liệu</label>
                  <input v-model="item.title" class="cms-form-control" placeholder="Ví dụ: Chứng chỉ năng lực hoạt động khảo sát xây dựng" />
                </div>
                <div class="cms-form-group">
                  <label class="cms-form-label">Nhãn nút</label>
                  <input v-model="item.buttonLabel" class="cms-form-control" placeholder="Xem PDF" />
                </div>
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">File PDF</label>
                <div class="relative">
                  <input v-model="item.pdfFilePath" class="cms-form-control pr-56" placeholder="media/ho-so-nang-luc.pdf" />
                  <div class="absolute right-1 top-1 flex gap-2">
                    <a v-if="item.pdfFilePath" :href="resolveUploadedMediaUrl(item.pdfFilePath)" target="_blank" rel="noopener noreferrer" class="input-mini-action-btn">Xem</a>
                    <button type="button" class="input-mini-action-btn" :disabled="uploadingCapabilityItemIndex === index" @click="openCapabilityItemPdfPicker(index)">
                      {{ uploadingCapabilityItemIndex === index ? 'Đang tải...' : 'Từ máy tính' }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <input ref="capabilityItemPdfInputRef" type="file" accept="application/pdf,.pdf" class="hidden" @change="handleCapabilityItemPdfSelected" />
        </section>

        <section class="rounded-xl border border-dashed border-gray-300 bg-gray-50 p-4">
          <div class="text-sm font-medium text-slate-700">Preview contentJson</div>
          <textarea :value="serializedAboutContent" class="cms-form-control mt-3 font-mono text-xs" rows="12" readonly />
        </section>
      </div>

      <div class="mt-4 flex gap-3 border-t border-gray-100 pt-4">
        <button type="submit" :disabled="saving" class="cms-btn cms-btn-primary">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</button>
        <router-link to="/pages" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { mediaApi } from '@/lib/api'
import { usePagesStore } from '@/stores/pages'
import { useProjectsStore } from '@/stores/projects'
import { useServicesStore } from '@/stores/services'
import type {
  AboutCapabilityItem,
  AboutPageContent,
  AboutPageTimelineItem,
  AboutPageValueItem,
  HomeHeroBadgeItem,
  HomePageContent,
  HomeSelectedProjectItem,
  HomeSelectedServiceItem,
  HomeStatItem,
  PagePayload,
  ProjectEntity,
  ServiceEntity,
} from '@/types'

const route = useRoute()
const router = useRouter()
const store = usePagesStore()
const servicesStore = useServicesStore()
const projectsStore = useProjectsStore()
const saving = ref(false)
const error = ref('')
const introEditorMode = ref<'visual' | 'html'>('visual')
const uploadingHeroImage = ref(false)
const heroImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingIntroImage = ref(false)
const introImageInputRef = ref<HTMLInputElement | null>(null)
const introQuillRef = ref<any>(null)
const uploadingOrgChartImage = ref(false)
const orgChartImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingCapabilityImage = ref(false)
const capabilityImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingHomeHeroImage = ref(false)
const homeHeroImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingHomeAboutImage = ref(false)
const homeAboutImageInputRef = ref<HTMLInputElement | null>(null)
const uploadingCapabilityItemIndex = ref<number | null>(null)
const capabilityItemPdfInputRef = ref<HTMLInputElement | null>(null)
const capabilityItemUploadIndex = ref<number | null>(null)

const defaultEditorMode = ref<'visual' | 'html'>('visual')
const uploadingDefaultImage = ref(false)
const defaultImageInputRef = ref<HTMLInputElement | null>(null)
const defaultQuillRef = ref<any>(null)

function createQuillToolbar(imageHandler: () => void) {
  return {
    container: [
      [{ header: [1, 2, 3, false] }],
      [{ font: [] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ color: [] }, { background: [] }],
      [{ script: 'super' }, { script: 'sub' }],
      [{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
      [{ align: [] }],
      ['blockquote', 'link', 'image'],
      ['clean'],
    ],
    handlers: {
      image: imageHandler,
    },
  }
}

const quillToolbar = createQuillToolbar(() => openIntroImagePicker())

const defaultQuillToolbar = createQuillToolbar(() => openDefaultImagePicker())

const isEdit = computed(() => !!route.params.id)

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
const availableServices = computed<ServiceEntity[]>(() => servicesStore.items.filter((item) => item.status === 'PUBLISHED'))
const availableProjects = computed<ProjectEntity[]>(() => projectsStore.items.filter((item) => item.status === 'PUBLISHED'))

onMounted(async () => {
  await Promise.all([
    servicesStore.fetchList(1, 100),
    projectsStore.fetchList(1, 100),
  ])

  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
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
  }
})

async function handleSubmit() {
  saving.value = true
  error.value = ''
  try {
    const payload: PagePayload = {
      ...form,
      content: form.templateCode === 'default' ? form.content : '',
      contentJson: form.templateCode === 'about_company'
        ? JSON.stringify(normalizeAboutContent(aboutContent))
        : form.templateCode === 'home_landing'
          ? JSON.stringify(normalizeHomeContent(homeContent))
          : null,
    }

    if (isEdit.value) {
      await store.update(Number(route.params.id), payload)
    } else {
      await store.create(payload)
    }
    router.push('/pages')
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Thao tác thất bại'
  } finally {
    saving.value = false
  }
}

function createValueItem(): AboutPageValueItem {
  return { title: '', icon: '', description: '' }
}

function createTimelineItem(): AboutPageTimelineItem {
  return { year: '', title: '', description: '', sortOrder: 1 }
}

function createCapabilityItem(): AboutCapabilityItem {
  return { title: '', pdfFilePath: '', buttonLabel: 'Xem PDF' }
}

function createHomeBadgeItem(): HomeHeroBadgeItem {
  return { label: '', iconKey: 'terrain' }
}

function createHomeStatItem(): HomeStatItem {
  return { value: '', label: '' }
}

function createSelectedServiceItem(): HomeSelectedServiceItem {
  return { serviceId: null, icon: '' }
}

function createSelectedProjectItem(): HomeSelectedProjectItem {
  return { projectId: null }
}

function createEmptyHomeContent(): HomePageContent {
  return {
    hero: {
      title: '',
      subtitle: '',
      backgroundImagePath: '',
      badges: [
        { label: 'Địa hình', iconKey: 'terrain' },
        { label: 'Địa chất', iconKey: 'geology' },
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
      title: 'Đơn vị khảo sát xây dựng tập trung vào độ chính xác và tiến độ thực địa',
      description: '',
      imagePath: '',
      highlights: [
        'Thiết bị khảo sát và đo đạc được bố trí theo yêu cầu từng công trình.',
        'Đội ngũ kỹ sư bám sát hiện trường, phối hợp nhanh với chủ đầu tư và tư vấn thiết kế.',
        'Quy trình triển khai gọn, hồ sơ bàn giao rõ ràng, phù hợp tiến độ thi công.',
      ],
      buttonLabel: 'Xem hồ sơ năng lực',
      buttonUrl: '/gioi-thieu',
    },
    servicesSection: {
      eyebrow: 'Lĩnh vực',
      title: 'Các Dịch Vụ Chính',
      description: '',
      mode: 'latest',
      limit: 6,
      selectedItems: [],
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
      description: '',
      items: ['Trong ngày', 'Miền Bắc', 'Nhà xưởng, khu công nghiệp, hạ tầng kỹ thuật'],
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
    hero: { title: '', backgroundImagePath: '' },
    intro: { heading: '', content: '<p></p>' },
    coreValues: { sectionTitle: 'Sứ Mệnh & Tầm Nhìn', items: [createValueItem()] },
    timeline: { sectionTitle: 'Quá Trình Phát Triển', items: [createTimelineItem()] },
    organization: {
      sectionTitle: 'Cơ Cấu Tổ Chức',
      heading: '',
      description: '',
      chartImagePath: '',
      chartCaption: '',
      departments: [''],
    },
    capability: {
      sectionTitle: 'Hồ Sơ Năng Lực',
      heading: '',
      description: '',
      imagePath: '',
      items: [createCapabilityItem()],
    },
  }
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
  const intro = input.intro ?? { heading: '', content: '<p></p>' }
  const coreValues = input.coreValues ?? { sectionTitle: 'Sứ Mệnh & Tầm Nhìn', items: [] }
  const timeline = input.timeline ?? { sectionTitle: 'Quá Trình Phát Triển', items: [] }
  const organization = input.organization ?? {
    sectionTitle: 'Cơ Cấu Tổ Chức',
    heading: '',
    description: '',
    chartImagePath: '',
    chartCaption: '',
    departments: [''],
  }
  const capability = input.capability ?? {
    sectionTitle: 'Hồ Sơ Năng Lực',
    heading: '',
    description: '',
    imagePath: '',
    items: [createCapabilityItem()],
  }

  return {
    hero: {
      title: hero.title || '',
      backgroundImagePath: hero.backgroundImagePath || '',
    },
    intro: {
      heading: intro.heading || '',
      content: normalizeIntroContent(intro),
    },
    coreValues: {
      sectionTitle: coreValues.sectionTitle || 'Sứ Mệnh & Tầm Nhìn',
      items: (coreValues.items || []).map((item) => ({
        title: item.title || '',
        icon: item.icon || '',
        description: item.description || '',
      })),
    },
    timeline: {
      sectionTitle: timeline.sectionTitle || 'Quá Trình Phát Triển',
      items: (timeline.items || []).map((item, index) => ({
        year: item.year || '',
        title: item.title || '',
        description: item.description || '',
        sortOrder: Number(item.sortOrder) || index + 1,
      })),
    },
    organization: {
      sectionTitle: organization.sectionTitle || 'Cơ Cấu Tổ Chức',
      heading: organization.heading || '',
      description: organization.description || '',
      chartImagePath: organization.chartImagePath || '',
      chartCaption: organization.chartCaption || '',
      departments: (organization.departments || ['']).map((item) => item || ''),
    },
    capability: {
      sectionTitle: capability.sectionTitle || 'Hồ Sơ Năng Lực',
      heading: capability.heading || '',
      description: capability.description || '',
      imagePath: capability.imagePath || '',
      items: (capability.items || [createCapabilityItem()]).map((item) => ({
        title: item.title || '',
        pdfFilePath: item.pdfFilePath || '',
        buttonLabel: item.buttonLabel || 'Xem PDF',
      })),
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
      badges: (input.hero?.badges || fallback.hero.badges).map((item) => ({
        label: item.label || '',
        iconKey: item.iconKey || 'terrain',
      })),
      primaryCtaLabel: input.hero?.primaryCtaLabel || fallback.hero.primaryCtaLabel,
      primaryCtaUrl: input.hero?.primaryCtaUrl || fallback.hero.primaryCtaUrl,
      secondaryCtaLabel: input.hero?.secondaryCtaLabel || fallback.hero.secondaryCtaLabel,
      secondaryCtaUrl: input.hero?.secondaryCtaUrl || fallback.hero.secondaryCtaUrl,
    },
    stats: {
      items: (input.stats?.items || fallback.stats.items).map((item) => ({
        value: item.value || '',
        label: item.label || '',
      })),
    },
    aboutSection: {
      eyebrow: input.aboutSection?.eyebrow || fallback.aboutSection.eyebrow,
      title: input.aboutSection?.title || fallback.aboutSection.title,
      description: input.aboutSection?.description || fallback.aboutSection.description,
      imagePath: input.aboutSection?.imagePath || fallback.aboutSection.imagePath,
      highlights: (input.aboutSection?.highlights || fallback.aboutSection.highlights).map((item) => item || ''),
      buttonLabel: input.aboutSection?.buttonLabel || fallback.aboutSection.buttonLabel,
      buttonUrl: input.aboutSection?.buttonUrl || fallback.aboutSection.buttonUrl,
    },
    servicesSection: {
      eyebrow: input.servicesSection?.eyebrow || fallback.servicesSection.eyebrow,
      title: input.servicesSection?.title || fallback.servicesSection.title,
      description: input.servicesSection?.description || fallback.servicesSection.description,
      mode: input.servicesSection?.mode === 'manual' ? 'manual' : fallback.servicesSection.mode,
      limit: Number(input.servicesSection?.limit) || fallback.servicesSection.limit,
      selectedItems: (input.servicesSection?.selectedItems || fallback.servicesSection.selectedItems).map((item) => ({
        serviceId: Number(item.serviceId) || null,
        categoryId: item.categoryId == null ? null : Number(item.categoryId) || null,
        icon: item.icon || '',
      })),
    },
    projectsSection: {
      eyebrow: input.projectsSection?.eyebrow || fallback.projectsSection.eyebrow,
      title: input.projectsSection?.title || fallback.projectsSection.title,
      description: input.projectsSection?.description || fallback.projectsSection.description,
      mode: input.projectsSection?.mode === 'manual' ? 'manual' : fallback.projectsSection.mode,
      limit: Number(input.projectsSection?.limit) || fallback.projectsSection.limit,
      selectedItems: (input.projectsSection?.selectedItems || fallback.projectsSection.selectedItems).map((item) => ({
        projectId: Number(item.projectId) || null,
      })),
    },
    finalCta: {
      eyebrow: input.finalCta?.eyebrow || fallback.finalCta.eyebrow,
      title: input.finalCta?.title || fallback.finalCta.title,
      description: input.finalCta?.description || fallback.finalCta.description,
      items: (input.finalCta?.items || fallback.finalCta.items).map((item) => item || ''),
      primaryButtonLabel: input.finalCta?.primaryButtonLabel || fallback.finalCta.primaryButtonLabel,
      primaryButtonUrl: input.finalCta?.primaryButtonUrl || fallback.finalCta.primaryButtonUrl,
      secondaryButtonLabel: input.finalCta?.secondaryButtonLabel || fallback.finalCta.secondaryButtonLabel,
      secondaryButtonUrl: input.finalCta?.secondaryButtonUrl || fallback.finalCta.secondaryButtonUrl,
      showPhoneButton: input.finalCta?.showPhoneButton ?? fallback.finalCta.showPhoneButton,
    },
  }
}

function removeArrayItem<T>(items: T[], index: number) {
  if (items.length <= 1) return
  items.splice(index, 1)
}

function removeAnyArrayItem<T>(items: T[], index: number) {
  items.splice(index, 1)
}

function normalizeIntroContent(intro: { heading?: string; content?: string; paragraphs?: string[] }) {
  const html = intro.content?.trim()
  if (html) return html

  const paragraphs = (intro.paragraphs || []).map((item) => item.trim()).filter(Boolean)
  if (!paragraphs.length) return '<p></p>'
  return paragraphs.map((item) => `<p>${escapeHtml(item)}</p>`).join('')
}

function escapeHtml(value: string) {
  return value
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')
}

function openIntroImagePicker() {
  introImageInputRef.value?.click()
}

async function handleHeroImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingHeroImage, 'Tải ảnh nền thất bại')
  if (storagePath) {
    aboutContent.hero.backgroundImagePath = storagePath
  }
}

async function handleIntroImageSelected(event: Event) {
  const input = event.target as HTMLInputElement | null
  const file = input?.files?.[0]
  if (!file) return

  uploadingIntroImage.value = true
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name)
    insertImageToIntro(resolveUploadedMediaUrl(media.storagePath))
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Tải ảnh vào nội dung thất bại'
  } finally {
    uploadingIntroImage.value = false
    if (input) input.value = ''
  }
}

async function handleOrgChartImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingOrgChartImage, 'Tải ảnh sơ đồ thất bại')
  if (storagePath) {
    aboutContent.organization.chartImagePath = storagePath
  }
}

async function handleCapabilityImageSelected(event: Event) {
  const storagePath = await uploadFileFromInput(event, uploadingCapabilityImage, 'Tải ảnh hồ sơ năng lực thất bại')
  if (storagePath) {
    aboutContent.capability.imagePath = storagePath
  }
}

async function handleHomeHeroImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingHomeHeroImage, 'Tải ảnh hero trang chủ thất bại')
  if (storagePath) {
    homeContent.hero.backgroundImagePath = storagePath
  }
}

async function handleHomeAboutImageSelected(event: Event) {
  const storagePath = await uploadImageFromInput(event, uploadingHomeAboutImage, 'Tải ảnh phần giới thiệu thất bại')
  if (storagePath) {
    homeContent.aboutSection.imagePath = storagePath
  }
}

function openCapabilityItemPdfPicker(index: number) {
  capabilityItemUploadIndex.value = index
  capabilityItemPdfInputRef.value?.click()
}

async function handleCapabilityItemPdfSelected(event: Event) {
  const targetIndex = capabilityItemUploadIndex.value
  if (targetIndex === null) return

  const loadingProxy = {
    get value() {
      return uploadingCapabilityItemIndex.value !== null
    },
    set value(next: boolean) {
      uploadingCapabilityItemIndex.value = next ? targetIndex : null
    },
  }

  const storagePath = await uploadFileFromInput(event, loadingProxy, 'Tải file PDF thất bại')
  if (storagePath && aboutContent.capability.items[targetIndex]) {
    aboutContent.capability.items[targetIndex].pdfFilePath = storagePath
  }
  capabilityItemUploadIndex.value = null
}

function insertImageToIntro(url: string) {
  const quill = introQuillRef.value?.getQuill?.()
  if (!quill) return

  const selection = quill.getSelection(true)
  const index = selection?.index ?? quill.getLength()
  quill.insertEmbed(index, 'image', url, 'user')
  quill.setSelection(index + 1, 0, 'user')
  aboutContent.intro.content = quill.root.innerHTML || '<p></p>'
}

function resolveUploadedMediaUrl(path: string) {
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
}

function openDefaultImagePicker() {
  defaultImageInputRef.value?.click()
}

async function handleDefaultImageSelected(event: Event) {
  const input = event.target as HTMLInputElement | null
  const file = input?.files?.[0]
  if (!file) return

  uploadingDefaultImage.value = true
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name)
    insertImageToDefault(resolveUploadedMediaUrl(media.storagePath))
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Tải ảnh vào nội dung thất bại'
  } finally {
    uploadingDefaultImage.value = false
    if (input) input.value = ''
  }
}

function insertImageToDefault(url: string) {
  const quill = defaultQuillRef.value?.getQuill?.()
  if (!quill) return

  const selection = quill.getSelection(true)
  const index = selection?.index ?? quill.getLength()
  quill.insertEmbed(index, 'image', url, 'user')
  quill.setSelection(index + 1, 0, 'user')
  form.content = quill.root.innerHTML || ''
}

async function uploadImageFromInput(event: Event, loadingState: { value: boolean }, fallbackMessage: string) {
  return uploadFileFromInput(event, loadingState, fallbackMessage)
}

async function uploadFileFromInput(event: Event, loadingState: { value: boolean }, fallbackMessage: string) {
  const input = event.target as HTMLInputElement | null
  const file = input?.files?.[0]
  if (!file) return ''

  loadingState.value = true
  error.value = ''
  try {
    const media = await mediaApi.upload(file, file.name)
    return media.storagePath
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || fallbackMessage
    return ''
  } finally {
    loadingState.value = false
    if (input) input.value = ''
  }
}
</script>

<style scoped>
.cms-collapsible > summary {
  cursor: pointer;
  list-style: none;
}

.cms-collapsible > summary::-webkit-details-marker {
  display: none;
}

.collapse-icon {
  transition: transform 0.2s ease;
}

.cms-collapsible[open] .collapse-icon {
  transform: rotate(180deg);
}

.input-action-btn {
  position: absolute;
  right: 0.4rem;
  top: 50%;
  transform: translateY(-50%);
  border: 1px solid #cbd5e1;
  background: #f8fafc;
  color: #334155;
  border-radius: 0.7rem;
  height: calc(100% - 0.8rem);
  min-width: 7.75rem;
  padding: 0 0.85rem;
  font-size: 0.8rem;
  font-weight: 600;
}

.input-action-btn:hover:not(:disabled) {
  background: #f1f5f9;
}

.input-action-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.editor-tab-btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #475569;
  border-radius: 999px;
  padding: 0.45rem 0.9rem;
  font-size: 0.82rem;
  font-weight: 600;
}

.editor-tab-btn.active {
  background: #0f172a;
  border-color: #0f172a;
  color: #fff;
}

.intro-quill-wrap :deep(.ql-toolbar.ql-snow) {
  border: 0;
  border-bottom: 1px solid #e2e8f0;
  background: #f8fafc;
}

.intro-quill-wrap :deep(.ql-container.ql-snow) {
  border: 0;
  font-size: 0.95rem;
  min-height: 320px;
}

.intro-quill-editor :deep(.ql-editor) {
  min-height: 320px;
  color: #334155;
  line-height: 1.8;
  padding: 1rem 1.125rem;
}

.intro-quill-editor :deep(.ql-editor.ql-blank::before) {
  color: #94a3b8;
  font-style: normal;
}
</style>
