import React, {Component} from "react";
import {observer} from "mobx-react";
import {Button, notification} from "antd";
import {inject} from "../../common/utils/IOC";
import ServiceLineUpdateModal from "./ServiceLineUpdateModal";
import {ServiceLineOpsModel, ServiceLineVo} from "../model/ServiceLineOpsModel";
import {ServiceLineList} from "./ServiceLineList";

interface DetailState {
    showServiceLineModal: boolean;
    serviceLineItem: ServiceLineVo;
    modalType: string;
}

interface DetailPropsI {
    location: any;
}

@observer
export default class ServiceLine extends Component<DetailPropsI, DetailState> {

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
        this.state = {
            showServiceLineModal: false,
            serviceLineItem: null,
            modalType: 'create'
        }
    }

    componentDidMount(): void {
        this.serviceLineOpsModel.query({});
        this.setState({})
    }

    showUpdateModal(serviceLine): void {
        this.setState({
            serviceLineItem: serviceLine,
            showServiceLineModal: true,
            modalType: 'edit'
        });
    };

    hideUpdateModal(): void {
        this.setState({
            serviceLineItem: null,
            showServiceLineModal: false
        });
    };

    showCreateServiceLineModal(): void {
        this.setState({
            serviceLineItem: null,
            showServiceLineModal: true,
            modalType: 'create'
        });
    };

    getServiceLineListProps() {
        let thisV = this;
        return {
            dataSource: this.serviceLineOpsModel.serviceLines ? this.serviceLineOpsModel.serviceLines : [],
            loading: this.serviceLineOpsModel.loading,
            pageConfig: this.serviceLineOpsModel.pageConfig,
            onUpdateServiceLine(serviceLine) {
                thisV.showUpdateModal(serviceLine);
            },
            onDeleteServiceLine(serviceLineName) {
                thisV.serviceLineOpsModel.deleteServiceLine(serviceLineName, (message) => {
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
            serviceLine: thisV.state.serviceLineItem,
            onOk: function (serviceLine: ServiceLine) {
                if (thisV.state.modalType == 'create') {
                    thisV.serviceLineOpsModel.insertServiceLine(serviceLine, (message) => {
                        notification['success']({
                            message: message,
                            description: '',
                        });
                    });
                } else {
                    thisV.serviceLineOpsModel.updateServiceLine(serviceLine, (message) => {
                        notification['success']({
                            message: message,
                            description: '',
                        });
                    });
                }
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
                <Button icon="plus" type="primary" style={{marginRight: 10, marginBottom: 10}}
                        onClick={() => this.showCreateServiceLineModal()}>新增业务线</Button>
                <ServiceLineList {...this.getServiceLineListProps()}/>
                {this.state.showServiceLineModal ? <ServiceLineUpdateModal {...this.getUpdateModalProps()}/> : ''}
            </div>
        )
    }
}
