var Mock = require('mockjs');
var qs = require('qs');

let clusterMonitorAlarmTaskData = Mock.mock({
    'data|8': [
        {
            'clusterId|+1': 1,
            'id|1-1000': 200,
            clusterName: '@cname',
            'indicatorName|2': '@last',
            'alertValue|1-100': 100,
            'alertInterval|1-200': 100,
            'alertFrequency|1-200': 100,
            'alertForm|0-1': 0,
            param1: '@cname'
        }
    ]
});

module.exports = {
    'GET /zkdoctor/monitor/getZkMonitorTaskDataByClusterId': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = clusterMonitorAlarmTaskData.data;
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
    }

};
