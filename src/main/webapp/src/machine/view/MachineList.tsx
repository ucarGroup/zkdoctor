import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import Search from "./Search";
import {List} from "./List";
import {Button, notification} from "antd";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import ModifyMachineModal from "./ModifyMachineModal";
import {MachineInfoVo, MachineModel, MachineSearchVo} from "../model/MachineModel";
import {SysUserModel} from "../../sys/model/SysUserModel";
import {ServiceLineOpsModel} from "../../ops/model/ServiceLineOpsModel";

/**
 * 机器列表信息
 */
interface DetailState {
    showModal: boolean;
    type: string;
    machineInfoItem: MachineInfoVo;
}

interface DetailPropsI {
    location: any;
}

@observer
export default class MachineList extends Component<DetailPropsI, DetailState> {

    @inject(MachineModel)
    private machineModel: MachineModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            machineInfoItem: null,
            type: 'create'
        }
    }

    componentDidMount(): void {
        this.serviceLineOpsModel.query({});
        this.machineModel.query({});
        this.setState({})
    }

    download() {
        this.machineModel.downloadMachineInitScript();
    }

    showModal(machineInfoItem: MachineInfoVo, type: string): void {
        this.setState({
            showModal: true,
            machineInfoItem: machineInfoItem,
            type: type
        });
    };

    hideModal(): void {
        this.setState({
            showModal: false,
            machineInfoItem: null
        });
    };

    getSearchProps() {
        let thisV = this;
        return {
            serviceLines: this.serviceLineOpsModel.serviceLines,
            onSearch(searchVo: MachineSearchVo) {
                thisV.machineModel.query(searchVo);
            }
        };
    };

    getListProps() {
        let thisV = this;
        return {
            dataSource: this.machineModel.machines,
            loading: this.machineModel.loading,
            pageConfig: this.machineModel.pageConfig,
            sysUser: this.sysUserModel.sysUser,
            baseInfoModel: this.baseInfoModel,
            onEdit(item) {
                thisV.showModal(item, 'edit');
            },
            onDelete(machineId) {
                thisV.machineModel.deleteMachine(machineId, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
            },
            switchChange(item, value) {
                thisV.machineModel.updateMonitorStatus(item.machineInfo.id, value);
            }
        };
    };

    getModalProps() {
        let thisV = this;
        return {
            serviceLines: this.serviceLineOpsModel.serviceLines,
            machineInfoItem: this.state.machineInfoItem,
            onOk: function (item: MachineInfoVo) {
                if (thisV.state.type == 'edit') {
                    thisV.machineModel.editMachineInfo(item, (message) => {
                        notification['success']({
                            message: message,
                            description: '',
                        });
                    });
                    thisV.setState({machineInfoItem: null});
                } else {
                    thisV.machineModel.addMachine(item, (message) => {
                        notification['success']({
                            message: message,
                            description: '',
                        });
                    });
                }
                thisV.hideModal();
            },
            onCancel() {
                thisV.hideModal();
            },
        };
    };

    render() {
        // 用户权限控制
        let userRole = this.sysUserModel.sysUser ? this.sysUserModel.sysUser.userRole : 0;
        let showOperate = false;
        if (userRole == 1 || userRole == 2) {
            showOperate = true;
        }
        return (
            <div>
                <div>
                    <div>
                        <Search {...this.getSearchProps()}/>
                    </div>
                    {showOperate ?
                    <div>
                        <Button icon="plus" type="primary" style={{marginRight: 10, marginBottom: 10}}
                                onClick={() => this.showModal(null, 'create')}>新增机器</Button>
                        <Button icon="download" type="primary" style={{marginBottom: 10}}
                                onClick={() => this.download()}>
                            服务器初始化脚本
                        </Button>
                    </div> : ''}
                </div>
                <List {...this.getListProps()}/>
                {this.state.showModal ? <ModifyMachineModal {...this.getModalProps()}/> : ''}
            </div>
        )
    }
}
