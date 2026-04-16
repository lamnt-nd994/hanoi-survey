<template>
  <div>
    <section class="container-shell py-16 md:py-20">
      <div v-if="pageHeading" class="mx-auto mb-20 max-w-4xl text-center">
        <h1 class="font-heading text-3xl font-extrabold leading-tight text-primary-navy md:text-5xl">
          {{ pageHeading }}
        </h1>
      </div>

      <div v-if="loading && !rendersStableRouteContent" class="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        <Skeleton v-for="index in 6" :key="index" class="h-40 rounded-2xl" />
      </div>

      <div v-if="errorMessage && !loading" class="mb-8 rounded-2xl border border-rose-200 bg-rose-50 px-5 py-4 text-sm text-rose-700">{{ errorMessage }}</div>

      <div v-if="currentRouteName === 'services'" class="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        <router-link
          v-for="(item, index) in serviceCards"
          :key="item.slug || `service-card-${index}`"
          :to="item.slug ? { name: 'service-detail', params: { slug: item.slug } } : { name: 'services' }"
          class="group flex h-full flex-col rounded-lg border border-neutral-200 bg-white p-5 shadow-sm transition-colors duration-200 hover:border-accent-green/50"
        >
          <div class="flex items-start justify-between gap-4">
            <span class="text-[11px] font-semibold uppercase tracking-[0.14em] text-accent-green">Lĩnh vực khảo sát</span>
            <span class="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-accent-green/10 text-primary-navy">
              <IconRenderer :icon="item.icon || 'settings-2'" class="h-5 w-5" />
            </span>
          </div>
          <h3 class="mt-4 min-h-[3.5rem] font-heading text-xl font-bold leading-tight text-primary-navy">{{ item.title || item }}</h3>
          <p class="mt-3 min-h-[7rem] text-sm leading-7 text-neutral-600">{{ item.overview || 'Dịch vụ triển khai theo tiêu chuẩn hiện hành, phù hợp hồ sơ thiết kế, thi công và kiểm định.' }}</p>
          <span class="mt-auto inline-flex min-h-[2rem] items-center gap-2 pt-5 text-sm font-semibold text-primary-navy">
            Xem chi tiết
            <svg class="h-4 w-4 text-accent-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </span>
        </router-link>
      </div>

      <div v-else-if="isProjectRoute" class="space-y-8">
        <div class="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
        <router-link
          v-for="(item, index) in projectCards"
          :key="item.slug"
          :to="item.slug ? { name: 'project-detail', params: { slug: item.slug } } : { name: 'projects' }"
          class="group flex h-full flex-col overflow-hidden rounded-lg border border-l-4 border-neutral-200 border-l-primary-navy bg-white shadow-sm transition-colors duration-200 hover:border-accent-green/50 hover:border-l-accent-green"
        >
          <div class="relative h-44 overflow-hidden border-b border-neutral-200 bg-gradient-to-br from-primary-navy to-primary-light">
            <img
              v-if="item.coverImagePath"
              :src="resolveMediaUrl(item.coverImagePath)"
              :alt="item.title"
              width="420"
              height="224"
              loading="lazy"
              decoding="async"
              class="absolute inset-0 h-full w-full object-cover"
              @error="handleImageError"
            />
          </div>

          <div class="flex flex-1 flex-col p-5">
            <div class="min-h-[8.5rem]">
              <div v-if="item.categoryName" class="text-[11px] font-semibold uppercase tracking-[0.14em] text-accent-green">{{ item.categoryName }}</div>
              <h3 class="mt-2 min-h-[3.5rem] font-heading text-xl font-bold leading-tight text-primary-navy transition-colors">{{ item.title }}</h3>

              <div class="mt-5 divide-y divide-neutral-100 border-y border-neutral-100 text-sm">
                <div class="grid grid-cols-[5.5rem_minmax(0,1fr)] gap-3 py-3">
                  <span class="text-xs leading-6 font-semibold uppercase tracking-[0.12em] text-neutral-400">Địa điểm</span>
                  <span class="font-medium leading-6 text-neutral-700">{{ item.location || 'Đang cập nhật' }}</span>
                </div>
                <div class="grid grid-cols-[5.5rem_minmax(0,1fr)] gap-3 py-3">
                  <span class="text-xs leading-6 font-semibold uppercase tracking-[0.12em] text-neutral-400">Quy mô</span>
                  <span class="font-medium leading-6 text-neutral-700">{{ item.scaleText || 'Đang cập nhật' }}</span>
                </div>
                <div class="grid min-h-[4rem] grid-cols-[5.5rem_minmax(0,1fr)] gap-3 py-3">
                  <span class="text-xs leading-6 font-semibold uppercase tracking-[0.12em] text-neutral-400">Chủ đầu tư</span>
                  <span class="font-medium leading-6 text-neutral-700">{{ item.clientName || 'Đang cập nhật' }}</span>
                </div>
              </div>
            </div>

            <div class="mt-auto pt-5 text-sm font-semibold text-primary-navy transition-colors">
              Xem hồ sơ dự án
            </div>
          </div>
        </router-link>
        </div>

        <div v-if="projectTotalPages > 1" class="flex items-center justify-center gap-1 pt-2">
          <Button :disabled="safeProjectPage <= 1" variant="secondary" size="icon" class="h-9 w-9 rounded border text-neutral-500 hover:border-primary-navy hover:text-primary-navy disabled:cursor-default disabled:opacity-30" @click="goToProjectPage(safeProjectPage - 1)">&laquo;</Button>
          <template v-for="p in projectVisiblePages" :key="`project-page-${p}`">
            <span v-if="p === '...'" class="flex h-9 w-9 items-center justify-center text-neutral-400">...</span>
            <Button v-else variant="secondary" size="icon" class="h-9 w-9 rounded border text-sm" :class="p === safeProjectPage ? 'border-primary-navy bg-primary-navy text-white hover:bg-primary-navy' : 'border-neutral-200 text-neutral-600 hover:border-primary-navy hover:text-primary-navy'" @click="goToProjectPage(p as number)">{{ p }}</Button>
          </template>
          <Button :disabled="safeProjectPage >= projectTotalPages" variant="secondary" size="icon" class="h-9 w-9 rounded border text-neutral-500 hover:border-primary-navy hover:text-primary-navy disabled:cursor-default disabled:opacity-30" @click="goToProjectPage(safeProjectPage + 1)">&raquo;</Button>
        </div>
      </div>

      <div v-else-if="isNewsRoute" class="space-y-8">
        <Card v-if="false && !postItems.length" class="p-8 text-center text-neutral-500">
          Chưa có bài viết nào.
        </Card>

        <template v-else>
          <router-link
            :to="newsHeroCard.slug ? { name: 'news-detail', params: { slug: newsHeroCard.slug } } : { name: 'news' }"
            class="group block overflow-hidden rounded-lg border border-l-4 border-neutral-200 border-l-primary-navy bg-white shadow-sm transition-colors duration-200 hover:border-accent-green/50 hover:border-l-accent-green"
          >
            <div class="grid md:grid-cols-[280px_minmax(0,1fr)]">
              <div class="relative h-48 overflow-hidden border-b border-neutral-200 bg-gradient-to-br from-primary-navy to-primary-light md:h-full md:min-h-[13rem] md:border-b-0 md:border-r">
                <img
                  v-if="newsHeroCard.coverImagePath"
                  :src="resolveMediaUrl(newsHeroCard.coverImagePath)"
                  :alt="newsHeroCard.title"
                  width="320"
                  height="240"
                  loading="eager"
                  fetchpriority="high"
                  decoding="async"
                  class="absolute inset-0 h-full w-full object-cover"
                  @error="handleImageError"
                />
              </div>
              <div class="flex flex-1 flex-col justify-center p-5 md:p-7">
                <div class="flex min-h-[1rem] flex-wrap items-center gap-3 text-xs font-semibold uppercase tracking-[0.12em] text-neutral-400">
                  <span class="text-accent-green">{{ newsHeroCard.categoryName || 'Tin tức' }}</span>
                  <span class="h-1 w-1 rounded-full bg-neutral-300" />
                  <span>{{ newsHeroCard.publishedAt ? formatDate(newsHeroCard.publishedAt) : 'Đang cập nhật' }}</span>
                </div>
                <h2 class="mt-2 min-h-[4.5rem] font-heading text-xl font-bold leading-snug text-primary-navy transition-colors md:text-2xl">{{ newsHeroCard.title }}</h2>
                <p class="mt-3 min-h-[3.5rem] text-sm leading-7 text-neutral-600 line-clamp-2">{{ newsHeroCard.excerpt || 'Nội dung bài viết đang được cập nhật theo hệ thống tin tức.' }}</p>
                <span class="mt-5 text-sm font-semibold text-primary-navy transition-colors">Đọc bản tin</span>
              </div>
            </div>
          </router-link>

          <div class="divide-y divide-neutral-200 border-y border-neutral-200 bg-white">
            <router-link
              v-for="(item, index) in newsListCards"
              :key="item.slug || `news-card-${index}`"
              :to="item.slug ? { name: 'news-detail', params: { slug: item.slug } } : { name: 'news' }"
              class="group flex gap-4 px-2 py-5 transition-colors hover:bg-neutral-50 md:px-4"
            >
              <div class="relative h-14 w-20 shrink-0 overflow-hidden rounded-sm border border-neutral-200 bg-neutral-100 md:h-16 md:w-24">
                <img
                  v-if="item.coverImagePath"
                  :src="resolveMediaUrl(item.coverImagePath)"
                  :alt="item.title"
                  width="96"
                  height="64"
                  loading="lazy"
                  decoding="async"
                  class="absolute inset-0 h-full w-full object-cover"
                  @error="($event.target as HTMLImageElement).style.display = 'none'"
                />
                <div v-else class="flex h-full w-full items-center justify-center">
                  <svg class="h-8 w-8 text-neutral-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"/></svg>
                </div>
              </div>
              <div class="flex min-w-0 flex-1 flex-col justify-center">
                <h3 class="min-h-[2.5rem] font-heading text-base font-semibold leading-snug text-primary-navy transition-colors">{{ item.title }}</h3>
                <div class="mt-2 flex min-h-[1rem] flex-wrap items-center gap-3 text-[11px] font-semibold uppercase tracking-[0.1em] text-neutral-400">
                  <span class="text-accent-green">{{ item.categoryName || 'Tin tức' }}</span>
                  <span>&middot;</span>
                  <span>{{ item.publishedAt ? formatDate(item.publishedAt) : 'Đang cập nhật' }}</span>
                </div>
              </div>
              <svg class="hidden h-4 w-4 shrink-0 self-center text-neutral-300 transition-colors sm:block" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
            </router-link>
          </div>

          <div v-if="postsMeta.totalPages > 1" class="flex items-center justify-center gap-1 pt-2">
            <Button :disabled="currentNewsPage <= 1" variant="secondary" size="icon" class="h-9 w-9 rounded border text-neutral-500 hover:border-primary-navy hover:text-primary-navy disabled:cursor-default disabled:opacity-30" @click="goToNewsPage(currentNewsPage - 1)">&laquo;</Button>
            <template v-for="p in newsVisiblePages" :key="p">
              <span v-if="p === '...'" class="flex h-9 w-9 items-center justify-center text-neutral-400">...</span>
              <Button v-else variant="secondary" size="icon" class="h-9 w-9 rounded border text-sm" :class="p === currentNewsPage ? 'border-primary-navy bg-primary-navy text-white hover:bg-primary-navy' : 'border-neutral-200 text-neutral-600 hover:border-primary-navy hover:text-primary-navy'" @click="goToNewsPage(p as number)">{{ p }}</Button>
            </template>
            <Button :disabled="currentNewsPage >= postsMeta.totalPages" variant="secondary" size="icon" class="h-9 w-9 rounded border text-neutral-500 hover:border-primary-navy hover:text-primary-navy disabled:cursor-default disabled:opacity-30" @click="goToNewsPage(currentNewsPage + 1)">&raquo;</Button>
          </div>
        </template>
      </div>

      <div v-else-if="currentRouteName === 'equipment'" class="space-y-12">
        <div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          <router-link
            v-for="(category, index) in equipmentCards"
            :key="category.id ?? `equipment-card-${index}`"
            :to="category.slug ? { name: 'equipment-detail', params: { slug: category.slug } } : { name: 'equipment' }"
            class="group flex h-full flex-col rounded-lg border border-neutral-200 bg-white p-5 shadow-sm transition-colors duration-200 hover:border-accent-green/50"
          >
            <div class="flex items-start justify-between gap-4">
              <div class="text-[11px] font-semibold uppercase tracking-[0.14em] text-accent-green">Danh mục thiết bị</div>
              <span class="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-accent-green/10 text-primary-navy">
                <IconRenderer :icon="category.icon || 'settings-2'" class="h-5 w-5" />
              </span>
            </div>
            <h3 class="mt-4 min-h-[3.5rem] font-heading text-xl font-bold leading-tight text-primary-navy">{{ category.name }}</h3>
            <div class="mt-4 inline-flex w-fit items-center rounded-md border border-neutral-200 bg-neutral-50 px-3 py-2 text-sm font-medium text-neutral-700">
              {{ category.countText }}
            </div>
            <span class="mt-auto inline-flex min-h-[2rem] items-center gap-2 pt-5 text-sm font-semibold text-primary-navy">
              Xem danh mục
              <svg class="h-4 w-4 text-accent-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </span>
          </router-link>
        </div>
      </div>

      <div v-else-if="currentRouteName === 'contact'" class="space-y-8">
        <section class="overflow-hidden rounded-[2rem] border border-neutral-200 bg-white shadow-[0_24px_60px_rgba(15,23,42,0.08)]">
          <div class="grid lg:grid-cols-[0.92fr_1.08fr]">
            <div class="border-b border-neutral-200 bg-primary-navy px-6 py-8 text-white lg:border-b-0 lg:border-r lg:border-white/10 lg:px-8 lg:py-10">
              <div class="text-[11px] font-semibold uppercase tracking-[0.22em] text-white/55">Liên hệ khảo sát</div>
              <h2 class="mt-4 font-heading text-3xl font-bold leading-tight lg:text-[2.35rem]">Trao đổi trực tiếp về nhu cầu khảo sát công trình</h2>
              <p class="mt-4 max-w-xl text-sm leading-7 text-white/72 lg:text-[0.95rem]">
                Phù hợp cho khảo sát địa chất, địa hình, thủy văn, quan trắc và thí nghiệm xây dựng. Chúng tôi ưu tiên phản hồi nhanh, gọn và đúng đầu việc.
              </p>

              <div class="mt-8 space-y-4 border-t border-white/10 pt-6">
                <a :href="mergedContactInfo.phone ? `tel:${mergedContactInfo.phone}` : undefined" class="group flex min-h-[5.5rem] items-center justify-between gap-4 rounded-2xl border border-white/10 bg-white/5 px-5 py-4 transition-colors hover:bg-white/10">
                  <div class="flex items-center gap-4">
                    <span class="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-accent-green text-white">
                      <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" /></svg>
                    </span>
                    <div>
                      <div class="text-[11px] font-semibold uppercase tracking-[0.16em] text-white/55">Hotline</div>
                      <div class="mt-1 text-lg font-semibold">{{ mergedContactInfo.phone || 'Đang cập nhật' }}</div>
                    </div>
                  </div>
                  <span class="text-white/35 transition-transform group-hover:translate-x-1">→</span>
                </a>

                <a :href="mergedContactInfo.email ? `mailto:${mergedContactInfo.email}` : undefined" class="group flex min-h-[5.5rem] items-center justify-between gap-4 rounded-2xl border border-white/10 bg-white/5 px-5 py-4 transition-colors hover:bg-white/10">
                  <div class="flex items-center gap-4">
                    <span class="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-white/10 text-white">
                      <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg>
                    </span>
                    <div>
                      <div class="text-[11px] font-semibold uppercase tracking-[0.16em] text-white/55">Email</div>
                      <div class="mt-1 text-base font-medium">{{ mergedContactInfo.email || 'Đang cập nhật' }}</div>
                    </div>
                  </div>
                  <span class="text-white/35 transition-transform group-hover:translate-x-1">→</span>
                </a>
              </div>

              <div class="mt-8 grid gap-3 sm:grid-cols-3 lg:grid-cols-1 xl:grid-cols-3">
                <div class="min-h-[7rem] rounded-2xl border border-white/10 px-4 py-4">
                  <div class="text-[11px] font-semibold uppercase tracking-[0.16em] text-white/50">Phạm vi</div>
                  <div class="mt-2 text-sm leading-6 text-white/88">Địa chất, địa hình, thủy văn, quan trắc</div>
                </div>
                <div class="min-h-[7rem] rounded-2xl border border-white/10 px-4 py-4">
                  <div class="text-[11px] font-semibold uppercase tracking-[0.16em] text-white/50">Phản hồi</div>
                  <div class="mt-2 text-sm leading-6 text-white/88">Ưu tiên xử lý nhanh qua điện thoại và email</div>
                </div>
                <div class="min-h-[7rem] rounded-2xl border border-white/10 px-4 py-4">
                  <div class="text-[11px] font-semibold uppercase tracking-[0.16em] text-white/50">Thời gian</div>
                  <div class="mt-2 text-sm leading-6 text-white/88">{{ mergedContactInfo.workingHours || 'Theo lịch làm việc của công ty' }}</div>
                </div>
              </div>
            </div>

            <div class="px-6 py-8 lg:px-8 lg:py-10">
              <div class="min-h-[11rem] rounded-[1.7rem] border border-neutral-200 bg-neutral-50 px-5 py-5">
                <div class="text-[11px] font-semibold uppercase tracking-[0.16em] text-neutral-400">Văn phòng giao dịch</div>
                <div class="mt-3 min-h-[4rem] text-xl font-semibold leading-8 text-primary-navy">{{ mergedContactInfo.officeAddress || mergedContactInfo.address || 'Đang cập nhật địa chỉ văn phòng giao dịch' }}</div>
                <div class="mt-5 text-sm leading-7 text-neutral-600">
                  Vui lòng liên hệ trước để chúng tôi bố trí đúng bộ phận kỹ thuật hoặc kinh doanh phụ trách đầu việc khảo sát của bạn.
                </div>
              </div>

              <div class="mt-5 overflow-hidden rounded-[1.7rem] border border-neutral-200 p-2 md:p-3">
                <iframe
                  :src="contactMapEmbedUrl"
                  class="h-[480px] w-full rounded-[1.2rem] border-0"
                  loading="lazy"
                  referrerpolicy="no-referrer-when-downgrade"
                  allowfullscreen
                />
              </div>
            </div>
          </div>
        </section>

      </div>

      <div v-else-if="introItems.length" class="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
        <article v-for="item in introItems" :key="item.title" class="group rounded-xl border border-neutral-200 bg-white p-6 shadow-[0_18px_45px_rgba(15,39,68,0.08)] transition-all hover:border-accent-green/30">
          <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-accent-green/10 text-accent-green transition-all group-hover:bg-accent-green group-hover:text-white">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 class="font-heading text-xl font-bold text-primary-navy">{{ item.title }}</h3>
          <div class="mt-3 min-h-[7rem] text-sm leading-7 text-neutral-600" v-html="item.body"></div>
        </article>
      </div>

      <Card v-else class="p-8 text-center text-neutral-500">
        Chưa có dữ liệu để hiển thị.
      </Card>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'
import IconRenderer from '../components/IconRenderer.vue'
import { Button } from '../components/ui/button'
import { Card } from '../components/ui/card'
import { Skeleton } from '../components/ui/skeleton'
import { resolveMediaUrl } from '../lib/media'
import { usePublicContentStore } from '../stores/publicContent'
import { useSiteSettingsStore } from '../stores/siteSettings'
import { ROUTE_NAMES } from '../router'

type StaticPageConfig = {
  items: string[]
}

type IntroCard = {
  title: string
  body: string
}

type ServiceCard = {
  slug: string
  title: string
  overview: string
  icon?: string
}

type ProjectCard = {
  slug: string
  title: string
  categoryName?: string | null
  coverImagePath?: string | null
  location?: string | null
  scaleText?: string | null
  clientName?: string | null
}

type NewsCard = {
  slug: string
  title: string
  categoryName?: string | null
  coverImagePath?: string | null
  publishedAt?: string | null
  excerpt?: string | null
}

type EquipmentCard = {
  id?: number | null
  slug: string
  name: string
  icon?: string | null
  countText: string
}

const FALLBACK_SERVICE_CARDS: ServiceCard[] = [
  { slug: '', title: 'Khảo sát địa hình', overview: 'Dịch vụ triển khai theo tiêu chuẩn hiện hành, phù hợp hồ sơ thiết kế, thi công và kiểm định.', icon: 'map' },
  { slug: '', title: 'Khảo sát địa chất', overview: 'Thu thập và phân tích dữ liệu nền móng cho công trình dân dụng, công nghiệp và hạ tầng kỹ thuật.', icon: 'mountain' },
  { slug: '', title: 'Khảo sát thủy văn', overview: 'Đánh giá hiện trạng dòng chảy, mực nước và các yếu tố ảnh hưởng đến giải pháp thiết kế.', icon: 'droplets' },
  { slug: '', title: 'Quan trắc công trình', overview: 'Theo dõi biến động địa kỹ thuật và trạng thái công trình trong suốt quá trình thi công, vận hành.', icon: 'activity' },
  { slug: '', title: 'Thí nghiệm xây dựng', overview: 'Thực hiện thí nghiệm vật liệu và kiểm định phục vụ thiết kế, nghiệm thu và kiểm soát chất lượng.', icon: 'flask-conical' },
  { slug: '', title: 'Tư vấn hiện trường', overview: 'Phối hợp kỹ thuật nhanh với chủ đầu tư và đơn vị thi công để xử lý yêu cầu khảo sát thực tế.', icon: 'briefcase' },
]

const FALLBACK_PROJECT_CARDS: ProjectCard[] = Array.from({ length: 6 }, (_, index) => ({
  slug: '',
  title: `Dự án khảo sát ${index + 1}`,
  categoryName: 'Dự án tiêu biểu',
  coverImagePath: '',
  location: 'Đang cập nhật',
  scaleText: 'Đang cập nhật',
  clientName: 'Đang cập nhật',
}))

const FALLBACK_NEWS_HERO_CARD: NewsCard = {
  slug: '',
  title: 'Tin tức cập nhật từ hệ thống nội dung',
  categoryName: 'Tin tức',
  coverImagePath: '',
  publishedAt: '',
  excerpt: 'Nội dung bản tin sẽ được hiển thị ổn định ngay khi dữ liệu được tải xong.',
}

const FALLBACK_NEWS_LIST_CARDS: NewsCard[] = Array.from({ length: 5 }, (_, index) => ({
  slug: '',
  title: `Bản tin khảo sát ${index + 1}`,
  categoryName: 'Tin tức',
  coverImagePath: '',
  publishedAt: '',
  excerpt: '',
}))

const FALLBACK_EQUIPMENT_CARDS: EquipmentCard[] = Array.from({ length: 6 }, (_, index) => ({
  id: null,
  slug: '',
  name: `Danh mục thiết bị ${index + 1}`,
  icon: 'settings-2',
  countText: '0 thiết bị',
}))

const $route = useRoute()
const router = useRouter()
const loading = ref(false)
const errorMessage = ref('')
const publicContentStore = usePublicContentStore()
const siteSettingsStore = useSiteSettingsStore()
const { mergedContactInfo, siteName, settings } = storeToRefs(siteSettingsStore)
const { postsMeta } = storeToRefs(publicContentStore)

const pageData: Record<string, StaticPageConfig> = {
  [ROUTE_NAMES.about]: {
    items: [],
  },
  [ROUTE_NAMES.services]: {
    items: [],
  },
  [ROUTE_NAMES.projects]: {
    items: [],
  },
  [ROUTE_NAMES.news]: {
    items: [],
  },
  [ROUTE_NAMES.equipment]: {
    items: [],
  },
  [ROUTE_NAMES.contact]: {
    items: [],
  },
}

const currentRouteName = computed(() => $route.name as string || ROUTE_NAMES.about)
const isProjectRoute = computed(() => currentRouteName.value === ROUTE_NAMES.projects || currentRouteName.value === ROUTE_NAMES.projectCategory)
const isNewsRoute = computed(() => currentRouteName.value === ROUTE_NAMES.news || currentRouteName.value === ROUTE_NAMES.newsCategory)
const rendersStableRouteContent = computed(() => [
  ROUTE_NAMES.services,
  ROUTE_NAMES.projects,
  ROUTE_NAMES.projectCategory,
  ROUTE_NAMES.news,
  ROUTE_NAMES.newsCategory,
  ROUTE_NAMES.equipment,
  ROUTE_NAMES.contact,
].includes(currentRouteName.value as (typeof ROUTE_NAMES)[keyof Pick<typeof ROUTE_NAMES, 'services' | 'projects' | 'projectCategory' | 'news' | 'newsCategory' | 'equipment' | 'contact'>]))
const currentData = computed(() => pageData[currentRouteName.value] || pageData[ROUTE_NAMES.about])
const selectedCategorySlug = computed(() => {
  const categoryParam = $route.params.category
  if (typeof categoryParam === 'string') return categoryParam
  const category = $route.query.category
  return typeof category === 'string' ? category : ''
})
const selectedProjectCategory = computed(() => publicContentStore.projectCategories.find((item) => item.slug === selectedCategorySlug.value) || null)
const selectedPostCategory = computed(() => publicContentStore.postCategories.find((item) => item.slug === selectedCategorySlug.value) || null)
const selectedEquipmentCategory = computed(() => publicContentStore.equipmentCategories.find((item) => item.slug === selectedCategorySlug.value) || null)
const pageHeading = computed(() => {
  if (isProjectRoute.value && selectedProjectCategory.value) return selectedProjectCategory.value.name
  if (isNewsRoute.value && selectedPostCategory.value) return selectedPostCategory.value.name
  if (currentRouteName.value === ROUTE_NAMES.equipment && selectedEquipmentCategory.value) return selectedEquipmentCategory.value.name
  return ''
})
const items = computed(() => currentData.value.items)
const introItems = computed<IntroCard[]>(() => [])
const serviceItems = computed(() => {
  return publicContentStore.services
})
const serviceCards = computed<ServiceCard[]>(() => {
  if (serviceItems.value.length) {
    return serviceItems.value.map((item) => ({
      slug: item.slug || '',
      title: item.title || '',
      overview: item.overview || '',
      icon: item.icon || 'settings-2',
    }))
  }

  if (currentRouteName.value === ROUTE_NAMES.services) {
    return FALLBACK_SERVICE_CARDS
  }

  return []
})
const projectCards = computed<ProjectCard[]>(() => {
  if (paginatedProjectItems.value.length) {
    return paginatedProjectItems.value.map((item) => ({
      slug: item.slug || '',
      title: item.title || '',
      categoryName: item.categoryName || '',
      coverImagePath: item.coverImagePath || '',
      location: item.location || '',
      scaleText: item.scaleText || '',
      clientName: item.clientName || '',
    }))
  }
  if (isProjectRoute.value) return FALLBACK_PROJECT_CARDS
  return []
})
const newsHeroCard = computed<NewsCard>(() => {
  const first = postItems.value[0]
  if (first) {
    return {
      slug: first.slug || '',
      title: first.title || '',
      categoryName: first.categoryName || '',
      coverImagePath: first.coverImagePath || '',
      publishedAt: first.publishedAt || '',
      excerpt: first.excerpt || '',
    }
  }
  return FALLBACK_NEWS_HERO_CARD
})
const newsListCards = computed<NewsCard[]>(() => {
  if (postItems.value.length > 1) {
    return postItems.value.slice(1).map((item) => ({
      slug: item.slug || '',
      title: item.title || '',
      categoryName: item.categoryName || '',
      coverImagePath: item.coverImagePath || '',
      publishedAt: item.publishedAt || '',
      excerpt: item.excerpt || '',
    }))
  }
  if (isNewsRoute.value) return FALLBACK_NEWS_LIST_CARDS
  return []
})
const projectItems = computed(() => {
  if (!isProjectRoute.value || !selectedProjectCategory.value) return publicContentStore.projects
  return publicContentStore.projects.filter((item) => item.categoryId === selectedProjectCategory.value?.id)
})
const projectPageSize = 8
const currentProjectPage = computed(() => {
  const rawPage = Array.isArray($route.query.page) ? $route.query.page[0] : $route.query.page
  const page = Number(rawPage || 1)
  return Number.isFinite(page) && page > 0 ? page : 1
})
const projectTotalPages = computed(() => Math.max(1, Math.ceil(projectItems.value.length / projectPageSize)))
const safeProjectPage = computed(() => Math.min(currentProjectPage.value, projectTotalPages.value))
const paginatedProjectItems = computed(() => {
  const start = (safeProjectPage.value - 1) * projectPageSize
  return projectItems.value.slice(start, start + projectPageSize)
})
const projectVisiblePages = computed<(number | string)[]>(() => {
  const totalPages = projectTotalPages.value
  const page = safeProjectPage.value
  if (totalPages <= 7) {
    return Array.from({ length: totalPages }, (_, index) => index + 1)
  }
  const pages: (number | string)[] = [1]
  if (page > 3) pages.push('...')
  for (let index = Math.max(2, page - 1); index <= Math.min(totalPages - 1, page + 1); index += 1) {
    pages.push(index)
  }
  if (page < totalPages - 2) pages.push('...')
  pages.push(totalPages)
  return pages
})
const postItems = computed(() => publicContentStore.posts)
const currentNewsPage = computed(() => {
  const rawPage = Array.isArray($route.query.page) ? $route.query.page[0] : $route.query.page
  const page = Number(rawPage || 1)
  return Number.isFinite(page) && page > 0 ? page : 1
})
const newsVisiblePages = computed<(number | string)[]>(() => {
  const totalPages = postsMeta.value.totalPages || 1
  const page = currentNewsPage.value
  if (totalPages <= 7) {
    return Array.from({ length: totalPages }, (_, index) => index + 1)
  }
  const pages: (number | string)[] = [1]
  if (page > 3) pages.push('...')
  for (let index = Math.max(2, page - 1); index <= Math.min(totalPages - 1, page + 1); index += 1) {
    pages.push(index)
  }
  if (page < totalPages - 2) pages.push('...')
  pages.push(totalPages)
  return pages
})
const equipmentItems = computed(() => {
  if (currentRouteName.value !== ROUTE_NAMES.equipment || !selectedEquipmentCategory.value) return publicContentStore.equipments
  return publicContentStore.equipments.filter((item) => item.categoryId === selectedEquipmentCategory.value?.id)
})
const sortedEquipmentCategories = computed(() => [...publicContentStore.equipmentCategories].sort((a, b) => a.sortOrder - b.sortOrder))
const equipmentCards = computed<EquipmentCard[]>(() => {
  if (sortedEquipmentCategories.value.length) {
    return sortedEquipmentCategories.value.map((category) => ({
      id: category.id,
      slug: category.slug || '',
      name: category.name || '',
      icon: category.icon || 'settings-2',
      countText: `${publicContentStore.equipments.filter((e) => e.categoryId === category.id).length} thiết bị`,
    }))
  }
  if (currentRouteName.value === ROUTE_NAMES.equipment) return FALLBACK_EQUIPMENT_CARDS
  return []
})
const equipmentGroups = computed(() => sortedEquipmentCategories.value
  .map((category) => ({
    id: category.id,
    slug: category.slug,
    name: category.name,
    items: equipmentItems.value.filter((item) => item.categoryId === category.id),
  }))
  .filter((group) => group.items.length))
const contactMapEmbedUrl = computed(() => normalizeMapEmbedUrl(settings.value?.mapEmbed, mergedContactInfo.value.officeAddress || mergedContactInfo.value.address))

function handleImageError(event: Event) {
  const image = event.target as HTMLImageElement | null
  if (!image) return
  image.style.display = 'none'
}

function formatDate(dateStr: string | null): string {
  if (!dateStr) return ''
  try {
    return new Date(dateStr).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' })
  } catch {
    return dateStr
  }
}

function isImageAsset(path: string) {
  return /\.(png|jpe?g|gif|webp|svg)$/i.test(path)
}

function isPdfAsset(path: string) {
  return /\.pdf$/i.test(path)
}

function normalizeMapEmbedUrl(rawUrl: string | null | undefined, fallbackAddress: string) {
  const fallback = `https://www.google.com/maps?q=${encodeURIComponent(fallbackAddress || 'Hanoi Survey')}&output=embed`

  if (!rawUrl || !rawUrl.trim()) {
    return fallback
  }

  const normalized = rawUrl.trim()

  if (isValidGoogleMapsEmbedUrl(normalized)) {
    return normalized
  }

  return fallback
}

function isValidGoogleMapsEmbedUrl(rawUrl: string) {
  try {
    const url = new URL(rawUrl)
    const isGoogleHost = /(^|\.)google\.[^/]+$/i.test(url.hostname)
    const isEmbedPath = /\/maps\/embed/i.test(url.pathname)
    const hasEmbedOutput = url.searchParams.get('output') === 'embed'
    return isGoogleHost && (isEmbedPath || hasEmbedOutput)
  } catch {
    return false
  }
}

async function goToNewsPage(page: number) {
  const categorySlug = typeof $route.params.category === 'string' ? $route.params.category : ''
  const query = page > 1 ? { ...$route.query, page: String(page) } : Object.fromEntries(Object.entries($route.query).filter(([key]) => key !== 'page'))

  if (categorySlug) {
    await router.push({ name: ROUTE_NAMES.newsCategory, params: { category: categorySlug }, query })
    return
  }

  await router.push({
    name: ROUTE_NAMES.news,
    query,
  })
}

async function goToProjectPage(page: number) {
  const categorySlug = typeof $route.params.category === 'string' ? $route.params.category : ''
  const query = page > 1 ? { ...$route.query, page: String(page) } : Object.fromEntries(Object.entries($route.query).filter(([key]) => key !== 'page'))

  if (categorySlug) {
    await router.push({ name: ROUTE_NAMES.projectCategory, params: { category: categorySlug }, query })
    return
  }

  await router.push({
    name: ROUTE_NAMES.projects,
    query,
  })
}

async function loadRemoteItems() {
  loading.value = true
  errorMessage.value = ''

  try {
    if (currentRouteName.value === ROUTE_NAMES.about) {
      await publicContentStore.loadIntroPage()
      errorMessage.value = publicContentStore.errors.introPage
    } else if (currentRouteName.value === ROUTE_NAMES.services) {
      await publicContentStore.loadServices()
      errorMessage.value = publicContentStore.errors.services
    } else if (isProjectRoute.value) {
      await Promise.all([publicContentStore.loadProjectCategories(), publicContentStore.loadProjects()])
      errorMessage.value = publicContentStore.errors.projects || publicContentStore.errors.projectCategories
    } else if (isNewsRoute.value) {
      await Promise.all([
        publicContentStore.loadPostCategories(),
        publicContentStore.loadPosts(currentNewsPage.value, true, selectedCategorySlug.value),
      ])
      errorMessage.value = publicContentStore.errors.posts || publicContentStore.errors.postCategories
    } else if (currentRouteName.value === ROUTE_NAMES.equipment) {
      await Promise.all([publicContentStore.loadEquipmentCategories(), publicContentStore.loadEquipments()])
      errorMessage.value = publicContentStore.errors.equipments || publicContentStore.errors.equipmentCategories
    } else if (currentRouteName.value === ROUTE_NAMES.contact) {
      await siteSettingsStore.ensureLoaded()
    }
  } finally {
    loading.value = false
  }
}

watch(() => $route.fullPath, loadRemoteItems, { immediate: true })
</script>

