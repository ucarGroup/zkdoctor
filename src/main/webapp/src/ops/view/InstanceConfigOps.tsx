import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {InstanceListVo} from "../../instance/model/InstanceModel";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    instanceListVo: InstanceListVo;
    zooConfFileContent: string;
    onOk: any;
    onCancel: any;
}

class InstanceConfigOpsModal extends Component<ModalProps, any> {
    render() {
        let instanceListVo = this.props.instanceListVo ? this.props.instanceListVo : new InstanceListVo();
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    instanceId: this.props.instanceListVo.instanceId ? this.props.instanceListVo.instanceId : '',
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '配置修改',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };
        if (!instanceListVo.instanceId) {
            return null;
        }
        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='实例ip：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('host', {
                                initialValue: instanceListVo['host'] ? instanceListVo['host'] : ''
                            })(
                                <Input style={{width: 300}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='配置文件路径：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('confDir', {
                                initialValue: '/usr/local/zookeeper/conf',
                            })(
                                <Input style={{width: 300}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='配置文件内容：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('zooConfFileContent', {
                                initialValue: this.props.zooConfFileContent
                            })(
                                <Input.TextArea style={{width: 300}} autosize={{minRows: 20, maxRows: 20}}>
                                </Input.TextArea>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(InstanceConfigOpsModal);


