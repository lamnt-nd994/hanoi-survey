<template>
  <section class="bg-neutral-50 py-16 md:py-20">
    <div class="container-shell">
      <div class="mb-12 text-center">
        <h2 class="section-title">Quy trình Khảo sát Tiêu chuẩn</h2>
        <p class="section-subtitle mt-4">Tuân thủ nghiêm ngặt các quy định TCVN và tiêu chuẩn quốc tế</p>
      </div>

      <div class="relative">
        <div class="absolute left-8 top-0 bottom-0 hidden w-0.5 bg-gradient-to-b from-accent-green via-accent-green/50 to-transparent md:block lg:left-1/2" />

        <div class="space-y-8 md:space-y-12">
          <div
            v-for="(step, index) in processSteps"
            :key="step.step"
            class="relative grid gap-6 md:grid-cols-2 md:gap-12"
            :class="index % 2 === 0 ? '' : 'md:flex-row-reverse'"
          >
            <div class="md:text-right" :class="index % 2 === 0 ? 'md:text-left' : 'md:text-right'">
              <div class="group rounded-2xl border border-neutral-200 bg-white p-6 shadow-panel transition-all hover:border-accent-green/30 hover:shadow-panel-lg md:pr-12" :class="index % 2 === 0 ? 'md:pr-12' : 'md:pl-12'">
                <div class="mb-3 flex items-start gap-3" :class="index % 2 === 0 ? 'md:justify-start' : 'md:justify-end'">
                  <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-accent-green text-lg font-bold text-white transition-transform group-hover:scale-110">
                    {{ step.step }}
                  </span>
                  <div>
                    <h3 class="font-heading text-xl font-bold text-primary-navy">{{ step.title }}</h3>
                    <p class="mt-2 text-neutral-600">{{ step.description }}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="hidden md:block" :class="index % 2 === 0 ? 'md:order-first' : 'md:order-last'">
              <div class="flex h-full items-center justify-center">
                <div class="relative">
                  <div class="absolute inset-0 flex h-12 w-12 items-center justify-center rounded-full bg-accent-green/10">
                    <div class="h-3 w-3 rounded-full bg-accent-green animate-pulse" />
                  </div>
                </div>
              </div>
            </div>

            <div class="absolute left-8 top-6 hidden h-12 w-12 -translate-x-1/2 items-center justify-center rounded-full bg-accent-green text-white shadow-glow md:flex lg:left-1/2">
              <span class="text-sm font-bold">{{ step.step }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-16 grid gap-6 md:grid-cols-3">
        <div class="stat-card text-center">
          <div class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-accent-green/10 text-accent-green">
            <svg class="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div class="font-heading text-3xl font-bold text-primary-navy">3-5 ngày</div>
          <div class="mt-2 text-sm text-neutral-600">Thời gian hoàn thành trung bình</div>
        </div>

        <div class="stat-card text-center">
          <div class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-accent-green/10 text-accent-green">
            <svg class="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
            </svg>
          </div>
          <div class="font-heading text-3xl font-bold text-primary-navy">100%</div>
          <div class="mt-2 text-sm text-neutral-600">Đạt chuẩn kiểm soát chất lượng</div>
        </div>

        <div class="stat-card text-center">
          <div class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-accent-green/10 text-accent-green">
            <svg class="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <div class="font-heading text-3xl font-bold text-primary-navy">24/7</div>
          <div class="mt-2 text-sm text-neutral-600">Hỗ trợ hiện trường khẩn cấp</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { processSteps } from '../content'
</script>
