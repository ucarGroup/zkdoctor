import React, {Component} from "react";
import {Button, Form, Input, notification, Row} from "antd";
import styles from "../../sys/view/login/login.less";
import {FormComponentProps} from "antd/lib/form/Form";
import config from "../../common/config.js";
import {Link} from "react-router";

export interface ModalProps extends FormComponentProps {
    isUserNameExisted: any;
    onOk: any;
    loading: boolean;
}

class UserRegisterForm extends Component<ModalProps, any> {
    render() {
        let thisV = this;
        function handleOk() {
            thisV.props.form.validateFieldsAndScroll((errors, values) => {
                if (errors) {
                    return
                }
                thisV.props.onOk({...values});
            })
        }

        const formItemLayout = {
            labelCol: {span: 4},
            wrapperCol: {span: 20},
        };

        function onBlur(e) {
            thisV.props.isUserNameExisted(e.target.value, () => {
                notification['error']({
                    message: '该用户名已经存在，请重新填写',
                    description: '',
                })
            });
        };

        return (
            <div className={styles.registerform}>
                <div className={styles.logo}>
                    <img src={`${config.assertPrefix}${config.logoSrc}`}/>
                </div>
                <form>
                    <Form.Item {...formItemLayout} hasFeedback label="用户名">
                        {thisV.props.form.getFieldDecorator('userName', {
                            rules: [
                                {
                                    required: true,
                                    message: '请输入正确的用户名',
                                    pattern: /[a-zA-Z0-9]{1,20}/
                                }
                            ]
                        })(<Input size='large' onPressEnter={handleOk} onBlur={onBlur}
                                  placeholder="用户名，由1-20位字母数字、组合，唯一识别用户"/>)}
                    </Form.Item>
                    <Form.Item  {...formItemLayout} hasFeedback label="密码">
                        {thisV.props.form.getFieldDecorator('password', {
                            rules: [
                                {
                                    required: true,
                                    message: '请输入密码',
                                    pattern: /[a-zA-Z0-9]{1,20}/
                                }
                            ]
                        })(<Input size='large' onPressEnter={handleOk} type='password'
                                  placeholder='密码，由1-20位字母、数字组合'/>)}
                    </Form.Item>
                    <Form.Item  {...formItemLayout} hasFeedback label="中文名">
                        {thisV.props.form.getFieldDecorator('chName', {
                            rules: [
                                {
                                    required: true,
                                    message: '请输入中文名'
                                }
                            ]
                        })(<Input size='large' onPressEnter={handleOk}
                                  placeholder='中文名称'/>)}
                    </Form.Item>
                    <Form.Item  {...formItemLayout} hasFeedback label="手机号">
                        {thisV.props.form.getFieldDecorator('mobile', {
                            rules: [
                                {
                                    message: '手机号格式不对，请重新输入',
                                    pattern: /^1[34578]\d{9}$/,
                                }
                            ]
                        })(<Input size='large' onPressEnter={handleOk}
                                  placeholder='请输入手机号'/>)}
                    </Form.Item>
                    <Form.Item  {...formItemLayout} hasFeedback label="邮箱">
                        {thisV.props.form.getFieldDecorator('mail', {
                            rules: [
                                {
                                    message: '邮箱格式不对，请重新输入',
                                    pattern: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/,
                                }
                            ]
                        })(<Input size='large' onPressEnter={handleOk}
                                  placeholder='请输入邮箱'/>)}
                    </Form.Item>
                    <Row offset={4}>
                        <Button type='primary' size='large' onClick={handleOk} loading={this.props.loading}>
                            注册
                        </Button>
                    </Row>
                </form>
            </div>
        );
    }
}

export default Form.create()(UserRegisterForm);