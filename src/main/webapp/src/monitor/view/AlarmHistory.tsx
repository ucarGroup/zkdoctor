import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {withRouter} from "react-router";
import {Tabs} from "antd";
import {IndexCollectList} from "./IndexCollectList";
import {AlarmInfoList} from "../../collect/view/AlarmInfoList";
import {ClusterMonitorAlarmInfoModel} from "../../collect/model/ClusterMonitorAlarmInfoModel";

@observer
export default class ClusterMonitorAlarmInfoList extends Component<{}, {}> {

    @inject(ClusterMonitorAlarmInfoModel)
    private clusterMonitorAlarmInfoModel: ClusterMonitorAlarmInfoModel;

    constructor(props) {
        super(props);
    }

    componentDidMount(): void {
        this.clusterMonitorAlarmInfoModel.query(null);
        this.setState({})
    }


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
                <Tabs defaultActiveKey="1" tabPosition="top" type="card">
                    <Tabs.TabPane tab="当天报警信息列表" key="1">
                        <AlarmInfoList {...this.getAlarmInfoListProps()}/>
                    </Tabs.TabPane>
                </Tabs>
            </div>
        );
    }
}
