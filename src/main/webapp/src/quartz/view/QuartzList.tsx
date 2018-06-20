import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {SysUserModel} from "../../sys/model/SysUserModel";
import {QuartzModel} from "../model/QuartzModel";
import {List} from "./List";
import Search from "./Search";

interface DetailPropsI {
    location: any;
}

@observer
export default class QuartzList extends Component<DetailPropsI, any> {

    @inject(QuartzModel)
    private quartzModel: QuartzModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    constructor(props) {
        super(props);
    }

    componentDidMount(): void {
        this.quartzModel.query({});
        this.setState({
        })
    }

    getSearchProps() {
        let thisV = this;
        return {
            onSearch(query) {
                thisV.quartzModel.query(query);
            }
        };
    };

    getListProps() {
        let thisV = this;
        return {
            dataSource: this.quartzModel.triggers,
            loading: this.quartzModel.loading,
            pageConfig: this.quartzModel.pageConfig,
            sysUser: this.sysUserModel.sysUser,
            onDelete(triggerName, triggerGroup, callback) {
                thisV.quartzModel.removeTrigger(triggerName, triggerGroup, callback);
            },
            onResume(triggerName, triggerGroup, callback) {
                thisV.quartzModel.resumeTrigger(triggerName, triggerGroup, callback);
            },
            onPause(triggerName, triggerGroup, callback) {
                thisV.quartzModel.pauseTrigger(triggerName, triggerGroup, callback);
            },
        };
    };

    render() {
        return (
            <div>
                <div>
                    <Search {...this.getSearchProps()}/>
                </div>
                <List {...this.getListProps()}/>
            </div>
        )
    }
}