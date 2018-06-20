import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import ServiceLine from "./ServiceLine";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    serviceLine: ServiceLine;
    onOk: any;
    onCancel: any;
}

class ServiceLineUpdateModal extends Component<ModalProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    id: this.props.serviceLine ? this.props.serviceLine.id : 0,
                    ...this.props.form.getFieldsValue(),
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '更新业务线信息',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='业务线名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('serviceLineName', {
                                initialValue: this.props.serviceLine ? this.props.serviceLine.serviceLineName : '',
                                rules: [
                                    {
                                        required: true,
                                        message: '业务线名称未填写'
                                    }
                                ],
                            })(
                                <Input style={{width: 250}} placeholder="英文区分，唯一识别业务线信息"/>
                            )}
                        </Form.Item>
                        <Form.Item label='业务线描述：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('serviceLineDesc', {
                                initialValue: this.props.serviceLine ? this.props.serviceLine.serviceLineDesc : '',
                                rules: [
                                    {
                                        required: true,
                                        message: '业务线描述未填写'
                                    }
                                ],
                            })(
                                <Input style={{width: 250}} placeholder="业务线名称"/>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ServiceLineUpdateModal);


