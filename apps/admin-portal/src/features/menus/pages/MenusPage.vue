<template>
  <PageShell>
    <PageHeader title="Quản lý Menu" description="Quản lý menu điều hướng của website">
      <template #actions>
        <Button size="sm" @click="showMenuForm = true; menuFormMode = 'create'; resetMenuForm()">＋ Thêm menu</Button>
      </template>
    </PageHeader>

    <AlertBox v-if="store.loading" message="Đang tải dữ liệu..." />
    <AlertBox v-if="alert" :message="alert" :type="alertType" />

    <div class="mt-6 grid gap-6 lg:grid-cols-[280px_minmax(0,1fr)]">
      <DataCard>
        <div class="border-b border-slate-200 bg-slate-50 px-4 py-3 text-sm font-semibold text-slate-900">Danh sách menu</div>
        <div v-if="store.menus.length === 0" class="px-4 py-10 text-center text-sm text-slate-400">
          <p>Chưa có menu</p>
        </div>
        <div
          v-for="menu in store.menus"
          :key="menu.id"
          class="flex cursor-pointer items-center justify-between gap-3 border-b border-slate-100 px-4 py-3 text-sm transition hover:bg-slate-50"
          :class="activeMenuId === menu.id ? 'bg-emerald-50 font-semibold' : ''"
          @click="selectMenu(menu)"
        >
          <div>
            <div>{{ menu.name }}</div>
            <div class="mt-1 text-xs font-normal text-slate-500">{{ menu.code }} &middot; {{ menu.position }}</div>
          </div>
          <div class="flex justify-end">
            <ActionButton class="text-blue-600" title="Sửa" @click.stop="editMenu(menu)">✎</ActionButton>
          </div>
        </div>
      </DataCard>

      <DataCard>
        <div class="flex min-h-14 items-center justify-between gap-3 border-b border-slate-200 px-4 py-3">
          <span class="text-sm font-semibold text-slate-900">
            {{ activeMenu ? `Menu items: ${activeMenu.name}` : 'Chọn menu bên trái' }}
          </span>
          <Button
            v-if="activeMenu"
            size="sm"
            @click="showItemForm = true; itemFormMode = 'create'; resetItemForm()"
          >＋ Thêm item</Button>
        </div>
        <div v-if="!activeMenu" class="px-4 py-16 text-center text-sm text-slate-400"><p>Chọn một menu để xem các mục</p></div>
        <Table v-else>
          <TableHeader>
            <TableRow>
              <TableHead>Tiêu đề</TableHead>
              <TableHead>Loại</TableHead>
              <TableHead>URL / Ref</TableHead>
              <TableHead>Thứ tự</TableHead>
              <TableHead>Hiện</TableHead>
              <TableHead class="w-[100px] text-right">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="item in store.menuItems" :key="item.id">
              <TableCell class="font-semibold">{{ item.title }}</TableCell>
              <TableCell><Badge variant="secondary">{{ item.itemType }}</Badge></TableCell>
              <TableCell class="text-slate-500">{{ item.resolvedUrl || item.url || `ref:${item.refId}` }}</TableCell>
              <TableCell>{{ item.sortOrder }}</TableCell>
              <TableCell>{{ item.visible ? '✓' : '—' }}</TableCell>
              <TableCell>
                <div class="flex justify-end gap-2">
                  <ActionButton class="text-blue-600" title="Sửa" @click="editMenuItem(item)">✎</ActionButton>
                  <ActionButton variant="destructive" title="Xóa" @click="handleDeleteItem(item.id)"><Trash2 class="h-4 w-4" /></ActionButton>
                </div>
              </TableCell>
            </TableRow>
            <TableRow v-if="!store.menuItems.length">
              <TableCell colspan="6" class="h-32 text-center text-slate-400">Chưa có mục nào</TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </DataCard>
    </div>

    <div v-if="showMenuForm" class="fixed inset-0 z-[200] flex items-center justify-center bg-black/40" @click.self="showMenuForm = false">
      <div class="flex max-h-[85vh] w-[520px] max-w-[90vw] flex-col rounded-lg bg-white shadow-2xl">
        <div class="flex items-center justify-between border-b border-slate-200 px-5 py-4">
          <h3 class="text-sm font-bold text-slate-900">{{ menuFormMode === 'create' ? 'Thêm menu' : 'Sửa menu' }}</h3>
          <Button variant="ghost" size="icon" @click="showMenuForm = false">&times;</Button>
        </div>
        <div class="flex-1 space-y-4 overflow-y-auto p-5">
          <FormField>
            <FormLabel>Tên menu <span class="text-rose-600">*</span></FormLabel>
            <Input v-model="menuForm.name" placeholder="VD: Main Menu" />
          </FormField>
          <div class="grid gap-4 md:grid-cols-2">
            <FormField>
              <FormLabel>Code <span class="text-rose-600">*</span></FormLabel>
              <Input v-model="menuForm.code" placeholder="VD: main" :disabled="menuFormMode === 'edit'" />
            </FormField>
            <FormField>
              <FormLabel>Vị trí</FormLabel>
              <Input v-model="menuForm.position" placeholder="VD: header" />
            </FormField>
          </div>
        </div>
        <div class="flex justify-end gap-2 border-t border-slate-200 px-5 py-3">
          <Button variant="secondary" @click="showMenuForm = false">Hủy</Button>
          <Button @click="handleSaveMenu">Lưu</Button>
        </div>
      </div>
    </div>

    <div v-if="showItemForm" class="fixed inset-0 z-[200] flex items-center justify-center bg-black/40" @click.self="showItemForm = false">
      <div class="flex max-h-[85vh] w-[520px] max-w-[90vw] flex-col rounded-lg bg-white shadow-2xl">
        <div class="flex items-center justify-between border-b border-slate-200 px-5 py-4">
          <h3 class="text-sm font-bold text-slate-900">{{ itemFormMode === 'create' ? 'Thêm mục menu' : 'Sửa mục menu' }}</h3>
          <Button variant="ghost" size="icon" @click="showItemForm = false">&times;</Button>
        </div>
        <div class="flex-1 space-y-4 overflow-y-auto p-5">
          <div class="grid gap-4 md:grid-cols-2">
            <FormField>
              <FormLabel>Tiêu đề <span class="text-rose-600">*</span></FormLabel>
              <Input v-model="itemForm.title" placeholder="VD: Trang chủ" />
            </FormField>
            <FormField>
              <FormLabel>Loại</FormLabel>
              <Select v-model="itemForm.itemType">
                <option value="PAGE">PAGE</option>
                <option value="POST">POST</option>
                <option value="SERVICE">SERVICE</option>
                <option value="PROJECT">PROJECT</option>
                <option value="EQUIPMENT">EQUIPMENT</option>
                <option value="URL">URL</option>
                <option value="CUSTOM">CUSTOM</option>
                <option value="POST_CATEGORY">POST_CATEGORY</option>
                <option value="SERVICE_CATEGORY">SERVICE_CATEGORY</option>
                <option value="PROJECT_CATEGORY">PROJECT_CATEGORY</option>
              </Select>
            </FormField>
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <FormField v-if="showReferenceSelect">
              <FormLabel>Nội dung liên kết</FormLabel>
              <Select v-model.number="itemForm.refId" @change="handleReferenceChange">
                <option :value="null">Chọn nội dung</option>
                <option v-for="option in currentReferenceOptions" :key="option.id" :value="option.id">{{ option.label }}</option>
              </Select>
            </FormField>
            <FormField v-if="showUrlField">
              <FormLabel>URL</FormLabel>
              <Input v-model="itemForm.url" placeholder="VD: /lien-he" />
            </FormField>
            <FormField v-if="showRefIdField">
              <FormLabel>Ref ID</FormLabel>
              <Input v-model.number="itemForm.refId" type="number" placeholder="ID tham chiếu" />
            </FormField>
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <FormField>
              <FormLabel>Icon</FormLabel>
              <Input v-model="itemForm.icon" placeholder="VD: home" />
            </FormField>
            <FormField>
              <FormLabel>Thứ tự</FormLabel>
              <Input v-model.number="itemForm.sortOrder" type="number" />
            </FormField>
          </div>
          <FormField>
            <label class="flex cursor-pointer items-center gap-2 text-sm text-slate-600">
              <input type="checkbox" v-model="itemForm.visible" /> Hiển thị
            </label>
          </FormField>
        </div>
        <div class="flex justify-end gap-2 border-t border-slate-200 px-5 py-3">
          <Button variant="secondary" @click="showItemForm = false">Hủy</Button>
          <Button @click="handleSaveItem">Lưu</Button>
        </div>
      </div>
    </div>
  </PageShell>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { Trash2 } from 'lucide-vue-next'
import ActionButton from '@/components/shared/ActionButton.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useMenusStore } from '@/stores/menus'
import { equipmentsApi } from '@/features/equipments/api/equipmentsApi'
import { pagesApi } from '@/features/pages/api/pagesApi'
import { postsApi } from '@/features/posts/api/postsApi'
import { projectsApi } from '@/features/projects/api/projectsApi'
import { servicesApi } from '@/features/services/api/servicesApi'
import type { MenuEntity, MenuPayload, MenuItemEntity, MenuItemPayload } from '@/types'

type ReferenceOption = {
  id: number
  label: string
}

const store = useMenusStore()

const activeMenu = ref<MenuEntity | null>(null)
const activeMenuId = ref<number | null>(null)
const alert = ref('')
const alertType = ref<'success' | 'error'>('success')

const showMenuForm = ref(false)
const menuFormMode = ref<'create' | 'edit'>('create')
const menuForm = ref<MenuPayload>({ code: '', name: '', position: '' })
const editingMenuId = ref<number | null>(null)

const showItemForm = ref(false)
const itemFormMode = ref<'create' | 'edit'>('create')
const itemForm = ref<MenuItemPayload>({
  menuId: 0, parentId: null, title: '', itemType: 'PAGE',
  refId: null, url: '', icon: '', sortOrder: 0, visible: true,
})
const editingItemId = ref<number | null>(null)
const pageOptions = ref<ReferenceOption[]>([])
const postOptions = ref<ReferenceOption[]>([])
const serviceOptions = ref<ReferenceOption[]>([])
const projectOptions = ref<ReferenceOption[]>([])
const equipmentOptions = ref<ReferenceOption[]>([])

const referenceDrivenTypes = ['PAGE', 'POST', 'SERVICE', 'PROJECT', 'EQUIPMENT']
const manualUrlTypes = ['URL', 'CUSTOM', 'POST_CATEGORY', 'SERVICE_CATEGORY', 'PROJECT_CATEGORY']

const showReferenceSelect = computed(() => referenceDrivenTypes.includes(itemForm.value.itemType))
const showUrlField = computed(() => manualUrlTypes.includes(itemForm.value.itemType))
const showRefIdField = computed(() => !showReferenceSelect.value && !['URL', 'CUSTOM'].includes(itemForm.value.itemType))
const currentReferenceOptions = computed(() => {
  switch (itemForm.value.itemType) {
    case 'PAGE': return pageOptions.value
    case 'POST': return postOptions.value
    case 'SERVICE': return serviceOptions.value
    case 'PROJECT': return projectOptions.value
    case 'EQUIPMENT': return equipmentOptions.value
    default: return []
  }
})

function resetMenuForm() {
  menuForm.value = { code: '', name: '', position: '' }
  editingMenuId.value = null
}

function resetItemForm() {
  itemForm.value = {
    menuId: activeMenuId.value || 0, parentId: null, title: '', itemType: 'PAGE',
    refId: null, url: '', icon: '', sortOrder: 0, visible: true,
  }
  editingItemId.value = null
}

function handleReferenceChange() {
  if (!showReferenceSelect.value || itemForm.value.refId == null) return
  const selected = currentReferenceOptions.value.find(option => option.id === itemForm.value.refId)
  if (!selected) return
  if (!itemForm.value.title.trim()) {
    itemForm.value.title = selected.label
  }
}

async function loadReferenceOptions() {
  const [pages, posts, services, projects, equipments] = await Promise.all([
    pagesApi.list({ page: 1, size: 100 }),
    postsApi.list({ page: 1, size: 100 }),
    servicesApi.list({ page: 1, size: 100 }),
    projectsApi.list({ page: 1, size: 100 }),
    equipmentsApi.list({ page: 1, size: 100 }),
  ])

  pageOptions.value = pages.data.map(item => ({ id: item.id, label: item.title }))
  postOptions.value = posts.data.map(item => ({ id: item.id, label: item.title }))
  serviceOptions.value = services.data.map(item => ({ id: item.id, label: item.title }))
  projectOptions.value = projects.data.map(item => ({ id: item.id, label: item.title }))
  equipmentOptions.value = equipments.data.map(item => ({ id: item.id, label: item.name }))
}

function flash(msg: string, type: 'success' | 'error' = 'success') {
  alert.value = msg
  alertType.value = type
  setTimeout(() => { alert.value = '' }, 3000)
}

async function selectMenu(menu: MenuEntity) {
  activeMenu.value = menu
  activeMenuId.value = menu.id
  await store.fetchMenuItems(menu.code)
}

function editMenu(menu: MenuEntity) {
  editingMenuId.value = menu.id
  menuForm.value = { code: menu.code, name: menu.name, position: menu.position }
  menuFormMode.value = 'edit'
  showMenuForm.value = true
}

async function handleSaveMenu() {
  try {
    if (menuFormMode.value === 'create') {
      await store.createMenu(menuForm.value)
      flash('Đã thêm menu thành công')
    } else if (editingMenuId.value) {
      await store.updateMenu(editingMenuId.value, menuForm.value)
      flash('Đã cập nhật menu thành công')
    }
    showMenuForm.value = false
    await store.fetchMenus()
    if (activeMenuId.value) {
      const found = store.menus.find(m => m.id === activeMenuId.value)
      if (found) activeMenu.value = found
    }
  } catch {
    flash('Có lỗi xảy ra', 'error')
  }
}

function editMenuItem(item: MenuItemEntity) {
  editingItemId.value = item.id
  itemForm.value = {
    menuId: item.menuId, parentId: item.parentId, title: item.title,
    itemType: item.itemType, refId: item.refId, url: item.url,
    icon: item.icon, sortOrder: item.sortOrder, visible: item.visible,
  }
  itemFormMode.value = 'edit'
  showItemForm.value = true
}

async function handleSaveItem() {
  try {
    if (itemFormMode.value === 'create') {
      await store.createMenuItem(itemForm.value)
      flash('Đã thêm mục thành công')
    } else if (editingItemId.value) {
      await store.updateMenuItem(editingItemId.value, itemForm.value)
      flash('Đã cập nhật mục thành công')
    }
    showItemForm.value = false
    if (activeMenu.value) await store.fetchMenuItems(activeMenu.value.code)
  } catch {
    flash('Có lỗi xảy ra', 'error')
  }
}

async function handleDeleteItem(id: number) {
  if (!confirm('Bạn có chắc muốn xóa mục này?')) return
  try {
    await store.removeMenuItem(id)
    flash('Đã xóa mục thành công')
    if (activeMenu.value) await store.fetchMenuItems(activeMenu.value.code)
  } catch {
    flash('Có lỗi xảy ra', 'error')
  }
}

watch(() => itemForm.value.itemType, (nextType) => {
  if (referenceDrivenTypes.includes(nextType)) {
    itemForm.value.url = ''
  } else {
    itemForm.value.refId = null
  }
})

onMounted(async () => {
  await Promise.all([store.fetchMenus(), loadReferenceOptions()])
})
</script>
