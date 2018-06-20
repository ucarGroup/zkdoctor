import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {withRouter} from "react-router";
import {ClusterMonitorAlarmInfoModel} from "../model/ClusterMonitorAlarmInfoModel";
import {IndexCollectList} from "./IndexCollectList";

@observer
export default class ClusterMonitorAlarmInfoList extends Component<{}, {}> {

    @inject(ClusterMonitorAlarmInfoModel)
    private clusterMonitorAlarmInfoModel: ClusterMonitorAlarmInfoModel;

    constructor(props) {
        super(props);
    }

    componentDidMount(): void {
        this.clusterMonitorAlarmInfoModel.query(null);
        this.clusterMonitorAlarmInfoModel.queryAllCollectInfo();
        this.setState({})
    }

    getIndexCollectInfoListProps() {
        let thisV = this;
        return {
            dataSource: thisV.clusterMonitorAlarmInfoModel.indexCollects,
        };
    };

    getAlarmInfoListProps() {
        let thisV = this;
        return {
            dataSource: thisV.clusterMonitorAlarmInfoModel.clusterMonitorAlarmInfos,
            loading: thisV.clusterMonitorAlarmInfoModel.loading,
            pageConfig: thisV.clusterMonitorAlarmInfoModel.pageConfig
        };
    };

    render() {
        return (
            <div>
                <IndexCollectList {...this.getIndexCollectInfoListProps()}/>
            </div>
        );
    }
}
