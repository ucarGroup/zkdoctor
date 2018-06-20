import React, {Component} from "react";
import {Form, Modal, Timeline} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";

export interface ModalProps extends FormComponentProps {
    isExpansionOver: boolean,
    resultList: Array<any>;
    onOk: any;
    onCancel: any;
}

class ClusterDynamicExpansionResultModal extends Component<ModalProps, any> {

    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.onOk();
        };

        const modalOpts = {
            title: '动态扩容中',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        let getResult = () => {
            const results = [];
            for (let i = 0; i < this.props.resultList.length; i++) {
                let line = this.props.resultList[i];
                let color = line.indexOf("失败") == -1 ? "green" : "red";
                results.push(
                    <Timeline.Item key={i} color={color}>{line}</Timeline.Item>
                );
            }
            return results;
        };

        return (
            <div>
                <Modal {...modalOpts}>
                    <Timeline pending={!this.props.isExpansionOver}>
                        {getResult()}
                    </Timeline>
                </Modal>
            </div>);
    }
}

export default Form.create()(ClusterDynamicExpansionResultModal);