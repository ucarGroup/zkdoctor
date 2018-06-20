import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {ClusterInfoVo} from "../../cluster/model/ClusterModel";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    clusterItem: ClusterInfoVo;
    onOk: any;
    onCancel: any;
}

class ClusterRestartModal extends Component<ModalProps, any> {
    render() {
        let clusterItem = this.props.clusterItem ? this.props.clusterItem : new ClusterInfoVo();
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    clusterId: clusterItem.id ? clusterItem.id : ''
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '重启集群',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };
        if (!clusterItem.clusterName) {
            return null;
        }
        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='集群名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('clusterName', {
                                initialValue: clusterItem['clusterName'] ? clusterItem['clusterName'] : ''
                            })(
                                <Input style={{width: 250}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='验证数据同步间隔(ms)：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('sleepTimeMs', {
                                initialValue: '300',
                                rules: [
                                    {
                                        required: true,
                                        message: '验证数据同步间隔未填写'
                                    }
                                ],
                            })(
                                <Input style={{width: 250}}/>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ClusterRestartModal);