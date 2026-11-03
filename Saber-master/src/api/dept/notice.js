import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/blade-desk/merchant/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/blade-desk/merchant/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/blade-desk/merchant/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/blade-desk/merchant/submit',
    method: 'post',
    data: row
  })
}

export const getmerchant = (id) => {
  return request({
    url: '/api/blade-desk/merchant/detail',
    method: 'get',
    params: {
      id
    }
  })
}

