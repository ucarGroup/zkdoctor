import React, {Component} from "react";
import {Table} from "antd";
import {Link} from "react-router";
import {ClusterMonitorAlarmInfoVo} from "../model/ClusterMonitorAlarmInfoModel";

export interface ListProps {
    dataSource: Array<ClusterMonitorAlarmInfoVo>;
    loading: boolean;
    pageConfig: any;
}

export class AlarmInfoList extends Component<ListProps, {}> {
    columns = [
        {
            title: '报警信息id',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: '集群名称',
            dataIndex: 'clusterName',
            key: 'clusterName'
        }
        /*  ,
         {
         title: '机器ID',
         dataIndex: 'machineId',
         key: 'machineId'
         }*/
        /* ,
         {
         title: '机器IP',
         dataIndex: 'machineIp',
         key: 'machineIp'
         }*/
        ,
        /*{
         title: '实例ID',
         dataIndex: 'instanceId',
         key: 'instanceId'
         },
         {
         title: '实例IP',
         dataIndex: 'instanceIp',
         key: 'instanceIp'
         },  {
         title: '端口号',
         dataIndex: 'port',
         key: 'port'
         },*/
        {
            title: '报警信息内容',
            dataIndex: 'infoContent',
            key: 'infoContent'
        }
        , {
            title: '报警时间',
            dataIndex: 'alarmTime',
            key: 'alarmTime'
        }
        /* , {
         title: '备注',
         dataIndex: 'remark',
         key: 'remark'
         }*/
    ];

    render() {
        return (<div>
            <Table bordered={true} dataSource={this.props.dataSource.slice()} columns={this.columns} rowKey="id"
                   loading={this.props.loading}
                   pagination={this.props.pageConfig}/>
        </div>)
    }

}