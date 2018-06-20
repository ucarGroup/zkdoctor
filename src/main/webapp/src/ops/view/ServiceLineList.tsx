import React from "react";
import {Button, Popconfirm, Table} from "antd";
import {Link} from "react-router";
import {ServiceLineVo} from "../model/ServiceLineOpsModel";

export interface ListProps {
    dataSource: Array<ServiceLineVo>;
    loading: boolean;
    pageConfig: any;
    onUpdateServiceLine: any;
    onDeleteServiceLine: any;
}

export const ServiceLineList = (props: ListProps) => {

    const columns = [
        {
            title: '业务线id',
            dataIndex: 'id',
            key: 'id'
        }, {
            title: '业务线名称',
            dataIndex: 'serviceLineName',
            key: 'serviceLineName'
        }, {
            title: '描述',
            dataIndex: 'serviceLineDesc',
            key: 'serviceLineDesc'
        }, {
            title: '创建时间',
            dataIndex: 'createTimeStr',
            key: 'createTimeStr'
        }, {
            title: '最后修改时间',
            dataIndex: 'modifyTimeStr',
            key: 'modifyTimeStr'
        }];

    let operationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span>
                    <Button type='primary' onClick={() => props.onUpdateServiceLine(bean)}
                            style={{marginRight: 5}}>修改</Button>
                    <Popconfirm
                        title={'确定删除该业务线配置么？'} onConfirm={() => {
                        props.onDeleteServiceLine(bean.serviceLineName)
                    }}> <Button type="primary" style={{marginRight: 5}}>删除</Button>
                    </Popconfirm>
            </span>
            );
        }
    };

    let finalColumns = columns.concat(operationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="id"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};
