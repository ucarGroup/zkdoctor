import React from "react";
import {Button, Popconfirm, Table, Tag} from "antd";
import {Link} from "react-router";
import {InstanceListVo} from "../../instance/model/InstanceModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import styles from "../../sys/view/layout/common.less";

export interface ListProps {
    dataSource: Array<InstanceListVo>;
    loading: boolean;
    baseInfoModel: BaseInfoModel;
    onOffLineInstance: any;
    onRemoveInstance: any;
    onRestartInstance: any;
    onConfigOps: any;
    onAddNewConfigFile: any;
    onUpdateServer: any;
}

export const InstanceOpsList = (props: ListProps) => {
    const columns = [
        {
            title: '实例id',
            dataIndex: 'instanceId',
            key: 'instanceId'
        }, {
            title: '服务器ip',
            dataIndex: 'host',
            key: 'host',
            render: (text, bean) => {
                return <Link to={'/instance/detail/' + bean.clusterName + '/' + bean.instanceId}>{text}</Link>;
            }
        }, {
            title: '端口',
            dataIndex: 'port',
            key: 'port'
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
            return (<span className={styles.antSpan}>
                    <Popconfirm
                        title={'确定移除该实例吗？' + bean.host + ':' + bean.port} onConfirm={() => {
                        props.onRemoveInstance(bean.instanceId)
                    }}> <Button type="primary" style={{marginRight: 5}}>移除实例</Button>
                </Popconfirm>

                    <Popconfirm
                        title={'确定下线该实例吗？' + bean.host + ':' + bean.port} onConfirm={() => {
                        props.onOffLineInstance(bean.instanceId)
                    }}> <Button type="primary" style={{marginRight: 5}}>下线实例</Button>
                </Popconfirm>
                    <Popconfirm
                        title={'确定重启该实例吗？' + bean.host + ':' + bean.port} onConfirm={() => {
                        props.onRestartInstance(bean.instanceId)
                    }}> <Button type="primary" style={{marginRight: 5}}>重启实例</Button>
                </Popconfirm>
                    <Button type="primary" style={{marginRight: 5}} onClick={() => {
                        props.onConfigOps(bean)
                    }}>配置修改</Button>
                    <Button type="primary" style={{marginRight: 5}} onClick={() => {
                        props.onAddNewConfigFile(bean)
                    }}>新增配置文件</Button>
                    <Button type="primary" onClick={() => {
                        props.onUpdateServer(bean)
                    }}>服务升级</Button>
            </span>
            );
        }
    };

    let finalColumns = columns.concat(oprationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="instanceId"
                  loading={props.loading}
                  pagination={false}/>
};


