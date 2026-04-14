<template>
  <textarea
    :value="modelValue"
    :class="cn('flex min-h-[96px] w-full rounded-md border border-slate-200 bg-white px-3 py-2 text-sm text-slate-900 shadow-sm transition-colors placeholder:text-slate-400 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-slate-400 disabled:cursor-not-allowed disabled:opacity-50', props.class)"
    v-bind="$attrs"
    @input="handleInput"
  />
</template>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import { coerceModelValue, type ModelModifiers, type ModelValue } from '@/lib/model-value'

const props = withDefaults(defineProps<{ class?: string; modelValue?: ModelValue; modelModifiers?: ModelModifiers }>(), { class: '', modelValue: '', modelModifiers: () => ({}) })
const emit = defineEmits<{ (e: 'update:modelValue', value: ModelValue): void }>()

function handleInput(event: Event) {
  emit('update:modelValue', coerceModelValue((event.target as HTMLTextAreaElement).value, props.modelModifiers))
}
</script>
