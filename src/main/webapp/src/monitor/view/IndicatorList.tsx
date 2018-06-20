import React, {Component} from "react";
import {Button, Switch, Table} from "antd";
import {Link} from "react-router";
import {MonitorIndicatorVo} from "../model/MonitorIndicatorModel";
import {SysUserVo} from "../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import styles from "../../sys/view/layout/common.less";

export interface ListProps {
    sysUser: SysUserVo;
    baseInfoModel: BaseInfoModel;
    dataSource: Array<MonitorIndicatorVo>;
    indicator: MonitorIndicatorVo;
    loading: boolean;
    pageConfig: any;
    switchChange: any;
    onEdit: any;
    onSearchTask: any;
}

export class IndicatorList extends Component<ListProps, {}> {

    columns = [
        {
            title: '名称',
            dataIndex: 'indicatorName',
            key: 'indicatorName'
        }, {
            title: '默认报警阈值',
            dataIndex: 'defaultAlertValue',
            key: 'defaultAlertValue'
        }, {
            title: '单位',
            dataIndex: 'alertValueUnit',
            key: 'alertValueUnit'
        }, {
            title: '默认报警间隔（分钟）',
            dataIndex: 'defaultAlertInterval',
            key: 'defaultAlertInterval'
        }, {
            title: '默认报警频率（报警间隔内发生n次则报警）',
            dataIndex: 'defaultAlertFrequency',
            key: 'defaultAlertFrequency',
            width: 170
        }, {
            title: '默认报警形式',
            dataIndex: 'defaultAlertForm',
            key: 'defaultAlertForm',
            render: (text) => {
                return <span>{this.props.baseInfoModel.getAlertFormName(text)}</span>;
            }
        }, {
            title: '备注',
            dataIndex: 'info',
            key: 'info',
            width: 150
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
                        onClick={() => this.props.onEdit(bean)}>修改指标</Button>
                    <Button type='primary'
                            onClick={() => this.props.onSearchTask(bean)}>监控任务</Button>
            </span>);
            }
        };

        let oprationCol = {
            title: '操作',
            dataIndex: 'operator',
            key: 'operator',

            render: (text, bean) => {
                return (<span>
                    <Button type='primary'
                            onClick={() => this.props.onSearchTask(bean)}>监控任务</Button>
            </span>);
            }
        };


        let finalColumns;
        let role = this.props.sysUser.userRole;
        if (role == 1 || role == 2) {//管理员和超级管理员
            finalColumns = this.columns.concat(switchOnCol).concat(managerOprationCol);
        } else {
            finalColumns = this.columns.concat(oprationCol);;
        }

        return (<div>
            <Table bordered={true} dataSource={this.props.dataSource.slice()} columns={finalColumns} rowKey="id"
                   loading={this.props.loading}
                   pagination={this.props.pageConfig}/>
        </div>)
    }
}