import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {notification} from "antd";
import Search from "../../cluster/view/clusterList/Search";
import {ClusterInfoVo, ClusterModel, ClusterSearchVo} from "../../cluster/model/ClusterModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import {SysUserModel} from "../../sys/model/SysUserModel";
import ClusterDynamicExpansionModal from "./ClusterDynamicExpansionModal";
import ClusterDynamicExpansionResultModal from "./ClusterDynamicExpansionResultModal";
import ClusterRestartModal from "./ClusterRestartModal";
import ClusterRestartResultModal from "./ClusterRestartResultModal";
import {ClusterOpsList} from "./ClusterOpsList";
import {ClusterOpsModel, ZKDynamicExpansionConfig} from "../model/ClusterOpsModel";
import {ServiceLineOpsModel} from "../model/ServiceLineOpsModel";
import ModifyClusterModal from "./ModifyClusterModal";

interface DetailState {
    showEditModal: boolean;
    showExpansionModal: boolean;
    showExpansionResultModal: boolean;
    showRestartModal: boolean;
    showRestartResultModal: boolean;
    type: string;
    clusterItem: ClusterInfoVo;
    isRestartOver: boolean;
    isExpansionOver: boolean;
}

interface DetailPropsI {
    location: any;
}

@observer
export default class ClusterOps extends Component<DetailPropsI, DetailState> {

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(ClusterOpsModel)
    private clusterOpsModel: ClusterOpsModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
        this.state = {
            showEditModal: false,
            showExpansionModal: false,
            showExpansionResultModal: false,
            showRestartModal: false,
            showRestartResultModal: false,
            clusterItem: null,
            type: 'create',
            isRestartOver: false,
            isExpansionOver: false
        }
    }

    componentDidMount(): void {
        this.serviceLineOpsModel.query({});
        this.clusterModel.query({});
        this.setState({})
    }

    showModal(clusterItem: ClusterInfoVo, type: string): void {
        this.setState({showExpansionModal: true, clusterItem: clusterItem, type: type});
    };

    hideModal(): void {
        this.setState({showExpansionModal: false, clusterItem: null});
    };

    getSearchProps() {
        let thisV = this;
        return {
            serviceLines: this.serviceLineOpsModel.serviceLines,
            baseInfoModel: this.baseInfoModel,
            onSearch(searchVo: ClusterSearchVo) {
                thisV.clusterModel.query(searchVo);
            }
        };
    };

    getListProps() {
        let thisV = this;
        return {
            dataSource: this.clusterModel.clusters,
            loading: this.clusterModel.loading,
            pageConfig: this.clusterModel.pageConfig,
            sysUser: this.sysUserModel.sysUser,
            baseInfoModel: this.baseInfoModel,
            serviceLineOpsModel: this.serviceLineOpsModel,
            onEdit(item) {
                thisV.setState({
                    clusterItem: item,
                    showEditModal: true
                })
            },
            onDynamicExpansion(item) {
                thisV.showModal(item, 'edit');
            },
            onOffLine(id) {
                thisV.clusterOpsModel.offLine(id, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
            },
            onRestartQuorum(item) {
                thisV.setState({
                    showRestartModal: true,
                    clusterItem: item,
                });
            }
        };
    };

    private expansionTimer;

    getExpansionModalProps() {
        let thisV = this;
        return {
            clusterItem: this.state.clusterItem,
            onOk: function (item: ZKDynamicExpansionConfig) {
                thisV.clusterOpsModel.clusterDynamicExpansion(item, (message) => {
                    // 最后刷新一次，获取最后结果
                    thisV.clusterOpsModel.getClusterDynamicExpansionResult(item.clusterId);
                    thisV.setState({
                        showExpansionResultModal: thisV.clusterOpsModel.lastDynamicExpansionResult || thisV.clusterOpsModel.dynamicExpansionResult,
                        isExpansionOver: true
                    });
                    // 停止刷新
                    clearInterval(thisV.expansionTimer);
                });
                thisV.hideModal();
                // 每1s执行一次，获取执行结果
                thisV.expansionTimer = setInterval(function () {
                    thisV.clusterOpsModel.getClusterDynamicExpansionResult(item.clusterId, (isClear) => {
                        if (isClear) {
                            clearInterval(thisV.expansionTimer);
                            thisV.setState({
                                isExpansionOver: true
                            })
                        }
                    });
                    // 处理结果为异常的时候，不显示结果框
                    thisV.setState({
                        showExpansionResultModal: thisV.clusterOpsModel.lastDynamicExpansionResult || thisV.clusterOpsModel.dynamicExpansionResult,
                    })
                }, 1000);
            },
            onCancel() {
                thisV.hideModal();
            },
        };
    };

    getExpansionResultModalProps() {
        let thisV = this;
        return {
            isExpansionOver: thisV.state.isExpansionOver,
            resultList: thisV.clusterOpsModel.dynamicExpansionResultList ? thisV.clusterOpsModel.dynamicExpansionResultList : [],
            onOk: function () {
                thisV.setState({
                    showExpansionResultModal: false,
                    isExpansionOver: false
                })
            },
            onCancel() {
                thisV.setState({
                    showExpansionResultModal: false,
                    isExpansionOver: false
                })
            },
        };
    };

    private restartTimer;

    getRestartModalProps() {
        let thisV = this;
        return {
            clusterItem: this.state.clusterItem,
            onOk: function (item) {
                thisV.clusterOpsModel.restartQuorum(item, (message) => {
                    // 最后刷新一次，获取最后结果
                    thisV.clusterOpsModel.getClusterRestartResult(thisV.state.clusterItem.id);
                    thisV.setState({
                        showRestartResultModal: thisV.clusterOpsModel.lastRestartResult || thisV.clusterOpsModel.restartResult,
                        isRestartOver: true
                    });
                    // 停止刷新
                    clearInterval(thisV.restartTimer);
                });
                thisV.setState({showRestartModal: false});
                // 每1s执行一次，获取执行结果
                thisV.restartTimer = setInterval(function () {
                    thisV.clusterOpsModel.getClusterRestartResult(thisV.state.clusterItem.id, (isClear) => {
                        if (isClear) {
                            clearInterval(thisV.restartTimer);
                            thisV.setState({
                                isRestartOver: true
                            });
                        }
                    });
                    // 处理结果为异常的时候，不显示结果框
                    thisV.setState({
                        showRestartResultModal: thisV.clusterOpsModel.lastRestartResult || thisV.clusterOpsModel.restartResult,
                    })
                }, 1000);
            },
            onCancel() {
                thisV.setState({
                    showRestartModal: false,
                    clusterItem: null
                })
            },
        };
    }

    getRestartResultModalModalProps() {
        let thisV = this;
        return {
            isOver: thisV.state.isRestartOver,
            title: "重启中",
            resultList: thisV.clusterOpsModel.restartResultList ? thisV.clusterOpsModel.restartResultList : [],
            onOk: function () {
                thisV.setState({
                    showRestartResultModal: false,
                    isRestartOver: false
                })
            },
            onCancel() {
                thisV.setState({
                    showRestartResultModal: false,
                    isRestartOver: false
                })
            },
        };
    }

    getModalProps() {
        let thisV = this;
        return {
            serviceLines: this.serviceLineOpsModel.serviceLines,
            baseInfoModel: this.baseInfoModel,
            clusterItem: this.state.clusterItem,
            onOk: function (item: ClusterInfoVo) {
                thisV.clusterModel.editClusterInfo(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.hideEditModal();
            },
            onCancel() {
                thisV.hideEditModal();
            },
        };
    };

    hideEditModal(): void {
        this.setState({showEditModal: false, clusterItem: null});
    };

    render() {
        return (
            <div>
                <Search {...this.getSearchProps()}/>
                <ClusterOpsList {...this.getListProps()}/>
                {this.state.showEditModal ? <ModifyClusterModal {...this.getModalProps()}/> : ''}
                {this.state.showExpansionModal ?
                    <ClusterDynamicExpansionModal {...this.getExpansionModalProps()}/> : ''}
                {this.state.showExpansionResultModal ?
                    <ClusterDynamicExpansionResultModal {...this.getExpansionResultModalProps()}/> : ''}
                {this.state.showRestartModal ? <ClusterRestartModal {...this.getRestartModalProps()}/> : ''}
                {this.state.showRestartResultModal ?
                    <ClusterRestartResultModal {...this.getRestartResultModalModalProps()}/> : ''}
            </div>
        )
    }
}
