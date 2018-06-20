var Mock = require('mockjs');
var qs = require('qs');

let machiensData = Mock.mock({
    'machines|100': [
        {
            machineInfo: {
                'id|+1': 1,
                host: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                memory: '@integer(1,4096)',
                cpu: '@integer(1,10)',
                'virtual|1': [false, true],
                realHost: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                room: "测试",
                'monitor|1': [false, true],
                'available|1': [false, true],
                'serviceLine|1-7': 1,
                hostDomain: "test.com",
                createTime: Mock.Random.datetime(),
                createTimeStr: "2018-02-22 15:33:28",
                modifyTime: Mock.Random.datetime(),
                modifyTimeStr: "2018-02-22 15:33:28",
            },
            machineState: {
                'id|+1': 1,
                'machineId|+1': 1,
                host: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                'cpuUsage|0-99.2': 11.11,
                avgLoad: '12',
                netTraffic: '454621',
                'memoryUsage|0-99.2': 11.11,
                memoryFree: '23423',
                memoryTotal: '2334545',
                'diskUsage|0-99.2': 11.11,
                dataDiskUsed: 1024,
                dataDiskTotal: 10240,
                createTime: Mock.Random.datetime(),
                createTimeStr: "2018-02-22 15:33:28",
                modifyTime: Mock.Random.datetime(),
                modifyTimeStr: "2018-02-22 15:33:28",
            }
        }],
});

module.exports = {
    'GET /zkdoctor/machine/query': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = machiensData.machines;
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
    'POST /zkdoctor/manage/machine/modifyMachine': {
        success: true,
        message: '修改机器信息成功'
    },
    'GET /zkdoctor/manage/machine/deleteMachine': {
        success: true,
        message: '删除机器成功'
    },
    'POST /zkdoctor/manage/machine/addMachine': {
        success: true,
        message: '新加机器成功'
    },
    'GET /zkdoctor/manage/machine/updateMonitorStatus': {
        success: true,
        message: ''
    },
    'GET /zkdoctor/manage/machine/supermanage/sshCommandExecute': {
        success: true,
        data: {
            data: 'ssh success',
            currentDirectory: '/usr/local'
        },
        message: ''
    },
};
