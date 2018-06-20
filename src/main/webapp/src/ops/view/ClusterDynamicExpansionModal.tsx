import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {ServiceLine} from "../../sys/model/BaseInfoModel";
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
    serviceLines: Array<ServiceLine>;
    clusterItem: ClusterInfoVo;
    onOk: any;
    onCancel: any;
}

class ClusterDynamicExpansionModal extends Component<ModalProps, any> {
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
            title: '动态扩容',
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
                                <Input style={{width: 300}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='新服务器id：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('serverId', {
                                initialValue: '',
                                rules: [
                                    {
                                        required: true,
                                        message: 'server id为正整数，请正确填写',
                                        pattern: /^[1-9]\d*$/
                                    }
                                ],
                            })(
                                <Input style={{width: 300}} placeholder='server id'/>
                            )}
                        </Form.Item>
                        <Form.Item label='ip：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('host', {
                                initialValue: '',
                                rules: [
                                    {
                                        required: true,
                                        message: '请正确填写扩容ip信息',
                                        pattern: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                                    }
                                ],
                            })(
                                <Input style={{width: 300}} placeholder='新服务器ip'/>
                            )}
                        </Form.Item>
                        <Form.Item label='域名：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('domain', {})(
                                <Input style={{width: 300}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='客户端端口：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('clientPort', {
                                initialValue: '2181',
                                rules: [
                                    {
                                        required: true,
                                        message: '客户端port为正整数，请正确填写',
                                        pattern: /^[1-9]\d*$/
                                    }
                                ],
                            })(
                                <Input style={{width: 300}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='法人端口：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('quorumPort', {
                                initialValue: '2888',
                                rules: [
                                    {
                                        required: true,
                                        message: '法人端口为正整数，请正确填写',
                                        pattern: /^[1-9]\d*$/
                                    }
                                ],
                            })(
                                <Input style={{width: 300}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='选举端口：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('electionPort', {
                                initialValue: '3888',
                                rules: [
                                    {
                                        required: true,
                                        message: '选举端口为正整数，请正确填写',
                                        pattern: /^[1-9]\d*$/
                                    }
                                ],
                            })(
                                <Input style={{width: 300}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='新服务器类型：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('peerType', {
                                initialValue: 'participant'
                            })(
                                <Select style={{width: 300}}>
                                    <Select.Option value="participant">participant</Select.Option>
                                    <Select.Option value="observer">observer</Select.Option>
                                </Select>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ClusterDynamicExpansionModal);