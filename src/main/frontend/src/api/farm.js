import request from '@/utils/request'

export function getFarms(data) {
  return request({
    url: '/api/admin/farms',
    method: 'get',
    params: data
  })
}

export function getFarmsByName(data) {
  return request({
    url: '/api/secure/farms/search',
    method: 'get',
    params: data
  })
}

export function updateFarms(data) {
  return request({
    url: '/api/secure/farms/info',
    method: 'put',
    data
  })
}

export function createFarms(data) {
  return request({
    url: '/api/secure/farms/info',
    method: 'post',
    data
  })
}

export function deleteFarms(id, status) {
  return request({
    url: '/api/secure/farms/info',
    method: 'delete',
    params: {
      id: id,
      status: status
    }
  })
}
