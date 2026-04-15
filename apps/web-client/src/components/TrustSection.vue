<template>
  <section class="bg-white py-16 md:py-20">
    <div class="container-shell">
      <div class="mb-12 text-center">
        <h2 class="section-title">Đối tác và Khách hàng</h2>
        <p class="section-subtitle mt-4">Được tin tưởng bởi các doanh nghiệp hàng đầu trong ngành xây dựng</p>
      </div>

      <div class="grid grid-cols-2 gap-6 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6">
        <Card
          v-for="partner in partners"
          :key="partner.name"
          class="group flex flex-col items-center justify-center p-6 hover:border-accent-green/30 hover:bg-accent-green/5 hover:shadow-[0_18px_45px_rgba(15,39,68,0.08)]"
        >
          <span class="text-4xl transition-transform group-hover:scale-110">{{ partner.logo }}</span>
          <span class="mt-3 text-sm font-medium text-neutral-700 transition-colors group-hover:text-primary-navy">{{ partner.name }}</span>
        </Card>
      </div>

      <div class="mt-20">
        <div class="mb-12 text-center">
          <h3 class="font-heading text-2xl font-bold text-primary-navy md:text-3xl">Chứng chỉ & Giấy phép hoạt động</h3>
        </div>

        <div class="grid gap-6 md:grid-cols-3">
          <Card
            v-for="cert in certificates"
            :key="cert.name"
            class="group flex items-start gap-4 bg-neutral-50 p-6 hover:border-accent-green/30 hover:shadow-[0_18px_45px_rgba(15,39,68,0.08)]"
          >
            <div class="flex h-14 w-14 shrink-0 items-center justify-center rounded-xl bg-accent-green/10 text-accent-green transition-all group-hover:bg-accent-green group-hover:text-white">
              <svg class="h-7 w-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" />
              </svg>
            </div>
            <div class="flex-1">
              <h4 class="font-heading text-lg font-semibold text-primary-navy">{{ cert.name }}</h4>
              <p class="mt-1 text-sm text-neutral-600">{{ cert.issuer }}</p>
              <span class="mt-2 inline-block rounded-full bg-accent-green/10 px-3 py-1 text-xs font-semibold text-accent-green">{{ cert.year }}</span>
            </div>
          </Card>
        </div>
      </div>

      <div class="mt-12 rounded-2xl border-2 border-dashed border-accent-green/30 bg-accent-green/5 p-8 text-center">
        <h4 class="font-heading text-xl font-bold text-primary-navy">Tải hồ sơ năng lực đầy đủ</h4>
        <p class="mt-2 text-neutral-600">Nhận thông tin chi tiết về năng lực, danh mục thiết bị và các dự án đã thực hiện</p>
        <Button as="router-link" :to="{ name: 'contact' }" class="mt-4">
          <svg class="mr-2 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
          </svg>
          Liên hệ để nhận hồ sơ
        </Button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { Button } from './ui/button'
import { Card } from './ui/card'
import { certificates, partners } from '../content'
</script>
