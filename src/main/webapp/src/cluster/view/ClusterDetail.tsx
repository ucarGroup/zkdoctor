import React, {Component} from "react";
import {observer} from "mobx-react";
import {Link, withRouter} from "react-router";
import {inject} from "../../common/utils/IOC";
import {Button, Tabs} from "antd";
import {ClusterAlarmUserVo, ClusterModel, ClusterStatSearchVo} from "../model/ClusterModel";
import {ClusterInfo} from "./clusterDetail/ClusterInfo";
import ClusterAlarmUserModal from "./clusterDetail/ClusterAlarmUserModal";
import {UserModel} from "../../user/model/UserModel";
import {ClusterAlarmUser} from "./clusterDetail/ClusterAlarmUser";
import HeadInfo from "./clusterDetail/HeadInfo";
import {ClusterStats} from "./clusterState/Stats";
import {ClusterStatsTrendModel} from "../model/ClusterStatsTrendModel";
import {ClusterStatsDetail} from "./clusterState/StatsDetail";
import moment from "moment/moment";
import {ServiceLineOpsModel} from "../../ops/model/ServiceLineOpsModel";

/**
 * 集群监控统计详细信息
 */
interface DetailState {
    clusterInfoId: number;
    showAlarmUserModal: boolean;
    userItem: any;
    clusterItem: any;
    // 集群统计时间轴信息
    receivedStart: any;
    receivedEnd: any;
    outstandingsStart: any;
    outstandingsEnd: any;
    maxLatencyMaxStart: any;
    maxLatencyMaxEnd: any;
    avgLatencyMaxStart: any;
    avgLatencyMaxEnd: any;
    znodeCountStart: any;
    znodeCountEnd: any;
    ephemeralsStart: any;
    ephemeralsEnd: any;
    watcherTotalStart: any;
    watcherTotalEnd: any;
    connectionTotalStart: any;
    connectionTotalEnd: any;
    // 详细监控时间轴信息
    detailStart: any;
    detailEnd: any;
    // 当前选中的详细监控指标
    indicator: string;
}

interface DetailPropsI {
    location: any;
    router: any
}

@observer
class ClusterDetail extends Component<DetailPropsI, DetailState> {

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(UserModel)
    private userModel: UserModel;

    @inject(ClusterStatsTrendModel)
    private clusterStatsTrendModel: ClusterStatsTrendModel;

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
        // 集群统计时间轴信息，初始化时间间隔1天
        let startDay = moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm');
        // 详细监控时间轴信息，初始化时间间隔6小时
        let startHours = moment().subtract(6, 'hours').format('YYYY-MM-DD HH:mm');
        let endDay = moment().format('YYYY-MM-DD HH:mm');
        this.state = {
            showAlarmUserModal: false,
            userItem: null,
            clusterInfoId: null,
            clusterItem: {},
            receivedStart: startDay,
            receivedEnd: endDay,
            outstandingsStart: startDay,
            outstandingsEnd: endDay,
            maxLatencyMaxStart: startDay,
            maxLatencyMaxEnd: endDay,
            avgLatencyMaxStart: startDay,
            avgLatencyMaxEnd: endDay,
            znodeCountStart: startDay,
            znodeCountEnd: endDay,
            ephemeralsStart: startDay,
            ephemeralsEnd: endDay,
            watcherTotalStart: startDay,
            watcherTotalEnd: endDay,
            connectionTotalStart: startDay,
            connectionTotalEnd: endDay,
            detailStart: startHours,
            detailEnd: endDay,
            indicator: 'received',
        }
    }

    componentDidMount(): void {
        this.serviceLineOpsModel.query({});
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        this.loadClusterDetail(strArray[3]);
        this.setState({
            clusterInfoId: strArray[3]
        });
    }

    loadClusterDetail(clusterInfoId: number) {
        this.clusterModel.getClusterDetail(clusterInfoId);
    }

    loadClusterStatistics(searchVo: ClusterStatSearchVo) {
        this.clusterStatsTrendModel.initData(searchVo)
    }

    loadClusterDetailStatistics(searchVo: ClusterStatSearchVo) {
        this.clusterStatsTrendModel.queryAllIns(searchVo, this.state.indicator);
    }

    getClusterInfoProps() {
        return {
            clusterDetailVo: this.clusterModel.clusterDetailVo,
            serviceLineOpsModel: this.serviceLineOpsModel,
        };
    };

    getAlarmUserProps() {
        let thisV = this;
        return {
            alarmUsers: this.clusterModel.alarmUsers,
            onAdd() {
                thisV.userModel.listAll(() => {
                    thisV.setState({
                        showAlarmUserModal: true,
                    })
                });
            },
            onDelete(item: ClusterAlarmUserVo) {
                item.clusterId = thisV.state.clusterInfoId;
                thisV.clusterModel.deleteAlarmUser(item)
            }
        };
    };

    getClusterStatsProps() {
        let thisV = this;
        return {
            receivedChart: this.clusterStatsTrendModel.receivedTrendData,
            outstandingsChart: this.clusterStatsTrendModel.outstandingsTrendData,
            maxLatencyMaxChart: this.clusterStatsTrendModel.maxLatencyMaxTrendData,
            avgLatencyMaxChart: this.clusterStatsTrendModel.avgLatencyMaxTrendData,
            znodeCountChart: this.clusterStatsTrendModel.znodeCountTrendData,
            ephemeralsChart: this.clusterStatsTrendModel.ephemeralsTrendData,
            watcherTotalChart: this.clusterStatsTrendModel.watcherTotalTrendData,
            connectionTotalChart: this.clusterStatsTrendModel.connectionTotalTrendData,
            receivedDateChange(start: string, end: string) {
                thisV.setState({
                    receivedStart: start,
                    receivedEnd: end
                });
                thisV.clusterStatsTrendModel.queryReceived({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            outstandingsDateChange(start: string, end: string) {
                thisV.setState({
                    outstandingsStart: start,
                    outstandingsEnd: end
                });
                thisV.clusterStatsTrendModel.queryOutstandings({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            maxLatencyMaxDateChange(start: string, end: string) {
                thisV.setState({
                    maxLatencyMaxStart: start,
                    maxLatencyMaxEnd: end
                });
                thisV.clusterStatsTrendModel.queryMaxLatencyMax({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            avgLatencyMaxDateChange(start: string, end: string) {
                thisV.clusterStatsTrendModel.queryAvgLatencyMax({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            znodeCountDateChange(start: string, end: string) {
                thisV.setState({
                    znodeCountStart: start,
                    znodeCountEnd: end
                });
                thisV.clusterStatsTrendModel.queryZnodeCount({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            ephemeralsDateChange(start: string, end: string) {
                thisV.setState({
                    ephemeralsStart: start,
                    ephemeralsEnd: end
                });
                thisV.clusterStatsTrendModel.queryEphemerals({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            watcherTotalDateChange(start: string, end: string) {
                thisV.setState({
                    watcherTotalStart: start,
                    watcherTotalEnd: end
                });
                thisV.clusterStatsTrendModel.queryWatcherTotal({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
            connectionTotalDateChange(start: string, end: string) {
                thisV.setState({
                    connectionTotalStart: start,
                    connectionTotalEnd: end
                });
                thisV.clusterStatsTrendModel.queryConnectionTotal({
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                });
            },
        }
    }

    getClusterStatsDetailProps() {
        let thisV = this;
        return {
            indicator: this.state.indicator,
            allInsChart: this.clusterStatsTrendModel.allInsTrendData,
            handleDatePickerChange(dates) {
                let start = dates[0].format('YYYY-MM-DD HH:mm');
                let end = dates[1].format('YYYY-MM-DD HH:mm');
                let params = {
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                };
                thisV.clusterStatsTrendModel.queryAllIns(params, thisV.state.indicator);
                thisV.setState({
                    detailStart: start,
                    detailEnd: end
                });
            },
            handleTimeChange(e) {
                let start = thisV.getStartTime(e);
                let end = moment().subtract().format('YYYY-MM-DD HH:mm');
                let params = {
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                };
                thisV.clusterStatsTrendModel.queryAllIns(params, thisV.state.indicator);
                thisV.setState({
                    detailStart: start,
                    detailEnd: end
                });
            },
            indicatorChange(indicator) {
                let start = thisV.state.detailStart;
                let end = thisV.state.detailEnd;
                let params = {
                    id: thisV.state.clusterInfoId,
                    start: start,
                    end: end
                };
                thisV.clusterStatsTrendModel.queryAllIns(params, indicator);
                thisV.setState({
                    indicator: indicator
                });
            },
        }
    }

    getStartTime(e) {
        let start = '';
        switch (e.target.value) {
            case 'a':
                start = moment().subtract(30, 'minutes').format('YYYY-MM-DD HH:mm');
                break;
            case 'b':
                start = moment().subtract(1, 'hours').format('YYYY-MM-DD HH:mm');
                break;
            case 'c':
                start = moment().subtract(6, 'hours').format('YYYY-MM-DD HH:mm');
                break;
            case 'd':
                start = moment().subtract(12, 'hours').format('YYYY-MM-DD HH:mm');
                break;
            case 'e':
                start = moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm');
                break;
            case 'f':
                start = moment().subtract(3, 'days').format('YYYY-MM-DD HH:mm');
                break;
            case 'g':
                start = moment().subtract(7, 'days').format('YYYY-MM-DD HH:mm');
                break;
            case 'h':
                start = moment().subtract(14, 'days').format('YYYY-MM-DD HH:mm');
                break;
        }
        return start;
    }

    getHeadInfoProps() {
        let clusterInfo = this.clusterModel.clusterDetailVo ? this.clusterModel.clusterDetailVo.clusterInfo : null;
        return {
            clusterName: clusterInfo ? clusterInfo.clusterName : '',
        }
    }

    hideClusterAlarmUserModal() {
        this.setState({showAlarmUserModal: false});
    };

    getAlarmUserModalProps() {
        let thisV = this;
        return {
            users: this.userModel.users,
            onOk: function (item: ClusterAlarmUserVo) {
                item.clusterId = thisV.state.clusterInfoId;
                thisV.clusterModel.addClusterAlarmUser(item);
                thisV.hideClusterAlarmUserModal();
            },
            onCancel() {
                thisV.hideClusterAlarmUserModal();
            },
        };
    };

    render() {
        if (!this.state.clusterInfoId) {
            return <h3>找不到对应的集群信息!</h3>
        }

        const handleChange = (index) => {
            let clusterInfoId = this.state.clusterInfoId;
            if (index == 1) {
                this.serviceLineOpsModel.query({});
                this.loadClusterDetail(clusterInfoId);
            } else if (index == 2) {
                this.clusterModel.getClusterUsers(clusterInfoId);
            } else if (index == 3) {
                let start = moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm');
                let end = moment().format('YYYY-MM-DD HH:mm');
                this.loadClusterStatistics({
                    id: clusterInfoId,
                    start: start,
                    end: end
                });
            } else if (index == 4) {
                let start = moment().subtract(6, 'hours').format('YYYY-MM-DD HH:mm');
                let end = moment().format('YYYY-MM-DD HH:mm');
                this.loadClusterDetailStatistics({
                    id: clusterInfoId,
                    start: start,
                    end: end
                });
            }
        };

        return (
            <div>
                <HeadInfo {...this.getHeadInfoProps()} />
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      tabBarExtraContent={<Link to={'/cluster/list'}><Button>返回集群列表</Button></Link>}
                      onChange={handleChange}>
                    <Tabs.TabPane tab="详细信息" key="1">
                        <ClusterInfo {...this.getClusterInfoProps()}/>
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="报警用户" key="2">
                        <ClusterAlarmUser {...this.getAlarmUserProps()} />
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="集群统计" key="3">
                        <ClusterStats {...this.getClusterStatsProps()} />
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="详细监控" key="4">
                        <ClusterStatsDetail {...this.getClusterStatsDetailProps()} />
                    </Tabs.TabPane>
                </Tabs>
                {this.state.showAlarmUserModal ? <ClusterAlarmUserModal {...this.getAlarmUserModalProps()}/> : ''}
            </div>
        );
    }
}

export default withRouter(ClusterDetail);
