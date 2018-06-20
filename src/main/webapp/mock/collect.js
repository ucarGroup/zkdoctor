var Mock = require('mockjs');
var qs = require('qs');

let collectsData = Mock.mock({
    'collects|3': [
        {
            'id|+1': 1,
            clusterName: "@cname",
            infoContent: "@FIRST",
            alarmTime: '2018 06-01 00:00:00',
        }
    ],
    all: {
        clusterCollectInfo: {
            sumTotal: '@integer(1,5000)',
            runningTotal: '@integer(1,5000)',
            notRunningTotal: '@integer(1,5000)',
            exceptionsTotal: '@integer(1,5000)',
            referralTotal: '@integer(1,5000)',
            unmonitoredTotal: '@integer(1,5000)',
        },
        instanceCollectInfo: {
            sumTotal: '@integer(1,5000)',
            runningTotal: '@integer(1,5000)',
            notRunningTotal: '@integer(1,5000)',
            exceptionsTotal: '@integer(1,5000)',
            referralTotal: '@integer(1,5000)',
            unmonitoredTotal: '@integer(1,5000)',
        },
    },
});

module.exports = {
    'GET /zkdoctor/alarmInfo/getAllZkAlarmInfo': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = collectsData.collects;
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
    'GET /zkdoctor/alarmInfo/getCollectInfo': {
        success: true,
        data: {
            data: collectsData.all
        },
        message: ''
    },
};
