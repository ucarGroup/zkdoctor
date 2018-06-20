import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";

const formItemLayout = {
    labelCol: {
        span: 6
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    clusterId: number;
    updatePath: string;
    onOk: any;
    onCancel: any
}

class updateZnodeModal extends Component<ModalProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    clusterId: this.props.clusterId ? this.props.clusterId : '',
                    ...this.props.form.getFieldsValue(),
                };
                this.props.onOk(data)
            })
        };
        const modalOpts = {
            title: '更新节点数据',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (<Modal {...modalOpts}>
            <Form layout="horizontal">
                <Form.Item label='节点路径：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('path', {
                        initialValue: this.props.updatePath,
                        rules: [
                            {
                                required: true,
                                message: '必须选择节点路径！'
                            }
                        ]
                    })(
                        <Input style={{width: 300}} placeholder='' readOnly={true}/>
                    )}
                </Form.Item>

                <Form.Item label='节点数据：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('data', {
                        initialValue: '',
                    })(
                        <Input.TextArea style={{width: 300}} placeholder='注意：只支持sring类型数据！'
                                        autosize={{minRows: 5, maxRows: 20}}/>
                    )}
                </Form.Item>

                <Form.Item label='版本号：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('version', {
                        initialValue: -1,
                        rules: [
                            {
                                required: true,
                                message: '必须指定版本号！默认值：-1'
                            }
                        ]
                    })(
                        <Input style={{width: 300}} placeholder=''/>
                    )}
                </Form.Item>
            </Form>
        </Modal>);
    }
}

export default Form.create()(updateZnodeModal);