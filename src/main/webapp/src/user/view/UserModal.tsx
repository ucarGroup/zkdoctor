import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {UserVo} from "../model/UserModel";
import {FormComponentProps} from "antd/lib/form/Form";
import {UserRole} from "../../sys/model/BaseInfoModel";

const formItemLayout = {
    labelCol: {
        span: 6
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    userRoles: Array<UserRole>,
    user: UserVo;
    type: string
    onOk: any;
    onCancel: any
}

class UserModal extends Component<ModalProps, any> {
    render() {
        let userItem = this.props.user ? this.props.user : new UserVo;

        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    userRole: this.props.form.getFieldValue('userRole')[0],
                    id: userItem.id ? userItem.id : ''
                };
                this.props.onOk(data)
            })
        };
        const modalOpts = {
            title: '修改用户',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (<Modal {...modalOpts}>
            <Form layout="horizontal">
                <Form.Item label='用户名：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('userName', {
                        initialValue: userItem.userName,
                        rules: [
                            {
                                required: true,
                                message: '用户名未填写'
                            }
                        ]
                    })(<Input/>)}
                </Form.Item>
                <Form.Item label='中文名：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('chName', {
                        initialValue: userItem.chName,
                        rules: [
                            {
                                required: true,
                                message: '中文名未填写'
                            }
                        ]
                    })(<Input/>)}
                </Form.Item>
                <Form.Item label='手机号：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('mobile', {
                        initialValue: userItem.mobile ? userItem.mobile : '',
                        rules: [
                            {
                                message: '手机号格式不对，请重新输入',
                                pattern: /^1[34578]\d{9}$/,
                            }
                        ]
                    })(<Input/>)}
                </Form.Item>
                <Form.Item label='邮箱：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('email', {
                        initialValue: userItem.email ? userItem.email : '',
                        rules: [
                            {
                                message: '邮箱格式不对，请重新输入',
                                pattern: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/,
                            }
                        ]
                    })(<Input/>)}
                </Form.Item>
                <Form.Item label='用户类型：' {...formItemLayout}>
                    {this.props.form.getFieldDecorator('userRole', {
                        initialValue: userItem.userRole ? userItem.userRole + '' : '0',
                    })(
                        <Select>
                            {this.props.userRoles.map((item) =>
                                <Select.Option key={item.index + ''} value={item.index + ''}>{item.name}</Select.Option>
                            )}
                        </Select>
                    )}
                </Form.Item>
            </Form>
        </Modal>);
    }
}

export default Form.create()(UserModal);