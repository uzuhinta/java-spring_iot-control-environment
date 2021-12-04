import request from '@/utils/request'

export function pay(data) {
  return request({
    url: '/api/admin/pay',
    method: 'post',
    data
  })
}
