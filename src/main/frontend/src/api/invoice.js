import request from '@/utils/request'

export function getInvoices(data) {
  return request({
    url: '/api/secure/invoices',
    method: 'get',
    params: data
  })
}

export function generateInvoices() {
  return request({
    url: '/api/admin/invoices/generate',
    method: 'get'
  })
}
