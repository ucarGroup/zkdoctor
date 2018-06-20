import React, {Component} from "react";
import {observer} from "mobx-react";
import {withRouter} from "react-router";
import {inject} from "../../../common/utils/IOC";
import {ClusterModel} from "../../model/ClusterModel";
import {FormComponentProps} from "antd/lib/form/Form";
import NewClusterForm from "./NewClusterForm";
import {BaseInfoModel} from "../../../sys/model/BaseInfoModel";
import {ServiceLineOpsModel} from "../../../ops/model/ServiceLineOpsModel";

/**
 * 新建集群
 */
export interface NewClusterPropsI extends FormComponentProps {
    router: any
}

@observer
class NewCluster extends Component<NewClusterPropsI, any> {

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
        this.state = ({})
    }

    componentDidMount(): void {
        this.serviceLineOpsModel.query({});
        this.setState({})
    }

    render() {
        const goListPage = () => {
            this.props.router.push("/cluster/list")
        };
        const getNewClusterFormProps = () => {
            let thisV = this;
            return {
                serviceLines: thisV.serviceLineOpsModel.serviceLines ? thisV.serviceLineOpsModel.serviceLines : [],
                baseInfoModel: thisV.baseInfoModel,
                addClusterSubmit(values){
                    thisV.clusterModel.addClusterSubmit(values)
                },
                addClusterSubmitResult: thisV.clusterModel.addClusterSubmitResult,
                goListPage: goListPage,
                clusterNameExist: thisV.clusterModel.clusterNameExist,
                resetAddClusterSubmitResult(){
                    thisV.clusterModel.resetAddClusterSubmitResult()
                },
                isExisted(clusterName, callback) {
                    thisV.clusterModel.checkClusterNameExist(clusterName, callback);
                },
            }
        };

        return (
            <div>
                <NewClusterForm {...getNewClusterFormProps()}/>
            </div>
        );
    }
}

export default withRouter(NewCluster);
