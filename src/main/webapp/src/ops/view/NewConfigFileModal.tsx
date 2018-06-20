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
    host: string;
    onOk: any;
    onCancel: any;
}

class NewConfigFileModal extends Component<ModalProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    host: this.props.host ? this.props.host : '',
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '新增配置文件',
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
                                initialValue: this.props.host ? this.props.host : ''
                            })(
                                <Input style={{width: 300}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='配置文件路径：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('confDir', {
                                initialValue: '/usr/local/zookeeper/data',
                                rules: [
                                    {
                                        required: true,
                                        message: '配置文件路径未填写！'
                                    }
                                ]
                            })(
                                <Input style={{width: 300}}/>
                            )}
                        </Form.Item>
                        <Form.Item label='配置文件名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('confFileName', {
                                initialValue: 'myid',
                                rules: [
                                    {
                                        required: true,
                                        message: '配置文件名称未填写！'
                                    }
                                ]
                            })(
                                <Input style={{width: 300}}/>
                            )}
                        </Form.Item>
                        <Form.Item label='配置文件内容：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('confFileContent', {
                                initialValue: '',
                                rules: [
                                    {
                                        required: true,
                                        message: '配置文件内容未填写！'
                                    }
                                ]
                            })(
                                <Input.TextArea style={{width: 300}} autosize={{minRows: 10, maxRows: 10}}>
                                </Input.TextArea>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(NewConfigFileModal);


