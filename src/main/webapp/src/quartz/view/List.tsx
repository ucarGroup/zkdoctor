import React from "react";
import {Button, notification, Popconfirm, Table} from "antd";
import {Link} from "react-router";
import {SysUserVo} from "../../sys/model/SysUserModel";
import {TriggerInfoVo} from "../model/QuartzModel";

const columns = [
    {
        title: '描述',
        dataIndex: 'description',
        key: 'description',
        width: '200px'
    }, {
        title: '触发器名称',
        dataIndex: 'triggerName',
        key: 'triggerName',
    }, {
        title: '触发器组',
        dataIndex: 'triggerGroup',
        key: 'triggerGroup',
    }, {
        title: 'cron',
        dataIndex: 'cron',
        key: 'cron',
    }, {
        title: '下一次执行时间',
        dataIndex: 'nextFireTimeStr',
        key: 'nextFireTimeStr',
    }, {
        title: '上一次执行时间',
        dataIndex: 'prevFireTimeStr',
        key: 'prevFireTimeStr',
    }, {
        title: '开始时间',
        dataIndex: 'startTimeStr',
        key: 'startTimeStr',
    }, {
        title: '状态',
        dataIndex: 'triggerState',
        key: 'triggerState',
    }];

export interface ListProps {
    dataSource: Array<TriggerInfoVo>;
    sysUser: SysUserVo;
    pageConfig: any;
    loading: boolean;
    onDelete: any;
    onResume: any;
    onPause: any;
}

export const List = (props: ListProps) => {

    let oprationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span>
                {/*<Popconfirm*/}
                    {/*title='确定删除该定时任务吗？' onConfirm={() => {*/}
                    {/*props.onDelete(bean.triggerName, bean.triggerGroup, (message) => {*/}
                        {/*notification['success']({*/}
                            {/*message: message,*/}
                            {/*description: '',*/}
                        {/*});*/}
                    {/*})*/}
                {/*}}>*/}
                    {/*<Button type="primary" style={{marginRight: 5}}>删除</Button>*/}
                {/*</Popconfirm>*/}

                {bean.triggerState !== 'PAUSED' ?
                    <Popconfirm
                        title='确定暂停该定时任务吗？' onConfirm={() => {
                        props.onPause(bean.triggerName, bean.triggerGroup, (message) => {
                            notification['success']({
                                message: message,
                                description: '',
                            });
                        })
                    }}><Button type="primary">暂停</Button></Popconfirm> : <Popconfirm
                        title='确定恢复该定时任务吗？' onConfirm={() => {
                        props.onResume(bean.triggerName, bean.triggerGroup, (message) => {
                            notification['success']({
                                message: message,
                                description: '',
                            });
                        })
                    }}><Button type="primary">恢复</Button></Popconfirm>}

            </span>);
        }
    };

    let finalColumns;
    let role = props.sysUser.userRole;
    if (role == 1 || role == 2) {//管理员和超级管理员
        finalColumns = columns.concat(oprationCol);
    } else {
        finalColumns = columns;
    }

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="triggerGroup"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};
