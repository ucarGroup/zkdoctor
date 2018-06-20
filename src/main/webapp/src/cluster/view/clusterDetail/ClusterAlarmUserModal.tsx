import React, {Component} from "react";
import {Form, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {UserVo} from "../../../user/model/UserModel";

const formItemLayout = {
    labelCol: {
        span: 6
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    users: Array<UserVo>;
    onOk: any;
    onCancel: any
}

class ClusterAlarmUserModal extends Component<ModalProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    userId:this.props.form.getFieldValue("userId"),
                };
                this.props.onOk(data)
            })
        };
        const modalOpts = {
            title: '新增报警用户',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (<Modal {...modalOpts}>
            <Form layout="horizontal">
                <Form.Item label='用户：' hasFeedback {...formItemLayout}>
                    {this.props.form.getFieldDecorator('userId', {
                        rules: [
                            {
                                required: true,
                                message: '用户未选择'
                            }
                        ]
                    })(
                        <Select style={{width: '80%', marginRight: 8}}
                                optionFilterProp="children" showSearch={true}>
                            {this.props.users.map(item => <Select.Option key={item.id}>{item.userName}</Select.Option>)}
                        </Select>
                    )}
                </Form.Item>
            </Form>
        </Modal>);
    }
}

export default Form.create()(ClusterAlarmUserModal);