import React, {Component} from "react";
import {observer} from "mobx-react";
import {notification} from "antd";
import {inject} from "../../common/utils/IOC";
import {SystemConfigList, SystemCongigList} from "./SystemConfigList";
import {SystemConfigVo, SystemOpsModel} from "../model/SystemOpsModel";
import SystemConfigUpdateModal from "./SystemConfigUpdateModal";

interface DetailState {
    showUpdateModal: boolean;
    sysConfigItem: SystemConfigVo;
}

interface DetailPropsI {
    location: any;
}

@observer
export default class SystemConfig extends Component<DetailPropsI, DetailState> {

    @inject(SystemOpsModel)
    private systemOpsModel: SystemOpsModel;

    constructor(props) {
        super(props);
        this.state = {
            showUpdateModal: false,
            sysConfigItem: null
        }
    }

    componentDidMount(): void {
        this.systemOpsModel.query({});
        this.setState({})
    }

    showUpdateModal(sysConfig): void {
        this.setState({
            sysConfigItem: sysConfig,
            showUpdateModal: true
        });
    };

    hideUpdateModal(): void {
        this.setState({
            sysConfigItem: null,
            showUpdateModal: false
        });
    };

    getSystemConfigListProps() {
        let thisV = this;
        return {
            dataSource: this.systemOpsModel.sysConfigs,
            loading: this.systemOpsModel.loading,
            pageConfig: this.systemOpsModel.pageConfig,
            onUpdateConfig(sysConfig) {
                thisV.showUpdateModal(sysConfig);
            },
            onDeleteConfigValue(configName) {
                thisV.systemOpsModel.deleteConfig(configName, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
            }
        };
    };

    getUpdateModalProps() {
        let thisV = this;
        return {
            systemConfig: thisV.state.sysConfigItem,
            onOk: function (sysConfig: SystemConfigVo) {
                thisV.systemOpsModel.updateConfig(sysConfig, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.hideUpdateModal();
            },
            onCancel() {
                thisV.hideUpdateModal();
            },
        };
    };

    render() {
        return (
            <div>
                <SystemConfigList {...this.getSystemConfigListProps()}/>
                {this.state.showUpdateModal ? <SystemConfigUpdateModal {...this.getUpdateModalProps()}/> : ''}
            </div>
        )
    }
}
