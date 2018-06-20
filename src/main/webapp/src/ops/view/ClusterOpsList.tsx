import React from "react";
import {Button, Popconfirm, Table, Tag} from "antd";
import {Link} from "react-router";
import {ClusterVo} from "../../cluster/model/ClusterModel";
import {SysUserVo} from "../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import {ServiceLineOpsModel} from "../model/ServiceLineOpsModel";
import styles from "../../sys/view/layout/common.less";

export interface ListProps {
    dataSource: Array<ClusterVo>;
    loading: boolean;
    pageConfig: any;
    sysUser: SysUserVo;
    baseInfoModel: BaseInfoModel;
    serviceLineOpsModel: ServiceLineOpsModel;
    onEdit: any;
    onDynamicExpansion: any;
    onOffLine: any;
    onRestartQuorum: any;
}

export const ClusterOpsList = (props: ListProps) => {

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
                return <Link target="_blank" to={'/cluster/detail/' + bean.clusterId}>{text}</Link>;
            }
        }, {
            title: '集群描述',
            dataIndex: 'intro',
            key: 'intro'
        }, {
            title: '版本号',
            dataIndex: 'version',
            key: 'version'
        }, {
            title: '集群状态',
            dataIndex: 'status',
            key: 'status',
            render: (text) => {
                return <Tag color={props.baseInfoModel.getClusterStatusColor(text)}>
                    {props.baseInfoModel.getClusterStatusName(text)}</Tag>;
            }
        }, {
            title: '业务线',
            dataIndex: 'serviceLine',
            key: 'serviceLine',
            render: (text) => {
                return props.serviceLineOpsModel.getServiceLineName(text);
            }
        }];

    let oprationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span className={styles.antSpan}>
                    <Button type='primary' style={{marginRight: 5}}
                            onClick={() => props.onEdit(bean.clusterInfo)}>修改信息</Button>
                    <Button type='primary' style={{marginRight: 5}}
                            onClick={() => props.onDynamicExpansion(bean.clusterInfo)}>动态扩容</Button>
                    <Popconfirm
                        title={'确定下线该集群吗？集群名：' + bean.clusterInfo.clusterName} onConfirm={() => {
                        props.onOffLine(bean.clusterInfo.id)
                    }}> <Button type="primary" style={{marginRight: 5}}>下线集群</Button>
                    </Popconfirm>
                    <Button type='primary' style={{marginRight: 5}}
                            onClick={() => props.onRestartQuorum(bean.clusterInfo)}>重启集群</Button>
                     <Link to={'/manage/cluster/opsInstance/' + bean.clusterInfo.id}>
                    <Button type="primary">实例运维</Button>
                </Link>
            </span>
            );
        }
    };

    let finalColumns = columns.concat(oprationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="clusterId"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};
