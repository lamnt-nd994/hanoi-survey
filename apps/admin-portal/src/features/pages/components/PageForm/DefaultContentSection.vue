<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between gap-3">
      <FormLabel>Nội dung</FormLabel>
      <Button type="button" variant="secondary" :disabled="uploading" @click="emit('trigger-image-upload')">
        {{ uploading ? `Đang tải ${uploadProgress}%` : 'Tải ảnh vào nội dung' }}
      </Button>
    </div>

    <div class="flex items-center gap-2">
      <Button type="button" :variant="editorMode === 'visual' ? 'default' : 'secondary'" size="sm" @click="emit('update:editorMode', 'visual')">Soạn thảo</Button>
      <Button type="button" :variant="editorMode === 'html' ? 'default' : 'secondary'" size="sm" @click="emit('update:editorMode', 'html')">HTML</Button>
    </div>

    <Card>
      <CardContent class="p-0">
        <div class="overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm intro-quill-wrap">
          <QuillEditor
            v-if="editorMode === 'visual'"
            ref="quillRef"
            v-model:content="contentProxy"
            content-type="html"
            theme="snow"
            :toolbar="toolbar"
            placeholder="Nhập nội dung chi tiết..."
            class="intro-quill-editor"
          />
          <div v-else class="p-4">
            <Textarea
              v-model="contentProxy"
              class="min-h-[360px] font-mono text-sm"
              rows="14"
              placeholder="<p>Nhập nội dung chi tiết...</p>"
            />
          </div>
        </div>
      </CardContent>
    </Card>

    <Card class="border-slate-200 bg-slate-50">
      <CardContent>
        <div class="text-xs font-medium uppercase tracking-[0.18em] text-slate-500">Xem trước</div>
        <div class="prose prose-slate mt-3 max-w-none text-sm" v-html="content"></div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { computed, defineAsyncComponent, ref } from 'vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Textarea } from '@/components/ui/textarea'
import type { QuillEditorComponent, QuillToolbar } from '@/types/api'

const QuillEditor = defineAsyncComponent(async () => {
  await import('@vueup/vue-quill/dist/vue-quill.snow.css')
  return (await import('@vueup/vue-quill')).QuillEditor
})

const props = defineProps<{
  content: string
  editorMode: 'visual' | 'html'
  toolbar: QuillToolbar
  uploading: boolean
  uploadProgress: number
}>()

const emit = defineEmits<{
  (e: 'update:content', value: string): void
  (e: 'update:editorMode', value: 'visual' | 'html'): void
  (e: 'trigger-image-upload'): void
}>()

const quillRef = ref<QuillEditorComponent | null>(null)

const contentProxy = computed({
  get: () => props.content,
  set: (value: string) => emit('update:content', value),
})

function insertImage(url: string) {
  const quill = quillRef.value?.getQuill?.()
  if (!quill) return
  const selection = quill.getSelection(true)
  const index = selection?.index ?? quill.getLength()
  quill.insertEmbed(index, 'image', url, 'user')
  quill.setSelection(index + 1, 0, 'user')
  emit('update:content', quill.root.innerHTML || '')
}

defineExpose({ insertImage })
</script>
