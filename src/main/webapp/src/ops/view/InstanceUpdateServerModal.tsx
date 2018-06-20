import React, {Component} from "react";
import {Button, Form, Icon, Input, Modal, Upload} from "antd";
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
    updatedFileList: any;
    onOk: any;
    onCancel: any;
}

class InstanceUpdateServerModal extends Component<ModalProps, any> {
    state = {
        fileList: [],
    };
    handleChange = (info) => {
        let fileList = info.fileList;
        fileList = fileList.slice(-2);
        fileList = fileList.map((file) => {
            if (file.response) {
                file.url = file.response.url;
                file.status = file.response.data.status;
            }
            return file;
        });
        fileList = fileList.filter((file) => {
            if (file.response) {
                return file.response.status === 'success';
            }
            return true;
        });
        this.setState({fileList});
    };

    render() {
        let instanceListVo = this.props.instanceListVo ? this.props.instanceListVo : new InstanceListVo();
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    // ...this.props.form.getFieldsValue(),
                    instanceId: this.props.instanceListVo.instanceId ? this.props.instanceListVo.instanceId : '',
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '服务升级（仅支持Jar升级情况）',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };
        if (!instanceListVo.instanceId) {
            return null;
        }

        const props = {
            action: '/zkdoctor/manage/instance/uploadNewJarFile',
            accept: '.jar',
            onChange: this.handleChange,
            multiple: false,
            defaultFileList: this.props.updatedFileList
        };

        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='实例ip：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('host', {
                                initialValue: instanceListVo['host'] ? instanceListVo['host'] : ''
                            })(
                                <Input style={{width: 250}} disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='升级Jar包上传：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('newJarFile', {})(
                                <Upload {...props}>
                                    <Button style={{width: 250}}>
                                        <Icon type="upload"/>上传新Jar包
                                    </Button>
                                </Upload>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(InstanceUpdateServerModal);


