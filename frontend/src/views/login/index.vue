<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-card__intro">
        <div class="login-card__badge">International Student Service</div>
        <h1>留学生服务与管理系统</h1>
        <p>International Student Service & Management System</p>
        <div class="login-card__features">
          <span>统一认证</span>
          <span>权限控制</span>
          <span>服务协同</span>
        </div>
      </div>

      <div class="login-card__form">
        <h2>账号登录</h2>
        <p>请输入管理员账号和密码</p>
        <el-form ref="loginFormRef" :model="loginForm" :rules="rules" label-position="top" @keyup.enter="handleLogin">
          <el-form-item label="用户名" prop="username">
            <el-input v-model.trim="loginForm.username" size="large" placeholder="请输入用户名" clearable>
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" size="large" type="password" placeholder="请输入密码" show-password>
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="验证码" prop="captcha">
            <div class="captcha-row">
              <el-input v-model.trim="loginForm.captcha" size="large" placeholder="验证码预留" clearable>
                <template #prefix>
                  <el-icon><CircleCheck /></el-icon>
                </template>
              </el-input>
              <div class="captcha-box">ABCD</div>
            </div>
          </el-form-item>

          <el-button class="login-card__button" type="primary" size="large" :loading="loading" @click="handleLogin">
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { CircleCheck, Lock, User } from '@element-plus/icons-vue'
import { usePermissionStore } from '@/stores/permission'
import { useUserStore } from '@/stores/user'
import type { LoginParams } from '@/types/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const permissionStore = usePermissionStore()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive<LoginParams>({
  username: '',
  password: '',
  captcha: ''
})

const rules: FormRules<LoginParams> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  await loginFormRef.value?.validate()
  loading.value = true
  try {
    await userStore.login(loginForm)
    permissionStore.resetRoutes()
    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    router.replace(redirect)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 24px;
  background:
    linear-gradient(135deg, rgb(37 99 235 / 92%), rgb(14 116 144 / 88%)),
    radial-gradient(circle at 20% 20%, rgb(255 255 255 / 24%), transparent 28%);
}

.login-card {
  display: grid;
  grid-template-columns: 1fr 420px;
  width: min(960px, 100%);
  min-height: 520px;
  overflow: hidden;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 28px 80px rgb(15 23 42 / 28%);

  &__intro {
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 56px;
    color: #fff;
    background:
      linear-gradient(160deg, rgb(15 47 95 / 92%), rgb(37 99 235 / 88%)),
      linear-gradient(45deg, #0f2f5f, #2563eb);

    h1 {
      margin: 22px 0 14px;
      font-size: 36px;
      line-height: 1.2;
    }

    p {
      max-width: 460px;
      margin: 0;
      color: #dbeafe;
      font-size: 16px;
      line-height: 1.8;
    }
  }

  &__badge {
    width: fit-content;
    padding: 7px 12px;
    color: #bfdbfe;
    font-size: 13px;
    font-weight: 600;
    background: rgb(255 255 255 / 12%);
    border: 1px solid rgb(255 255 255 / 22%);
    border-radius: 999px;
  }

  &__features {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 34px;

    span {
      padding: 8px 12px;
      color: #eff6ff;
      font-size: 13px;
      background: rgb(255 255 255 / 12%);
      border-radius: 999px;
    }
  }

  &__form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 48px 42px;

    h2 {
      margin: 0 0 8px;
      color: #111827;
      font-size: 26px;
    }

    p {
      margin: 0 0 28px;
      color: #6b7280;
      font-size: 14px;
    }
  }

  &__button {
    width: 100%;
    margin-top: 8px;
  }
}

.captcha-row {
  display: grid;
  grid-template-columns: 1fr 104px;
  gap: 10px;
  width: 100%;
}

.captcha-box {
  display: grid;
  height: 40px;
  place-items: center;
  color: #2563eb;
  font-weight: 700;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
}

@media (max-width: 820px) {
  .login-card {
    grid-template-columns: 1fr;

    &__intro {
      padding: 34px;

      h1 {
        font-size: 28px;
      }
    }

    &__form {
      padding: 34px;
    }
  }
}
</style>
