import React from "react";
import {Button, Table, Tag} from "antd";
import {Link} from "react-router";
import {InstanceListVo} from "../model/InstanceModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

export interface ListProps {
    dataSource: Array<InstanceListVo>;
    loading: boolean;
    baseInfoModel: BaseInfoModel;
}

export const InstanceExceptionList = (props: ListProps) => {

    const columns = [
        {
            title: '集群',
            dataIndex: 'clusterName',
            key: 'clusterName'
        },
        {
            title: '实例id',
            dataIndex: 'instanceId',
            key: 'instanceId',
            render: (text, bean) => {
                return <Link to={'/instance/detail/' + bean.clusterName + '/' + bean.instanceId}>{text}</Link>;
            }
        }, {
            title: '实例ip',
            dataIndex: 'host',
            key: 'host',
        }, {
            title: '端口',
            dataIndex: 'port',
            key: 'port'
        }, {
            title: '连接数',
            dataIndex: 'currConnections',
            key: 'currConnections'
        }, {
            title: '收包数',
            dataIndex: 'received',
            key: 'received'
        }, {
            title: '角色',
            dataIndex: 'serverStateLag',
            key: 'serverStateLag',
            render: (text) => {
                if (text == 0) {
                    return "follower";
                } else if (text == 1) {
                    return "leader";
                } else if (text == 2) {
                    return "observer";
                } else if (text == 3) {
                    return "standalone";
                }
            }
        }, {
            title: '实例状态',
            dataIndex: 'status',
            key: 'status',
            render: (text) => {
                return <Tag color={props.baseInfoModel.getInstanceStatusColor(text)}>
                    {props.baseInfoModel.getInstanceStatusName(text)}</Tag>;
            }
        }];

    let oprationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span>
                    <Link target="_blank" to={'/instance/detail/' + bean.clusterName + '/' + bean.instanceId}>
                    <Button type="primary">监控统计</Button>
                </Link>
            </span>);
        }
    };

    let finalColumns = columns.concat(oprationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="instanceId"
                  loading={props.loading}
                  pagination={false}/>
};
