import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {SystemConfigVo} from "../model/SystemOpsModel";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    systemConfig: SystemConfigVo;
    onOk: any;
    onCancel: any;
}

class SystemConfigUpdateModal extends Component<ModalProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '更新配置值',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='配置名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('configName', {
                                initialValue: this.props.systemConfig ? this.props.systemConfig.configName : ''
                            })(
                                <Input style={{width: 300}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='功能：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('configDesc', {
                                initialValue: this.props.systemConfig ? this.props.systemConfig.configDesc : ''
                            })(
                                <Input style={{width: 300}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='默认值：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('defaultConfigValue', {
                                initialValue: this.props.systemConfig ? this.props.systemConfig.defaultConfigValue : ''
                            })(
                                <Input style={{width: 300}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='当前值：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('configValue', {
                                initialValue: this.props.systemConfig ? this.props.systemConfig.configValue : ''
                            })(
                                <Input style={{width: 300}}/>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(SystemConfigUpdateModal);


