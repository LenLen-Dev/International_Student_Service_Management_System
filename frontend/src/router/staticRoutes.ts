import type { RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'

export const staticRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'House' }
      }
    ]
  },
  {
    path: '/student',
    component: Layout,
    redirect: '/student/profiles',
    meta: { title: '留学生管理', icon: 'School' },
    children: [
      {
        path: 'profiles',
        name: 'StudentProfiles',
        component: () => import('@/views/student/profile/index.vue'),
        meta: { title: '留学生档案', icon: 'Collection', permission: 'student:profile:list' }
      }
    ]
  },
  {
    path: '/admission',
    component: Layout,
    redirect: '/admission/my-application',
    meta: { title: '招生申请管理', icon: 'Tickets' },
    children: [
      {
        path: 'my-application',
        name: 'AdmissionMyApplication',
        component: () => import('@/views/admission/my-application/index.vue'),
        meta: { title: '我的申请', icon: 'EditPen', permission: 'admission:application:my' }
      },
      {
        path: 'review',
        name: 'AdmissionReview',
        component: () => import('@/views/admission/review/index.vue'),
        meta: { title: '申请审核', icon: 'Checked', permission: 'admission:application:list' }
      }
    ]
  },
  {
    path: '/visa',
    component: Layout,
    redirect: '/visa/record',
    meta: { title: '签证与居留管理', icon: 'OfficeBuilding' },
    children: [
      {
        path: 'record',
        name: 'VisaRecord',
        component: () => import('@/views/visa/record/index.vue'),
        meta: { title: '签证信息', icon: 'Document', permission: 'visa:record:list' }
      },
      {
        path: 'permit',
        name: 'VisaPermit',
        component: () => import('@/views/visa/permit/index.vue'),
        meta: { title: '居留许可', icon: 'Folder', permission: 'visa:permit:list' }
      },
      {
        path: 'renewal',
        name: 'VisaRenewal',
        component: () => import('@/views/visa/renewal/index.vue'),
        meta: { title: '续签记录', icon: 'Edit', permission: 'visa:renewal:list' }
      },
      {
        path: 'alert',
        name: 'VisaAlert',
        component: () => import('@/views/visa/alert/index.vue'),
        meta: { title: '合规预警', icon: 'Warning', permission: 'visa:alert:list' }
      },
      {
        path: 'my',
        name: 'VisaMy',
        component: () => import('@/views/visa/my/index.vue'),
        meta: { title: '我的签证居留', icon: 'User', permission: 'visa:my:view' }
      }
    ]
  },
  {
    path: '/config',
    component: Layout,
    redirect: '/config/dict',
    meta: { title: '系统配置与审计', icon: 'Setting' },
    children: [
      {
        path: 'dict',
        name: 'ConfigDict',
        component: () => import('@/views/config/dict/index.vue'),
        meta: { title: '字典配置', icon: 'Menu', permission: 'config:dict:list' }
      },
      {
        path: 'flow',
        name: 'ConfigFlow',
        component: () => import('@/views/config/flow/index.vue'),
        meta: { title: '流程配置', icon: 'Share', permission: 'config:flow:list' }
      },
      {
        path: 'log',
        name: 'ConfigLog',
        component: () => import('@/views/config/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document', permission: 'audit:operation-log:list' }
      },
      {
        path: 'backup',
        name: 'ConfigBackup',
        component: () => import('@/views/config/backup/index.vue'),
        meta: { title: '数据备份', icon: 'Folder', permission: 'config:backup:list' }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/dashboard/index.vue'),
    meta: { title: '页面不存在', hidden: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    meta: { hidden: true }
  }
]
