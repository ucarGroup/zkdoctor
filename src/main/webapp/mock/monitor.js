var Mock = require('mockjs');
var qs = require('qs');

let monitorData = Mock.mock({
    'indicators|100': [
        {
            'id|+1': 1,
            indicatorName: '监控指标',
            className: 'class',
            defaultAlertValue: '100',
            defaultAlertInterval: '@integer(1,100)',
            defaultAlertFrequency: '@integer(1,100)',
            'defaultAlertForm|1-2': 1,
            alertValueUnit: '个',
            'switchOn|1': [false, true],
            'modifyUserId|+1': 1,
            info: '测试',
            createTime: Mock.Random.datetime(),
            createTimeStr: "2018-02-22 15:33:28",
            modifyTime: Mock.Random.datetime(),
            modifyTimeStr: "2018-02-22 15:33:28",
        }
    ],
    'tasks|100': [
        {
            'id|+1': 1,
            'indicatorId|+1': 1,
            'clusterId|+1': 1,
            alertValue: '100',
            alertInterval: '@integer(1,100)',
            alertFrequency: '@integer(1,100)',
            'alertForm|1-2': 1,
            'switchOn|1': [false, true],
            'modifyUserId|+1': 1,
            createTime: Mock.Random.datetime(),
            createTimeStr: "2018-02-22 15:33:28",
            modifyTime: Mock.Random.datetime(),
            modifyTimeStr: "2018-02-22 15:33:28",
        }
    ],
});

module.exports = {
    'GET /zkdoctor/monitor/indicator/query': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = monitorData.indicators;
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
    'GET /zkdoctor/monitor/tasks/query': function (req, res) {
        const page = qs.parse(req.query);
        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = monitorData.tasks;
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
    'GET /zkdoctor/manage/monitor/indicator/updateSwitchOn': {
        success: true,
        message: ''
    },
    'GET /zkdoctor/monitor/task/updateSwitchOn': {
        success: true,
        message: ''
    },
    'POST /zkdoctor/manage/monitor/indicator/updateIndicator': {
        success: true,
        message: '修改监控指标信息成功！'
    },
    'POST /zkdoctor/monitor/task/updateTask': {
        success: true,
        message: '修改监控任务信息成功！'
    },

};
