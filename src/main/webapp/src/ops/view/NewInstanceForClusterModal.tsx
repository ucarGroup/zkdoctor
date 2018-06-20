import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    clusterId: number;
    onOk: any;
    onCancel: any;
}

class NewInstanceForClusterModal extends Component<ModalProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    clusterId: this.props.clusterId,
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '新增实例',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };
        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='实例ip：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('host', {
                                rules: [
                                    {
                                        required: true,
                                        message: '请正确填写ip信息！',
                                        pattern: /^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$/,
                                    }
                                ]
                            })(
                                <Input style={{width: 250}}/>
                            )}
                        </Form.Item>
                        <Form.Item label='实例port：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('port', {
                                rules: [
                                    {
                                        required: true,
                                        message: '请正确填写port信息！',
                                        pattern: /^[1-9]\d*$/
                                    }
                                ]
                            })(
                                <Input style={{width: 250}}/>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(NewInstanceForClusterModal);

