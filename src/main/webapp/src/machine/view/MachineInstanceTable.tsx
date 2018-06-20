import React from "react";
import {Table, Tag} from "antd";
import {Link} from "react-router";
import {BaseInfoModel} from "../../../sys/model/BaseInfoModel";
import {InstanceInfoVo} from "../../instance/model/InstanceModel";

export interface ListProps {
    dataSource: Array<InstanceInfoVo>;
    loading: boolean;
    pageConfig: any;
    baseInfoModel: BaseInfoModel;
}

export const MachineInstanceTable = (props: ListProps) => {
    const columns = [
        {
            title: '实例id',
            dataIndex: 'id',
            key: 'id'
        }, {
            title: '集群id',
            dataIndex: 'clusterId',
            key: 'clusterId',
            render: (text, bean) => {
                return <Link target="_blank" to={'/cluster/detail/' + bean.clusterId}>{text}</Link>;
            }
        }, {
            title: '实例ip',
            dataIndex: 'host',
            key: 'host'
        }, {
            title: '实例端口号',
            dataIndex: 'port',
            key: 'port'
        }, {
            title: '实例角色',
            dataIndex: 'serverStateLag',
            key: 'serverStateLag',
            render: (text) => {
                return <Tag>{props.baseInfoModel.getServerStatesName(text)}</Tag>;
            }
        }, {
            title: '状态',
            dataIndex: 'status',
            key: 'status',
            render: (text) => {
                return <Tag color={props.baseInfoModel.getInstanceStatusColor(text)}>
                    {props.baseInfoModel.getInstanceStatusName(text)}</Tag>;
            }
        }];

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={columns} rowKey="id"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};

