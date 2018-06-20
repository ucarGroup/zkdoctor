import React from "react";
import {Button, Switch, Table, Tag} from "antd";
import {Link} from "react-router";
import {ClusterVo} from "../../model/ClusterModel";
import {SysUserVo} from "../../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../../sys/model/BaseInfoModel";
import styles from "../../../sys/view/layout/common.less";

export interface ListProps {
    dataSource: Array<ClusterVo>;
    loading: boolean;
    pageConfig: any;
    sysUser: SysUserVo;
    baseInfoModel: BaseInfoModel;
    switchChange: any;
}

export const List = (props: ListProps) => {
    const columns = [
        {
            title: '集群id',
            dataIndex: 'clusterId',
            key: 'clusterId'
        }, {
            title: '集群名称',
            dataIndex: 'clusterName',
            key: 'clusterName',
            render: (text, bean) => {
                return <Link to={'/cluster/detail/' + bean.clusterId}>{text}</Link>;
            }
        }, {
            title: '描述',
            dataIndex: 'intro',
            key: 'intro'
        }, {
            title: 'znode数',
            dataIndex: 'znodeCount',
            key: 'znodeCount'
        }, {
            title: '请求数',
            dataIndex: 'requestCount',
            key: 'requestCount'
        }, {
            title: '连接数',
            dataIndex: 'connections',
            key: 'connections'
        }, {
            title: '状态统计时间',
            dataIndex: 'collectTime',
            key: 'collectTime'
        }, {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            render: (text) => {
                return <Tag color={props.baseInfoModel.getClusterStatusColor(text)}>
                    {props.baseInfoModel.getClusterStatusName(text)}</Tag>;
            }
        }];

    let monitorCol = {
        title: '监控开关',
        dataIndex: 'monitor',
        key: 'monitor',

        render: (text, bean) => {
            let switchOn = false;
            if (bean.status == 2 || bean.status == 4) {
                switchOn = true;
            }
            return (<span>
                <Switch checkedChildren="开" unCheckedChildren="关" checked={switchOn}
                        onChange={(value) => props.switchChange(bean, value)}/>
            </span>);
        }
    };

    let oprationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',
        render: (text, bean) => {
            return (<span className={styles.antSpan}>
                    <Link target="_blank" to={'/cluster/detail/' + bean.clusterId}>
                    <Button style={{marginRight: 5}} type="primary">监控统计</Button>
                </Link>
                <Link target="_blank" to={'/instance/listByCluster/' + bean.clusterName + '/' + bean.clusterId}>
                    <Button style={{marginRight: 5}} type="primary">拓扑结构</Button>
                </Link>
                    <Link target="_blank" to={'/cluster/znode/' + bean.clusterName + '/' + bean.clusterId}>
                    <Button type="primary">节点操作</Button>
                </Link>

                  <Link target="_blank" to={'/cluster/monitorTaskData/' + bean.clusterId}>
                    <Button style={{marginLeft:5}} type="primary">报警任务</Button>
                </Link>
            </span>);
        }
    };

    let finalColumns = columns;
    let role = props.sysUser.userRole;
    if (role == 1 || role == 2) {//管理员和超级管理员
        finalColumns = finalColumns.concat(monitorCol);
    }
    finalColumns = finalColumns.concat(oprationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="clusterId"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};

