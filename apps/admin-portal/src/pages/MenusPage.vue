<template>
  <div>
    <PageHeader title="Quản lý Menu" description="Quản lý menu điều hướng của website">
      <template #actions>
        <button @click="showMenuForm = true; menuFormMode = 'create'; resetMenuForm()" class="cms-btn cms-btn-primary cms-btn-sm">＋ Thêm menu</button>
      </template>
    </PageHeader>

    <AlertBox v-if="store.loading" message="Đang tải dữ liệu..." />
    <AlertBox v-if="alert" :message="alert" :type="alertType" />

    <div class="mt-6 grid gap-6" style="grid-template-columns: 280px 1fr;">
      <div class="cms-list-panel">
        <div class="cms-list-panel-header">Danh sách menu</div>
        <div v-if="store.menus.length === 0" class="cms-empty-state" style="padding:30px">
          <p>Chưa có menu</p>
        </div>
        <div
          v-for="menu in store.menus"
          :key="menu.id"
          class="cms-list-panel-item"
          :style="{ background: activeMenuId === menu.id ? '#f0fdf4' : '', fontWeight: activeMenuId === menu.id ? '600' : '' }"
          @click="selectMenu(menu)"
        >
          <div>
            <div>{{ menu.name }}</div>
            <div class="item-sub">{{ menu.code }} &middot; {{ menu.position }}</div>
          </div>
          <div class="cms-actions-cell">
            <button @click.stop="editMenu(menu)" class="cms-act-btn edit" title="Sửa">&#9998;</button>
          </div>
        </div>
      </div>

      <div class="cms-card">
        <div class="cms-card-header">
          <span style="font-weight:600;font-size:14px;">
            {{ activeMenu ? `Menu items: ${activeMenu.name}` : 'Chọn menu bên trái' }}
          </span>
          <button
            v-if="activeMenu"
            @click="showItemForm = true; itemFormMode = 'create'; resetItemForm()"
            class="cms-btn cms-btn-primary cms-btn-sm"
          >＋ Thêm item</button>
        </div>
        <div v-if="!activeMenu" class="cms-empty-state"><p>Chọn một menu để xem các mục</p></div>
        <div v-else class="cms-table-wrap">
          <table class="cms-table">
            <thead>
              <tr><th>Tiêu đề</th><th>Loại</th><th>URL / Ref</th><th>Thứ tự</th><th>Hiện</th><th style="width:100px">Thao tác</th></tr>
            </thead>
            <tbody>
              <tr v-for="item in store.menuItems" :key="item.id">
                <td class="font-semibold">{{ item.title }}</td>
                <td><span class="cms-badge cms-badge-blue">{{ item.itemType }}</span></td>
                <td class="text-gray-500">{{ item.resolvedUrl || item.url || `ref:${item.refId}` }}</td>
                <td>{{ item.sortOrder }}</td>
                <td>{{ item.visible ? '✓' : '—' }}</td>
                <td>
                  <div class="cms-actions-cell">
                    <button @click="editMenuItem(item)" class="cms-act-btn edit" title="Sửa">&#9998;</button>
                    <button @click="handleDeleteItem(item.id)" class="cms-act-btn delete" title="Xóa">&#10005;</button>
                  </div>
                </td>
              </tr>
              <tr v-if="!store.menuItems.length"><td colspan="6" style="text-align:center;padding:40px;color:#9ca3af">Chưa có mục nào</td></tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div v-if="showMenuForm" class="cms-modal-overlay" @click.self="showMenuForm = false">
      <div class="cms-modal">
        <div class="cms-modal-header">
          <h3>{{ menuFormMode === 'create' ? 'Thêm menu' : 'Sửa menu' }}</h3>
          <button @click="showMenuForm = false" class="cms-modal-close">&times;</button>
        </div>
        <div class="cms-modal-body">
          <div class="cms-form-group">
            <label class="cms-form-label">Tên menu <span class="required">*</span></label>
            <input v-model="menuForm.name" class="cms-form-control" placeholder="VD: Main Menu" />
          </div>
          <div class="cms-form-row">
            <div class="cms-form-group">
              <label class="cms-form-label">Code <span class="required">*</span></label>
              <input v-model="menuForm.code" class="cms-form-control" placeholder="VD: main" :disabled="menuFormMode === 'edit'" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Vị trí</label>
              <input v-model="menuForm.position" class="cms-form-control" placeholder="VD: header" />
            </div>
          </div>
        </div>
        <div class="cms-modal-footer">
          <button @click="showMenuForm = false" class="cms-btn cms-btn-secondary">Hủy</button>
          <button @click="handleSaveMenu" class="cms-btn cms-btn-primary">Lưu</button>
        </div>
      </div>
    </div>

    <div v-if="showItemForm" class="cms-modal-overlay" @click.self="showItemForm = false">
      <div class="cms-modal">
        <div class="cms-modal-header">
          <h3>{{ itemFormMode === 'create' ? 'Thêm mục menu' : 'Sửa mục menu' }}</h3>
          <button @click="showItemForm = false" class="cms-modal-close">&times;</button>
        </div>
        <div class="cms-modal-body">
          <div class="cms-form-row">
            <div class="cms-form-group">
              <label class="cms-form-label">Tiêu đề <span class="required">*</span></label>
              <input v-model="itemForm.title" class="cms-form-control" placeholder="VD: Trang chủ" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Loại</label>
              <select v-model="itemForm.itemType" class="cms-form-control">
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
              </select>
            </div>
          </div>
          <div class="cms-form-row">
            <div v-if="showReferenceSelect" class="cms-form-group">
              <label class="cms-form-label">Nội dung liên kết</label>
              <select v-model.number="itemForm.refId" class="cms-form-control" @change="handleReferenceChange">
                <option :value="null">Chọn nội dung</option>
                <option v-for="option in currentReferenceOptions" :key="option.id" :value="option.id">{{ option.label }}</option>
              </select>
            </div>
            <div v-if="showUrlField" class="cms-form-group">
              <label class="cms-form-label">URL</label>
              <input v-model="itemForm.url" class="cms-form-control" placeholder="VD: /lien-he" />
            </div>
            <div v-if="showRefIdField" class="cms-form-group">
              <label class="cms-form-label">Ref ID</label>
              <input v-model.number="itemForm.refId" type="number" class="cms-form-control" placeholder="ID tham chiếu" />
            </div>
          </div>
          <div class="cms-form-row">
            <div class="cms-form-group">
              <label class="cms-form-label">Icon</label>
              <input v-model="itemForm.icon" class="cms-form-control" placeholder="VD: home" />
            </div>
            <div class="cms-form-group">
              <label class="cms-form-label">Thứ tự</label>
              <input v-model.number="itemForm.sortOrder" type="number" class="cms-form-control" />
            </div>
          </div>
          <div class="cms-form-group">
            <label class="cms-form-check">
              <input type="checkbox" v-model="itemForm.visible" /> Hiển thị
            </label>
          </div>
        </div>
        <div class="cms-modal-footer">
          <button @click="showItemForm = false" class="cms-btn cms-btn-secondary">Hủy</button>
          <button @click="handleSaveItem" class="cms-btn cms-btn-primary">Lưu</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { useMenusStore } from '@/stores/menus'
import { equipmentsApi, pagesApi, postsApi, projectsApi, servicesApi } from '@/lib/api'
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

<style scoped>
.cms-modal-overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.4); z-index: 200;
  display: flex; align-items: center; justify-content: center;
}
.cms-modal {
  background: #fff; border-radius: 12px; width: 520px; max-width: 90vw;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15); max-height: 85vh; display: flex; flex-direction: column;
}
.cms-modal-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 16px 20px; border-bottom: 1px solid #e2e8f0;
}
.cms-modal-header h3 { font-size: 15px; font-weight: 700; color: #0f172a; }
.cms-modal-close {
  width: 28px; height: 28px; border: none; background: none; font-size: 18px;
  cursor: pointer; color: #94a3b8; border-radius: 6px; display: flex;
  align-items: center; justify-content: center;
}
.cms-modal-close:hover { background: #f1f5f9; color: #0f172a; }
.cms-modal-body { padding: 20px; overflow-y: auto; flex: 1; }
.cms-modal-footer {
  display: flex; justify-content: flex-end; gap: 8px;
  padding: 12px 20px; border-top: 1px solid #e2e8f0;
}
.cms-form-check {
  display: flex; align-items: center; gap: 8px; font-size: 13px; color: #475569; cursor: pointer;
}
.cms-form-check input { width: 16px; height: 16px; accent-color: #059669; }
</style>
