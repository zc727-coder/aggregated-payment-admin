import request from '@/router/axios';
export const getList = (merchantID) => {
    return request({
        url: '/api/blade-manager/app/list/byMerNo',
        method: 'get'
    })
}
export const submit = (row) => {
    return request({
        url: '/api/blade-manager/pay/order/api/pay/unifiedOrder',
        method: 'post',
        data: row
    })
}