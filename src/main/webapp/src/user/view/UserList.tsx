import React from "react";
import {Button, Popconfirm, Table} from "antd";
import {UserVo} from "../model/UserModel";

const columns = [
    {
        title: '用户id',
        dataIndex: 'id',
        key: 'id'
    }, {
        title: '用户名',
        dataIndex: 'userName',
        key: 'userName'
    }, {
        title: '中文名',
        dataIndex: 'chName',
        key: 'chName'
    }, {
        title: '邮箱',
        dataIndex: 'email',
        key: 'email'
    }, {
        title: '手机号',
        dataIndex: 'mobile',
        key: 'mobile'
    }, {
        title: '用户类型',
        dataIndex: 'userRole',
        key: 'userRole',
        render: (text) => {
            if (text == 1) return <span>管理员</span>;
            if (text == 2) return <span>超级管理员</span>;
            return <span>普通用户</span>
        }
    }];

export interface ListProps {
    user: UserVo;
    dataSource: Array<UserVo>;
    loading: boolean;
    onEdit: any;
    onDelete: any;
    pageConfig:any
}

export const UserList = (props: ListProps) => {
    let oprationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span>
                <Button type='primary' style={{marginRight: 5}} onClick={() => props.onEdit(bean)}>修改</Button>
                <Popconfirm title='确定删除吗？' onConfirm={() => props.onDelete(bean.id)}>
                <Button type='primary' >删除</Button>
                </Popconfirm>
            </span>);
        }
    };
    let finalColumns = columns.concat(oprationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="id" loading={props.loading}
                  pagination={props.pageConfig}/>
};
