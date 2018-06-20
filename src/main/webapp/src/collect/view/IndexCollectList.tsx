import React, {Component} from "react";
import {Card, Col, Row} from "antd";
import {Link} from "react-router";
import {IndexCollectInfosVo} from "../model/ClusterMonitorAlarmInfoModel";

export interface ListProps {
    dataSource: IndexCollectInfosVo;
}

export class IndexCollectList extends Component<ListProps, {}> {

    clusterExceptionInfoFun() {
        const status = 4;
        if (this.props.dataSource != null && this.props.dataSource.clusterCollectInfo.exceptionsTotal != 0) {
            return (<Link to={'/cluster/list/' + status}
                          style={{color: '#FF0000'}}> {this.props.dataSource.clusterCollectInfo.exceptionsTotal}</Link>);
        }
        return 0;
    }

    instanceExceptionInfoFun() {
        const status = 0;
        if (this.props.dataSource != null && this.props.dataSource.instanceCollectInfo.exceptionsTotal != 0) {
            return (<Link to={'/instance/listByCluster/' + status}
                          style={{color: '#FF0000'}}> {this.props.dataSource.instanceCollectInfo.exceptionsTotal}</Link>);
        }
        return 0;
    }

    render() {
        return (<div>
            <Card title="集群汇总信息" bordered={false}>
                <Row gutter={16}>
                    <Col span={5}>
                        <Card title="总数" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.clusterCollectInfo.sumTotal}
                        </Card>
                    </Col>
                    <Col span={5}>
                        <Card title="正在运行" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.clusterCollectInfo.runningTotal}
                        </Card>
                    </Col>
                    <Col span={5}>
                        <Card title="异常" hoverable={true} style={{textAlign: 'center'}}>
                            {this.clusterExceptionInfoFun()}
                        </Card>
                    </Col>
                    <Col span={5}>
                        <Card title="已下线" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.clusterCollectInfo.referralTotal}
                        </Card>
                    </Col>
                    <Col span={4}>
                        <Card title="未监控" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.clusterCollectInfo.unmonitoredTotal}
                        </Card>
                    </Col>
                </Row>
            </Card>
            <Card title="实例汇总信息" bordered={false}>
                <Row gutter={16}>
                    <Col span={5}>
                        <Card title="总数" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.instanceCollectInfo.sumTotal}
                        </Card>
                    </Col>
                    <Col span={5}>
                        <Card title="正在运行" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.instanceCollectInfo.runningTotal}
                        </Card>
                    </Col>
                    <Col span={5}>
                        <Card title="异常" hoverable={true} style={{textAlign: 'center'}}>
                            {this.instanceExceptionInfoFun()}
                        </Card>
                    </Col>
                    <Col span={5}>
                        <Card title="已下线" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.instanceCollectInfo.referralTotal}
                        </Card>
                    </Col>
                    <Col span={4}>
                        <Card title="未运行" hoverable={true} style={{textAlign: 'center'}}>
                            {this.props.dataSource == null ? 0 : this.props.dataSource.instanceCollectInfo.notRunningTotal}
                        </Card>
                    </Col>
                </Row>
            </Card>
        </div>)
    }
}