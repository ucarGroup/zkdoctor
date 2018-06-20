var Mock = require('mockjs');
var qs = require('qs');

let usersData = Mock.mock({
    'data|100': [
        {
            'id|+1': 1,
            mobile: /^1[34578]\d{9}$/,
            userName: '@cname',
            chName: '@cname',
            email: '@email',
            'userRole|0-2': 0
        }
    ]
});

module.exports = {
    'GET /zkdoctor/users/query': function (req, res) {
        const page = qs.parse(req.query);

        let pageSize = page.pageSize;
        let pageNum = page.pageNum;
        if (!pageSize) {
            pageSize = 10;
        }
        if (!pageNum) {
            pageNum = 1;
        }
        let data = usersData.data;
        if (page.chName || page.email) {
            data = data.filter(bean => {
                let flag = true;
                if (page.chName) {
                    flag = bean.chName.indexOf(page.chName) >= 0;
                }
                if (flag && page.email) {
                    flag = bean.email.indexOf(page.email) >= 0
                }
                return flag;
            });
        }
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
    'GET /zkdoctor/users/listAll': {
        success: true,
        data: usersData.data,
        message: ''
    },
    'POST /zkdoctor/users/create': {
        success: true,
        data: 101,
        message: '新增用户成功'
    },
    'POST /zkdoctor/users/delete': {
        success: true,
        message: '删除用户成功'
    },
    'POST /zkdoctor/users/update': {
        success: true,
        message: '更新用户成功'
    }
};
