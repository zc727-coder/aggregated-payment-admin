import Mock from 'mockjs'
function getFakeList() {
    const json = { code: 200, success: true, msg: '操作成功' };
    const list = [];
    list.push(
        {
            id: '1',
            merchantName: '猪杂粉',
            merchantNo: '10000001',
            serverNo: '10000001',
            state: true,
            type: '普通商户',
            time: '2019.09.26',
        },
        {
            id: '2',
            merchantName: 'it培训机构',
            merchantNo: '10000002',
            serverNo: '10000002',
            state: false,
            type: '特约商户',
            time: '2022-09-26',
        },

    );
    json.data = {
        total: 10,
        size: 10,
        current: 1,
        searchCount: true,
        pages: 1,
        records: list,
    };
    return json;
}
function getFakeDetail() {
    const json = { code: 200, success: true, msg: '操作成功' };
    json.data = {
        id: "1",
        merchantName: "云码教育",
        account:"sy",
        nickname:"上云it",
        concatName:'上云',
        concatEmail:'sy@163.com',
        concatPhone:'666',
        // merchantNo: "",
        // serverNo: "",
        state: true,
        type: "1",
        remark:'测试',
    };
    return json;
}
function fakeSuccess() {
    return { code: 200, success: true, msg: '操作成功' };
}
export default ({ mock }) => {
    if (!mock) return;
    Mock.mock(/\/api\/blade-desk\/blog\/list/, 'get', getFakeList);
    Mock.mock(/\/api\/blade-desk\/blog\/detail/, 'get', getFakeDetail);
    Mock.mock(/\/api\/blade-desk\/blog\/submit/, 'post', fakeSuccess);
    Mock.mock(/\/api\/blade-desk\/blog\/remove/, 'post', fakeSuccess);
    // Mock.mock('/api/blade-desk/blog/list', 'get', getFakeList);
    // Mock.mock('/api/blade-desk/blog/detail', 'get', getFakeDetail);
    // Mock.mock('/api/blade-desk/blog/submit', 'post', fakeSuccess);
    // Mock.mock('/api/blade-desk/blog/remove', 'post', fakeSuccess);
}