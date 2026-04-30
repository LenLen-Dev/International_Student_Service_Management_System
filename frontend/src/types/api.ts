export interface ApiResult<T = unknown> {
  code: number
  message: string
  data: T
}

export interface PageResult<T = unknown> {
  records: T[]
  total: number
}

export interface PageQuery {
  pageNum?: number
  pageSize?: number
  keyword?: string
  status?: number | string
  [key: string]: unknown
}
