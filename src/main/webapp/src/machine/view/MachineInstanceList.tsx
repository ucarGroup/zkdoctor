import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {Link, withRouter} from "react-router";
import {Button, Tabs} from "antd";
import {MachineModel} from "../model/MachineModel";
import {MachineInstanceTable} from "./MachineInstanceTable";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

interface DetailState {
    machineId: number;
}

interface DetailPropsI {
    router: any;
    location: any;
}

@observer
export default class MachineInstanceList extends Component<DetailPropsI, DetailState> {

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    @inject(MachineModel)
    private machineModel: MachineModel;

    constructor(props) {
        super(props);
        this.state = {machineId: null};
    }

    componentDidMount(): void {
        let pathname = this.props.location.pathname;
        let strArray = pathname.split('/');
        this.loadMachineInstances(strArray[3]);
        this.setState({
            machineId: strArray[3]
        });
    }

    loadMachineInstances(machineId: number) {
        this.machineModel.queryMachineInstances(machineId);
    }

    getListProps() {
        return {
            dataSource: this.machineModel.machineInstances ? this.machineModel.machineInstances : [],
            loading: this.machineModel.loading,
            pageConfig: this.machineModel.pageConfig,
            baseInfoModel: this.baseInfoModel,
        };
    };

    render() {
        const goListPage = () => {
            this.props.router.goBack();
        };
        return (
            <div>
                <Tabs defaultActiveKey="1" tabPosition="top" type="card"
                      tabBarExtraContent={<Button onClick={goListPage}>返回列表</Button>}>
                    <Tabs.TabPane tab="实例列表" key="1">
                        <MachineInstanceTable {...this.getListProps()}/>
                    </Tabs.TabPane>
                </Tabs>
            </div>
        );
    }
}
