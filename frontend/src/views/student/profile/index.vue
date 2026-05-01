<template>
  <div class="page-container student-profile-page">
    <el-card class="search-card">
      <el-form class="profile-search-form" :model="searchForm" label-width="84px">
        <el-form-item label="学号">
          <el-input v-model.trim="searchForm.studentNo" placeholder="请输入学号" clearable />
        </el-form-item>
        <el-form-item label="中文姓名">
          <el-input v-model.trim="searchForm.chineseName" placeholder="请输入中文姓名" clearable />
        </el-form-item>
        <el-form-item label="英文姓名">
          <el-input v-model.trim="searchForm.englishName" placeholder="请输入英文姓名" clearable />
        </el-form-item>
        <el-form-item label="国籍">
          <el-input v-model.trim="searchForm.nationality" placeholder="请输入国籍" clearable />
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model.trim="searchForm.college" placeholder="请输入学院" clearable />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model.trim="searchForm.major" placeholder="请输入专业" clearable />
        </el-form-item>
        <el-form-item label="学生状态">
          <el-select v-model="searchForm.studentStatus" placeholder="全部状态" clearable>
            <el-option v-for="item in studentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item class="search-actions">
          <el-button v-permission="'student:profile:list'" type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button v-permission="'student:profile:add'" type="primary" :icon="Plus" @click="openCreateDialog">新增档案</el-button>
          <el-button type="success" plain :icon="User" @click="openMyProfile">我的档案</el-button>
        </div>
      </div>

      <el-table v-loading="tableLoading" :data="profiles" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="130" />
        <el-table-column prop="chineseName" label="中文姓名" min-width="120" />
        <el-table-column prop="englishName" label="英文姓名" min-width="170" show-overflow-tooltip />
        <el-table-column prop="gender" label="性别" width="90" align="center">
          <template #default="{ row }">{{ formatOption(genderOptions, row.gender) }}</template>
        </el-table-column>
        <el-table-column prop="nationality" label="国籍" min-width="120" />
        <el-table-column prop="college" label="学院" min-width="140" show-overflow-tooltip />
        <el-table-column prop="major" label="专业" min-width="150" show-overflow-tooltip />
        <el-table-column prop="degreeLevel" label="学历层次" min-width="110" />
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="studentStatus" label="学生状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="studentStatusTag(row.studentStatus)">{{ formatOption(studentStatusOptions, row.studentStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="启用状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag class="status-tag" :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="340" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'student:profile:detail'" link type="primary" :icon="View" @click="openDetail(row.id)">详情</el-button>
            <el-button v-permission="'student:profile:update'" link type="primary" :icon="Edit" @click="openEditDialog(row.id)">编辑</el-button>
            <el-button v-permission="'student:profile:status'" link type="warning" @click="openStatusDialog(row)">状态</el-button>
            <el-button v-permission="'student:profile:delete'" link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
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
          @size-change="loadProfiles"
          @current-change="loadProfiles"
        />
      </div>
    </el-card>

    <el-dialog v-model="profileDialogVisible" :title="profileDialogMode === 'create' ? '新增留学生档案' : '编辑留学生档案'" width="980px" @closed="resetProfileDialog">
      <el-form ref="profileFormRef" :model="profileForm" :rules="profileRules" label-width="112px">
        <el-divider content-position="left">基础信息</el-divider>
        <div class="form-grid">
          <el-form-item label="绑定用户ID" prop="userId">
            <el-input-number v-model="profileForm.userId" :min="1" controls-position="right" placeholder="可为空" />
          </el-form-item>
          <el-form-item label="学号" prop="studentNo">
            <el-input v-model.trim="profileForm.studentNo" placeholder="请输入学号" />
          </el-form-item>
          <el-form-item label="申请编号" prop="applicationNo">
            <el-input v-model.trim="profileForm.applicationNo" placeholder="请输入申请编号" />
          </el-form-item>
          <el-form-item label="中文姓名" prop="chineseName">
            <el-input v-model.trim="profileForm.chineseName" placeholder="请输入中文姓名" />
          </el-form-item>
          <el-form-item label="英文姓名" prop="englishName">
            <el-input v-model.trim="profileForm.englishName" placeholder="请输入英文姓名" />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="profileForm.gender">
              <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker v-model="profileForm.birthDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择出生日期" />
          </el-form-item>
          <el-form-item label="国籍" prop="nationality">
            <el-input v-model.trim="profileForm.nationality" placeholder="请输入国籍" />
          </el-form-item>
          <el-form-item label="母语" prop="nativeLanguage">
            <el-input v-model.trim="profileForm.nativeLanguage" placeholder="请输入母语" />
          </el-form-item>
        </div>

        <el-divider content-position="left">联系方式与证件</el-divider>
        <div class="form-grid">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model.trim="profileForm.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model.trim="profileForm.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="微信号" prop="wechat">
            <el-input v-model.trim="profileForm.wechat" placeholder="请输入微信号" />
          </el-form-item>
          <el-form-item label="护照号码" prop="passportNo">
            <el-input v-model.trim="profileForm.passportNo" placeholder="请输入护照号码" />
          </el-form-item>
          <el-form-item label="签发国家" prop="passportCountry">
            <el-input v-model.trim="profileForm.passportCountry" placeholder="请输入签发国家" />
          </el-form-item>
          <el-form-item label="签发日期" prop="passportIssueDate">
            <el-date-picker v-model="profileForm.passportIssueDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择签发日期" />
          </el-form-item>
          <el-form-item label="有效期" prop="passportExpireDate">
            <el-date-picker v-model="profileForm.passportExpireDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择有效期" />
          </el-form-item>
          <el-form-item label="证件照地址" prop="avatarUrl">
            <el-input v-model.trim="profileForm.avatarUrl" placeholder="请输入头像或证件照地址" />
          </el-form-item>
        </div>

        <el-divider content-position="left">学籍信息</el-divider>
        <div class="form-grid">
          <el-form-item label="学院" prop="college">
            <el-input v-model.trim="profileForm.college" placeholder="请输入学院" />
          </el-form-item>
          <el-form-item label="专业" prop="major">
            <el-input v-model.trim="profileForm.major" placeholder="请输入专业" />
          </el-form-item>
          <el-form-item label="学历层次" prop="degreeLevel">
            <el-input v-model.trim="profileForm.degreeLevel" placeholder="本科 / 硕士 / 博士" />
          </el-form-item>
          <el-form-item label="年级" prop="grade">
            <el-input v-model.trim="profileForm.grade" placeholder="请输入年级" />
          </el-form-item>
          <el-form-item label="班级" prop="className">
            <el-input v-model.trim="profileForm.className" placeholder="请输入班级" />
          </el-form-item>
          <el-form-item label="入学日期" prop="enrollmentDate">
            <el-date-picker v-model="profileForm.enrollmentDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择入学日期" />
          </el-form-item>
          <el-form-item label="预计毕业" prop="expectedGraduationDate">
            <el-date-picker v-model="profileForm.expectedGraduationDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择预计毕业日期" />
          </el-form-item>
          <el-form-item label="学生状态" prop="studentStatus">
            <el-select v-model="profileForm.studentStatus">
              <el-option v-for="item in studentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="启用状态" prop="status">
            <el-radio-group v-model="profileForm.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <el-form-item label="备注" prop="remark">
          <el-input v-model.trim="profileForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="profileDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitProfileForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="statusDialogVisible" title="学生状态变更" width="520px" @closed="resetStatusDialog">
      <el-form ref="statusFormRef" :model="statusForm" :rules="statusRules" label-width="96px">
        <el-form-item label="当前状态">
          <el-tag :type="studentStatusTag(currentRow?.studentStatus)">{{ formatOption(studentStatusOptions, currentRow?.studentStatus) }}</el-tag>
        </el-form-item>
        <el-form-item label="新状态" prop="newStatus">
          <el-select v-model="statusForm.newStatus" placeholder="请选择新状态">
            <el-option v-for="item in studentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="变更原因" prop="changeReason">
          <el-input v-model.trim="statusForm.changeReason" type="textarea" :rows="3" placeholder="请输入变更原因" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model.trim="statusForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitStatusForm">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailDrawerVisible" :title="drawerTitle" size="82%" @closed="resetDetail">
      <div v-loading="detailLoading" class="detail-drawer">
        <template v-if="detail">
          <section class="profile-summary">
            <div class="profile-summary__avatar">{{ (detail.chineseName || detail.englishName || 'S').slice(0, 1) }}</div>
            <div class="profile-summary__main">
              <h2>{{ detail.chineseName || '-' }} <span>{{ detail.englishName }}</span></h2>
              <p>{{ detail.studentNo }} / {{ detail.nationality }} / {{ formatOption(studentStatusOptions, detail.studentStatus) }}</p>
            </div>
            <el-button v-if="drawerMode === 'my'" type="primary" :icon="Edit" @click="openMyEditDialog">编辑我的档案</el-button>
          </section>

          <el-descriptions :column="3" border class="detail-descriptions">
            <el-descriptions-item label="绑定用户ID">{{ detail.userId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="申请编号">{{ detail.applicationNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ formatOption(genderOptions, detail.gender) }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ detail.birthDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ detail.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ detail.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="护照号码">{{ detail.passportNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="学院">{{ detail.college || '-' }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ detail.major || '-' }}</el-descriptions-item>
            <el-descriptions-item label="学历层次">{{ detail.degreeLevel || '-' }}</el-descriptions-item>
            <el-descriptions-item label="年级">{{ detail.grade || '-' }}</el-descriptions-item>
            <el-descriptions-item label="班级">{{ detail.className || '-' }}</el-descriptions-item>
          </el-descriptions>

          <el-tabs v-model="activeTab" class="detail-tabs">
            <el-tab-pane label="联系人" name="contacts">
              <SubTableToolbar permission="student:contact:add" @add="openContactDialog()" />
              <el-table :data="detail.contacts" border>
                <el-table-column prop="contactType" label="类型" width="120">
                  <template #default="{ row }">{{ formatOption(contactTypeOptions, row.contactType) }}</template>
                </el-table-column>
                <el-table-column prop="contactName" label="姓名" min-width="120" />
                <el-table-column prop="relationship" label="关系" min-width="100" />
                <el-table-column prop="phone" label="电话" min-width="140" />
                <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
                <el-table-column prop="address" label="地址" min-width="220" show-overflow-tooltip />
                <el-table-column prop="isPrimary" label="主联系人" width="100" align="center">
                  <template #default="{ row }">
                    <el-tag :type="row.isPrimary === 1 ? 'success' : 'info'">{{ row.isPrimary === 1 ? '是' : '否' }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="140" fixed="right">
                  <template #default="{ row }">
                    <el-button v-permission="'student:contact:update'" link type="primary" @click="openContactDialog(row)">编辑</el-button>
                    <el-button v-permission="'student:contact:delete'" link type="danger" @click="deleteContactRow(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="教育背景" name="educations">
              <SubTableToolbar permission="student:education:add" @add="openEducationDialog()" />
              <el-table :data="detail.educations" border>
                <el-table-column prop="schoolName" label="学校名称" min-width="180" />
                <el-table-column prop="country" label="国家/地区" min-width="120" />
                <el-table-column prop="degreeLevel" label="学历层次" min-width="110" />
                <el-table-column prop="major" label="专业" min-width="150" />
                <el-table-column prop="startDate" label="开始日期" min-width="120" />
                <el-table-column prop="endDate" label="结束日期" min-width="120" />
                <el-table-column prop="certificateUrl" label="证书地址" min-width="220" show-overflow-tooltip />
                <el-table-column label="操作" width="140" fixed="right">
                  <template #default="{ row }">
                    <el-button v-permission="'student:education:update'" link type="primary" @click="openEducationDialog(row)">编辑</el-button>
                    <el-button v-permission="'student:education:delete'" link type="danger" @click="deleteEducationRow(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="附件材料" name="documents">
              <SubTableToolbar permission="student:document:add" @add="openDocumentDialog()" />
              <el-table :data="detail.documents" border>
                <el-table-column prop="documentType" label="材料类型" min-width="140">
                  <template #default="{ row }">{{ formatOption(documentTypeOptions, row.documentType) }}</template>
                </el-table-column>
                <el-table-column prop="documentName" label="材料名称" min-width="160" />
                <el-table-column prop="fileUrl" label="文件地址" min-width="260" show-overflow-tooltip />
                <el-table-column prop="fileSize" label="文件大小" min-width="110" />
                <el-table-column prop="mimeType" label="文件类型" min-width="130" />
                <el-table-column prop="reviewStatus" label="审核状态" width="110" align="center">
                  <template #default="{ row }">
                    <el-tag :type="documentStatusTag(row.reviewStatus)">{{ formatOption(documentReviewStatusOptions, row.reviewStatus) }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="140" fixed="right">
                  <template #default="{ row }">
                    <el-button v-permission="'student:document:update'" link type="primary" @click="openDocumentDialog(row)">编辑</el-button>
                    <el-button v-permission="'student:document:delete'" link type="danger" @click="deleteDocumentRow(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="状态日志" name="logs">
              <el-table :data="detail.statusLogs" border>
                <el-table-column prop="oldStatus" label="原状态" min-width="110">
                  <template #default="{ row }">{{ formatOption(studentStatusOptions, row.oldStatus) }}</template>
                </el-table-column>
                <el-table-column prop="newStatus" label="新状态" min-width="110">
                  <template #default="{ row }">{{ formatOption(studentStatusOptions, row.newStatus) }}</template>
                </el-table-column>
                <el-table-column prop="changeReason" label="变更原因" min-width="220" show-overflow-tooltip />
                <el-table-column prop="operatorName" label="操作人" min-width="120" />
                <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
                <el-table-column prop="createTime" label="创建时间" min-width="170" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </template>
      </div>
    </el-drawer>

    <el-dialog v-model="myEditVisible" title="编辑我的档案" width="720px" @closed="resetMyForm">
      <el-form :model="myForm" label-width="108px">
        <div class="form-grid">
          <el-form-item label="中文姓名"><el-input v-model.trim="myForm.chineseName" /></el-form-item>
          <el-form-item label="性别">
            <el-select v-model="myForm.gender" clearable>
              <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期"><el-date-picker v-model="myForm.birthDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="母语"><el-input v-model.trim="myForm.nativeLanguage" /></el-form-item>
          <el-form-item label="邮箱"><el-input v-model.trim="myForm.email" /></el-form-item>
          <el-form-item label="手机号"><el-input v-model.trim="myForm.phone" /></el-form-item>
          <el-form-item label="微信号"><el-input v-model.trim="myForm.wechat" /></el-form-item>
          <el-form-item label="护照号码"><el-input v-model.trim="myForm.passportNo" /></el-form-item>
          <el-form-item label="签发国家"><el-input v-model.trim="myForm.passportCountry" /></el-form-item>
          <el-form-item label="签发日期"><el-date-picker v-model="myForm.passportIssueDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="有效期"><el-date-picker v-model="myForm.passportExpireDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="证件照地址"><el-input v-model.trim="myForm.avatarUrl" /></el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="myForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="myEditVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitMyForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="contactDialogVisible" :title="contactForm.id ? '编辑联系人' : '新增联系人'" width="620px" @closed="resetContactForm">
      <el-form ref="contactFormRef" :model="contactForm" :rules="contactRules" label-width="96px">
        <el-form-item label="联系人类型" prop="contactType">
          <el-select v-model="contactForm.contactType">
            <el-option v-for="item in contactTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人姓名" prop="contactName"><el-input v-model.trim="contactForm.contactName" /></el-form-item>
        <el-form-item label="关系"><el-input v-model.trim="contactForm.relationship" /></el-form-item>
        <el-form-item label="电话"><el-input v-model.trim="contactForm.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model.trim="contactForm.email" /></el-form-item>
        <el-form-item label="地址"><el-input v-model.trim="contactForm.address" /></el-form-item>
        <el-form-item label="主联系人">
          <el-radio-group v-model="contactForm.isPrimary">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="contactDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitContactForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="educationDialogVisible" :title="educationForm.id ? '编辑教育背景' : '新增教育背景'" width="680px" @closed="resetEducationForm">
      <el-form ref="educationFormRef" :model="educationForm" :rules="educationRules" label-width="96px">
        <el-form-item label="学校名称" prop="schoolName"><el-input v-model.trim="educationForm.schoolName" /></el-form-item>
        <el-form-item label="国家/地区"><el-input v-model.trim="educationForm.country" /></el-form-item>
        <el-form-item label="学历层次"><el-input v-model.trim="educationForm.degreeLevel" /></el-form-item>
        <el-form-item label="专业"><el-input v-model.trim="educationForm.major" /></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="educationForm.startDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="结束日期"><el-date-picker v-model="educationForm.endDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="证书地址"><el-input v-model.trim="educationForm.certificateUrl" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="educationForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="educationDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitEducationForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="documentDialogVisible" :title="documentForm.id ? '编辑附件材料' : '新增附件材料'" width="680px" @closed="resetDocumentForm">
      <el-form ref="documentFormRef" :model="documentForm" :rules="documentRules" label-width="96px">
        <el-form-item label="材料类型" prop="documentType">
          <el-select v-model="documentForm.documentType">
            <el-option v-for="item in documentTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="材料名称" prop="documentName"><el-input v-model.trim="documentForm.documentName" /></el-form-item>
        <el-form-item label="文件地址" prop="fileUrl"><el-input v-model.trim="documentForm.fileUrl" /></el-form-item>
        <el-form-item label="文件大小"><el-input-number v-model="documentForm.fileSize" :min="0" controls-position="right" /></el-form-item>
        <el-form-item label="文件类型"><el-input v-model.trim="documentForm.mimeType" /></el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="documentForm.reviewStatus">
            <el-option v-for="item in documentReviewStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="documentForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="documentDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitDocumentForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, defineComponent, h, nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElButton, ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, Search, User, View } from '@element-plus/icons-vue'
import {
  createStudentContact,
  createStudentDocument,
  createStudentEducation,
  createStudentProfile,
  deleteStudentContact,
  deleteStudentDocument,
  deleteStudentEducation,
  deleteStudentProfile,
  getMyStudentProfile,
  getStudentProfileDetail,
  getStudentProfilePage,
  updateMyStudentProfile,
  updateStudentContact,
  updateStudentDocument,
  updateStudentEducation,
  updateStudentProfile,
  updateStudentProfileStatus
} from '@/api/student/profile'
import { hasPermission } from '@/utils/permission'
import type {
  StudentContact,
  StudentContactForm,
  StudentDocument,
  StudentDocumentForm,
  StudentEducation,
  StudentEducationForm,
  StudentProfileDetail,
  StudentProfileForm,
  StudentProfileListItem,
  StudentProfileQuery,
  StudentStatusUpdateForm
} from '@/types/student'

type Option = { label: string; value: string }

const genderOptions: Option[] = [
  { label: '男', value: 'MALE' },
  { label: '女', value: 'FEMALE' },
  { label: '未知', value: 'UNKNOWN' }
]
const studentStatusOptions: Option[] = [
  { label: '预录取', value: 'PRE_ADMITTED' },
  { label: '在读', value: 'ENROLLED' },
  { label: '休学', value: 'SUSPENDED' },
  { label: '已毕业', value: 'GRADUATED' },
  { label: '退学', value: 'DROPPED' },
  { label: '已离校', value: 'LEFT' }
]
const contactTypeOptions: Option[] = [
  { label: '紧急联系人', value: 'EMERGENCY' },
  { label: '家庭联系人', value: 'FAMILY' },
  { label: '监护人', value: 'GUARDIAN' },
  { label: '其他', value: 'OTHER' }
]
const documentTypeOptions: Option[] = [
  { label: '护照', value: 'PASSPORT' },
  { label: '照片', value: 'PHOTO' },
  { label: '录取通知书', value: 'ADMISSION_NOTICE' },
  { label: '学历证明', value: 'DEGREE_CERTIFICATE' },
  { label: '语言成绩', value: 'LANGUAGE_SCORE' },
  { label: '体检证明', value: 'PHYSICAL_EXAM' },
  { label: '保险材料', value: 'INSURANCE' },
  { label: '其他', value: 'OTHER' }
]
const documentReviewStatusOptions: Option[] = [
  { label: '待审核', value: 'PENDING' },
  { label: '已通过', value: 'APPROVED' },
  { label: '已拒绝', value: 'REJECTED' }
]

const searchForm = reactive<StudentProfileQuery>({ pageNum: 1, pageSize: 10, status: '' })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const profiles = ref<StudentProfileListItem[]>([])
const total = ref(0)
const tableLoading = ref(false)
const submitLoading = ref(false)
const detailLoading = ref(false)
const profileDialogVisible = ref(false)
const profileDialogMode = ref<'create' | 'edit'>('create')
const statusDialogVisible = ref(false)
const detailDrawerVisible = ref(false)
const myEditVisible = ref(false)
const contactDialogVisible = ref(false)
const educationDialogVisible = ref(false)
const documentDialogVisible = ref(false)
const detail = ref<StudentProfileDetail | null>(null)
const currentRow = ref<StudentProfileListItem | null>(null)
const drawerMode = ref<'detail' | 'my'>('detail')
const activeTab = ref('contacts')

const profileFormRef = ref<FormInstance>()
const statusFormRef = ref<FormInstance>()
const contactFormRef = ref<FormInstance>()
const educationFormRef = ref<FormInstance>()
const documentFormRef = ref<FormInstance>()

const emptyProfileForm = (): StudentProfileForm => ({
  studentNo: '',
  englishName: '',
  gender: 'UNKNOWN',
  nationality: '',
  studentStatus: 'PRE_ADMITTED',
  status: 1
})
const profileFieldKeys: Array<keyof StudentProfileForm> = [
  'id',
  'userId',
  'studentNo',
  'applicationNo',
  'chineseName',
  'englishName',
  'gender',
  'birthDate',
  'nationality',
  'nativeLanguage',
  'email',
  'phone',
  'wechat',
  'passportNo',
  'passportCountry',
  'passportIssueDate',
  'passportExpireDate',
  'college',
  'major',
  'degreeLevel',
  'grade',
  'className',
  'enrollmentDate',
  'expectedGraduationDate',
  'studentStatus',
  'avatarUrl',
  'remark',
  'status'
]
const profileForm = reactive<StudentProfileForm>(emptyProfileForm())
const myForm = reactive<Partial<StudentProfileForm>>({})
const statusForm = reactive<StudentStatusUpdateForm>({ newStatus: '', changeReason: '', remark: '' })
const contactForm = reactive<StudentContactForm & { id?: number }>({ contactType: 'EMERGENCY', contactName: '', isPrimary: 0 })
const educationForm = reactive<StudentEducationForm & { id?: number }>({ schoolName: '' })
const documentForm = reactive<StudentDocumentForm & { id?: number }>({
  documentType: 'PASSPORT',
  documentName: '',
  fileUrl: '',
  reviewStatus: 'PENDING'
})

const drawerTitle = computed(() => (drawerMode.value === 'my' ? '我的留学生档案' : '留学生档案详情'))

const profileRules: FormRules<StudentProfileForm> = {
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  englishName: [{ required: true, message: '请输入英文姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  nationality: [{ required: true, message: '请输入国籍', trigger: 'blur' }],
  studentStatus: [{ required: true, message: '请选择学生状态', trigger: 'change' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}
const statusRules: FormRules<StudentStatusUpdateForm> = {
  newStatus: [{ required: true, message: '请选择新状态', trigger: 'change' }]
}
const contactRules: FormRules<StudentContactForm> = {
  contactType: [{ required: true, message: '请选择联系人类型', trigger: 'change' }],
  contactName: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}
const educationRules: FormRules<StudentEducationForm> = {
  schoolName: [{ required: true, message: '请输入学校名称', trigger: 'blur' }]
}
const documentRules: FormRules<StudentDocumentForm> = {
  documentType: [{ required: true, message: '请选择材料类型', trigger: 'change' }],
  documentName: [{ required: true, message: '请输入材料名称', trigger: 'blur' }],
  fileUrl: [{ required: true, message: '请输入文件地址', trigger: 'blur' }]
}

const SubTableToolbar = defineComponent({
  props: { permission: { type: String, required: true } },
  emits: ['add'],
  setup(props, { emit }) {
    return () =>
      h('div', { class: 'sub-toolbar' }, [
        hasPermission(props.permission)
          ? h(ElButton, { type: 'primary', icon: Plus, onClick: () => emit('add') }, () => '新增')
          : null
      ])
  }
})

function formatOption(options: Option[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function studentStatusTag(status?: string) {
  const map: Record<string, 'success' | 'warning' | 'info' | 'danger' | 'primary'> = {
    PRE_ADMITTED: 'primary',
    ENROLLED: 'success',
    SUSPENDED: 'warning',
    GRADUATED: 'info',
    DROPPED: 'danger',
    LEFT: 'info'
  }
  return status ? map[status] || 'info' : 'info'
}

function documentStatusTag(status?: string) {
  if (status === 'APPROVED') return 'success'
  if (status === 'REJECTED') return 'danger'
  return 'warning'
}

async function loadProfiles() {
  tableLoading.value = true
  try {
    const result = await getStudentProfilePage({
      ...searchForm,
      pageNum: pageQuery.pageNum,
      pageSize: pageQuery.pageSize,
      status: searchForm.status === '' ? undefined : searchForm.status
    })
    profiles.value = result.records || []
    total.value = result.total || 0
  } finally {
    tableLoading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadProfiles()
}

function handleReset() {
  Object.assign(searchForm, { studentNo: '', chineseName: '', englishName: '', nationality: '', college: '', major: '', studentStatus: '', status: '' })
  pageQuery.pageNum = 1
  loadProfiles()
}

function copyProfileForm(source: Partial<StudentProfileForm>) {
  profileFieldKeys.forEach((key) => delete profileForm[key])
  const clean = profileFieldKeys.reduce<Partial<StudentProfileForm>>((target, key) => {
    if (source[key] !== undefined) {
      target[key] = source[key] as never
    }
    return target
  }, {})
  Object.assign(profileForm, emptyProfileForm(), clean)
}

function openCreateDialog() {
  profileDialogMode.value = 'create'
  copyProfileForm({})
  profileDialogVisible.value = true
  nextTick(() => profileFormRef.value?.clearValidate())
}

async function openEditDialog(id: number) {
  profileDialogMode.value = 'edit'
  detailLoading.value = true
  try {
    const data = await getStudentProfileDetail(id)
    copyProfileForm(data)
    profileDialogVisible.value = true
    nextTick(() => profileFormRef.value?.clearValidate())
  } finally {
    detailLoading.value = false
  }
}

function resetProfileDialog() {
  copyProfileForm({})
  profileFormRef.value?.resetFields()
}

async function submitProfileForm() {
  await profileFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (profileDialogMode.value === 'create') {
      await createStudentProfile(profileForm)
      ElMessage.success('新增档案成功')
    } else if (profileForm.id) {
      await updateStudentProfile(profileForm.id, profileForm)
      ElMessage.success('编辑档案成功')
    }
    profileDialogVisible.value = false
    await loadProfiles()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row: StudentProfileListItem) {
  try {
    await ElMessageBox.confirm(`确认删除留学生档案「${row.chineseName || row.englishName}」吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteStudentProfile(row.id)
    ElMessage.success('删除档案成功')
    await loadProfiles()
  } catch {
    // Cancelled.
  }
}

function openStatusDialog(row: StudentProfileListItem) {
  currentRow.value = row
  Object.assign(statusForm, { newStatus: row.studentStatus, changeReason: '', remark: '' })
  statusDialogVisible.value = true
  nextTick(() => statusFormRef.value?.clearValidate())
}

function resetStatusDialog() {
  currentRow.value = null
  Object.assign(statusForm, { newStatus: '', changeReason: '', remark: '' })
}

async function submitStatusForm() {
  await statusFormRef.value?.validate()
  if (!currentRow.value) return
  submitLoading.value = true
  try {
    await updateStudentProfileStatus(currentRow.value.id, statusForm)
    ElMessage.success('状态变更成功')
    statusDialogVisible.value = false
    await loadProfiles()
    if (detail.value?.id === currentRow.value.id) await refreshDetail()
  } finally {
    submitLoading.value = false
  }
}

async function openDetail(id: number) {
  drawerMode.value = 'detail'
  detailDrawerVisible.value = true
  await loadDetail(id)
}

async function openMyProfile() {
  drawerMode.value = 'my'
  detailDrawerVisible.value = true
  detailLoading.value = true
  try {
    detail.value = await getMyStudentProfile()
  } finally {
    detailLoading.value = false
  }
}

async function loadDetail(id: number) {
  detailLoading.value = true
  try {
    detail.value = await getStudentProfileDetail(id)
  } finally {
    detailLoading.value = false
  }
}

async function refreshDetail() {
  if (!detail.value) return
  if (drawerMode.value === 'my') {
    detail.value = await getMyStudentProfile()
  } else {
    detail.value = await getStudentProfileDetail(detail.value.id)
  }
}

function resetDetail() {
  detail.value = null
  activeTab.value = 'contacts'
}

function openMyEditDialog() {
  if (!detail.value) return
  Object.assign(myForm, {
    chineseName: detail.value.chineseName,
    gender: detail.value.gender,
    birthDate: detail.value.birthDate,
    nativeLanguage: detail.value.nativeLanguage,
    email: detail.value.email,
    phone: detail.value.phone,
    wechat: detail.value.wechat,
    passportNo: detail.value.passportNo,
    passportCountry: detail.value.passportCountry,
    passportIssueDate: detail.value.passportIssueDate,
    passportExpireDate: detail.value.passportExpireDate,
    avatarUrl: detail.value.avatarUrl,
    remark: detail.value.remark
  })
  myEditVisible.value = true
}

function resetMyForm() {
  Object.keys(myForm).forEach((key) => delete myForm[key as keyof StudentProfileForm])
}

async function submitMyForm() {
  submitLoading.value = true
  try {
    await updateMyStudentProfile(myForm)
    ElMessage.success('我的档案已更新')
    myEditVisible.value = false
    await refreshDetail()
  } finally {
    submitLoading.value = false
  }
}

function openContactDialog(row?: StudentContact) {
  Object.assign(contactForm, { id: row?.id, contactType: row?.contactType || 'EMERGENCY', contactName: row?.contactName || '', relationship: row?.relationship || '', phone: row?.phone || '', email: row?.email || '', address: row?.address || '', isPrimary: row?.isPrimary ?? 0 })
  contactDialogVisible.value = true
  nextTick(() => contactFormRef.value?.clearValidate())
}

function resetContactForm() {
  Object.assign(contactForm, { id: undefined, contactType: 'EMERGENCY', contactName: '', relationship: '', phone: '', email: '', address: '', isPrimary: 0 })
}

async function submitContactForm() {
  if (!detail.value) return
  await contactFormRef.value?.validate()
  submitLoading.value = true
  try {
    const { id, ...payload } = contactForm
    if (id) await updateStudentContact(id, payload)
    else await createStudentContact(detail.value.id, payload)
    ElMessage.success('联系人保存成功')
    contactDialogVisible.value = false
    await refreshDetail()
  } finally {
    submitLoading.value = false
  }
}

async function deleteContactRow(row: StudentContact) {
  try {
    await ElMessageBox.confirm(`确认删除联系人「${row.contactName}」吗？`, '删除确认', { type: 'warning' })
    await deleteStudentContact(row.id)
    ElMessage.success('联系人已删除')
    await refreshDetail()
  } catch {
    // Cancelled.
  }
}

function openEducationDialog(row?: StudentEducation) {
  Object.assign(educationForm, { id: row?.id, schoolName: row?.schoolName || '', country: row?.country || '', degreeLevel: row?.degreeLevel || '', major: row?.major || '', startDate: row?.startDate || '', endDate: row?.endDate || '', certificateUrl: row?.certificateUrl || '', remark: row?.remark || '' })
  educationDialogVisible.value = true
  nextTick(() => educationFormRef.value?.clearValidate())
}

function resetEducationForm() {
  Object.assign(educationForm, { id: undefined, schoolName: '', country: '', degreeLevel: '', major: '', startDate: '', endDate: '', certificateUrl: '', remark: '' })
}

async function submitEducationForm() {
  if (!detail.value) return
  await educationFormRef.value?.validate()
  submitLoading.value = true
  try {
    const { id, ...payload } = educationForm
    if (id) await updateStudentEducation(id, payload)
    else await createStudentEducation(detail.value.id, payload)
    ElMessage.success('教育背景保存成功')
    educationDialogVisible.value = false
    await refreshDetail()
  } finally {
    submitLoading.value = false
  }
}

async function deleteEducationRow(row: StudentEducation) {
  try {
    await ElMessageBox.confirm(`确认删除教育背景「${row.schoolName}」吗？`, '删除确认', { type: 'warning' })
    await deleteStudentEducation(row.id)
    ElMessage.success('教育背景已删除')
    await refreshDetail()
  } catch {
    // Cancelled.
  }
}

function openDocumentDialog(row?: StudentDocument) {
  Object.assign(documentForm, { id: row?.id, documentType: row?.documentType || 'PASSPORT', documentName: row?.documentName || '', fileUrl: row?.fileUrl || '', fileSize: row?.fileSize, mimeType: row?.mimeType || '', reviewStatus: row?.reviewStatus || 'PENDING', remark: row?.remark || '' })
  documentDialogVisible.value = true
  nextTick(() => documentFormRef.value?.clearValidate())
}

function resetDocumentForm() {
  Object.assign(documentForm, { id: undefined, documentType: 'PASSPORT', documentName: '', fileUrl: '', fileSize: undefined, mimeType: '', reviewStatus: 'PENDING', remark: '' })
}

async function submitDocumentForm() {
  if (!detail.value) return
  await documentFormRef.value?.validate()
  submitLoading.value = true
  try {
    const { id, ...payload } = documentForm
    if (id) await updateStudentDocument(id, payload)
    else await createStudentDocument(detail.value.id, payload)
    ElMessage.success('附件材料保存成功')
    documentDialogVisible.value = false
    await refreshDetail()
  } finally {
    submitLoading.value = false
  }
}

async function deleteDocumentRow(row: StudentDocument) {
  try {
    await ElMessageBox.confirm(`确认删除附件材料「${row.documentName}」吗？`, '删除确认', { type: 'warning' })
    await deleteStudentDocument(row.id)
    ElMessage.success('附件材料已删除')
    await refreshDetail()
  } catch {
    // Cancelled.
  }
}

onMounted(loadProfiles)
</script>

<style scoped lang="scss">
.student-profile-page {
  .el-date-editor,
  .el-select,
  .el-input-number {
    width: 100%;
  }

  :deep(.profile-search-form) {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(236px, 1fr));
    gap: 12px 16px;

    .el-form-item {
      min-width: 0;
      margin-bottom: 0;
    }

    .el-form-item__content {
      min-width: 144px;
    }

    .search-actions {
      .el-form-item__content {
        display: flex;
        flex-wrap: nowrap;
      }
    }
  }
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 0 14px;
}

.detail-drawer {
  min-height: 420px;
}

.profile-summary {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
  padding: 18px;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  border-radius: 8px;

  &__avatar {
    display: grid;
    flex: 0 0 56px;
    width: 56px;
    height: 56px;
    place-items: center;
    color: #fff;
    font-size: 22px;
    font-weight: 700;
    background: #2563eb;
    border-radius: 8px;
  }

  &__main {
    flex: 1;
    min-width: 0;

    h2 {
      margin: 0 0 8px;
      font-size: 20px;

      span {
        margin-left: 8px;
        color: #64748b;
        font-size: 15px;
        font-weight: 500;
      }
    }

    p {
      margin: 0;
      color: #64748b;
    }
  }
}

.detail-tabs {
  margin-top: 18px;
}

.sub-toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 12px;
}

@media (max-width: 960px) {
  .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .profile-summary {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
