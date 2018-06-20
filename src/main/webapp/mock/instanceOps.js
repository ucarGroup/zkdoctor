var Mock = require('mockjs');
var qs = require('qs');

let instanceOpsData = Mock.mock({
    'data': [{
        uid: 1,
        name: 'zookeeper-3.4.10.jar',
        status: 'done',
        reponse: 'success',
        url: '/usr/local/zookeeper-3.4.10.jar',
    }]
});

module.exports = {
    'POST /zkdoctor/manage/instance/removeInstance': {
        success: true,
        message: '移除实例成功！'
    },
    'POST /zkdoctor/manage/instance/offLineInstance': {
        success: true,
        message: '下线实例成功！'
    },
    'POST /zkdoctor/manage/instance/restartInstance': {
        success: true,
        message: '重启实例成功！'
    },
    'POST /zkdoctor/manage/instance/queryInstanceConfig': {
        success: true,
        data: 'server.1=127.0.0.1:5008:6008',
        message: ''
    },
    'POST /zkdoctor/manage/instance/instanceConfOps': {
        success: true,
        message: '修改配置成功！'
    },
    'POST /zkdoctor/manage/instance/addNewInstance': {
        success: true,
        message: '新增实例成功！'
    },
    'POST /zkdoctor/manage/instance/addNewConfigFile': {
        success: true,
        message: '新增配置文件成功！'
    },
    'POST /zkdoctor/manage/instance/uploadNewJarFile': {
        success: true,
        message: '',
        data: {status: 'done'}
    },
    'GET /zkdoctor/manage/instance/queryUploadedJarFile': {
        success: true,
        data: instanceOpsData.data,
        message: ''
    },
    'POST /zkdoctor/manage/instance/instanceUpdateServer': {
        success: true,
        message: '升级成功！'
    },

};