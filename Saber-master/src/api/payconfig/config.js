import request from '@/router/axios';
export const getList = (current, size, params) => {
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
        url: '/api/blade-manager/pay/config/detail',
        method: 'get',
        params: {
            id,
        }
    })
}
export const getWayCodeDetail = () => {
    return request({
        url: '/api/blade-manager/pay/way/codeDetail',
        method: 'get',
    })
}
export const remove = (ids) => {
    return request({
        url: '/api/blade-manager/pay/config/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const picUpload = (form) => {
    return request({
        headers:{'Content-Type': 'multipart/form-data'},
        url: '/api/blade-oss/ossFiles/ifBG',
        method: 'post',
        data:form
    })
}
export const submit = (row) => {
    return request({
        url: '/api/blade-manager/pay/config/submit',
        method: 'post',
        data: row
    })
}
