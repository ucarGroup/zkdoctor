var Mock = require('mockjs');
var qs = require('qs');

let clustersData = Mock.mock({
    'data|100': [
        {
            'id|+1': 1,
            'clusterId|+1': 1,
            clusterName: '@last',
            intro: '测试环境zk',
            znodeCount: '@integer(1,1000)',
            requestCount: '@integer(1,100000)',
            connections: '@integer(1,100)',
            collectTime: '2018 01-01 00:00:00',
            version: "3.4.10",
            'status|1-4': 1,
            'serviceLine|0-7': 1,
            clusterInfo: {
                'id|+1': 1,
                clusterName: '@last',
                officer: '@last',
                instanceNumber: '@integer(1,100)',
                'status|1-4': 1,
                'deployType|1-2': 1,
                'serviceLine|1-7': 1,
                version: '3.4.10',
                intro: '测试环境zk',
                createTime: Mock.Random.datetime(),
                createTimeStr: "2018-02-22 15:33:28",
                modifyTime: Mock.Random.datetime(),
                modifyTimeStr: "2018-02-22 15:33:28",
            }
        }
    ],
    clusterDetail: {
        clusterInfo: {
            'id|+1': 1,
            clusterName: '@last',
            officer: '@last',
            instanceNumber: '@integer(1,100)',
            'status|1-4': 1,
            'deployType|1-2': 1,
            'serviceLine|1-7': 1,
            version: '3.4.10',
            intro: '测试环境zk',
            createTime: Mock.Random.datetime(),
            createTimeStr: "2018-02-22 15:33:28",
            modifyTime: Mock.Random.datetime(),
            modifyTimeStr: "2018-02-22 15:33:28",
        },
        clusterState: {
            'clusterId|+1': 1,
            instanceNumber: '@integer(1,100)',
            avgLatencyMax: '@integer(1,10000)',
            maxLatencyMax: '@integer(1,10000)',
            minLatencyMax: '@integer(1,10000)',
            receivedTotal: '@integer(1,10000)',
            sentTotal: '@integer(1,10000)',
            connectionTotal: '@integer(1,10000)',
            znodeCount: '@integer(1,10000)',
            watcherTotal: '@integer(1,10000)',
            ephemerals: '@integer(1,10000)',
            outstandingTotal: '@integer(1,10000)',
            approximateDataSize: '@integer(1,10000)',
            openFileDescriptorCountTotal: '@integer(1,10000)',
            modifyTime: Mock.Random.datetime(),
            modifyTimeStr: "2018-02-22 15:33:28",
        }
    },
    'alarmUsers|10': [
        {
            'id|+1': 1,
            'userId|+1': 10,
            'clusterId|+1': 1,
            mobile: /^1[34578]\d{9}$/,
            chName: '@cname',
            userName: '@last',
            email: '@email',
        }],
    'clusterRootZnodes|5': [
        {
            title: '/' + '@last',
            key: '/' + '@last' + '/' + '@last',
            isLeaf: false,
        }],
    'clusterZnodesChildren|3': [
        {
            title: '/' + '@last' + '/' + '@last',
            key: '/' + '@last' + '/' + '@last' + '/' + '@last',
        }],
    znodeData: {
        data: 'test data',
        czxid: '0x123456',
        mzxid: '0x454672',
        ctime: '2018-02-02 15:15:15',
        mtime: '2018-02-02 15:15:15',
        version: 12,
        cversion: 34,
        aversion: 24,
        ephemeralOwner: '0x34736573',
        dataLength: 500,
        numChildren: 3546,
        pzxid: '0x454672',
    }

});

module.exports = {
    'GET /zkdoctor/cluster/query': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = clustersData.data;
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
    'GET /zkdoctor/cluster/info': {
        success: true,
        data: clustersData.data[0],
        message: ''
    },
    'GET /zkdoctor/cluster/detail': {
        success: true,
        data: clustersData.clusterDetail,
        message: ''
    },
    'GET /zkdoctor/cluster/clusterAlarmUsers': {
        success: true,
        data: clustersData.alarmUsers,
        message: ''
    },
    'POST /zkdoctor/cluster/deleteAlarmUser': {
        success: true,
        message: ''
    },
    'POST /zkdoctor/cluster/addClusterAlarmUser': {
        success: true,
        data: 11,
        message: ''
    },
    'GET /zkdoctor/cluster/checkClusterNameExist': function (req, res) {
        res.json({
            success: true,
            data: false,
        });
    },
    'POST /zkdoctor/manage/cluster/addCluster': {
        success: true,
        message: ''
    },
    'GET /zkdoctor/manage/cluster/modifyCluster': {
        success: true,
        message: '修改集群信息成功'
    },
    'GET /zkdoctor/cluster/clusterRootZnodes': {
        success: true,
        data: clustersData.clusterRootZnodes,
        message: ''
    },
    'GET /zkdoctor/cluster/clusterZnodesChildren': {
        success: true,
        data: clustersData.clusterZnodesChildren,
        message: ''
    },
    'GET /zkdoctor/cluster/searchZnodeData': {
        success: true,
        data: clustersData.znodeData,
        message: ''
    },

};
