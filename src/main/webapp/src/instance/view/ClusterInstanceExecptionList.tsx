import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {InstanceListVo, InstanceModel} from "../model/InstanceModel";
import {Link, withRouter} from "react-router";
import {InstanceExceptionList} from "./InstanceExceptionList";
import {Tabs} from "antd";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

interface DetailState {
    status: number;
}

interface DetailPropsI {
    router: any;
    location: any;
}

@observer
export default class ClusterInstanceList extends Component<DetailPropsI, DetailState> {

    @inject(InstanceModel)
    private instanceModel: InstanceModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    constructor(props) {
        super(props);
        this.state = {
            status: null
        }
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        this.loadClusterInstances(strArray[3]);
        this.setState({
            status: strArray[3]
        });
    }

    loadClusterInstances(status: number) {
        this.instanceModel.queryAllExceptionData(status);
    }

    getListProps() {
        return {
            dataSource: this.getDataSource(),
            loading: this.instanceModel.loading,
            baseInfoModel: this.baseInfoModel,
        };
    };

    getDataSource() {
        let instanceListVos = [];
        if (this.instanceModel.instances == null) {
            return instanceListVos;
        }
        let instances = this.instanceModel.instances.slice();
        for (let index in instances) {
            let instance = instances[index];
            let listvo: InstanceListVo = new InstanceListVo();
            if (instance.instanceInfo != null) {
                listvo.instanceId = instance.instanceInfo.id;
                listvo.clusterName = instance.instanceInfo.clusterName;
                listvo.host = instance.instanceInfo.host;
                listvo.port = instance.instanceInfo.port;
                listvo.status = instance.instanceInfo.status;
                listvo.clusterId = instance.instanceInfo.clusterId;
            }
            if (instance.instanceState != null) {
                listvo.currConnections = instance.instanceState.currConnections;
                listvo.received = instance.instanceState.received;
                listvo.serverStateLag = instance.instanceState.serverStateLag;
                listvo.clusterId = instance.instanceInfo.clusterId;
            }
            instanceListVos.push(listvo);
        }
        return instanceListVos;
    }

    render() {
        return (
            <div>
                <Tabs defaultActiveKey="1" tabPosition="top" type="card">
                    <Tabs.TabPane tab="实例异常信息" key="1">
                        <InstanceExceptionList {...this.getListProps()}/>
                    </Tabs.TabPane>
                </Tabs>
            </div>
        );
    }
}
