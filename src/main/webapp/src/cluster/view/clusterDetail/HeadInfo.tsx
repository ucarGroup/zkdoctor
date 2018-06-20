import React, {Component} from "react";
import {Link} from "react-router";

export interface HeadInfoProps {
    clusterName: string;
}

export default class HeadInfo extends Component<HeadInfoProps, {}> {

    constructor(props) {
        super(props)
    }
    
    render() {
        return( 
            <div style={{fontSize: 15, fontWeight: 'bold', marginBottom: 10, marginLeft: 10}} >
                <span style={{color: '#66CCFF'}} >集群【{this.props.clusterName}】</span>
            </div>
        )
    }
}