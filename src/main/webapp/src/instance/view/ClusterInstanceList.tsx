import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {InstanceListVo, InstanceModel} from "../model/InstanceModel";
import {Link, withRouter} from "react-router";
import HeadInfo from "../../cluster/view/clusterDetail/HeadInfo";
import {InstanceList} from "./InstanceList";
import {Button, Tabs} from "antd";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

interface DetailState {
    clusterInfoId: number;
    clusterName: string;
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
            clusterInfoId: null,
            clusterName: null
        }
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        this.loadClusterInstances(strArray[4]);
        this.setState({
            clusterInfoId: strArray[4],
            clusterName: strArray[3]
        });
    }

    loadClusterInstances(clusterInfoId: number) {
        this.instanceModel.queryAllData(clusterInfoId);
    }

    getHeadInfoProps() {
        return {
            clusterName: this.state.clusterName,
        }
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
            listvo.clusterName = this.state.clusterName;
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
        return (
            <div>
                <HeadInfo {...this.getHeadInfoProps()} />
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      tabBarExtraContent={<Link to={'/cluster/list'}><Button>返回集群列表</Button></Link>}>
                    <Tabs.TabPane tab="实例拓扑" key="1">
                        <InstanceList {...this.getListProps()}/>
                    </Tabs.TabPane>
                </Tabs>
            </div>
        );
    }
}
