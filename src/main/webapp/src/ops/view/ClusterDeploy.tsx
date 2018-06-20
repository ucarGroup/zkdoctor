import React, {Component} from "react";
import {observer} from "mobx-react";
import {withRouter} from "react-router";
import {inject} from "../../common/utils/IOC";
import {ClusterOpsModel} from "../model/ClusterOpsModel";
import {FormComponentProps} from "antd/lib/form/Form";
import ClusterDeploy from "./ClusterDeployForm";
import ClusterRestartResultModal from "./ClusterRestartResultModal";
import {ServiceLineOpsModel} from "../model/ServiceLineOpsModel";
import {ClusterModel} from "../../cluster/model/ClusterModel";

export interface SubmitPropsI extends FormComponentProps {
    location: any;
    router: any
}

export interface SubmitState {
    showSubmitResult: boolean;
    isDeployOver: boolean;
}

@observer
class AuditDetail extends Component<SubmitPropsI, SubmitState> {

    @inject(ClusterOpsModel)
    private clusterOpsModel: ClusterOpsModel;

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
        this.state = {
            showSubmitResult: false,
            isDeployOver: false
        }
    }

    componentDidMount() {
        this.serviceLineOpsModel.query({});
        this.setState({});
    }

    getDeployResultModalProps() {
        let thisV = this;
        return {
            isOver: thisV.state.isDeployOver,
            title: "部署中",
            resultList: thisV.clusterOpsModel.deployResultList ? thisV.clusterOpsModel.deployResultList : [],
            onOk: function () {
                thisV.setState({
                    showSubmitResult: false,
                    isDeployOver: false
                })
            },
            onCancel() {
                thisV.setState({
                    showSubmitResult: false,
                    isDeployOver: false
                })
            },
        };
    };

    private timer;

    render() {
        const getClusterDeployFormProps = () => {
            let thisV = this;
            return {
                serviceLines: thisV.serviceLineOpsModel.serviceLines,
                clusterDeploySubmit(values){ // 执行部署
                    thisV.clusterOpsModel.clusterDeploySubmit(values, (message) => {
                        // 最后刷新一次，获取最后结果
                        thisV.clusterOpsModel.getClusterDeployResult();
                        thisV.setState({
                            showSubmitResult: thisV.clusterOpsModel.lastDeployResult || thisV.clusterOpsModel.deployResult,
                            isDeployOver: true
                        });
                        // 停止刷新
                        clearInterval(thisV.timer);
                    });
                    // 每1s执行一次，获取执行结果
                    thisV.timer = setInterval(function () {
                        thisV.clusterOpsModel.getClusterDeployResult((isClear) => {
                            if (isClear) {
                                clearInterval(thisV.timer);
                                thisV.setState({
                                    isDeployOver: true
                                })
                            }
                        });
                        // 处理结果为异常的时候，不显示结果框
                        thisV.setState({
                            showSubmitResult: thisV.clusterOpsModel.lastDeployResult || thisV.clusterOpsModel.deployResult
                        })
                    }, 1000);
                },
                clusterNameExist: thisV.clusterOpsModel.clusterNameExist,
                isExisted(clusterName, callback) {
                    thisV.clusterModel.checkClusterNameExist(clusterName, callback);
                },
            }
        };

        return (
            <div>
                <ClusterDeploy {...getClusterDeployFormProps()}/>
                {this.state.showSubmitResult ? <ClusterRestartResultModal {...this.getDeployResultModalProps()}/> : ''}
            </div>
        );
    }
}

export default withRouter(AuditDetail);
