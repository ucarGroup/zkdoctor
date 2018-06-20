var Mock = require('mockjs');
var qs = require('qs');

let generalTreeList = Mock.mock({
    data: [{
        "key": "026001",
        "name": "Dashboard",
        "url": "/dashboard",
        "extra": "laptop",
        "order": 1,
        "children": []
    }, {
        "key": "026002",
        "name": "集群管理",
        "url": null,
        "extra": "cloud-download-o",
        "order": 2,
        "children": [{
            "key": "026002001",
            "name": "集群列表",
            "url": "/cluster/list",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }, {
        "key": "026004",
        "name": "监控报警",
        "url": null,
        "extra": "notification",
        "order": 4,
        "children": [{
            "key": "026004001",
            "name": "监控报警",
            "url": "/monitor/list",
            "extra": null,
            "order": 1,
            "children": []
        },{
            "key": "026004002",
            "name": "报警历史",
            "url": "/monitor/alarmHistory",
            "extra": null,
            "order": 2,
            "children": []
        }]
    }, {
        "key": "026005",
        "name": "机器管理",
        "url": null,
        "extra": "desktop",
        "order": 5,
        "children": [{
            "key": "026005001",
            "name": "机器列表",
            "url": "/machine/list",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }]
});

let adminTreeList = Mock.mock({
    data: [{
        "key": "026001",
        "name": "Dashboard",
        "url": "/dashboard",
        "extra": "laptop",
        "order": 1,
        "children": []
    }, {
        "key": "026002",
        "name": "集群管理",
        "url": null,
        "extra": "cloud-download-o",
        "order": 2,
        "children": [{
            "key": "026002001",
            "name": "集群列表",
            "url": "/cluster/list",
            "extra": null,
            "order": 1,
            "children": []
        }, {
            "key": "026002002",
            "name": "添加集群",
            "url": "/cluster/add",
            "extra": null,
            "order": 2,
            "children": []
        }]
    }, {
        "key": "026003",
        "name": "运维管理",
        "url": null,
        "extra": "tool",
        "order": 3,
        "children": [{
            "key": "026003001",
            "name": "集群运维",
            "url": "/manage/cluster/opsManage",
            "extra": null,
            "order": 1,
            "children": []
        }, {
            "key": "026003002",
            "name": "服务部署",
            "url": "/manage/cluster/opsDeploy",
            "extra": null,
            "order": 2,
            "children": []
        }, {
            "key": "026003003",
            "name": "系统配置",
            "url": "/manage/system/configlist",
            "extra": null,
            "order": 3,
            "children": []
        }, {
            "key": "026003004",
            "name": "业务线配置",
            "url": "/manage/system/serviceLineList",
            "extra": null,
            "order": 4,
            "children": []
        }]
    }, {
        "key": "026004",
        "name": "监控报警",
        "url": null,
        "extra": "notification",
        "order": 4,
        "children": [{
            "key": "026004001",
            "name": "监控报警",
            "url": "/monitor/list",
            "extra": null,
            "order": 1,
            "children": []
        },{
            "key": "026004002",
            "name": "报警历史",
            "url": "/monitor/alarmHistory",
            "extra": null,
            "order": 2,
            "children": []
        }]
    }, {
        "key": "026005",
        "name": "机器管理",
        "url": null,
        "extra": "desktop",
        "order": 5,
        "children": [{
            "key": "026005001",
            "name": "机器列表",
            "url": "/machine/list",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }, {
        "key": "026006",
        "name": "Quartz管理",
        "url": null,
        "extra": "schedule",
        "order": 6,
        "children": [{
            "key": "026006001",
            "name": "定时任务列表",
            "url": "/quartz/list",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }, {
        "key": "026007",
        "name": "用户管理",
        "url": null,
        "extra": "contacts",
        "order": 7,
        "children": [{
            "key": "026007001",
            "name": "用户列表",
            "url": "/user",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }]
});

let superAdminTreeList = Mock.mock({
    data: [{
        "key": "026001",
        "name": "Dashboard",
        "url": "/dashboard",
        "extra": "laptop",
        "order": 1,
        "children": []
    }, {
        "key": "026002",
        "name": "集群管理",
        "url": null,
        "extra": "cloud-download-o",
        "order": 2,
        "children": [{
            "key": "026002001",
            "name": "集群列表",
            "url": "/cluster/list",
            "extra": null,
            "order": 1,
            "children": []
        }, {
            "key": "026002002",
            "name": "添加集群",
            "url": "/cluster/add",
            "extra": null,
            "order": 2,
            "children": []
        }]
    }, {
        "key": "026003",
        "name": "运维管理",
        "url": null,
        "extra": "tool",
        "order": 3,
        "children": [{
            "key": "026003001",
            "name": "集群运维",
            "url": "/manage/cluster/opsManage",
            "extra": null,
            "order": 1,
            "children": []
        }, {
            "key": "026003002",
            "name": "服务部署",
            "url": "/manage/cluster/opsDeploy",
            "extra": null,
            "order": 2,
            "children": []
        }, {
            "key": "026003003",
            "name": "系统配置",
            "url": "/manage/system/configlist",
            "extra": null,
            "order": 3,
            "children": []
        }, {
            "key": "026003004",
            "name": "业务线配置",
            "url": "/manage/system/serviceLineList",
            "extra": null,
            "order": 4,
            "children": []
        }]
    }, {
        "key": "026004",
        "name": "监控报警",
        "url": null,
        "extra": "notification",
        "order": 4,
        "children": [{
            "key": "026004001",
            "name": "监控报警",
            "url": "/monitor/list",
            "extra": null,
            "order": 1,
            "children": []
        },{
            "key": "026004002",
            "name": "报警历史",
            "url": "/monitor/alarmHistory",
            "extra": null,
            "order": 2,
            "children": []
        }]
    }, {
        "key": "026005",
        "name": "机器管理",
        "url": null,
        "extra": "desktop",
        "order": 5,
        "children": [{
            "key": "026005001",
            "name": "机器列表",
            "url": "/machine/list",
            "extra": null,
            "order": 1,
            "children": []
        }, {
            "key": "026005002",
            "name": "SSH终端",
            "url": "/machine/ssh",
            "extra": null,
            "order": 2,
            "children": []
        }]
    }, {
        "key": "026006",
        "name": "Quartz管理",
        "url": null,
        "extra": "schedule",
        "order": 6,
        "children": [{
            "key": "026006001",
            "name": "定时任务列表",
            "url": "/quartz/list",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }, {
        "key": "026007",
        "name": "用户管理",
        "url": null,
        "extra": "contacts",
        "order": 7,
        "children": [{
            "key": "026007001",
            "name": "用户列表",
            "url": "/user",
            "extra": null,
            "order": 1,
            "children": []
        }]
    }]
});

module.exports = {
    'POST /zkdoctor/user/login': function (req, res) {
        let result = true;
        const response = {
            success: result,
            data: {userName: 'xiaoling.lv', chName: '吕小玲', userRole: 2, userId: 1},
            message: '请检查用户名是否正确'
        };
        res.json(response)
    },
    'GET /zkdoctor/user/info': {
        success: true,
        data: {
            userName: 'xiaoling.lv',
            chName: '吕小玲',
            userRole: 2,
            userId: 1
        },
        message: ''
    },
    'GET /zkdoctor/user/logout': {success: true, message: '退出成功'},

    'GET /zkdoctor/menu/treeList': function (req, res) {
        let data = superAdminTreeList.data;
        res.json({
            success: true,
            data: data
        })
    },
    'POST /zkdoctor/user/register': {
        success: true,
        message: '注册成功，请登录'
    },
    'GET /zkdoctor/user/checkUserExists': function (req, res) {
        res.json({
            success: true,
            data: true,
        });
    },
};
