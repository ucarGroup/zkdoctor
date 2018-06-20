import React, {Component} from "react";
import {Card, Col, Row, Table, Tag} from "antd";
import {Link} from "react-router";
import {ClusterDetailVo} from "../../model/ClusterModel";
import {BaseInfoModel} from "../../../sys/model/BaseInfoModel";
import {inject} from "../../../common/utils/IOC";
import styles from "../../../themes/Search.less";
import {ServiceLineOpsModel} from "../../../ops/model/ServiceLineOpsModel";

export interface ClusterInfoProps {
    clusterDetailVo: ClusterDetailVo;
    serviceLineOpsModel: ServiceLineOpsModel;
}

export class ClusterInfo extends Component<ClusterInfoProps, {}> {
    columns = [
        {
            title: '状态信息',
            dataIndex: 'desc',
            key: 'desc',
            width: 500
        }, {
            title: '状态值',
            dataIndex: 'value',
            key: 'value',
        }];

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    runningInfo = (status) => {
        return <Tag color={this.baseInfoModel.getClusterStatusColor(status)}>
            {this.baseInfoModel.getClusterStatusName(status)}</Tag>;
    };

    render() {
        let clusterDetailVo = this.props.clusterDetailVo;
        if (!clusterDetailVo) {
            return null;
        }
        let clusterInfoVo = clusterDetailVo.clusterInfo;
        let clusterState = clusterDetailVo.clusterState;

        if (!clusterState) {
            return null;
        }
        if (!clusterInfoVo) {
            return null;
        }
        let dataSource = [{
            key: '0',
            name: 'modifyTimeStr',
            value: clusterState.modifyTimeStr,
            desc: '该状态收集时间'
        }, {
            key: '1',
            name: 'avgLatencyMax',
            value: clusterState.avgLatencyMax,
            desc: '同一时间，该集群中所有实例的平均延时的最大值，单位：ms'
        }, {
            key: '2',
            name: 'maxLatencyMax',
            value: clusterState.maxLatencyMax,
            desc: '同一时间，该集群中实例的最大延时的最大值，单位：ms'
        }, {
            key: '3',
            name: 'minLatencyMax',
            value: clusterState.minLatencyMax,
            desc: '同一时间，该集群中实例的最小延时的最大值，单位：ms'
        }, {
            key: '4',
            name: 'receivedTotal',
            value: clusterState.receivedTotal,
            desc: '集群所有实例的总收包数'
        }, {
            key: '5',
            name: 'sentTotal',
            value: clusterState.sentTotal,
            desc: '集群所有实例的总发包数'
        }, {
            key: '6',
            name: 'connectionTotal',
            value: clusterState.connectionTotal,
            desc: '集群所有实例的总连接数'
        }, {
            key: '7',
            name: 'znodeCount',
            value: clusterState.znodeCount,
            desc: '节点数，包含临时节点'
        }, {
            key: '8',
            name: 'watcherTotal',
            value: clusterState.watcherTotal,
            desc: '集群所有实例总watch数'
        }, {
            key: '9',
            name: 'ephemerals',
            value: clusterState.ephemerals,
            desc: '临时节点数'
        }, {
            key: '10',
            name: 'outstandingTotal',
            value: clusterState.outstandingTotal,
            desc: '集群所有实例的总堆积请求数'
        }, {
            key: '11',
            name: 'approximateDataSize',
            value: clusterState.approximateDataSize,
            desc: '数据大小,单位:byte。（zookeeper内存中存储的所有数据节点path+data的大小）'
        }, {
            key: '12',
            name: 'openFileDescriptorCountTotal',
            value: clusterState.openFileDescriptorCountTotal,
            desc: '集群所有实例打开文件描述符数量总数'
        }];

        return (<div>
            <Card title="集群基本信息" bordered={false} hoverable={true}>
                <Row gutter={40} type="flex" justify="start">
                    <Col span={8} className={styles.detailCols}>
                        集群ID: <span
                        className={styles.detailColsContent}>{clusterInfoVo.id ? clusterInfoVo.id : '无'}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        集群名称：<span
                        className={styles.detailColsContent}>{clusterInfoVo.clusterName}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        集群描述：<span className={styles.detailColsContent}>{clusterInfoVo.intro}</span>
                    </Col>

                    <Col span={8} className={styles.detailCols}>
                        版本：<span
                        className={styles.detailColsContent}>{clusterInfoVo.version}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        部署类型：<span
                        className={styles.detailColsContent}>{this.baseInfoModel.getDeployTypeName(clusterInfoVo.deployType)}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        实例个数：<span className={styles.detailColsContent}>{clusterInfoVo.instanceNumber}</span>
                    </Col>

                    <Col span={8} className={styles.detailCols}>
                        负责人：<span className={styles.detailColsContent}>{clusterInfoVo.officer}</span>
                    </Col>

                    <Col span={8} className={styles.detailCols}>
                        集群状态：<span className={styles.detailColsContent}>
                        {this.runningInfo(clusterInfoVo.status)}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        业务线 ：<span
                        className={styles.detailColsContent}>{this.props.serviceLineOpsModel.getServiceLineName(clusterInfoVo.serviceLine)}</span>
                    </Col>
                </Row>
            </Card>
            <Card title="集群实时状态信息" bordered={false} hoverable={true}>
                <Table bordered={true} dataSource={dataSource} columns={this.columns} rowKey="name"
                       pagination={false}/>
            </Card>
        </div>)
    };
}
;
