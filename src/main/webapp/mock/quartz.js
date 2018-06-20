var Mock = require('mockjs');
var qs = require('qs');

let triggerData = Mock.mock({
    'triggers|100': [
        {
            id: '@last',
            triggerName: '@last',
            triggerGroup: '@last',
            cron: '0 0 1 ? * *',
            nextFireTimeStr: '2018-02-05 18:39:20',
            prevFireTimeStr: '2018-02-05 18:39:20',
            startTimeStr: '2018-02-05 18:39:20',
            triggerState: 'WAITING',
            description: '定时任务'
        }]
});

module.exports = {
    'GET /zkdoctor/manage/quartz/query': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = triggerData.triggers;
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
    'GET /zkdoctor/manage/quartz/pauseTrigger': {
        success: true,
        message: '暂停定时任务成功'
    },
    'GET /zkdoctor/manage/quartz/resumeTrigger': {
        success: true,
        message: '恢复定时任务成功'
    },
    'GET /zkdoctor/manage/quartz/removeTrigger': {
        success: true,
        message: '删除定时任务成功'
    },

};
