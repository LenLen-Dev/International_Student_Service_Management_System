<template>
  <div class="page-container admission-review-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="84px">
        <el-form-item label="申请编号">
          <el-input v-model.trim="query.applicationNo" placeholder="请输入申请编号" clearable />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model.trim="query.name" placeholder="中英文姓名" clearable />
        </el-form-item>
        <el-form-item label="国籍">
          <el-input v-model.trim="query.nationality" placeholder="请输入国籍" clearable />
        </el-form-item>
        <el-form-item label="申请学院">
          <el-input v-model.trim="query.applyCollege" placeholder="请输入学院" clearable />
        </el-form-item>
        <el-form-item label="申请专业">
          <el-input v-model.trim="query.applyMajor" placeholder="请输入专业" clearable />
        </el-form-item>
        <el-form-item label="申请状态">
          <el-select v-model="query.applicationStatus" placeholder="全部状态" clearable>
            <el-option v-for="item in applicationStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="录取状态">
          <el-select v-model="query.admissionStatus" placeholder="全部状态" clearable>
            <el-option label="未录取" value="PENDING" />
            <el-option label="已录取" value="ADMITTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item class="search-actions">
          <el-button v-permission="'admission:application:list'" type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table v-loading="loading" :data="applications" border stripe>
        <el-table-column prop="applicationNo" label="申请编号" min-width="150" />
        <el-table-column prop="chineseName" label="中文姓名" min-width="110" />
        <el-table-column prop="englishName" label="英文姓名" min-width="160" show-overflow-tooltip />
        <el-table-column label="性别" width="90" align="center">
          <template #default="{ row }">{{ formatOption(genderOptions, row.gender) }}</template>
        </el-table-column>
        <el-table-column prop="nationality" label="国籍" min-width="110" />
        <el-table-column prop="applyCollege" label="申请学院" min-width="140" show-overflow-tooltip />
        <el-table-column prop="applyMajor" label="申请专业" min-width="140" show-overflow-tooltip />
        <el-table-column label="学历层次" width="110" align="center">
          <template #default="{ row }">{{ formatOption(degreeOptions, row.degreeLevel) }}</template>
        </el-table-column>
        <el-table-column label="申请状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="applicationStatusTag(row.applicationStatus)">
              {{ formatOption(applicationStatusOptions, row.applicationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="录取状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="row.admissionStatus === 'ADMITTED' ? 'success' : 'info'">
              {{ row.admissionStatus === 'ADMITTED' ? '已录取' : '未录取' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'admission:application:detail'" link type="primary" :icon="View" @click="openDetail(row.id)">详情</el-button>
            <el-button
              v-permission="'admission:application:return'"
              link
              type="warning"
              :disabled="!canReview(row)"
              @click="openReviewAction(row, 'return')"
            >
              退回
            </el-button>
            <el-button
              v-permission="'admission:application:reject'"
              link
              type="danger"
              :disabled="!canReview(row)"
              @click="openReviewAction(row, 'reject')"
            >
              拒绝
            </el-button>
            <el-button
              v-permission="'admission:application:admit'"
              link
              type="success"
              :disabled="!canReview(row)"
              @click="openReviewAction(row, 'admit')"
            >
              录取
            </el-button>
            <el-button
              v-permission="'admission:notice:generate'"
              link
              type="primary"
              :disabled="row.applicationStatus !== 'ADMITTED'"
              @click="handleGenerateNotice(row)"
            >
              生成通知书
            </el-button>
            <el-button
              v-permission="'admission:notice:download'"
              link
              type="primary"
              :disabled="!['NOTICE_ISSUED', 'ADMITTED'].includes(row.applicationStatus)"
              @click="handleDownloadNotice(row)"
            >
              下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-pagination
          v-model:current-page="pageQuery.pageNum"
          v-model:page-size="pageQuery.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadApplications"
          @current-change="loadApplications"
        />
      </div>
    </el-card>

    <el-drawer v-model="drawerVisible" title="申请详情" size="84%" @closed="resetDetail">
      <div v-loading="detailLoading" class="detail-drawer">
        <template v-if="detail?.application">
          <section class="detail-summary">
            <div>
              <h2>{{ detail.application.chineseName || '-' }} <span>{{ detail.application.englishName }}</span></h2>
              <p>{{ detail.application.applicationNo }} / {{ detail.application.nationality }} / {{ formatOption(applicationStatusOptions, detail.application.applicationStatus) }}</p>
            </div>
            <div class="drawer-actions">
              <el-button v-permission="'admission:application:return'" type="warning" :disabled="!canReview(detail.application)" @click="openReviewAction(detail.application, 'return')">
                退回
              </el-button>
              <el-button v-permission="'admission:application:reject'" type="danger" :disabled="!canReview(detail.application)" @click="openReviewAction(detail.application, 'reject')">
                拒绝
              </el-button>
              <el-button v-permission="'admission:application:admit'" type="success" :disabled="!canReview(detail.application)" @click="openReviewAction(detail.application, 'admit')">
                录取
              </el-button>
            </div>
          </section>

          <el-descriptions :column="3" border class="detail-descriptions">
            <el-descriptions-item label="申请编号">{{ detail.application.applicationNo }}</el-descriptions-item>
            <el-descriptions-item label="中文姓名">{{ detail.application.chineseName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="英文姓名">{{ detail.application.englishName }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ formatOption(genderOptions, detail.application.gender) }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ detail.application.birthDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="国籍">{{ detail.application.nationality }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ detail.application.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ detail.application.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="护照号码">{{ detail.application.passportNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="护照签发国">{{ detail.application.passportCountry || '-' }}</el-descriptions-item>
            <el-descriptions-item label="护照有效期">{{ detail.application.passportExpireDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="申请学院">{{ detail.application.applyCollege || '-' }}</el-descriptions-item>
            <el-descriptions-item label="申请专业">{{ detail.application.applyMajor || '-' }}</el-descriptions-item>
            <el-descriptions-item label="学历层次">{{ formatOption(degreeOptions, detail.application.degreeLevel) }}</el-descriptions-item>
            <el-descriptions-item label="项目类型">{{ formatOption(programOptions, detail.application.programType) }}</el-descriptions-item>
            <el-descriptions-item label="关联档案ID">{{ detail.application.studentProfileId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ detail.application.createTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="教育背景" :span="3">{{ detail.application.educationBackground || '-' }}</el-descriptions-item>
            <el-descriptions-item label="审核意见" :span="3">{{ detail.application.reviewOpinion || '-' }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="3">{{ detail.application.remark || '-' }}</el-descriptions-item>
          </el-descriptions>

          <h3>申请材料</h3>
          <el-table :data="detail.materials" border stripe>
            <el-table-column label="材料类型" min-width="140">
              <template #default="{ row }">{{ formatOption(materialTypeOptions, row.materialType) }}</template>
            </el-table-column>
            <el-table-column prop="fileName" label="文件名" min-width="220" show-overflow-tooltip />
            <el-table-column label="文件大小" width="110" align="center">
              <template #default="{ row }">{{ formatSize(row.fileSize) }}</template>
            </el-table-column>
            <el-table-column label="审核状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="materialStatusTag(row.reviewStatus)">{{ formatOption(materialStatusOptions, row.reviewStatus) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reviewOpinion" label="审核意见" min-width="180" show-overflow-tooltip />
            <el-table-column label="操作" width="170" fixed="right">
              <template #default="{ row }">
                <el-button v-permission="'admission:material:review'" link type="success" @click="openMaterialReview(row, 'APPROVED')">通过</el-button>
                <el-button v-permission="'admission:material:review'" link type="danger" @click="openMaterialReview(row, 'REJECTED')">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>

          <h3>录取通知书</h3>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="通知书编号">{{ detail.notice?.noticeNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="文件名">{{ detail.notice?.fileName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="下载次数">{{ detail.notice?.downloadCount ?? '-' }}</el-descriptions-item>
            <el-descriptions-item label="签发日期">{{ detail.notice?.issueDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="签发人">{{ detail.notice?.issuerName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="生成时间">{{ detail.notice?.createTime || '-' }}</el-descriptions-item>
          </el-descriptions>
          <div class="notice-actions">
            <el-button
              v-permission="'admission:notice:generate'"
              type="primary"
              :disabled="detail.application.applicationStatus !== 'ADMITTED'"
              @click="handleGenerateNotice(detail.application)"
            >
              生成通知书
            </el-button>
            <el-button
              v-permission="'admission:notice:download'"
              type="success"
              :disabled="!detail.notice"
              @click="handleDownloadNotice(detail.application)"
            >
              下载通知书
            </el-button>
          </div>

          <h3>审核记录</h3>
          <el-timeline v-if="detail.reviews.length">
            <el-timeline-item v-for="item in detail.reviews" :key="item.id" :timestamp="item.createTime" placement="top">
              <div class="timeline-title">{{ actionText(item.actionType) }}</div>
              <div class="timeline-text">{{ item.fromStatus || '-' }} -> {{ item.toStatus || '-' }}</div>
              <div class="timeline-text">{{ item.opinion || '无意见' }}</div>
              <div class="timeline-user">{{ item.operatorName || '-' }}</div>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无审核记录" />
        </template>
      </div>
    </el-drawer>

    <el-dialog v-model="reviewDialogVisible" :title="reviewTitle" width="520px" @closed="resetReviewDialog">
      <el-form ref="reviewFormRef" :model="reviewForm" :rules="reviewRules" label-width="86px">
        <el-form-item label="申请编号">
          <el-input :model-value="currentApplication?.applicationNo" disabled />
        </el-form-item>
        <el-form-item label="审核意见" prop="opinion">
          <el-input v-model.trim="reviewForm.opinion" type="textarea" :rows="4" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitReviewAction">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="materialReviewDialogVisible" title="材料审核" width="520px" @closed="resetMaterialReviewDialog">
      <el-form ref="materialReviewFormRef" :model="materialReviewForm" :rules="materialReviewRules" label-width="86px">
        <el-form-item label="文件名">
          <el-input :model-value="currentMaterial?.fileName" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="reviewStatus">
          <el-radio-group v-model="materialReviewForm.reviewStatus">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="reviewOpinion">
          <el-input v-model.trim="materialReviewForm.reviewOpinion" type="textarea" :rows="4" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="materialReviewDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitMaterialReview">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Refresh, Search, View } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import {
  admitAdmissionApplication,
  downloadAdmissionNotice,
  generateAdmissionNotice,
  getAdmissionApplicationDetail,
  getAdmissionApplicationPage,
  rejectAdmissionApplication,
  returnAdmissionApplication,
  reviewAdmissionMaterial
} from '@/api/admission/application'
import type { DictOption } from '@/types/config'
import type {
  AdmissionApplication,
  AdmissionApplicationDetail,
  AdmissionApplicationQuery,
  AdmissionMaterial,
  AdmissionMaterialReviewForm,
  AdmissionReviewForm
} from '@/types/admission'

type ReviewAction = 'return' | 'reject' | 'admit'

const fallbackGenderOptions: DictOption[] = [
  { label: '男', value: 'MALE' },
  { label: '女', value: 'FEMALE' },
  { label: '未知', value: 'UNKNOWN' }
]
const fallbackApplicationStatusOptions: DictOption[] = [
  { label: '草稿', value: 'DRAFT', tagType: 'info' },
  { label: '已提交', value: 'SUBMITTED', tagType: 'primary' },
  { label: '审核中', value: 'UNDER_REVIEW', tagType: 'warning' },
  { label: '已退回', value: 'RETURNED', tagType: 'warning' },
  { label: '已拒绝', value: 'REJECTED', tagType: 'danger' },
  { label: '已录取', value: 'ADMITTED', tagType: 'success' },
  { label: '已发通知书', value: 'NOTICE_ISSUED', tagType: 'success' }
]
const fallbackMaterialTypeOptions: DictOption[] = [
  { label: '护照', value: 'PASSPORT' },
  { label: '照片', value: 'PHOTO' },
  { label: '学历证明', value: 'DEGREE_CERTIFICATE' },
  { label: '语言成绩', value: 'LANGUAGE_SCORE' },
  { label: '体检证明', value: 'PHYSICAL_EXAM' },
  { label: '其他材料', value: 'OTHER' }
]
const fallbackMaterialStatusOptions: DictOption[] = [
  { label: '待审核', value: 'PENDING', tagType: 'warning' },
  { label: '已通过', value: 'APPROVED', tagType: 'success' },
  { label: '已拒绝', value: 'REJECTED', tagType: 'danger' }
]
const fallbackDegreeOptions: DictOption[] = [
  { label: '本科', value: 'BACHELOR' },
  { label: '硕士', value: 'MASTER' },
  { label: '博士', value: 'DOCTOR' }
]
const fallbackProgramOptions: DictOption[] = [
  { label: '学历项目', value: 'DEGREE' },
  { label: '交换项目', value: 'EXCHANGE' },
  { label: '语言项目', value: 'LANGUAGE' }
]

const query = reactive<AdmissionApplicationQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const dateRange = ref<string[]>([])
const applications = ref<AdmissionApplication[]>([])
const total = ref(0)
const loading = ref(false)
const detailLoading = ref(false)
const submitLoading = ref(false)
const drawerVisible = ref(false)
const detail = ref<AdmissionApplicationDetail>()
const currentApplication = ref<AdmissionApplication>()
const currentMaterial = ref<AdmissionMaterial>()
const reviewAction = ref<ReviewAction>('return')
const reviewDialogVisible = ref(false)
const materialReviewDialogVisible = ref(false)
const reviewFormRef = ref<FormInstance>()
const materialReviewFormRef = ref<FormInstance>()
const genderOptions = ref<DictOption[]>(fallbackGenderOptions)
const applicationStatusOptions = ref<DictOption[]>(fallbackApplicationStatusOptions)
const materialTypeOptions = ref<DictOption[]>(fallbackMaterialTypeOptions)
const materialStatusOptions = ref<DictOption[]>(fallbackMaterialStatusOptions)
const degreeOptions = ref<DictOption[]>(fallbackDegreeOptions)
const programOptions = ref<DictOption[]>(fallbackProgramOptions)

const reviewForm = reactive<AdmissionReviewForm>({ opinion: '' })
const materialReviewForm = reactive<AdmissionMaterialReviewForm>({ reviewStatus: 'APPROVED', reviewOpinion: '' })
const reviewRules: FormRules<AdmissionReviewForm> = {
  opinion: [{ required: true, message: '请输入审核意见', trigger: 'blur' }]
}
const materialReviewRules: FormRules<AdmissionMaterialReviewForm> = {
  reviewStatus: [{ required: true, message: '请选择审核结果', trigger: 'change' }]
}

const reviewTitle = computed(() => {
  const map: Record<ReviewAction, string> = { return: '退回申请', reject: '拒绝申请', admit: '录取申请' }
  return map[reviewAction.value]
})

async function loadOptions(code: string, fallback: DictOption[], target: typeof genderOptions) {
  try {
    const options = await getDictOptions(code)
    target.value = options.length ? options : fallback
  } catch {
    target.value = fallback
  }
}

async function loadDictionaries() {
  await Promise.all([
    loadOptions('gender', fallbackGenderOptions, genderOptions),
    loadOptions('admission_application_status', fallbackApplicationStatusOptions, applicationStatusOptions),
    loadOptions('admission_material_type', fallbackMaterialTypeOptions, materialTypeOptions),
    loadOptions('admission_material_review_status', fallbackMaterialStatusOptions, materialStatusOptions),
    loadOptions('admission_degree_level', fallbackDegreeOptions, degreeOptions),
    loadOptions('admission_program_type', fallbackProgramOptions, programOptions)
  ])
}

function buildQuery(): AdmissionApplicationQuery {
  return {
    ...query,
    pageNum: pageQuery.pageNum,
    pageSize: pageQuery.pageSize,
    applicationStatus: query.applicationStatus || undefined,
    admissionStatus: query.admissionStatus || undefined,
    startTime: dateRange.value?.[0],
    endTime: dateRange.value?.[1]
  }
}

async function loadApplications() {
  loading.value = true
  try {
    const result = await getAdmissionApplicationPage(buildQuery())
    applications.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadApplications()
}

function handleReset() {
  Object.assign(query, {
    applicationNo: '',
    name: '',
    nationality: '',
    applyCollege: '',
    applyMajor: '',
    applicationStatus: '',
    admissionStatus: ''
  })
  dateRange.value = []
  pageQuery.pageNum = 1
  loadApplications()
}

async function openDetail(id: number) {
  drawerVisible.value = true
  detailLoading.value = true
  try {
    detail.value = await getAdmissionApplicationDetail(id)
  } finally {
    detailLoading.value = false
  }
}

function resetDetail() {
  detail.value = undefined
}

function canReview(row: AdmissionApplication) {
  return ['SUBMITTED', 'UNDER_REVIEW'].includes(row.applicationStatus)
}

function openReviewAction(row: AdmissionApplication, action: ReviewAction) {
  currentApplication.value = row
  reviewAction.value = action
  reviewForm.opinion = action === 'admit' ? '符合录取条件，予以录取。' : ''
  reviewDialogVisible.value = true
}

function resetReviewDialog() {
  currentApplication.value = undefined
  reviewForm.opinion = ''
  reviewFormRef.value?.clearValidate()
}

async function submitReviewAction() {
  await reviewFormRef.value?.validate()
  if (!currentApplication.value) return
  submitLoading.value = true
  try {
    const id = currentApplication.value.id
    if (reviewAction.value === 'return') {
      await returnAdmissionApplication(id, reviewForm)
      ElMessage.success('申请已退回')
    } else if (reviewAction.value === 'reject') {
      await rejectAdmissionApplication(id, reviewForm)
      ElMessage.success('申请已拒绝')
    } else {
      await admitAdmissionApplication(id, reviewForm)
      ElMessage.success('申请已录取，已同步生成留学生档案')
    }
    reviewDialogVisible.value = false
    await loadApplications()
    if (drawerVisible.value) {
      await openDetail(id)
    }
  } finally {
    submitLoading.value = false
  }
}

function openMaterialReview(row: AdmissionMaterial, status: string) {
  currentMaterial.value = row
  materialReviewForm.reviewStatus = status
  materialReviewForm.reviewOpinion = status === 'APPROVED' ? '材料审核通过。' : ''
  materialReviewDialogVisible.value = true
}

function resetMaterialReviewDialog() {
  currentMaterial.value = undefined
  materialReviewForm.reviewStatus = 'APPROVED'
  materialReviewForm.reviewOpinion = ''
  materialReviewFormRef.value?.clearValidate()
}

async function submitMaterialReview() {
  await materialReviewFormRef.value?.validate()
  if (!currentMaterial.value) return
  submitLoading.value = true
  try {
    await reviewAdmissionMaterial(currentMaterial.value.id, materialReviewForm)
    ElMessage.success('材料审核结果已保存')
    materialReviewDialogVisible.value = false
    if (detail.value?.application?.id) {
      await openDetail(detail.value.application.id)
    }
  } finally {
    submitLoading.value = false
  }
}

async function handleGenerateNotice(row: AdmissionApplication) {
  await ElMessageBox.confirm(`确认生成申请「${row.applicationNo}」的录取通知书吗？`, '生成确认', { type: 'warning' })
  await generateAdmissionNotice(row.id)
  ElMessage.success('录取通知书已生成')
  await loadApplications()
  if (drawerVisible.value) {
    await openDetail(row.id)
  }
}

async function handleDownloadNotice(row: AdmissionApplication) {
  const blob = await downloadAdmissionNotice(row.id)
  const filename = detail.value?.notice?.fileName || `${row.applicationNo}-admission-notice.pdf`
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.click()
  URL.revokeObjectURL(url)
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

function applicationStatusTag(status?: string) {
  return optionTag(applicationStatusOptions.value, status)
}

function materialStatusTag(status?: string) {
  return optionTag(materialStatusOptions.value, status)
}

function formatSize(size?: number) {
  if (!size) return '-'
  if (size < 1024) return `${size} B`
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`
  return `${(size / 1024 / 1024).toFixed(1)} MB`
}

function actionText(action: string) {
  const map: Record<string, string> = {
    CREATE: '创建申请',
    UPDATE: '更新申请',
    SUBMIT: '提交申请',
    RETURN: '退回申请',
    REJECT: '拒绝申请',
    ADMIT: '录取申请',
    GENERATE_NOTICE: '生成录取通知书'
  }
  return map[action] || action
}

onMounted(async () => {
  await loadDictionaries()
  await loadApplications()
})
</script>

<style scoped lang="scss">
.admission-review-page {
  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }

  .detail-summary {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
    padding-bottom: 16px;

    h2 {
      margin: 0 0 8px;
      font-size: 22px;
      font-weight: 650;

      span {
        color: #667085;
        font-size: 16px;
        font-weight: 500;
      }
    }

    p {
      margin: 0;
      color: #667085;
    }
  }

  .drawer-actions,
  .notice-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 8px;
  }

  .detail-descriptions {
    margin-bottom: 20px;
  }

  h3 {
    margin: 22px 0 12px;
    color: #1f2937;
    font-size: 16px;
  }

  .notice-actions {
    margin-top: 12px;
  }

  .timeline-title {
    color: #1f2937;
    font-weight: 600;
  }

  .timeline-text,
  .timeline-user {
    margin-top: 6px;
    color: #667085;
    font-size: 13px;
  }
}

@media (max-width: 900px) {
  .admission-review-page {
    .detail-summary {
      align-items: flex-start;
      flex-direction: column;
    }
  }
}
</style>
