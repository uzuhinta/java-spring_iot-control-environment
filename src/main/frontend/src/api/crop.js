import request from '@/utils/request'

export function getCrops(data) {
  return request({
    url: '/api/admin/crops',
    method: 'get',
    params: data
  })
}
