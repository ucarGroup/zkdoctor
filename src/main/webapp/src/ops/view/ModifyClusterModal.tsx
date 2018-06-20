import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";
import {ClusterInfoVo} from "../../cluster/model/ClusterModel";
import {ServiceLineVo} from "../model/ServiceLineOpsModel";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    serviceLines: Array<ServiceLineVo>;
    baseInfoModel: BaseInfoModel;
    clusterItem: ClusterInfoVo;
    onOk: any;
    onCancel: any;
}

class ModifyClusterModal extends Component<ModalProps, any> {
    render() {
        let item = this.props.clusterItem ? this.props.clusterItem : new ClusterInfoVo();
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    id: item.id ? item.id : ''
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '集群信息修改',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        let clusterItem = this.props.clusterItem;

        if (!clusterItem) {
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
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='负责人：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('officer', {
                                initialValue: clusterItem.officer ? clusterItem.officer : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='实例个数：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('instanceNumber', {
                                initialValue: this.props.clusterItem['instanceNumber'],
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='部署类型：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('deployType', {
                                initialValue: clusterItem.deployType
                            })(
                                <Select style={{width: 200}}>
                                    {this.props.baseInfoModel.deployTypes.map((line) =>
                                        <Select.Option key={line.index} value={line.index}>{line.name}</Select.Option>
                                    )}
                                </Select>
                            )}
                        </Form.Item>
                        <Form.Item label='所属业务：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('serviceLine', {
                                initialValue: clusterItem.serviceLine
                            })(
                                <Select style={{width: 200}}>
                                    {this.props.serviceLines.map((line) =>
                                        <Select.Option key={line.id} value={line.id}>{line.serviceLineDesc}</Select.Option>
                                    )}
                                </Select>
                            )}
                        </Form.Item>
                        <Form.Item label='版本号：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('version', {
                                initialValue: this.props.clusterItem['version'],
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='集群描述：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('intro', {
                                initialValue: this.props.clusterItem['intro'],
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ModifyClusterModal);