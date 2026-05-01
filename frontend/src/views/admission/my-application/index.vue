<template>
  <div class="page-container admission-my-page">
    <el-card class="summary-card">
      <div class="summary">
        <div>
          <h2>我的招生申请</h2>
          <p>{{ application?.applicationNo || '尚未创建申请' }}</p>
        </div>
        <div class="summary-status">
          <el-tag :type="applicationStatusTag(application?.applicationStatus)" size="large">
            {{ formatOption(applicationStatusOptions, application?.applicationStatus || 'DRAFT') }}
          </el-tag>
          <el-tag v-if="application?.admissionStatus" :type="admissionStatusTag(application.admissionStatus)" size="large">
            {{ application.admissionStatus === 'ADMITTED' ? '已录取' : '未录取' }}
          </el-tag>
        </div>
      </div>
      <el-alert
        v-if="application?.reviewOpinion"
        class="review-alert"
        type="warning"
        :closable="false"
        show-icon
        :title="application.reviewOpinion"
      />
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>申请信息</span>
          <div class="header-actions">
            <el-button v-if="application?.applicationStatus === 'REJECTED'" :icon="Refresh" @click="startNewApplication">重新创建</el-button>
            <el-button
              v-permission="'admission:application:create'"
              type="primary"
              :icon="DocumentChecked"
              :loading="submitLoading"
              :disabled="!formEditable"
              @click="saveDraft"
            >
              保存草稿
            </el-button>
            <el-button
              v-permission="'admission:application:submit'"
              type="success"
              :icon="Promotion"
              :loading="submitLoading"
              :disabled="!canSubmit"
              @click="submitApplication"
            >
              提交申请
            </el-button>
          </div>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="108px" :disabled="!formEditable">
        <el-divider content-position="left">基础信息</el-divider>
        <div class="form-grid">
          <el-form-item label="中文姓名" prop="chineseName">
            <el-input v-model.trim="form.chineseName" placeholder="请输入中文姓名" />
          </el-form-item>
          <el-form-item label="英文姓名" prop="englishName">
            <el-input v-model.trim="form.englishName" placeholder="请输入英文姓名" />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" placeholder="请选择性别">
              <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择出生日期" />
          </el-form-item>
          <el-form-item label="国籍" prop="nationality">
            <el-input v-model.trim="form.nationality" placeholder="请输入国籍" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model.trim="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model.trim="form.phone" placeholder="请输入手机号" />
          </el-form-item>
        </div>

        <el-divider content-position="left">护照信息</el-divider>
        <div class="form-grid">
          <el-form-item label="护照号码" prop="passportNo">
            <el-input v-model.trim="form.passportNo" placeholder="请输入护照号码" />
          </el-form-item>
          <el-form-item label="签发国家" prop="passportCountry">
            <el-input v-model.trim="form.passportCountry" placeholder="请输入护照签发国家" />
          </el-form-item>
          <el-form-item label="护照有效期" prop="passportExpireDate">
            <el-date-picker v-model="form.passportExpireDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择护照有效期" />
          </el-form-item>
        </div>

        <el-divider content-position="left">申请志愿</el-divider>
        <div class="form-grid">
          <el-form-item label="申请学院" prop="applyCollege">
            <el-input v-model.trim="form.applyCollege" placeholder="请输入申请学院" />
          </el-form-item>
          <el-form-item label="申请专业" prop="applyMajor">
            <el-input v-model.trim="form.applyMajor" placeholder="请输入申请专业" />
          </el-form-item>
          <el-form-item label="学历层次" prop="degreeLevel">
            <el-select v-model="form.degreeLevel" placeholder="请选择学历层次" clearable>
              <el-option v-for="item in degreeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="项目类型" prop="programType">
            <el-select v-model="form.programType" placeholder="请选择项目类型" clearable>
              <el-option v-for="item in programOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="教育背景" prop="educationBackground">
          <el-input v-model.trim="form.educationBackground" type="textarea" :rows="3" placeholder="请填写最高学历、毕业院校、专业等摘要" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model.trim="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>申请材料</span>
          <div class="upload-actions">
            <el-select v-model="uploadMaterialType" placeholder="材料类型" :disabled="!canUpload" class="material-select">
              <el-option v-for="item in materialTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
            <el-upload
              :show-file-list="false"
              :disabled="!canUpload"
              :http-request="handleUpload"
              :before-upload="beforeUpload"
              accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
            >
              <el-button v-permission="'admission:material:upload'" type="primary" :icon="Upload" :loading="uploadLoading" :disabled="!canUpload">
                上传材料
              </el-button>
            </el-upload>
          </div>
        </div>
      </template>

      <el-alert v-if="!application" type="info" :closable="false" show-icon title="请先保存草稿，再上传申请材料。" />
      <el-table v-loading="loading" :data="materials" border stripe>
        <el-table-column label="材料类型" min-width="140">
          <template #default="{ row }">{{ formatOption(materialTypeOptions, row.materialType) }}</template>
        </el-table-column>
        <el-table-column prop="fileName" label="文件名" min-width="220" show-overflow-tooltip />
        <el-table-column label="文件大小" width="120" align="center">
          <template #default="{ row }">{{ formatSize(row.fileSize) }}</template>
        </el-table-column>
        <el-table-column label="审核状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="materialStatusTag(row.reviewStatus)">{{ formatOption(materialStatusOptions, row.reviewStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewOpinion" label="审核意见" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="上传时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button
              v-permission="'admission:material:delete'"
              link
              type="danger"
              :disabled="!formEditable"
              @click="deleteMaterial(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>审核记录与录取通知书</span>
          <el-button
            v-if="notice"
            v-permission="'admission:notice:download'"
            type="success"
            :icon="Download"
            @click="downloadNotice"
          >
            下载录取通知书
          </el-button>
        </div>
      </template>
      <el-timeline v-if="reviews.length">
        <el-timeline-item v-for="item in reviews" :key="item.id" :timestamp="item.createTime" placement="top">
          <div class="timeline-title">{{ actionText(item.actionType) }}</div>
          <div class="timeline-text">{{ item.opinion || '无备注' }}</div>
          <div class="timeline-user">{{ item.operatorName || '-' }}</div>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无审核记录" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules, type UploadRequestOptions, type UploadRawFile } from 'element-plus'
import { DocumentChecked, Download, Promotion, Refresh, Upload } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import {
  createMyAdmissionApplication,
  deleteMyAdmissionMaterial,
  downloadMyAdmissionNotice,
  getMyAdmissionApplication,
  submitMyAdmissionApplication,
  updateMyAdmissionApplication,
  uploadMyAdmissionMaterial
} from '@/api/admission/application'
import type { DictOption } from '@/types/config'
import type { AdmissionApplication, AdmissionApplicationForm, AdmissionMaterial, AdmissionNotice, AdmissionReviewRecord } from '@/types/admission'

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

const formRef = ref<FormInstance>()
const loading = ref(false)
const submitLoading = ref(false)
const uploadLoading = ref(false)
const application = ref<AdmissionApplication>()
const materials = ref<AdmissionMaterial[]>([])
const reviews = ref<AdmissionReviewRecord[]>([])
const notice = ref<AdmissionNotice>()
const genderOptions = ref<DictOption[]>(fallbackGenderOptions)
const applicationStatusOptions = ref<DictOption[]>(fallbackApplicationStatusOptions)
const materialTypeOptions = ref<DictOption[]>(fallbackMaterialTypeOptions)
const materialStatusOptions = ref<DictOption[]>(fallbackMaterialStatusOptions)
const degreeOptions = ref<DictOption[]>(fallbackDegreeOptions)
const programOptions = ref<DictOption[]>(fallbackProgramOptions)
const uploadMaterialType = ref('PASSPORT')

function createDefaultForm(): AdmissionApplicationForm {
  return {
    chineseName: '',
    englishName: '',
    gender: 'UNKNOWN',
    birthDate: '',
    nationality: '',
    email: '',
    phone: '',
    passportNo: '',
    passportCountry: '',
    passportExpireDate: '',
    applyCollege: '',
    applyMajor: '',
    degreeLevel: '',
    programType: '',
    educationBackground: '',
    remark: ''
  }
}

const form = reactive<AdmissionApplicationForm>(createDefaultForm())
const rules: FormRules<AdmissionApplicationForm> = {
  englishName: [{ required: true, message: '请输入英文姓名', trigger: 'blur' }],
  nationality: [{ required: true, message: '请输入国籍', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}

const formEditable = computed(() => !application.value || ['DRAFT', 'RETURNED'].includes(application.value.applicationStatus))
const canSubmit = computed(() => Boolean(application.value?.id && formEditable.value))
const canUpload = computed(() => Boolean(application.value?.id && formEditable.value))

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

function fillForm(data?: AdmissionApplication) {
  Object.assign(form, createDefaultForm(), data || {})
}

async function loadMyApplication() {
  loading.value = true
  try {
    const detail = await getMyAdmissionApplication()
    application.value = detail?.application
    materials.value = detail?.materials || []
    reviews.value = detail?.reviews || []
    notice.value = detail?.notice
    fillForm(application.value)
  } finally {
    loading.value = false
  }
}

async function saveDraft() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (application.value?.id && application.value.applicationStatus !== 'REJECTED') {
      await updateMyAdmissionApplication(form)
      ElMessage.success('申请草稿已保存')
    } else {
      await createMyAdmissionApplication(form)
      ElMessage.success('申请草稿已创建')
    }
    await loadMyApplication()
  } finally {
    submitLoading.value = false
  }
}

async function submitApplication() {
  await formRef.value?.validate()
  const materialTypes = materials.value.map((item) => item.materialType)
  if (!materialTypes.includes('PASSPORT') || !materialTypes.includes('PHOTO')) {
    ElMessage.warning('请至少上传护照和照片材料后再提交')
    return
  }
  await ElMessageBox.confirm('提交后申请信息将进入审核，确认提交吗？', '提交确认', { type: 'warning' })
  submitLoading.value = true
  try {
    await updateMyAdmissionApplication(form)
    await submitMyAdmissionApplication()
    ElMessage.success('申请已提交')
    await loadMyApplication()
  } finally {
    submitLoading.value = false
  }
}

function startNewApplication() {
  application.value = undefined
  materials.value = []
  reviews.value = []
  notice.value = undefined
  fillForm()
}

function beforeUpload(file: UploadRawFile) {
  const allowed = [
    'application/pdf',
    'image/jpeg',
    'image/png',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
  ]
  if (!allowed.includes(file.type)) {
    ElMessage.error('仅支持 PDF、JPG、PNG、DOC、DOCX 文件')
    return false
  }
  if (file.size > 20 * 1024 * 1024) {
    ElMessage.error('单个材料不能超过 20MB')
    return false
  }
  return true
}

async function handleUpload(options: UploadRequestOptions) {
  if (!application.value?.id) {
    ElMessage.warning('请先保存草稿')
    return
  }
  uploadLoading.value = true
  try {
    await uploadMyAdmissionMaterial(uploadMaterialType.value, options.file as File)
    ElMessage.success('材料已上传')
    await loadMyApplication()
  } finally {
    uploadLoading.value = false
  }
}

async function deleteMaterial(row: AdmissionMaterial) {
  await ElMessageBox.confirm(`确认删除材料「${row.fileName}」吗？`, '删除确认', { type: 'warning' })
  await deleteMyAdmissionMaterial(row.id)
  ElMessage.success('材料已删除')
  await loadMyApplication()
}

async function downloadNotice() {
  const blob = await downloadMyAdmissionNotice()
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = notice.value?.fileName || 'admission-notice.pdf'
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
  return optionTag(applicationStatusOptions.value, status || 'DRAFT')
}

function materialStatusTag(status?: string) {
  return optionTag(materialStatusOptions.value, status)
}

function admissionStatusTag(status?: string) {
  return status === 'ADMITTED' ? 'success' : 'info'
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
  await loadMyApplication()
})
</script>

<style scoped lang="scss">
.admission-my-page {
  .summary-card {
    border: 0;
  }

  .summary {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;

    h2 {
      margin: 0 0 8px;
      font-size: 22px;
      font-weight: 650;
    }

    p {
      margin: 0;
      color: #667085;
    }
  }

  .summary-status,
  .header-actions,
  .upload-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
  }

  .review-alert {
    margin-top: 14px;
  }

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
  }

  .form-grid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 0 16px;
  }

  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }

  .material-select {
    width: 180px;
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
  .admission-my-page {
    .form-grid {
      grid-template-columns: 1fr;
    }

    .summary,
    .card-header {
      align-items: flex-start;
      flex-direction: column;
    }
  }
}
</style>
