import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {MachineInfoVo} from "../model/MachineModel";
import {ServiceLineVo} from "../../ops/model/ServiceLineOpsModel";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    serviceLines: Array<ServiceLineVo>,
    machineInfoItem: MachineInfoVo;
    onOk: any;
    onCancel: any;
}

class ModifyMachineModal extends Component<ModalProps, any> {
    render() {
        let item = this.props.machineInfoItem? this.props.machineInfoItem: new MachineInfoVo();
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
            title: '机器信息修改',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        let machineInfoItem = this.props.machineInfoItem ? this.props.machineInfoItem : new MachineInfoVo();
        let isNew = this.props.machineInfoItem ? true : false;
        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='机器ip：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('host', {
                                initialValue: machineInfoItem.host ? machineInfoItem.host : '',
                                rules: [
                                    {
                                        required: true,
                                        message: '请正确填写机器ip',
                                        pattern: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                                    }]
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={isNew}/>
                            )}
                        </Form.Item>
                        <Form.Item label='内存：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('memory', {
                                initialValue: machineInfoItem.memory ? machineInfoItem.memory : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='cpu：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('cpu', {
                                initialValue: machineInfoItem.cpu ? machineInfoItem.cpu : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='是否虚机：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('virtual', {
                                initialValue: (machineInfoItem.virtual != null && machineInfoItem.virtual) ? '1' : '0'
                            })(
                                <Select style={{width: 200}}>
                                    <Select.Option value="1">是</Select.Option>
                                    <Select.Option value="0">否</Select.Option>
                                </Select>
                            )}
                        </Form.Item>
                        <Form.Item label='物理机ip：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('realHost', {
                                initialValue: machineInfoItem.realHost ? machineInfoItem.realHost : '',
                                rules: [
                                    {
                                        message: '请正确填写物理机ip',
                                        pattern: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                                    }]
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='机房：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('room', {
                                initialValue: machineInfoItem.room ? machineInfoItem.room : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='主机名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('hostName', {
                                initialValue: machineInfoItem.hostName ? machineInfoItem.hostName : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='域名：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('hostDomain', {
                                initialValue: machineInfoItem.hostDomain ? machineInfoItem.hostDomain : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='所属业务线：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('serviceLine', {
                                initialValue: machineInfoItem.serviceLine ? machineInfoItem.serviceLine : ''
                            })(
                                <Select style={{width: 200}}>
                                    {this.props.serviceLines.map((line) =>
                                        <Select.Option key={line.id} value={line.id}>{line.serviceLineDesc}</Select.Option>
                                    )}
                                </Select>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ModifyMachineModal);