import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {withRouter} from "react-router";
import {Tabs} from "antd";
import {InstanceConfigVo, InstanceInfoVo, InstanceModel, MachineStatSearchVo} from "../model/InstanceModel";
import {InstanceConfigInfo, InstanceConnections, InstanceDetailInfo, InstanceMachineStats} from "./BaseInfo";
import InstanceHeadInfo from "./InstanceHeadInfo";
import moment from "moment/moment";
import {SysUserModel} from "../../sys/model/SysUserModel";
import TrafficModal from "./TrafficModal";

/**
 * 实例监控信息
 */
interface DetailState {
    instanceId: number;
    clusterName: string;
    showTrafficModal: boolean;
    dateTime: string;
    // 流量top10数据
    trafficDetailData: any;
    // 监控统计时间点信息
    netTrafficStart: any;
    netTrafficEnd: any;
    avgLoadStart: any;
    avgLoadEnd: any;
    cpuStart: any;
    cpuEnd: any;
    cpuSingleStart: any;
    cpuSingleEnd: any;
    memoryStart: any;
    memoryEnd: any;
    diskStart: any;
    diskEnd: any;
}

interface DetailPropsI {
    location: any;
    router: any
}

@observer
class InstanceDetail extends Component<DetailPropsI, DetailState> {

    @inject(InstanceModel)
    private instanceModel: InstanceModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    constructor(props) {
        super(props);
        let startDay = moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm');
        let endDay = moment().format('YYYY-MM-DD HH:mm');
        this.state = {
            instanceId: null,
            clusterName: null,
            showTrafficModal: false,
            dateTime: null,
            netTrafficStart: startDay,
            netTrafficEnd: endDay,
            avgLoadStart: startDay,
            avgLoadEnd: endDay,
            cpuStart: startDay,
            cpuEnd: endDay,
            cpuSingleStart: startDay,
            cpuSingleEnd: endDay,
            memoryStart: startDay,
            memoryEnd: endDay,
            diskStart: startDay,
            diskEnd: endDay,
        }
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let id = Number.parseInt(pathname.substr(pathname.lastIndexOf('/') + 1));
        let strArray = pathname.split('/');
        let clusterName = strArray[3];
        this.loadInstanceDetail(id);
        this.setState({
            instanceId: id,
            clusterName: clusterName
        });
    }

    loadInstanceDetail(instanceId: number) {
        this.instanceModel.getInstanceDetailInfo(instanceId);
    }

    loadInstanceConfig(instanceId: number) {
        this.instanceModel.getInstanceConfig(instanceId);
    }

    loadInstanceConnections(instanceId: number) {
        this.instanceModel.getInstanceConnections(instanceId);
    }

    getInstanceInfoProps() {
        return {
            instance: this.instanceModel.instance
        };
    };

    getInstanceConfigProps() {
        let thisV = this;
        return {
            instanceConfigVo: this.instanceModel.instanceConfigVo ? thisV.instanceModel.instanceConfigVo : new InstanceConfigVo()
        };
    };

    getInstanceConnectionsProps() {
        let thisV = this;
        return {
            loading: this.instanceModel.loading,
            sysUser: this.sysUserModel.sysUser,
            instanceInfo: thisV.instanceModel.instance ? thisV.instanceModel.instance.instanceInfo : new InstanceInfoVo(),
            instanceConnections: thisV.instanceModel.instanceConnections ? thisV.instanceModel.instanceConnections : [],
            connectionsHistory: thisV.instanceModel.connectionsHistory ? thisV.instanceModel.connectionsHistory : [],
            connectionCollectTime: thisV.instanceModel.connectionCollectTime,
            switchChange(instanceId, monitor, callback) {
                thisV.instanceModel.updateInstanceConnCollectMonitor(instanceId, monitor, callback);
            },
            onSearchConnectionHistory(data) {
                thisV.instanceModel.searchConnectionHistory(data);
            }
        };
    }

    initMachineStateData(searchVo: MachineStatSearchVo) {
        this.instanceModel.initMachineStateData(searchVo)
    }

    getMachineStatsProps() {
        let thisV = this;
        return {
            netTrafficChart: thisV.instanceModel.netTrafficTrendData,
            avgLoadChart: thisV.instanceModel.avgLoadTrendData,
            cpuChart: thisV.instanceModel.cpuTrendData,
            cpuSingleChart: thisV.instanceModel.cpuSingleTrendData,
            memoryChart: thisV.instanceModel.memoryTrendData,
            diskChart: thisV.instanceModel.diskTrendData,
            onTrafficDetail(dateTime) {
                thisV.instanceModel.queryNetTrafficDetail(thisV.state.instanceId, dateTime, (data) => {
                    thisV.setState({
                        dateTime: dateTime,
                        trafficDetailData: data,
                        showTrafficModal: true
                    })
                });
            },
            netTrafficDateChange(start: string, end: string) {
                thisV.setState({netTrafficStart: start, netTrafficEnd: end});
                let params = {
                    id: thisV.state.instanceId,
                    start: start,
                    end: end
                };
                thisV.instanceModel.queryNetTraffic(params);
            },
            avgLoadDateChange(start: string, end: string) {
                thisV.setState({avgLoadStart: start, avgLoadEnd: end});
                let params = {
                    id: thisV.state.instanceId,
                    start: start,
                    end: end
                };
                thisV.instanceModel.queryAvgLoad(params);
            },
            cpuDateChange(start: string, end: string) {
                thisV.setState({cpuStart: start, cpuEnd: end});
                let params = {
                    id: thisV.state.instanceId,
                    start: start,
                    end: end
                };
                thisV.instanceModel.queryCpu(params);
            },
            cpuSingleDateChange(start: string, end: string) {
                thisV.setState({cpuSingleStart: start, cpuSingleEnd: end});
                let params = {
                    id: thisV.state.instanceId,
                    start: start,
                    end: end
                };
                thisV.instanceModel.queryCpuSingle(params);
            },
            memoryDateChange(start: string, end: string) {
                thisV.setState({memoryStart: start, memoryEnd: end});
                let params = {
                    id: thisV.state.instanceId,
                    start: start,
                    end: end
                };
                thisV.instanceModel.queryMemory(params);
            },
            diskDateChange(start: string, end: string) {
                thisV.setState({diskStart: start, diskEnd: end});
                let params = {
                    id: thisV.state.instanceId,
                    start: start,
                    end: end
                };
                thisV.instanceModel.queryDisk(params);
            },
        }
    }

    getHeadInfoProps() {
        let thisV = this;
        let hostInfo = '';
        if (thisV.instanceModel.instance) {
            hostInfo = thisV.instanceModel.instance.instanceInfo.host + ':' + thisV.instanceModel.instance.instanceInfo.port
        }
        return {
            clusterName: thisV.state.clusterName ? (thisV.state.clusterName == 'undefined' ? '' : thisV.state.clusterName) : '',
            hostInfo: hostInfo
        }
    }

    getTrafficDetailModalProps() {
        let thisV = this;
        return {
            dateTime: this.state.dateTime,
            trafficInDetail: this.state.trafficDetailData ? this.state.trafficDetailData.trafficInDetail : '',
            trafficOutDetail: this.state.trafficDetailData ? this.state.trafficDetailData.trafficOutDetail : '',
            onOk: function () {
                thisV.hideTrafficDetailModal()
            },
            onCancel() {
                thisV.hideTrafficDetailModal()
            },
        }
    }

    hideTrafficDetailModal() {
        this.setState({
            dateTime: null,
            trafficDetailData: null,
            showTrafficModal: false
        })
    }

    render() {
        if (!this.state.instanceId) {
            return <h3>找不到对应的实例信息!</h3>
        }
        const handleChange = (index) => {
            let instanceId = this.state.instanceId;
            if (index == 1) {
                this.loadInstanceDetail(instanceId);
            } else if (index == 2) {
                this.loadInstanceConfig(this.state.instanceId);
            } else if (index == 3) {
                this.loadInstanceConnections(this.state.instanceId);
            } else if (index == 4) {
                let start = moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm');
                let end = moment().format('YYYY-MM-DD HH:mm');
                this.initMachineStateData({id: instanceId, start, end})
            }
        };

        return (
            <div>
                {this.state.showTrafficModal ?
                    <TrafficModal {...this.getTrafficDetailModalProps()}/> : ''}
                <InstanceHeadInfo {...this.getHeadInfoProps()} />
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      onChange={handleChange}>
                    <Tabs.TabPane tab="实例详情" key="1">
                        <InstanceDetailInfo {...this.getInstanceInfoProps()}/>
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="配置信息" key="2">
                        <InstanceConfigInfo {...this.getInstanceConfigProps()}/>
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="连接信息" key="3">
                        <InstanceConnections {...this.getInstanceConnectionsProps()}/>
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="机器状态" key="4">
                        <InstanceMachineStats {...this.getMachineStatsProps()}/>
                    </Tabs.TabPane>
                </Tabs>
            </div>
        );
    }
}

export default withRouter(InstanceDetail);
