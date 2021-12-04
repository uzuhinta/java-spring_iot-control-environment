import request from '@/utils/request'

export function getPlants(data) {
  return request({
    url: '/api/secure/plants',
    method: 'get',
    params: data
  })
}

export function updatePlants(data) {
  return request({
    url: '/api/secure/plants/info',
    method: 'put',
    data
  })
}

export function createPlants(data) {
  return request({
    url: '/api/secure/plants/info',
    method: 'post',
    data
  })
}

export function deletePlants(id, status) {
  return request({
    url: '/api/secure/plants/info',
    method: 'delete',
    params: {
      id: id,
      status: status
    }
  })
}
