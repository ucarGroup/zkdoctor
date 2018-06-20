import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import Search from "./clusterList/Search";
import {List} from "./clusterList/List";
import {ClusterModel, ClusterSearchVo} from "../model/ClusterModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import {SysUserModel} from "../../sys/model/SysUserModel";
import {ServiceLineOpsModel} from "../../ops/model/ServiceLineOpsModel";

interface DetailPropsI {
    location: any;
}

@observer
export default class ClusterList extends Component<DetailPropsI, any> {

    @inject(ClusterModel)
    private clusterModel: ClusterModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    @inject(ServiceLineOpsModel)
    private serviceLineOpsModel: ServiceLineOpsModel;

    constructor(props) {
        super(props);
    }

    componentDidMount(): void {
        this.serviceLineOpsModel.query({});
        this.clusterModel.query({});
        this.setState({})
    }

    getSearchProps() {
        let thisV = this;
        return {
            serviceLines: this.serviceLineOpsModel.serviceLines,
            baseInfoModel: this.baseInfoModel,
            onSearch(searchVo: ClusterSearchVo) {
                thisV.clusterModel.query(searchVo);
            }
        };
    };

    getListProps() {
        let thisV = this;
        return {
            dataSource: this.clusterModel.clusters,
            loading: this.clusterModel.loading,
            pageConfig: this.clusterModel.pageConfig,
            sysUser: this.sysUserModel.sysUser,
            baseInfoModel: this.baseInfoModel,
            switchChange(item, value) {
                thisV.clusterModel.updateMonitorStatus(item.clusterId, value);
            }
        };
    };

    render() {
        return (
            <div>
                <Search {...this.getSearchProps()}/>
                <List {...this.getListProps()}/>
            </div>
        )
    }
}
