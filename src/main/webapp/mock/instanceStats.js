var Mock = require('mockjs');
var qs = require('qs');

let instanceStatsData = Mock.mock({
    chartData: {
        time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
        value: {
            '127.0.0.1:2181': [1832, 2324, 5451, 4311, 431, 461],
            '127.0.0.2:2181': [1242, 2141, 3556, 2341, 512, 341],
            '127.0.0.3:2181': [953, 1943, 2415, 2100, 214, 312],
        }
    },
    chartData1: {
        time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
        value: {
            '127.0.0.1:2181': [832, 2324, 451, 311, 431, 461],
            '127.0.0.2:2181': [242, 2141, 556, 341, 512, 341],
            '127.0.0.3:2181': [53, 1943, 415, 100, 214, 312],
        }
    },
    initChartData: {
        netTraffic: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: {
                '写流量': [1832, 2324, 5451, 4311, 431, 461],
                '读流量': [1242, 2141, 3556, 2341, 512, 341],
            }
        },
        avgLoad: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [18.32, 23.24, 54.51, 43.11, 43.1, 46.1],
        },
        cpu: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: [13.2, 2.24, 4.51, 41.1, 4.1, 61],
        },
        cpuSingle: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: {
                'CPU 0': [20.12, 24.98, 45.09, 23.98, 2.89, 87.9],
                'CPU 1': [34.82, 19.43, 4.15, 10.01, 2.14, 3.12],
                'CPU 2': [12.42, 21.41, 35.56, 23.41, 5.12, 3.41],
                'CPU 3': [18.32, 23.24, 54.51, 43.11, 4.31, 46.1],
            }
        },
        memory: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: {
                'total': [20.12, 24.98, 45.09, 23.98, 2.89, 87.9],
                'used': [34.82, 19.43, 4.15, 10.01, 2.14, 3.12],
            }
        },
        disk: {
            time: ['2018-01-25 00:00', '2018-01-25 00:05', '2018-01-25 00:10', '2018-01-25 00:15', '2018-01-25 00:20', '2018-01-25 00:25'],
            value: {
                'total': [20.12, 24.98, 45.09, 23.98, 2.89, 87.9],
                'used': [34.82, 19.43, 4.15, 10.01, 2.14, 3.12],
            }
        },
    },
    trafficDetail: {
        trafficInDetail: "写流量",
        trafficOutDetail: "读流量"
    },
});

module.exports = {
    'GET /zkdoctor/instance/stat/receivedAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/sendAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/outstandingsAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/maxLatencyAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/avgLatencyAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/znodeCountAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/ephemeralsdAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/watcherCountAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/connectionsAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/minLatencyAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/approximateDataSizeAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/openFileDescriptorCountAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/maxFileDescriptorCountAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/followersAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/syncedFollowersAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/pendingSyncsAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/stat/serverStateLagAllIns/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.chartData1,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/init/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/netTraffic/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData.netTraffic,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/avgLoad/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData.avgLoad,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/cpu/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData.cpu,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/cpuSingle/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData.cpuSingle,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/memory/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData.memory,
            message: ''
        })
    },
    'GET /zkdoctor/instance/machine/disk/trend': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.initChartData.disk,
            message: ''
        })
    },
    'GET /zkdoctor/instance/queryNetTrafficDetail': function (req, res) {
        res.json({
            success: true,
            data: instanceStatsData.trafficDetail,
            message: ''
        })
    },
};
