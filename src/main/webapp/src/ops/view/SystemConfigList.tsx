import React from "react";
import {Button, Popconfirm, Table} from "antd";
import {Link} from "react-router";
import {SystemConfigVo} from "../model/SystemOpsModel";

export interface ListProps {
    dataSource: Array<SystemConfigVo>;
    loading: boolean;
    pageConfig: any;
    onUpdateConfig: any;
    onDeleteConfigValue: any;
}

export const SystemConfigList = (props: ListProps) => {

    const columns = [
        {
            title: '配置名称',
            dataIndex: 'configName',
            key: 'configName'
        }, {
            title: '功能',
            dataIndex: 'configDesc',
            key: 'configDesc',
            width: 300
        }, {
            title: '默认值',
            dataIndex: 'defaultConfigValue',
            key: 'defaultConfigValue',
            width: 300
        }, {
            title: '当前配置值',
            dataIndex: 'configValue',
            key: 'configValue'
        }];

    let operationCol = {
        title: '操作',
        dataIndex: 'operator',
        key: 'operator',

        render: (text, bean) => {
            return (<span>
                    <Button type='primary' onClick={() => props.onUpdateConfig(bean)}
                            style={{marginRight: 5}}>修改</Button>
                    <Popconfirm
                        title={'确定删除当前配置值，使用默认配置值么？'} onConfirm={() => {
                        props.onDeleteConfigValue(bean.configName)
                    }}> <Button type="primary" style={{marginRight: 5}}>删除</Button>
                    </Popconfirm>
            </span>
            );
        }
    };

    let finalColumns = columns.concat(operationCol);

    return <Table bordered={true} dataSource={props.dataSource.slice()} columns={finalColumns} rowKey="configName"
                  loading={props.loading}
                  pagination={props.pageConfig}/>
};
