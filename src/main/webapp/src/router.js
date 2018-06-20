import App from "./sys/view/App";
import Login from "./sys/view/Login";
import LoginRegister from "./sys/view/LoginRegister";
import UserRegister from "./user/view/UserRegister";
import {Router} from "react-router";
import {request} from "./common/utils/request";


export default function ({history, sysUserModel}) {
    async function userInfo(params) {
        return request('/user/info', {})
    }

    const userIsInATeam = (nextState, replace, callback) => {
        userInfo().then(result => {
            if (result.success && result.data.userName != 'null') {
                sysUserModel.loginSuccess({
                    userName: result.data.userName,
                    chName: result.data.chName,
                    userRole: result.data.userRole,
                    userId: result.data.userId
                })
            } else {
                replace('/login');
            }
            callback();
        }).catch(err => {
            callback(err);
        });

    };

    const routes = [{
        path: '/',
        component: App,
        onEnter: userIsInATeam,
        indexRoute: {
            onEnter: function (nextState, replace) {
                replace('/dashboard')
            }
        },
        childRoutes: [
            {
                path: 'dashboard',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./collect/view/ClusterCollectInfo'))
                    })
                }
            }, {
                path: 'user',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./user/view/User'))
                    })
                }
            }, {
                path: 'cluster/list',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./cluster/view/ClusterList'))
                    })
                }
            }, {
                path: 'cluster/list/:status',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./cluster/view/ClusterList'))
                    })
                }
            },

            {
                path: 'cluster/detail/:id',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./cluster/view/ClusterDetail'))
                    })
                }
            }, {
                path: 'cluster/znode/:clusterName/:id',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./cluster/view/clusterZnode/ClusterZnode'))
                    })
                }
            }, {
                path: 'cluster/add',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./cluster/view/newCluster/NewCluster'))
                    })
                }
            }
            , {
                path: 'cluster/monitorTaskData/:clusterId',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./monitor/view/ClusterMonitorAlarmTaskList'))
                    })
                }
            }
            , {
                path: '/instance/listByCluster/:clusterName/:id',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./instance/view/ClusterInstanceList'))
                    })
                }
            }
            , {
                path: '/instance/listByCluster/:status',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./instance/view/ClusterInstanceExecptionList'))
                    })
                }
            }, {
                path: '/manage/cluster/opsDeploy',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./ops/view/ClusterDeploy'))
                    })
                }
            }, {
                path: '/manage/system/configlist',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./ops/view/SystemConfig'))
                    })
                }
            }, {
                path: '/manage/system/serviceLineList',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./ops/view/ServiceLine'))
                    })
                }
            }, {
                path: '/manage/cluster/opsManage',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./ops/view/ClusterOps'))
                    })
                }
            }, {
                path: '/monitor/list',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./monitor/view/MonitorIndicatorList'))
                    })
                }
            }, {
                path: '/monitor/alarmHistory',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./monitor/view/AlarmHistory'))
                    })
                }
            }, {
               path: '/monitorAlarm/getAllZkMonitorTask',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./monitor/view/ClusterMonitorAlarmTaskList'))
                    })
                }
            },{
                path: '/machine/list',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./machine/view/MachineList'))
                    })
                }
            }, {
                path: '/machine/detail/:machineId',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./machine/view/MachineInstanceList'))
                    })
                }
            }, {
                path: '/machine/ssh',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./machine/view/SSHInit'))
                    })
                }
            }, {
                path: '/quartz/list',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./quartz/view/QuartzList'))
                    })
                }
            }, {
                path: '/instance/detail/:clusterName/:id',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./instance/view/InstanceDetail'))
                    })
                }
            }, {
                path: '/manage/cluster/opsInstance/:id',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./ops/view/InstanceOps'))
                    })
                }
            }
            , {
                path: '/clusterCollectManage',
                getComponent(nextState, cb) {
                    require.ensure([], require => {
                        cb(null, require('./collect/view/ClusterCollectInfo'))
                    })
                }
            }
        ]
    }, {
        path: '/login',
        // PS：如果走内部的LDAP，直接使用Login，否则使用LoginRegister，提供用户注册功能
        // component: Login
        component: LoginRegister
    },  {
        path: 'user/register',
        component: UserRegister
    }, {
        path: '*',
        name: 'error',
        getComponent(nextState, cb) {
            require.ensure([], require => {
                cb(null, require('./sys/view/error'))
            })
        }
    }
    ];

    return <Router history={history} routes={routes}/>
}