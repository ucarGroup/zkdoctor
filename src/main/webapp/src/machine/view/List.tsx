import React from "react";
import {Button, Popconfirm, Progress, Switch, Table, Tag} from "antd";
import {Link} from "react-router";
import {MachineDetailVo} from "../model/MachineModel";
import {SysUserVo} from "../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import styles from "../../sys/view/layout/common.less";

export interface ListProps {
    dataSource: Array<MachineDetailVo>;
    sysUser: SysUserVo;
    baseInfoModel: BaseInfoModel;
    pageConfig: any;
    loading: boolean;
    onEdit: any;
    onDelete: any;
    switchChange: any;
}

export const List = (props: ListProps) => {
    const columns = [
        {
            // title: '机器id',
            // dataIndex: 'machineInfo.id',
            // key: 'machineId',
            // render: (text, bean) => {
            //     return <Link to={'/machine/detail/' + bean.machineInfo.id}>{text}</Link>;
            // }
        // }, {
            title: '服务器ip',
            dataIndex: 'machineInfo.host',
            key: 'host',
            width: '17%',
            render: (text, bean) => {
                return <Link to={'/machine/detail/' + bean.machineInfo.id}>{text}</Link>;
            }
        }, {
            title: '内存情况',
            dataIndex: 'machineState.memoryUsage',
            key: 'memoryUsage',
            width: '22%',
            render: (text, bean) => {
                let memoryFree = bean.machineState.memoryFree ? bean.machineState.memoryFree : 0;
                let memoryTotal = bean.machineState.memoryTotal ? bean.machineState.memoryTotal : 0;
                let memoryUsed = (memoryTotal - memoryFree).toFixed(2);
                let percent = parseFloat(bean.machineState.memoryUsage);
                percent = percent > 100 ? 100 : percent;
                return <span><Progress percent={percent} showInfo={false}
                                       status={percent >= 70 ? 'exception' : 'success'}/>
                    {memoryUsed + 'G Used/' + memoryTotal + 'G Total'}</span>
            }
        }, {
            title: 'CPU使用率(%)',
            dataIndex: 'machineState.cpuUsage',
            key: 'cpuUsage',
            width: '13%',
            render: (text, bean) => {
                let percent = parseFloat(bean.machineState.cpuUsage);
                percent = percent > 100 ? 100 : percent;
                return <span><Progress percent={percent} showInfo={false}
                                       status={percent >= 70 ? 'exception' : 'success'}/>
                    {bean.machineState.cpuUsage ? bean.machineState.cpuUsage : ' - '}%</span>
            }
        }, {
            title: '流量(KBps)',
            dataIndex: 'machineState.netTraffic',
            key: 'netTraffic',
            width: '9%',
        }, {
            title: '磁盘(dataDir)使用情况',
            dataIndex: 'machineState.diskUsage',
            key: 'diskUsage',
            width: '22%',
            render: (text, bean) => {
                let percent = parseFloat(bean.machineState.diskUsage);
                percent = percent > 100 ? 100 : percent;
                return <span><Progress percent={percent} showInfo={false}
                                       status={percent >= 70 ? 'exception' : 'success'}/>
                    {(bean.machineState.dataDiskUsed ? bean.machineState.dataDiskUsed : '-') + 'G Used/' + (bean.machineState.dataDiskTotal ? bean.machineState.dataDiskTotal : '-') + 'G Total'}</span>
            }
        // }, {
        //     title: '平均负载',
        //     dataIndex: 'machineState.avgLoad',
        //     key: 'avgLoad'
        }, {
            title: '是否可用',
            dataIndex: 'machineInfo.available',
            key: 'available',
            width: '2%',
            render: (text) => {
                return <Tag color={props.baseInfoModel.getMachineStatusColor(text)}>
                    {props.baseInfoModel.getMachineStatusName(text)}</Tag>;
            }
        }, {
            title: '最后统计时间',
            dataIndex: 'machineState.modifyTimeStr',
            key: 'modifyTimeStr',
            width: '13%',
        }];
    let operationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span className={styles.antSpan}>
                <Popconfirm
                    title='确定删除吗？' onConfirm={() => {
                    props.onDelete(bean.machineInfo.id)
                }}>
                    <Button type="primary" style={{marginRight: 5}}>删除</Button>
                </Popconfirm>
                <Button type='primary' onClick={() => props.onEdit(bean.machineInfo)}>修改</Button>
            </span>);
        }
    };

    let monitorCol = {
        title: '监控开关',
        dataIndex: 'monitor',
        key: 'monitor',
        width: '2%',
        render: (text, bean) => {
            let switchOn = false;
            if (bean.machineInfo.monitor == true) {
                switchOn = true;
            }
            return (<span>
                <Switch checkedChildren="开" unCheckedChildren="关" checked={switchOn}
                        onChange={(value) => props.switchChange(bean, value)}/>
            </span>);
        }
    };

    let finalColumns;
    let role = props.sysUser.userRole;
    if (role == 1 || role == 2) {//管理员和超级管理员
        finalColumns = columns.concat(monitorCol);
        finalColumns = finalColumns.concat(operationCol);
    } else {
        finalColumns = columns;
    }

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="machineInfo.id"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};
