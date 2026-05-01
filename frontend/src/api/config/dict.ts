import request from '@/utils/request'
import type { DictData, DictDataForm, DictOption, DictType, DictTypeForm } from '@/types/config'

export function getDictTypes(params?: { keyword?: string; status?: number | string }) {
  return request.get<DictType[], DictType[]>('/config/dict-types', { params })
}

export function createDictType(data: DictTypeForm) {
  return request.post<number, number>('/config/dict-types', data)
}

export function updateDictType(id: number, data: DictTypeForm) {
  return request.put<void, void>(`/config/dict-types/${id}`, data)
}

export function updateDictTypeStatus(id: number, status: number) {
  return request.put<void, void>(`/config/dict-types/${id}/status`, { status })
}

export function deleteDictType(id: number) {
  return request.delete<void, void>(`/config/dict-types/${id}`)
}

export function getDictData(dictCode: string, status?: number | string) {
  return request.get<DictData[], DictData[]>(`/config/dict-types/${dictCode}/items`, { params: { status } })
}

export function getDictOptions(dictCode: string) {
  return getDictData(dictCode, 1).then((items) =>
    items.map<DictOption>((item) => ({
      label: item.dictLabel,
      value: item.dictValue,
      tagType: item.tagType
    }))
  )
}

export function createDictData(data: DictDataForm) {
  return request.post<number, number>('/config/dict-data', data)
}

export function updateDictData(id: number, data: DictDataForm) {
  return request.put<void, void>(`/config/dict-data/${id}`, data)
}

export function updateDictDataStatus(id: number, status: number) {
  return request.put<void, void>(`/config/dict-data/${id}/status`, { status })
}

export function deleteDictData(id: number) {
  return request.delete<void, void>(`/config/dict-data/${id}`)
}
