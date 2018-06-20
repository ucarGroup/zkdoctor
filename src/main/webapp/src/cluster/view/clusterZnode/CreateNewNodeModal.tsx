import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
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
    parentPath: string;
    onOk: any;
    onCancel: any
}

class CreateNewZnodeModal extends Component<ModalProps, any> {
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
            title: '增加新节点',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (<Modal {...modalOpts}>
            <Form layout="horizontal">
                <Form.Item label='节点父路径：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('parentPath', {
                        initialValue: this.props.parentPath ? this.props.parentPath : '/',
                        rules: [
                            {
                                required: true,
                                message: '必须选择父节点路径！'
                            }
                        ]
                    })(
                        <Input style={{width: 300}} placeholder='' readOnly={true}/>
                    )}
                </Form.Item>
                <Form.Item label='子节点路径：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('childPath', {
                        initialValue: '',
                        rules: [
                            {
                                required: true,
                                message: '必须填写新节点路径！'
                            }
                        ]
                    })(
                        <Input style={{width: 300}} placeholder='新节点路径为：父路径+子节点路径'/>
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
                <Form.Item label='是否创建父节点：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('createParentNeeded', {
                        initialValue: '1',
                        rules: [
                            {
                                required: true,
                                message: '请选择'
                            }
                        ]
                    })(
                        <Select style={{width: 300, marginRight: 8}}>
                            <Select.Option key='1'  value='1'>是</Select.Option>
                            <Select.Option key='0' value='0'>否</Select.Option>
                        </Select>
                    )}
                </Form.Item>

                <Form.Item label='ACL类型：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('acl', {
                        initialValue: '0',
                        rules: [
                            {
                                required: true,
                                message: '请选择'
                            }
                        ]
                    })(
                        <Select style={{width: 300, marginRight: 8}}>
                            <Select.Option value='0' key='0'>OPEN_ACL_UNSAFE</Select.Option>
                            <Select.Option value='1' key='1'>CREATOR_ALL_ACL</Select.Option>
                            <Select.Option value='2' key='2'>READ_ACL_UNSAFE</Select.Option>
                        </Select>
                    )}
                </Form.Item>
            </Form>
        </Modal>);
    }
}
export default Form.create()(CreateNewZnodeModal);