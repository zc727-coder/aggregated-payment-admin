import request from '@/router/axios';
export const getList = (current, size, params) => {
    return request({
        url: '/api/blade-manager/app/list',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const getPayConfigList = (current, size, params) => {
    return request({
        url: '/api/blade-manager/pay/config/list',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const getDetail = (id) => {
    return request({
        url: '/api/blade-manager/app/detail',
        method: 'get',
        params: {
            id,
        }
    })
}
export const getPpayConfigDetail = (appid,ifCode) => {
    return request({
        url: '/api/blade-manager/pay/configs/'+appid+'/'+ifCode,
        method: 'get'
    })
}
export const remove = (ids) => {
    return request({
        url: '/api/blade-manager/app/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const submit = (row) => {
    return request({
        url: '/api/blade-manager/app/submit',
        method: 'post',
        data: row
    })
}

export const payOnSubmit = (row) => {
    return request({
        url: '/api/blade-manager/pay/configs/payOnSubmit',
        method: 'post',
        data: row
    })
}