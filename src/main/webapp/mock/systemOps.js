var Mock = require('mockjs');
var qs = require('qs');

let systemOpsData = Mock.mock({
    'systemConfigList|5': [{
        'id|+1': 1,
        configName: '@last',
        configValue: '@last',
        defaultConfigValue: '@last',
        configDesc: '@last',
    }],
    'serviceLineList|5': [{
        'id|+1': 1,
        serviceLineName: '@last',
        serviceLineDesc: '@last',
        createTimeStr: '2018-00-00-00 00:00:00',
        modifyTimestr: '2018-00-00-00 00:00:00',
    }],
});

module.exports = {
    'GET /zkdoctor/manage/system/config/query': function (req, res) {
        const page = qs.parse(req.query);
        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = systemOpsData.systemConfigList;
        let result = data.length > pageSize ? data.slice((pageNum - 1) * pageSize, pageNum * pageSize) : data.slice();
        res.json({
            success: true,
            data: {
                data: result,
                page: {
                    pageSize: parseInt(pageSize),
                    pageNum: parseInt(pageNum),
                    total: data.length
                }
            }
        })
    },
    'POST /zkdoctor/manage/system/config/update': {
        success: true,
        message: '更新配置成功！'
    },
    'POST /zkdoctor/manage/system/config/delete': {
        success: true,
        message: '删除配置值成功！'
    },
    'GET /zkdoctor/cluster/serviceLine/query': function (req, res) {
        const page = qs.parse(req.query);
        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = systemOpsData.serviceLineList;
        let result = data.length > pageSize ? data.slice((pageNum - 1) * pageSize, pageNum * pageSize) : data.slice();
        res.json({
            success: true,
            data: {
                data: result,
                page: {
                    pageSize: parseInt(pageSize),
                    pageNum: parseInt(pageNum),
                    total: data.length
                }
            }
        })
    },
    'POST /zkdoctor/manage/system/serviceLine/insert': {
        success: true,
        message: '新增业务线成功！'
    },
    'POST /zkdoctor/manage/system/serviceLine/update': {
        success: true,
        message: '更新业务线成功！'
    },
    'POST /zkdoctor/manage/system/serviceLine/delete': {
        success: true,
        message: '删除业务线成功！'
    },
};