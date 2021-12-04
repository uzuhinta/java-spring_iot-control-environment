import request from '@/utils/request'

export function getUsers(data) {
  return request({
    url: '/api/admin/users',
    method: 'get',
    params: data
  })
}

export function updateUser(data) {
  return request({
    url: '/api/secure/users/info',
    method: 'put',
    data
  })
}

export function deleteUser(id, status) {
  return request({
    url: '/api/admin/users/change_status',
    method: 'delete',
    params: {
      id: id,
      status: status
    }
  })
}

export function createUser(data) {
  return request({
    url: '/api/auth/signUp',
    method: 'post',
    data
  })
}

export function resetPass(username, password) {
  return request({
    url: '/api/admin/users/reset_pass',
    method: 'post',
    data: {
      username: username,
      password: password
    }
  })
}

export function changePass(data) {
  return request({
    url: '/api/secure/users/change_pass',
    method: 'post',
    data
  })
}

export function login(data) {
  return request({
    url: '/api/auth/signIn',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/api/secure/users/info',
    method: 'get',
    headers: { 'Authorization': 'Bearer ' + token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}
