var Mock = require('mockjs');
var qs = require('qs');

let clusterStatsData = Mock.mock({
    'data|100': [
        {
            ip: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
            name: '@last',
            count: '@integer(1,500)',
            timeCount: '@float(1,500)',
            time: '@date(\'yyyy-MM-dd HH:mm:ss\')'
        }
    ],
    trendChartData: {
        time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
        value: [12363, 4238, 1310, 34124, 1441, 5187],
    },
    trendChartData1: {
        time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
        value: [345, 567, 500, 565, 345, 234],
    },
    initChartData: {
        received: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [363, 438, 310, 124, 1141, 587],
        },
        outstandings: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [1832, 2324, 5451, 4311, 431, 461],
        },
        maxLatency: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [1242, 2141, 3556, 2341, 512, 341],
        },
        avgLatency: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [2335, 2325, 4567, 2351, 5611, 345],
        },
        znodeCount: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [1113, 1999, 3921, 3211, 374, 410],
        },
        ephemerals: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [235, 235, 457, 251, 561, 35],
        },
        watcherCount: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [953, 1943, 2415, 2100, 214, 312],
        },
        connections: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [235, 325, 467, 231, 611, 145],
        }
    },
});

module.exports = {
    'GET /zkdoctor/cluster/stat/init/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.initChartData,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/received/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/outstandings/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData1,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/maxLatencyMax/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData1,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/avgLatencyMax/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/znodeCount/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData1,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/ephemerals/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/watcherTotal/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData,
            message: ''
        })
    },
    'GET /zkdoctor/cluster/stat/connectionTotal/trend': function (req, res) {
        res.json({
            success: true,
            data: clusterStatsData.trendChartData1,
            message: ''
        })
    },
};
