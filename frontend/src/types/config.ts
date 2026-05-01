import type { PageQuery } from './api'

export interface DictType {
  id: number
  dictName: string
  dictCode: string
  description?: string
  status: number
  sort: number
  createTime?: string
  updateTime?: string
}

export interface DictTypeForm {
  id?: number
  dictName: string
  dictCode: string
  description?: string
  status: number
  sort: number
}

export interface DictData {
  id: number
  dictCode: string
  dictLabel: string
  dictValue: string
  tagType?: string
  description?: string
  status: number
  sort: number
  createTime?: string
  updateTime?: string
}

export interface DictDataForm {
  id?: number
  dictCode: string
  dictLabel: string
  dictValue: string
  tagType?: string
  description?: string
  status: number
  sort: number
}

export interface DictOption {
  label: string
  value: string
  tagType?: string
}

export interface FlowTemplate {
  id: number
  flowName: string
  flowCode: string
  businessType?: string
  description?: string
  status: number
  sort: number
  createTime?: string
  updateTime?: string
}

export interface FlowTemplateForm {
  id?: number
  flowName: string
  flowCode: string
  businessType?: string
  description?: string
  status: number
  sort: number
}

export interface FlowNode {
  id?: number
  flowId?: number
  nodeName: string
  nodeCode?: string
  nodeType: string
  approverRoleCode?: string
  description?: string
  status: number
  sort: number
}

export interface FlowDetail {
  flow: FlowTemplate
  nodes: FlowNode[]
}

export interface OperationLogQuery extends PageQuery {
  moduleName?: string
  operationType?: string
  operatorName?: string
  success?: number | string
  startTime?: string
  endTime?: string
}

export interface OperationLog {
  id: number
  moduleName: string
  operationType: string
  operationName: string
  requestMethod?: string
  requestUri?: string
  controllerMethod?: string
  requestParams?: string
  responseResult?: string
  success: number
  errorMessage?: string
  operatorId?: number
  operatorName?: string
  ipAddress?: string
  userAgent?: string
  costTime?: number
  createTime?: string
}

export interface BackupQuery extends PageQuery {
  backupName?: string
  backupStatus?: string
}

export interface DataBackup {
  id: number
  backupName: string
  fileName?: string
  filePath?: string
  fileSize?: number
  backupStatus: string
  startTime?: string
  endTime?: string
  costTime?: number
  operatorId?: number
  operatorName?: string
  errorMessage?: string
  remark?: string
  createTime?: string
}

export interface BackupCreateForm {
  backupName?: string
  remark?: string
}
