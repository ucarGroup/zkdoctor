import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {withRouter} from "react-router";
import {notification, Tabs} from "antd";
import {MonitorIndicatorModel, MonitorIndicatorVo} from "../model/MonitorIndicatorModel";
import {MonitorTaskModel, MonitorTaskVo} from "../model/MonitorTaskModel";
import {IndicatorList} from "./IndicatorList";
import {TaskList} from "./TaskList";
import TaskModal from "./TaskModal";
import ModifyIndicatorModal from "./IndicatorModal";
import {SysUserModel} from "../../sys/model/SysUserModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

interface DetailState {
    showIndicatorModal: boolean;
    showTaskModal: boolean;
    indicator: MonitorIndicatorVo;
    indicatorArray: Array<MonitorIndicatorVo>;
    task: MonitorTaskVo;
    editTaskIndex: number;
    panes: Array<any>;
    activeKey: any;
    newTabIndex: number;
}

interface DetailPropsI {
    location: any;
    router: any
}

@observer
export default class MonitorIndicatorList extends Component<DetailPropsI, DetailState> {

    @inject(MonitorIndicatorModel)
    private monitorIndicatorModel: MonitorIndicatorModel;

    @inject(MonitorTaskModel)
    private monitorTaskModel: MonitorTaskModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    constructor(props) {
        super(props);
        this.state = {
            showIndicatorModal: false,
            showTaskModal: false,
            indicator: null,
            indicatorArray: [],
            task: null,
            editTaskIndex: 0,
            panes: [],
            activeKey: '1',
            newTabIndex: 1
        }
    }

    componentDidMount(): void {
        this.monitorIndicatorModel.query(null);
        this.setState({})
    }

    getIndicatorListProps() {
        let thisV = this;
        return {
            sysUser: this.sysUserModel.sysUser,
            baseInfoModel: this.baseInfoModel,
            indicator: thisV.state.indicator,
            dataSource: thisV.monitorIndicatorModel.indicators,
            loading: thisV.monitorIndicatorModel.loading,
            pageConfig: thisV.monitorIndicatorModel.pageConfig,
            switchChange(item, value) {
                thisV.monitorIndicatorModel.updateIndicatorSwitchOn(item.id, value);
            },
            onEdit(item) {
                thisV.setState({indicator: item, showIndicatorModal: true});
            },
            onSearchTask(indicator) {
                // 当前显示的任务所对应的指标信息，进行保存
                thisV.state.indicatorArray.push(indicator);
                // 搜索对应的监控任务信息时，先保证设置好当前指标信息后，再设置显示列表内容，否则将获取不到当前监控指标所对应的监控任务
                thisV.setState({
                    indicator: indicator,
                }, () => {
                    const panes = thisV.state.panes;
                    const activeKey = `${++thisV.state.newTabIndex}`;
                    panes.push({
                        title: indicator.indicatorName + '监控任务列表',
                        content: <TaskList {{activeKey: activeKey, ...thisV.getTaskListProps()}}/>,
                        key: activeKey
                    });
                    thisV.setState({
                        panes: panes,
                        activeKey: activeKey,
                        newTabIndex: activeKey,
                    });
                });
            }
        };
    };

    getTaskListProps() {
        let thisV = this;
        return {
            sysUser: this.sysUserModel.sysUser,
            baseInfoModel: thisV.baseInfoModel,
            indicator: thisV.state.indicator,
            monitorTaskModel: thisV.monitorTaskModel,
            switchChange(activeKey, item, value) {
                thisV.monitorTaskModel.updateTaskSwitchOn(activeKey, item.id, value);
            },
            onEdit(activeKey, item) {
                thisV.setState({
                    editTaskIndex: activeKey,
                    task: item,
                    showTaskModal: true
                });
            }
        };
    };

    getTaskModalProps() {
        let thisV = this;
        return {
            baseInfoModel: thisV.baseInfoModel,
            task: this.state.task,
            // 当前任务所对应的监控指标信息
            indicator: thisV.state.indicatorArray[thisV.state.editTaskIndex - 2],
            onOk: function (item) {
                thisV.monitorTaskModel.updateTask(thisV.state.editTaskIndex, item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.setState({showTaskModal: false});
            },
            onCancel() {
                thisV.setState({showTaskModal: false});
            },
        };
    };

    getIndicatorModalProps() {
        let thisV = this;
        return {
            baseInfoModel: thisV.baseInfoModel,
            indicator: this.state.indicator,
            onOk: function (item) {
                thisV.monitorIndicatorModel.updateIndicator(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.setState({showIndicatorModal: false});
            },
            onCancel() {
                thisV.setState({showIndicatorModal: false});
            },
        };
    };

    onChangeTab = (activeKey) => {
        this.setState({
            activeKey: activeKey
        });
    };

    onEditTab = (targetKey, action) => {
        this[action + 'Tab'](targetKey);
    };

    removeTab = (targetKey) => {
        let activeKey = this.state.activeKey;
        let lastIndex = 1;
        this.state.panes.forEach((pane, i) => {
            if (pane.key === targetKey) {
                lastIndex = i - 1;
            }
        });
        const panes = this.state.panes.filter(pane => pane.key !== targetKey);
        if (lastIndex >= 0 && activeKey === targetKey) {
            activeKey = panes[lastIndex].key;
        } else {
            activeKey = '1';
        }
        this.setState({
            panes: panes,
            activeKey: activeKey
        });
    };

    render() {
        return (
            <div>
                <Tabs
                    hideAdd
                    activeKey=this.state.activeKey
                    onChange={this.onChangeTab}
                    onEdit={this.onEditTab}
                    type="editable-card"
                >
                    <Tabs.TabPane tab="监控指标列表" key="1" closable={false}>
                        <IndicatorList {...this.getIndicatorListProps()}/>
                    </Tabs.TabPane>
                    {this.state.panes.map(pane => <Tabs.TabPane tab={pane.title}
                                                                key={pane.key}>{pane.content}</Tabs.TabPane>)}
                </Tabs>

                {this.state.showIndicatorModal ? <ModifyIndicatorModal {...this.getIndicatorModalProps()}/> : ''}
                {this.state.showTaskModal ? <TaskModal {...this.getTaskModalProps()}/> : ''}
            </div>
        );
    }
}
