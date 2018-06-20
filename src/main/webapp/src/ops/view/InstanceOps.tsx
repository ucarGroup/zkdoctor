import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {InstanceConfigOpsVo, InstanceListVo, InstanceModel} from "../../instance/model/InstanceModel";
import {Link, withRouter} from "react-router";
import {ClusterModel} from "../../cluster/model/ClusterModel";
import HeadInfo from "../../cluster/view/clusterDetail/HeadInfo";
import {InstanceOpsList} from "./InstanceOpsList";
import {InstanceOpsModel} from "../model/InstanceOpsModel";
import InstanceConfigOpsModal from "./InstanceConfigOps";
import NewInstanceForClusterModal from "./NewInstanceForClusterModal";
import NewConfigFileModal from "./NewConfigFileModal";
import InstanceUpdateServerModal from "./InstanceUpdateServerModal";
import {Button, notification, Tabs} from "antd";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

interface DetailState {
    clusterInfoId: number;
    instanceListVo: InstanceListVo;
    zooConfFileContent: string;
    showConfigEditModal: boolean;
    showNewInstanceModal: boolean;
    showNewConfigFileModal: boolean;
    showUpdateServerModal: boolean;
    updatedJarFile: Array<any>;
}

interface DetailPropsI {
    location: any;
    router: any;
    onAddNewInstance: any;
}

@observer
export default class InstanceOps extends Component<DetailPropsI, DetailState> {

    @inject(InstanceModel)
    private instanceModel: InstanceModel;

    @inject(InstanceOpsModel)
    private instanceOpsModel: InstanceOpsModel;

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    constructor(props) {
        super(props);
        this.state = {
            clusterInfoId: null,
            instanceListVo: null,
            zooConfFileContent: null,
            showConfigEditModal: false,
            showNewInstanceModal: false,
            showNewConfigFileModal: false,
            showUpdateServerModal: false,
            updatedJarFile: []
        }
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        this.loadClusterInstances(strArray[4]);
        this.loadClusterInfo(strArray[4]);
        this.setState({
            clusterInfoId: strArray[4],
        });
    }

    loadClusterInstances(clusterInfoId: number) {
        this.instanceModel.queryAllData(clusterInfoId);
    }

    loadClusterInfo(clusterInfoId: number) {
        this.clusterModel.getClusterInfo(clusterInfoId);
    }

    getHeadInfoProps() {
        return {
            clusterName: this.clusterModel.clusterInfo ? this.clusterModel.clusterInfo.clusterName : '',
        }
    }

    getListProps() {
        let thisV = this;
        return {
            dataSource: this.getDataSource(),
            loading: this.instanceModel.loading || this.instanceOpsModel.loading,
            baseInfoModel: this.baseInfoModel,
            onOffLineInstance(instanceId) {
                thisV.instanceOpsModel.offLineInstance(instanceId, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.loadClusterInstances(thisV.state.clusterInfoId);
            },
            onRemoveInstance(instanceId) {
                thisV.instanceOpsModel.removeInstance(instanceId, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                    thisV.instanceModel.queryAllData(thisV.state.clusterInfoId);
                });
                thisV.loadClusterInstances(thisV.state.clusterInfoId);
            },
            onRestartInstance(instanceId) {
                thisV.instanceOpsModel.restartInstance(instanceId, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
            },
            onConfigOps(instanceListVo: InstanceListVo) {
                // 查询配置文件内容
                thisV.instanceOpsModel.queryInstanceConfig(instanceListVo.host,
                    (zooConfFileContent) => {
                        thisV.showModal(instanceListVo, zooConfFileContent);
                    });
            },
            onAddNewConfigFile(instanceListVo: InstanceListVo) {
                thisV.setState({instanceListVo: instanceListVo, showNewConfigFileModal: true});
            },
            onUpdateServer(instanceListVo: InstanceListVo) {
                thisV.instanceOpsModel.queryUploadedJarFile((updatedJarFile) => {
                    thisV.setState({
                        instanceListVo: instanceListVo,
                        showUpdateServerModal: true,
                        updatedJarFile: updatedJarFile
                    });
                });

            }
        };
    };

    getModalProps() {
        let thisV = this;
        return {
            instanceListVo: this.state.instanceListVo,
            zooConfFileContent: this.state.zooConfFileContent,
            onOk: function (item: InstanceConfigOpsVo) {
                thisV.instanceOpsModel.instanceConfOps(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.hideModal();
            },
            onCancel() {
                thisV.hideModal();
            },
        };
    };

    getNewInstanceModalProps() {
        let thisV = this;
        return {
            clusterId: this.state.clusterInfoId,
            onOk: function (item) {
                thisV.instanceOpsModel.addNewInstance(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                    thisV.instanceModel.queryAllData(thisV.state.clusterInfoId);
                });
                thisV.setState({showNewInstanceModal: false});
            },
            onCancel() {
                thisV.setState({showNewInstanceModal: false});
            },
        };
    }

    getNewConfigFileModalProps() {
        let thisV = this;
        return {
            host: this.state.instanceListVo.host,
            onOk: function (item) {
                thisV.instanceOpsModel.addNewConfigFile(item, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.setState({showNewConfigFileModal: false});
            },
            onCancel() {
                thisV.setState({showNewConfigFileModal: false});
            },
        };
    }

    getUpdateServerModalProps() {
        let thisV = this;
        return {
            instanceListVo: thisV.state.instanceListVo,
            updatedFileList: thisV.state.updatedJarFile,
            onOk: function (instanceId) {
                thisV.instanceOpsModel.instanceUpdateServer(instanceId, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    })
                });
                thisV.setState({showUpdateServerModal: false});
            },
            onCancel() {
                thisV.setState({showUpdateServerModal: false});
            },
        };
    }


    hideModal(): void {
        this.setState({showConfigEditModal: false, instanceListVo: null});
    };

    showModal(instanceListVo: InstanceListVo, zooConfFileContent: string): void {
        this.setState({
            showConfigEditModal: true,
            instanceListVo: instanceListVo,
            zooConfFileContent: zooConfFileContent
        });
    };

    showNewInstanceModal() {
        this.setState({
            showNewInstanceModal: true
        });
    }

    getDataSource() {
        let instanceListVos = [];
        if (this.instanceModel.instances == null) {
            return instanceListVos;
        }
        let instances = this.instanceModel.instances.slice();
        for (let index in instances) {
            let instance = instances[index];
            let listvo: InstanceListVo = new InstanceListVo();
            listvo.clusterName = this.clusterModel.clusterInfo ? this.clusterModel.clusterInfo.clusterName : '';
            if (instance.instanceInfo != null) {
                listvo.instanceId = instance.instanceInfo.id;
                listvo.host = instance.instanceInfo.host;
                listvo.port = instance.instanceInfo.port;
                listvo.status = instance.instanceInfo.status;
            }
            if (instance.instanceState != null) {
                listvo.currConnections = instance.instanceState.currConnections;
                listvo.received = instance.instanceState.received;
                listvo.serverStateLag = instance.instanceState.serverStateLag;
            }
            instanceListVos.push(listvo);
        }
        return instanceListVos;
    }

    render() {
        const goListPage = () => {
            this.props.router.goBack();
        };
        return (
            <div>
                <HeadInfo {...this.getHeadInfoProps()} />
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      tabBarExtraContent={<Button onClick={goListPage}>返回列表</Button>}>
                    <Tabs.TabPane tab="实例运维" key="1">
                        <Button icon="plus" type="primary" style={{marginBottom: 10}}
                                onClick={() => this.showNewInstanceModal()}>新增实例</Button>
                        <br />
                        <InstanceOpsList {...this.getListProps()}/>
                        {this.state.showConfigEditModal ? <InstanceConfigOpsModal {...this.getModalProps()}/> : ''}
                        {this.state.showNewInstanceModal ?
                            <NewInstanceForClusterModal {...this.getNewInstanceModalProps()}/> : ''}
                        {this.state.showNewConfigFileModal ?
                            <NewConfigFileModal {...this.getNewConfigFileModalProps()}/> : ''}
                        {this.state.showUpdateServerModal ?
                            <InstanceUpdateServerModal {...this.getUpdateServerModalProps()}/> : ''}

                    </Tabs.TabPane>
                </Tabs>

            </div>
        );
    }
}