import React, {Component} from "react";
import {Button, Switch, Table} from "antd";
import {Link} from "react-router";
import {MonitorTaskModel, MonitorTaskSearchVo} from "../model/MonitorTaskModel";
import {observer} from "mobx-react";
import {MonitorIndicatorVo} from "../model/MonitorIndicatorModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import {SysUserVo} from "../../sys/model/SysUserModel";

export interface ListProps {
    sysUser: SysUserVo;
    activeKey: number;
    baseInfoModel:BaseInfoModel,
    indicator: MonitorIndicatorVo;
    monitorTaskModel: MonitorTaskModel;
    switchChange: any;
    onEdit: any;
}

@observer
export class TaskList extends Component<ListProps, {}> {

    constructor(props) {
        super(props);
    }

    componentDidMount(): void {
        let monitorTaskSearchVo = new MonitorTaskSearchVo();
        monitorTaskSearchVo.indicatorId = this.props.indicator ? this.props.indicator.id : null;
        this.props.monitorTaskModel.query(monitorTaskSearchVo);
        this.setState({})
    }

    columns = [
        {
            title: '监控任务id',
            dataIndex: 'id',
            key: 'id'
        }, {
            title: '集群',
            dataIndex: 'clusterName',
            key: 'clusterName',
            render: (text, bean) => {
                return <Link target="_blank" to={'/cluster/detail/' + bean.clusterId}>{text}</Link>;
            }
        }, {
            title: '报警阈值' + (props.indicator.alertValueUnit ? '（' + props.indicator.alertValueUnit + '）' : ''),
            dataIndex: 'alertValue',
            key: 'alertValue'
        }, {
            title: '报警间隔（分钟）',
            dataIndex: 'alertInterval',
            key: 'alertInterval'
        }, {
            title: '报警频率（报警间隔内发生n次则报警）',
            dataIndex: 'alertFrequency',
            key: 'alertFrequency',
            width: 150
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
                               onChange={(value) => this.props.switchChange(this.props.activeKey, bean, value)}/>
            }
        };

        let managerOprationCol = {
            title: '操作',
            dataIndex: 'operator',
            key: 'operator',

            render: (text, bean) => {
                return (
                    <Button type='primary' onClick={() => {
                        this.props.onEdit(this.props.activeKey, bean)
                    }}>修改</Button>
                );
            }
        };

        let finalColumns;
        let role = this.props.sysUser.userRole;
        if (role == 1 || role == 2) {//管理员和超级管理员
            finalColumns = this.columns.concat(switchOnCol).concat(managerOprationCol);
        } else {
            finalColumns = this.columns;
        }

        let data = [];
        if (this.props.activeKey >= 2 &&
            this.props.monitorTaskModel.tasksArray.length > 0 &&
            this.props.monitorTaskModel.totalIndex <= this.props.monitorTaskModel.tasksArray.length &&
            this.props.activeKey <= this.props.monitorTaskModel.totalIndex + 1) {
            data = this.props.monitorTaskModel.tasksArray[this.props.activeKey - 2];
        }
        return (<div>
            <Table bordered={true} dataSource={data.slice()} columns={finalColumns}
                   rowKey="id"
                   loading={this.props.monitorTaskModel.loading}
                   pagination={this.props.monitorTaskModel.pageConfig}/>
        </div>)
    }
}