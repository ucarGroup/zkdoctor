import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {Link, withRouter} from "react-router";
import {Button, notification, Tabs} from "antd";
import {ClusterMonitorAlarmTaskModel, ClusterMonitorAlarmTaskVo} from "../model/ClusterMonitorAlarmTaskModel";
import {MonitorTaskVo} from "../model/MonitorTaskModel";
import {MonitorAlarmTaskList} from "./MonitorAlarmTaskList";
import ModifyMonitorAlarmTaskModal from "./MonitorAlarmTaskModal";
import {SysUserModel} from "../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
interface DetailState {
    showMonitorTaskModal: boolean;
    clusterMonitorAlarmTask: ClusterMonitorAlarmTaskVo;
    monitorTaskArray: Array<ClusterMonitorAlarmTaskVo>;
    task: MonitorTaskVo;
    clusterId:number;
}

interface DetailPropsI {
    location: any;
    router: any
}

@observer
export default class ClusterMonitorAlarmTaskList extends Component<DetailPropsI, DetailState> {

    @inject(ClusterMonitorAlarmTaskModel)
    private clusterMonitorAlarmTaskModel: ClusterMonitorAlarmTaskModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    constructor(props) {
        super(props);
        this.state = {
            showMonitorTaskModal: false,
            clusterMonitorAlarmTask: null,
            monitorTaskArray:[],
            task: null,
            clusterId:1
        }
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        let clusterMonitorAlarmTaskVo = new ClusterMonitorAlarmTaskVo();
        clusterMonitorAlarmTaskVo.clusterId =strArray[3];
        this.clusterMonitorAlarmTaskModel.query(clusterMonitorAlarmTaskVo);
        this.setState({})
    }

    getMonitorTaskListProps() {
        let thisV = this;
        return {
            sysUser: this.sysUserModel.sysUser,
            baseInfoModel: this.baseInfoModel,
            dataSource: thisV.clusterMonitorAlarmTaskModel.clusterMonitorAlarmTasks ,
            loading: thisV.clusterMonitorAlarmTaskModel.loading,
            pageConfig: thisV.clusterMonitorAlarmTaskModel.pageConfig,
            switchChange(item, value) {
                thisV.clusterMonitorAlarmTaskModel.updateClusterTaskSwitchOn(item.id, value);
            },
            onEdit(item) {
                thisV.setState({clusterMonitorAlarmTask: item, showMonitorTaskModal: true});
            }
        };
    };

    getMonitorTaskModalProps() {
        let thisV = this;
        return {
            baseInfoModel: thisV.baseInfoModel,
            clusterMonitorAlarmTask: this.state.clusterMonitorAlarmTask,
            onOk: function (item) {
                thisV.clusterMonitorAlarmTaskModel.updateClusterMonitorTask(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.setState({showMonitorTaskModal: false});
            },
            onCancel() {
                thisV.setState({showMonitorTaskModal: false});
            },
        };
    };

    render() {
        return (
            <div>
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      tabBarExtraContent={<Link to={'/cluster/list'}><Button>返回集群列表</Button></Link>}>
                    <Tabs.TabPane tab="集群监控报警任务列表" key="1">
                        <MonitorAlarmTaskList {...this.getMonitorTaskListProps()}/>
                    </Tabs.TabPane>
                </Tabs>
                {this.state.showMonitorTaskModal ? <ModifyMonitorAlarmTaskModal {...this.getMonitorTaskModalProps()}/> : ''}
            </div>
        );
    }
}
