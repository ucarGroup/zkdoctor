import React, {Component} from "react";
import {Link} from "react-router";

export interface InstanceHeadInfoProps {
    clusterName: string;
    hostInfo: string;
}

export default class InstanceHeadInfo extends Component<InstanceHeadInfoProps, {}> {

    constructor(props) {
        super(props)
    }

    render() {
        return(
            <div style={{fontSize: 15, fontWeight: 'bold', marginBottom: 10, marginLeft: 10}} >
                <span style={{color: '#66CCFF'}} >实例【{this.props.hostInfo}{this.props.clusterName == '' ? '' : '-' + this.props.clusterName}】</span>
            </div>
        )
    }
}