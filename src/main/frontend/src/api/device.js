import request from '@/utils/request'

export function getDevices(data) {
  return request({
    url: '/api/admin/devices',
    method: 'get',
    params: data
  })
}

export function getProperties(data) {
  return request({
    url: '/api/admin/device/properties',
    method: 'get',
    responseType: 'arraybuffer',
    params: { deviceId: data }
  })
}

export function updateDevices(data) {
  return request({
    url: '/api/secure/devices/info',
    method: 'put',
    data
  })
}

export function createDevices(data) {
  return request({
    url: '/api/secure/devices/info',
    method: 'post',
    data
  })
}

export function deleteDevices(id, status) {
  return request({
    url: '/api/secure/devices/info',
    method: 'delete',
    params: {
      id: id,
      status: status
    }
  })
}
