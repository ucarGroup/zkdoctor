import React, {Component} from "react";
import {Button, Card, Popconfirm, Table} from "antd";
import {Link} from "react-router";
import {ClusterAlarmUserVo} from "../../model/ClusterModel";

export interface ClusterAlarmUserProps {
    alarmUsers: Array<ClusterAlarmUserVo>;
    onAdd: any,
    onDelete: any;
}

export class ClusterAlarmUser extends Component<ClusterAlarmUserProps, {}> {
    columns = [
        {
            title: '用户id',
            dataIndex: 'id',
            key: 'id',
        }, {
            title: '用户名',
            dataIndex: 'userName',
            key: 'userName',
        }, {
            title: '中文名',
            dataIndex: 'chName',
            key: 'chName'
        }, {
            title: '邮箱',
            dataIndex: 'email',
            key: 'email',
        }, {
            title: '手机',
            dataIndex: 'mobile',
            key: 'mobile',
        }, {
            title: '操作',
            dataIndex: 'operator',
            key: 'operator',
            render: (text, bean) => {
                return <span>
                <Popconfirm
                    title='确定删除吗？' onConfirm={() => this.props.onDelete(bean)}>
                    <Button type="primary">删除</Button>
                </Popconfirm></span>;
            }
        }];

    render() {
        return (<div>
            <Card title="报警用户信息" bordered={false} hoverable={true}
                  extra={<Button icon="plus" type="primary" onClick={this.props.onAdd}>新增报警用户</Button>}>
                <Table bordered={true} dataSource={this.props.alarmUsers.slice()} columns={this.columns} rowKey="id"
                       pagination={false}/>
            </Card>
        </div>)
    };
}
;
