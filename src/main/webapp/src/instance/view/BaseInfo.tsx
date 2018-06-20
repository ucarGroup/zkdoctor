import React, {Component} from "react";
import {Button, Card, Col, DatePicker, Row, Switch, Table} from "antd";
import {Link} from "react-router";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import {inject} from "../../common/utils/IOC";
import styles from "../../themes/Search.less";
import {
    ClientConnectionSearchVo,
    InstanceConfigVo,
    InstanceConnectionHistoryVo,
    InstanceConnectionVo,
    InstanceInfoVo,
    InstanceVo
} from "../model/InstanceModel";
import Tag from "antd/lib/tag";
import ReactEcharts from "echarts-for-react";
import moment from "moment/moment";
import {SysUserVo} from "../../sys/model/SysUserModel";
import ConnectionSearch from "./ConnectionSearch";

export interface InstanceInfoProps {
    instance: InstanceVo;
}

/**
 * 实例实时状态信息展示
 */
export class InstanceDetailInfo extends Component<InstanceInfoProps, {}> {
    columns = [
        {
            title: '状态信息',
            dataIndex: 'desc',
            key: 'desc',
        }, {
            title: '状态值',
            dataIndex: 'value',
            key: 'value',
        }];

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    runningInfo = (status) => {
        return <Tag color={this.baseInfoModel.getInstanceStatusColor(status)}>
            {this.baseInfoModel.getInstanceStatusName(status)}</Tag>;
    };

    render() {
        let instance = this.props.instance;
        if (!instance) {
            return null;
        }
        let instanceInfo = instance.instanceInfo;
        let instanceState = instance.instanceState;

        if (!instanceInfo) {
            return null;
        }
        if (!instanceState) {
            return null;
        }
        let dataSource = [{
            key: '0',
            name: 'modifyTimeStr',
            value: instanceState.modifyTimeStr,
            desc: '该状态收集时间'
        }, {
            key: '1',
            name: 'version',
            value: instanceState.version,
            desc: 'zookeeper版本信息'
        }, {
            key: '2',
            name: 'avgLatency',
            value: instanceState.avgLatency,
            desc: '平均延时（ms）'
        }, {
            key: '3',
            name: 'maxLatency',
            value: instanceState.maxLatency,
            desc: '最大延时（ms）'
        }, {
            key: '4',
            name: 'minLatency',
            value: instanceState.minLatency,
            desc: '最小延时（ms）'
        }, {
            key: '5',
            name: 'received',
            value: instanceState.received,
            desc: '收包数（个）'
        }, {
            key: '6',
            name: 'sent',
            value: instanceState.sent,
            desc: '发包数（个）'
        }, {
            key: '7',
            name: 'currConnections',
            value: instanceState.currConnections,
            desc: '当前连接数（个）'
        }, {
            key: '8',
            name: 'currOutstandings',
            value: instanceState.currOutstandings,
            desc: '堆积请求数（个）'
        }, {
            key: '9',
            name: 'currWatchCount',
            value: instanceState.currWatchCount,
            desc: 'watch数量（个）'
        }, {
            key: '10',
            name: 'currEphemeralsCount',
            value: instanceState.currEphemeralsCount,
            desc: '临时节点数（个）'
        }, {
            key: '11',
            name: 'approximateDataSize',
            value: instanceState.approximateDataSize,
            desc: '数据大小（byte）（zookeeper内存中存储的所有数据节点path+data的大小）'
        }, {
            key: '12',
            name: 'openFileDescriptorCount',
            value: instanceState.openFileDescriptorCount,
            desc: '打开文件描述符数量（个）'
        }, {
            key: '13',
            name: 'maxFileDescriptorCount',
            value: instanceState.maxFileDescriptorCount,
            desc: '最大文件描述符数量（个）'
        }, {
            key: '14',
            name: 'followers',
            value: instanceState.followers,
            desc: 'follower数'
        }, {
            key: '15',
            name: 'syncedFollowers',
            value: instanceState.syncedFollowers,
            desc: '同步的follower数'
        }];

        return (<div>
            <Card title="实例基本信息" bordered={false} hoverable={true} style={{fontWeight: 'bold'}}>
                <Row gutter={40} type="flex" justify="start">
                    <Col span={8} className={styles.detailCols}>
                        实例ID: <span
                        className={styles.detailColsContent}>{instanceInfo.id ? instanceInfo.id : '无'}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        实例地址：<span
                        className={styles.detailColsContent}>{instanceInfo.host}:{instanceInfo.port}</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        运行状态：
                        <span className={styles.detailColsContent}>
                            {this.runningInfo(instanceInfo.status)}</span>
                    </Col>

                    <Col span={8} className={styles.detailCols}>
                        当前连接数: <span
                        className={styles.detailColsContent}>{instanceState.currConnections}（个）</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        收包数：<span
                        className={styles.detailColsContent}>{instanceState.received}（个）</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        发包数：<span className={styles.detailColsContent}>{instanceState.sent}（个）</span>
                    </Col>

                    <Col span={8} className={styles.detailCols}>
                        平均延时: <span
                        className={styles.detailColsContent}>{instanceState.avgLatency}（ms）</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        最大延时：<span
                        className={styles.detailColsContent}>{instanceState.maxLatency}（ms）</span>
                    </Col>
                    <Col span={8} className={styles.detailCols}>
                        最小延时：<span className={styles.detailColsContent}>{instanceState.minLatency}（ms）</span>
                    </Col>
                </Row>
            </Card>
            <Card title="实例实时状态信息" bordered={false} hoverable={true}>
                <Table bordered={true} dataSource={dataSource} columns={this.columns} rowKey="name"
                       pagination={false}/>
            </Card>
        </div>)
    };
};


export interface InstanceConfigProps {
    instanceConfigVo: InstanceConfigVo;
}

/**
 * 实例配置信息展示
 */
export class InstanceConfigInfo extends Component<InstanceConfigProps, {}> {
    columns = [
        {
            title: '配置名称',
            dataIndex: 'name',
            key: 'name',
        }, {
            title: '配置值',
            dataIndex: 'value',
            key: 'value',
        }, {
            title: '描述',
            dataIndex: 'desc',
            key: 'desc',
        }];

    render() {
        let instanceConfigVo = this.props.instanceConfigVo;
        let dataSource = [{
            key: '1',
            name: 'serverId',
            value: instanceConfigVo.serverId,
            desc: 'server id'
        }, {
            key: '2',
            name: 'clientPort',
            value: instanceConfigVo.clientPort,
            desc: '客户端端口号'
        }, {
            key: '3',
            name: 'dataDir',
            value: instanceConfigVo.dataDir,
            desc: '数据目录'
        }, {
            key: '4',
            name: 'dataLogDir',
            value: instanceConfigVo.dataLogDir,
            desc: '事务日志目录'
        }, {
            key: '5',
            name: 'tickTime',
            value: instanceConfigVo.tickTime,
            desc: '单位时间（ms）'
        }, {
            key: '6',
            name: 'maxClientCnxns',
            value: instanceConfigVo.maxClientCnxns,
            desc: '最大连接数'
        }, {
            key: '7',
            name: 'minSessionTimeout',
            value: instanceConfigVo.minSessionTimeout,
            desc: '最小超时时长（ms）'
        }, {
            key: '8',
            name: 'maxSessionTimeout',
            value: instanceConfigVo.maxSessionTimeout,
            desc: '最大超时时长（ms）'
        }, {
            key: '9',
            name: 'initLimit',
            value: instanceConfigVo.initLimit,
            desc: '初始化时间单位（n个tickTime）'
        }, {
            key: '10',
            name: 'syncLimit',
            value: instanceConfigVo.syncLimit,
            desc: '同步时间单位（n个tickTime）'
        }, {
            key: '11',
            name: 'electionAlg',
            value: instanceConfigVo.electionAlg,
            desc: '选举算法（0:LeaderElection,1:AuthFastLeaderElection,2:AuthFastLeaderElection,3:FastLeaderElection，默认为3，其他废除）'
        }, {
            key: '12',
            name: 'electionPort',
            value: instanceConfigVo.electionPort,
            desc: '选举端口号'
        }, {
            key: '13',
            name: 'quorumPort',
            value: instanceConfigVo.quorumPort,
            desc: '法人端口号'
        }, {
            key: '14',
            name: 'peerType',
            value: instanceConfigVo.peerType,
            desc: '服务类型（0:PARTICIPANT,,1:OBSERVER）'
        }];

        return (<div>
            <Card title="实例配置信息" bordered={false} hoverable={true}>
                <Table bordered={true} dataSource={dataSource} columns={this.columns} rowKey="name"
                       pagination={false}/>
            </Card>
        </div>)
    };
}
;

export interface InstanceConnectionsProps {
    loading: boolean;
    sysUser: SysUserVo;
    instanceInfo: InstanceInfoVo;
    instanceConnections: Array<InstanceConnectionVo>;
    connectionsHistory: Array<InstanceConnectionHistoryVo>;
    connectionCollectTime: any;
    switchChange: any;
    onSearchConnectionHistory: any;
}

export interface InstanceConnectionsState {
    orderBy: string;
    connectionCollectMonitor: boolean;
    showConnectionHistory: boolean;
}
/**
 * 实例连接信息展示
 */
export class InstanceConnections extends Component<InstanceConnectionsProps, InstanceConnectionsState> {

    constructor(props) {
        super(props);

        console.log(this.props.instanceInfo)

        this.state = {
            orderBy: 'clientIp',
            connectionCollectMonitor: this.props.instanceInfo.connectionMonitor,
            showConnectionHistory: false,
        }
    }

    expandedConnectionRowRender(bean) {
        const data = bean.infoLine ? bean.infoLine : '';
        return (
            <span>{data}</span>
        );
    };

    expandedConnectionIpHistoryRowRender(bean) {
        const connectionHistoryColumns = [
            {
                title: '客户端ip',
                dataIndex: 'clientIp',
                key: 'clientIp',
            }, {
                title: 'sid',
                dataIndex: 'sid',
                key: 'sid',
            }, {
                title: '堆积请求数',
                dataIndex: 'queued',
                key: 'queued',
            }, {
                title: '收包数',
                dataIndex: 'recved',
                key: 'recved',
            }, {
                title: '发包数',
                dataIndex: 'sent',
                key: 'sent',
            }, {
                title: '最大延时(ms)',
                dataIndex: 'maxlat',
                key: 'maxlat',
            }, {
                title: '平均延时(ms)',
                dataIndex: 'avglat',
                key: 'avglat',
            }, {
                title: '收集时间',
                dataIndex: 'createTimeStr',
                key: 'createTimeStr',
            }];
        return (<Table bordered={true} dataSource={bean.clientInfoList ? bean.clientInfoList.slice() : []}
                       columns={connectionHistoryColumns}
                       rowKey="key" style={{marginTop: 10}}
                       pagination={false}/>);
    };

    expandedConnectionHistoryRowRender(bean) {
        const data = bean.info ? bean.info : '';
        return (
            <span>{data}</span>
        );
    };

    getSearchProps() {
        let thisV = this;
        return {
            clusterId: thisV.props.instanceInfo.clusterId,
            instanceId: thisV.props.instanceInfo.id,
            onSearch(data) {
                thisV.setState({orderBy: data.orderBy});
                thisV.props.onSearchConnectionHistory(data);
            }
        };
    }

    render() {
        const connectionColumns = [
            {
                title: '客户端ip:port',
                dataIndex: 'hostInfo',
                key: 'hostInfo',
            }, {
                title: '收包数',
                dataIndex: 'recved',
                key: 'recved',
            }, {
                title: '发包数',
                dataIndex: 'sent',
                key: 'sent',
            }, {
                title: '堆积请求数',
                dataIndex: 'queued',
                key: 'queued',
            }, {
                title: '最后操作',
                dataIndex: 'lop',
                key: 'lop',
            }, {
                title: '最大延时(ms)',
                dataIndex: 'maxlat',
                key: 'maxlat',
            }, {
                title: '平均延时(ms)',
                dataIndex: 'avglat',
                key: 'avglat',
            }, {
                title: '最后事务id',
                dataIndex: 'lzxid',
                key: 'lzxid',
            }, {
                title: '超时时间(ms)',
                dataIndex: 'to',
                key: 'to',
            }];

        const connectionHistoryColumns = [
            {
                title: '客户端ip',
                dataIndex: 'clientIp',
                key: 'clientIp',
            }, {
                title: 'sid',
                dataIndex: 'sid',
                key: 'sid',
            }, {
                title: '堆积请求数',
                dataIndex: 'queued',
                key: 'queued',
            }, {
                title: '收包数',
                dataIndex: 'recved',
                key: 'recved',
            }, {
                title: '发包数',
                dataIndex: 'sent',
                key: 'sent',
            }, {
                title: '最大延时(ms)',
                dataIndex: 'maxlat',
                key: 'maxlat',
            }, {
                title: '平均延时(ms)',
                dataIndex: 'avglat',
                key: 'avglat',
            }, {
                title: '收集时间',
                dataIndex: 'createTimeStr',
                key: 'createTimeStr',
            }];

        const connectionHistoryClientIpColumns = [
            {
                title: '客户端ip',
                dataIndex: 'clientIp',
                key: 'clientIp',
            }, {
                title: '收集连接个数',
                dataIndex: 'number',
                key: 'number',
            }, {
                title: '堆积请求数',
                dataIndex: 'queued',
                key: 'queued',
            }, {
                title: '收包数',
                dataIndex: 'recved',
                key: 'recved',
            }, {
                title: '发包数',
                dataIndex: 'sent',
                key: 'sent',
            }, {
                title: '最大延时(ms)',
                dataIndex: 'maxlat',
                key: 'maxlat',
            }];

        let connectionHisColumns = connectionHistoryColumns;
        if (this.state.orderBy == 'clientIp') {
            connectionHisColumns = connectionHistoryClientIpColumns;
        }

        if (this.state.showConnectionHistory) {
            return (<div>
                <Card title="实例连接历史" bordered={false} hoverable={true}
                      extra={
                          this.props.sysUser.userRole == 1 || this.props.sysUser.userRole == 2 ?
                              <span>
                                  <label style={{marginRight: 5}}>连接信息收集开关：</label>
                                  <Switch checkedChildren="开" unCheckedChildren="关"
                                          checked={this.state.connectionCollectMonitor}
                                          onChange={(value) => {
                                              this.props.switchChange(this.props.instanceInfo.id, value, () => {
                                                  this.setState({connectionCollectMonitor: value});
                                              });
                                          }}/>
                                  <Button style={{marginLeft: 15}} type="primary"
                                          onClick={() => this.setState({showConnectionHistory: false})}>返回连接信息</Button>
                              </span>
                              :
                              <span>
                                  <Button style={{marginLeft: 15}} type="primary"
                                          onClick={() => this.setState({showConnectionHistory: false})}>返回连接信息</Button>
                              </span>
                      }>

                    <ConnectionSearch {...this.getSearchProps()}/>
                    <Table bordered={true} dataSource={this.props.connectionsHistory.slice()}
                           columns={connectionHisColumns}
                           rowKey="key" style={{marginTop: 10}}
                           loading={this.props.loading}
                           pagination={false}
                           expandedRowRender={this.state.orderBy == 'clientIp' ? this.expandedConnectionIpHistoryRowRender : this.expandedConnectionHistoryRowRender}/>
                </Card>
            </div>)
        } else {
            return (<div>
                <Card title={"实例连接信息-连接数：" + this.props.instanceConnections.slice().length + " 个，" + (this.props.connectionCollectTime ? ("以下统计数据曾在" + this.props.connectionCollectTime + "被清空") : "以下数据未被清空")} bordered={false}
                      hoverable={true}
                      extra={
                          this.props.sysUser.userRole == 1 || this.props.sysUser.userRole == 2 ?
                              <span>
                                  <label style={{marginRight: 5}}>连接信息收集开关：</label>
                                  <Switch checkedChildren="开" unCheckedChildren="关"
                                          checked={this.state.connectionCollectMonitor}
                                          onChange={(value) => {
                                              this.props.switchChange(this.props.instanceInfo.id, value, () => {
                                                  this.setState({connectionCollectMonitor: value});
                                              });
                                          }}/>
                                  <Button icon="search" style={{marginLeft: 15}} type="primary"
                                          onClick={() => {
                                              let data = new ClientConnectionSearchVo();
                                              data.startDate = moment().subtract(1, 'minutes').format('YYYY-MM-DD HH:mm');
                                              data.endDate = moment().format('YYYY-MM-DD HH:mm');
                                              data.clusterId = this.props.instanceInfo.clusterId;
                                              data.instanceId = this.props.instanceInfo.id;
                                              data.orderBy = 'clientIp';
                                              this.props.onSearchConnectionHistory(data);
                                              this.setState({showConnectionHistory: true});
                                          }}>查询连接信息历史</Button>
                              </span>
                              :
                              <span>
                                  <Button icon="search" style={{marginLeft: 15}} type="primary"
                                          onClick={() => {
                                              let data = new ClientConnectionSearchVo();
                                              data.startDate = moment().subtract(1, 'minutes').format('YYYY-MM-DD HH:mm');
                                              data.endDate = moment().format('YYYY-MM-DD HH:mm');
                                              data.clusterId = this.props.instanceInfo.clusterId;
                                              data.instanceId = this.props.instanceInfo.id;
                                              data.orderBy = 'clientIp';
                                              this.props.onSearchConnectionHistory(data);
                                              this.setState({showConnectionHistory: true});
                                          }}>查询连接信息历史</Button>
                              </span>
                      }>
                    <Table bordered={true} dataSource={this.props.instanceConnections.slice()}
                           columns={connectionColumns}
                           rowKey="hostInfo"
                           loading={this.props.loading}
                           pagination={false} expandedRowRender={this.expandedConnectionRowRender}/>
                </Card>
            </div>)
        }
    };
}

export interface MachineStatsProps {
    netTrafficChart: Array<any>;
    avgLoadChart: Array<any>;
    cpuChart: Array<any>;
    cpuSingleChart: Array<any>;
    memoryChart: Array<any>;
    diskChart: Array<any>;

    onTrafficDetail: any;

    netTrafficDateChange: any;
    avgLoadDateChange: any;
    cpuDateChange: any;
    cpuSingleDateChange: any;
    memoryDateChange: any;
    diskDateChange: any;
}

const percentLineYaxis = [
    {
        name: "使用率(%)",
        type: "value",
        min: 0,
        max: 100,
    }
]

const lineYaxis = [
    {
        type: "value",
    }
]

const lineGrid = {
    left: 80,
    right: 30,
    top: 30,
    bottom: 30
}

/**
 * 机器状态信息展示
 */
export class InstanceMachineStats extends Component<MachineStatsProps, {}> {

    constructor(props) {
        super(props);
    }

    getNetTrafficChart() {
        return this.getManyLineChart(false, this.props.netTrafficChart['time'] ? this.props.netTrafficChart['time'].slice() : [],
            this.props.netTrafficChart['value'] ? this.props.netTrafficChart['value'] : [], "KBps");
    }

    getAvgLoadChart() {
        return this.getLineChart("平均负载", false, this.props.avgLoadChart['time'] ? this.props.avgLoadChart['time'].slice() : [],
            this.props.avgLoadChart['value'] ? this.props.avgLoadChart['value'].slice() : [], "");
    }

    getCpuChart() {
        return this.getLineChart("CPU使用率", true, this.props.cpuChart['time'] ? this.props.cpuChart['time'].slice() : [],
            this.props.cpuChart['value'] ? this.props.cpuChart['value'].slice() : [], "%");
    }

    getCpuSingleChart() {
        return this.getManyLineChart(true, this.props.cpuSingleChart['time'] ? this.props.cpuSingleChart['time'].slice() : [],
            this.props.cpuSingleChart['value'] ? this.props.cpuSingleChart['value'] : [], "%");
    }

    getMemoryChart() {
        return this.getManyLineChart(false, this.props.memoryChart['time'] ? this.props.memoryChart['time'].slice() : [],
            this.props.memoryChart['value'] ? this.props.memoryChart['value'] : [], "GB");
    }

    getDiskChart() {
        return this.getManyLineChart(false, this.props.diskChart['time'] ? this.props.diskChart['time'].slice() : [],
            this.props.diskChart['value'] ? this.props.diskChart['value'] : [], "GB");
    }

    getLineChart(seriesName, isPercent, xAxisData, yAxisData, unit) {
        const lineTooltip = {
            trigger: "axis",
            axisPointer: {
                type: 'cross'
            },
            formatter: function (params) {
                let result = params[0].name + '<br/>';
                for (var i = 0; i < params.length; i++) {
                    result += params[i].seriesName + ' : ' + params[i].value + ' ' + unit + '<br/>'
                }
                return result;
            }
        }
        return {
            tooltip: lineTooltip,
            calculable: true,
            xAxis: [
                {
                    type: "category",
                    boundaryGap: false,
                    data: xAxisData,
                }
            ],
            yAxis: isPercent ? percentLineYaxis : lineYaxis,
            series: [
                {
                    name: seriesName,
                    type: "line",
                    data: yAxisData,
                    markLine: {
                        label: {
                            normal: {
                                position: 'middle',
                                formatter: '平均值：' + '{c}' + unit
                            }
                        },
                        data: [{
                            type: "average",
                            name: "平均值",
                        }, [{
                            symbol: 'none',
                            x: '80%',
                            y: '20%',
                            yAxis: 'max'
                        }, {
                            type: 'max',
                            symbol: 'circle',
                            label: {
                                normal: {
                                    position: 'right',
                                    formatter: '最大值：' + '{c}' + unit
                                }
                            },
                        }]
                        ]
                    }
                },
            ],
            grid: lineGrid,
            dataZoom: {
                show: true,
                start: 10,
                type: 'inside',
            },
            toolbox: {
                show: true,
                orient: 'horizontal',
                left: 'right',
                feature: {
                    dataView: {show: true, readOnly: true},
                    restore: {show: true},
                }
            },
        }
    }

    getManyLineChart(isPercent, xAxisData, yAxisDataArrayAll, unit) {
        let yAxisDataArray = [];
        let keyName = [];
        for (let [key, value] of Object.entries(yAxisDataArrayAll)) {
            yAxisDataArray.push(value.slice());
            keyName.push(key);
        }
        let seriesAll = [];
        for (let j = 0; j < yAxisDataArray.length; j++) {
            seriesAll.push({
                name: keyName[j],
                type: "line",
                data: yAxisDataArray[j],
                markLine: {
                    label: {
                        normal: {
                            position: 'middle',
                            formatter: '平均值：' + '{c}' + unit
                        }
                    },
                    data: [{
                        type: "average",
                        name: "平均值",
                    }
                    ]
                }
            })
        }

        const lineTooltip = {
            trigger: "axis",
            axisPointer: {
                type: 'cross'
            },
            formatter: function (params) {
                let result = params[0].name + '<br/>';
                for (var i = 0; i < params.length; i++) {
                    result += params[i].seriesName + ' : ' + params[i].value + ' ' + unit + '<br/>'
                }
                return result;
            }
        }

        return {
            tooltip: lineTooltip,
            calculable: true,
            legend: {
                data: keyName,
            },
            xAxis: [
                {
                    type: "category",
                    boundaryGap: false,
                    data: xAxisData,
                }
            ],
            yAxis: isPercent ? percentLineYaxis : lineYaxis,
            series: seriesAll,
            grid: lineGrid,
            dataZoom: {
                show: true,
                start: 10,
                type: 'inside',
            },
            toolbox: {
                show: true,
                orient: 'horizontal',
                left: 'right',
                feature: {
                    dataView: {show: true, readOnly: true},
                    restore: {show: true},
                }
            },
        }
    }

    onTrifficEvents = {
        'click': (e) => {
            // 获取机器的出入流量top10数据
            if (e.componentType == 'series') { // 确保点击的为数据序列
                this.props.onTrafficDetail(e.name);
            }
        }
    };

    render() {
        let netTrafficDateChange = (dates) => {
            this.props.netTrafficDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let avgLoadDateChange = (dates) => {
            this.props.avgLoadDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let cpuDateChange = (dates) => {
            this.props.cpuDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let cpuSingleDateChange = (dates) => {
            this.props.cpuSingleDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let memoryDateChange = (dates) => {
            this.props.memoryDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };
        let diskDateChange = (dates) => {
            this.props.diskDateChange(dates[0].format('YYYY-MM-DD HH:mm'), dates[1].format('YYYY-MM-DD HH:mm'));
        };

        return (<div>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>网络流量（KBps）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={netTrafficDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getNetTrafficChart()} notMerge={true}
                                      onEvents={this.onTrifficEvents}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>平均负载（最近一分钟）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={avgLoadDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getAvgLoadChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>

            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>CPU使用率（%）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={cpuDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getCpuChart()} notMerge={true} style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>单CPU使用率（%）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={cpuSingleDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getCpuSingleChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>

            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                borderBottom: '1px #d7d8d9 solid',
                paddingTop: 20,
                paddingBottom: 20
            }}>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>内存情况（GB）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={memoryDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getMemoryChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
                <div style={{width: '49%'}}>
                    <div style={{display: 'flex'}}>
                        <div style={{marginBottom: 6, display: 'flex'}}>
                            <div style={{marginRight: 10, fontWeight: 'bold'}}>磁盘（dataDir）情况（GB）</div>
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    defaultValue={[moment().subtract(1, 'days'), moment()]}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"
                                                    onOk={diskDateChange}/>
                        </div>
                    </div>
                    <div style={{border: '1px solid #e7e8e9', borderRadius: 4, paddingTop: 10}}>
                        <ReactEcharts option={this.getDiskChart()} notMerge={true}
                                      style={{height: 280, width: '100%'}}/>
                    </div>
                </div>
            </div>
        </div>)
    };
}
;
