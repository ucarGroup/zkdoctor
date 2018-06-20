var Mock = require('mockjs');
var qs = require('qs');

let instanceData = Mock.mock({
        'instances|5': [
            {
                instanceInfo: {
                    'id|+1': 1,
                    'clusterId|+1': 1,
                    'machineId|+1': 1,
                    clusterName: 'test',
                    host: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                    port: 2181,
                    'connectionMonitor|1': [true, false],
                    'deployType|1-2': 1,
                    'serverStateLag|0-3': 0,
                    'status|1': [0, 1, 3, 4],
                    createTime: Mock.Random.datetime(),
                    createTimeStr: "2018-02-22 15:33:28",
                    modifyTime: Mock.Random.datetime(),
                    modifyTimeStr: "2018-02-22 15:33:28",
                },
                instanceState: {
                    'id|+1': 1,
                    'instanceId|+1': 1,
                    'clusterId|+1': 1,
                    version: '3.4.10',
                    leaderId: 1,
                    avgLatency: '@integer(1,100000)',
                    maxLatency: '@integer(1,100000)',
                    minLatency: '@integer(1,100000)',
                    received: '@integer(1,100000)',
                    sent: '@integer(1,100000)',
                    currConnections: '@integer(1,100000)',
                    currOutstandings: '@integer(1,100000)',
                    'serverStateLag|0-3': 0,
                    currZnodeCount: '@integer(1,100000)',
                    currWatchCount: '@integer(1,100000)',
                    currEphemeralsCount: '@integer(1,100000)',
                    approximateDataSize: '@integer(1,100000)',
                    openFileDescriptorCount: '@integer(1,100000)',
                    maxFileDescriptorCount: '@integer(1,100000)',
                    followers: '@integer(1,100000)',
                    syncedFollowers: '@integer(1,100000)',
                    pendingSyncs: '@integer(1,100000)',
                    zxid: '@integer(1,100000)',
                    runOk: true | false,
                    createTime: Mock.Random.datetime(),
                    createTimeStr: "2018-02-22 15:33:28",
                    modifyTime: Mock.Random.datetime(),
                    modifyTimeStr: "2018-02-22 15:33:28",
                }
            }],
        'instanceInfos|5': [{
            'id|+1': 1,
            'clusterId|+1': 1,
            'machineId|+1': 1,
            host: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
            port: 2181,
            'connectionMonitor|1': [true, false],
            'deployType|1-2': 1,
            'serverStateLag|0-3': 0,
            'status|1': [0, 1, 3, 4],
            createTime: Mock.Random.datetime(),
            createTimeStr: "2018-02-22 15:33:28",
            modifyTime: Mock.Random.datetime(),
            modifyTimeStr: "2018-02-22 15:33:28",
        }],
        instanceState: {
            'id|+1': 1,
            'instanceId|+1': 1,
            'clusterId|+1': 1,
            version: '3.4.10',
            leaderId: 1,
            avgLatency: '@integer(1,100000)',
            maxLatency: '@integer(1,100000)',
            minLatency: '@integer(1,100000)',
            received: '@integer(1,100000)',
            sent: '@integer(1,100000)',
            currConnections: '@integer(1,100000)',
            currOutstandings: '@integer(1,100000)',
            'serverStateLag|0-3': 0,
            currZnodeCount: '@integer(1,100000)',
            currWatchCount: '@integer(1,100000)',
            currEphemeralsCount: '@integer(1,100000)',
            approximateDataSize: '@integer(1,100000)',
            openFileDescriptorCount: '@integer(1,100000)',
            maxFileDescriptorCount: '@integer(1,100000)',
            followers: '@integer(1,100000)',
            syncedFollowers: '@integer(1,100000)',
            pendingSyncs: '@integer(1,100000)',
            zxid: '@integer(1,100000)',
            runOk: true | false,
            createTime: Mock.Random.datetime(),
            createTimeStr: "2018-02-22 15:33:28",
            modifyTime: Mock.Random.datetime(),
            modifyTimeStr: "2018-02-22 15:33:28",
        },
        instanceConfig: {
            'clusterId|+1': 1,
            'instanceId|+1': 1,
            clientPort: 2181,
            dataDir: '/usr/local/data',
            dataLogDir: '/usr/local/data',
            tickTime: 2000,
            maxClientCnxns: 1000,
            minSessionTimeout: 4000,
            maxSessionTimeout: 100000,
            'serverId|1-2': 1,
            initLimit: 10,
            syncLimit: 5,
            electionAlg: 3,
            electionPort: 5008,
            quorumPort: 6008,
            'peerType|0-1': 1,
        },
        'instanceConnections|50': [
            {
                hostInfo: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                ip: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                port: '@integer(1,5000)',
                recved: '@integer(1,10000)',
                sent: '@integer(1,10000)',
                queued: '@integer(1,100)',
                'lop|1': ['PING', 'SETD', 'GETD', 'GETC'],
                maxlat: '@integer(1,10000)',
                avglat: '@integer(1,10000)',
                lzxid: '123456789',
                'to|+1000': '@integer(4000,100000)',
                infoLine: 'null[0](queued=0,recved=7,sent=7,sid=0x15c3849316fdf03,lop=PING,est=1503026322076,to=10000,lcxid=0x0,lzxid=0x4ce3f0f9,lresp=1503026338765,llat=0,minlat=0,avglat=0,maxlat=0)',
            }
        ],
        'instanceConnectionsHistory|50': [
            {
                'key|+1': 1,
                'id|+1': 1,
                'clusterId|+1': 1,
                'instanceId|+1': 1,
                clientIp: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                clientPort: 2181,
                sid: '0x124',
                queued: '@integer(1,5000)',
                sent: '@integer(1,5000)',
                est: '@integer(1,5000)',
                estDate: '2018',
                toTime: '@integer(1,5000)',
                lcxid: '0x124',
                lzxid: '0x124',
                lresp: '@integer(1,5000)',
                lrespStr: '2018',
                llat: '@integer(1,5000)',
                minlat: '@integer(1,5000)',
                avglat: '@integer(1,5000)',
                maxlat: '@integer(1,5000)',
                createTimeStr: '2018',
                info: '这是测试',
            }
        ]
    })
;

module.exports = {
    'GET /zkdoctor/instance/queryByClusterId': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = instanceData.instances;
        let result = data.length > pageSize ? data.slice((pageNum - 1) * pageSize, pageNum * pageSize) : data.slice();
        res.json({
            success: true,
            data: result,
            page: {
                pageSize: parseInt(pageSize),
                pageNum: parseInt(pageNum),
                total: data.length
            }
        })
    },
    'GET /zkdoctor/instance/queryByInstanceStatus': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = instanceData.instances;
        let result = data.length > pageSize ? data.slice((pageNum - 1) * pageSize, pageNum * pageSize) : data.slice();
        res.json({
            success: true,
            data: result,
            page: {
                pageSize: parseInt(pageSize),
                pageNum: parseInt(pageNum),
                total: data.length
            }
        })
    },
    'GET /zkdoctor/instance/instanceDetailInfo': {
        success: true,
        data: instanceData.instances[0],
        message: ''
    },
    'GET /zkdoctor/instance/state': {
        success: true,
        data: instanceData.instanceState,
        message: ''
    },
    'POST /zkdoctor/instance/edit': {
        success: true,
        message: ''
    },
    'GET /zkdoctor/instance/instanceConfig': {
        success: true,
        data: instanceData.instanceConfig,
        message: ''
    },
    'GET /zkdoctor/instance/instanceConnections': {
        success: true,
        data: instanceData.instanceConnections,
        message: ''
    },
    'GET /zkdoctor/manage/instance/updateInstanceConnCollectMonitor': {
        success: true,
        message: ''
    },
    'GET /zkdoctor/instance/searchConnectionHistory': {
        success: true,
        data: instanceData.instanceConnectionsHistory,
        message: ''
    },
    'GET /zkdoctor/machine/queryMachineInstances': {
        success: true,
        data: {data: instanceData.instanceInfos},
        message: ''
    },
    'GET /zkdoctor/instance/getConnectionCollectTime': {
        success: true,
        data: "2018-04-09 18:00:00",
        message: ''
    },

};
