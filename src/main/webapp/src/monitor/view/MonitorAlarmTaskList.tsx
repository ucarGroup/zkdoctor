import React, {Component} from "react";
import {Button, Switch, Table} from "antd";
import {Link} from "react-router";
import {ClusterMonitorAlarmTaskVo} from "../model/ClusterMonitorAlarmTaskModel";
import {SysUserVo} from "../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import styles from "../../sys/view/layout/common.less";

export interface ListProps {
    sysUser: SysUserVo;
    baseInfoModel: BaseInfoModel;
    dataSource: Array<ClusterMonitorAlarmTaskVo>;
    loading: boolean;
    pageConfig: any;
    switchChange: any;
    onEdit: any;
}

export class MonitorAlarmTaskList extends Component<ListProps, {}> {

    columns = [
        {
            title: '集群名称',
            dataIndex: 'clusterName',
            key: 'clusterName',
        },
        {
            title: '指标名称',
            dataIndex: 'indicatorName',
            key: 'indicatorName',
            width: 150
        },
        {
            title: '报警阈值',
            dataIndex: 'alertValue',
            key: 'alertValue',
        },
        {
            title: '单位',
            dataIndex: 'alertValueUnit',
            key: 'alertValueUnit',
        },
        {
            title: '报警间隔（分钟）',
            dataIndex: 'alertInterval',
            key: 'alertInterval',
        }, {
            title: '报警频率（报警间隔内发生n次则报警）',
            dataIndex: 'alertFrequency',
            key: 'alertFrequency',
            width: 160
        }, {
            title: '报警形式',
            dataIndex: 'alertForm',
            key: 'alertForm',
            render: (text) => {
                return <span>{this.props.baseInfoModel.getAlertFormName(text)}</span>;
            }
        }
    ];

    render() {
        let switchOnCol = {
            title: '开关',
            dataIndex: 'switchOn',
            key: 'switchOn',

            render: (text, bean) => {
                return <Switch checkedChildren="开" unCheckedChildren="关" checked={text}
                               onChange={(value) => this.props.switchChange(bean, value)}/>
            }
        };

        let managerOprationCol = {
            title: '操作',
            dataIndex: 'operator',
            key: 'operator',

            render: (text, bean) => {
                return (<span className={styles.antSpan}>
                <Button type='primary' style={{marginRight: 5}}
                        onClick={() => this.props.onEdit(bean)}>修改</Button>
            </span>);
            }
        };

        let finalColumns;
        let role = this.props.sysUser.userRole;
        if (role == 1 || role == 2) {//管理员和超级管理员
            finalColumns = this.columns.concat(switchOnCol).concat(managerOprationCol);
        } else {
            finalColumns = this.columns;
        }

        return (<div>
            <Table bordered={true} dataSource={this.props.dataSource.slice()} columns={finalColumns} rowKey="id"
                   loading={this.props.loading}
                   pagination={this.props.pageConfig}/>
        </div>)
    }
}