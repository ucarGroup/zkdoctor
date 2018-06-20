var Mock = require('mockjs');
var qs = require('qs');

let clusterOpsData = Mock.mock({
    'dataExpand': {
        data: ["扩容开始...",
            "正在扩容：正在启动新服务器...",
            "正在扩容：正在扩容learner实例...",
            "正在扩容：扩容learner实例成功",
            "正在扩容：正在扩容leader实例...",
            "正在扩容：扩容leader实例失败",
            "扩容失败！",
        ],
        isClear: true
    },
    'dataDeploy': {
        data: ["部署开始...",
            "正在部署：正在部署新服务器...",
            "正在部署：正在部署实例...",
            "部署失败！",
        ],
        isClear: true
    },
    'dataRestart': {
        data: ["重启开始...",
            "正在重启：正在重启新服务器...",
            "正在重启：正在重启learner实例...",
            "正在重启：重启learner实例成功",
            "正在重启：正在重启leader实例...",
            "正在重启：重启leader实例失败",
            "重启失败！",
        ],
        isClear: true
    }
});

module.exports = {
    'POST /zkdoctor/manage/cluster/clusterDeploy': {
        success: true,
        message: ''
    },
    'POST /zkdoctor/manage/cluster/dynamicExpansion': {
        success: true,
        message: '动态扩容成功！'
    },
    'POST /zkdoctor/manage/cluster/offLine': {
        success: true,
        message: '下线集群成功'
    },
    'POST /zkdoctor/manage/cluster/restartQuorum': {
        success: true,
        message: '重启集群成功'
    },
    'GET /zkdoctor/manage/cluster/updateMonitorStatus': {
        success: true,
        message: ''
    },
    'GET /zkdoctor/manage/cluster/deleteZnode': {
        success: true,
        message: '删除节点成功'
    },
    'POST /zkdoctor/manage/cluster/updateZnode': {
        success: true,
        message: '更新节点成功'
    },
    'POST /zkdoctor/manage/cluster/createZnode': {
        success: true,
        message: '创建节点成功'
    },
    'POST /zkdoctor/manage/cluster/getClusterDynamicExpansionResult': {
        success: true,
        data: clusterOpsData.dataExpand,
        message: ''
    },
    'POST /zkdoctor/manage/cluster/getClusterRestartResult': {
        success: true,
        data: clusterOpsData.dataRestart,
        message: ''
    },
    'GET /zkdoctor/manage/cluster/getClusterDeployResult': {
        success: true,
        data: clusterOpsData.dataDeploy,
        message: ''
    },
};