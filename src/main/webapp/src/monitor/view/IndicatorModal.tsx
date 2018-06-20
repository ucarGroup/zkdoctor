import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {MonitorIndicatorVo} from "../model/MonitorIndicatorModel";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    baseInfoModel: BaseInfoModel;
    indicator: MonitorIndicatorVo;
    onOk: any;
    onCancel: any;
}

class ModifyIndicatorModal extends Component<ModalProps, any> {
    render() {
        let indicator = this.props.indicator? this.props.indicator: new MonitorIndicatorVo();
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    switchOn: indicator.switchOn,
                    id: indicator.id ? indicator.id : ''
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '监控任务信息修改',
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };

        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='报警指标：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('indicatorName', {
                                initialValue: indicator.indicatorName ? indicator.indicatorName : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='默认报警阈值：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('defaultAlertValue', {
                                initialValue: indicator.defaultAlertValue ? indicator.defaultAlertValue : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='默认报警间隔：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('defaultAlertInterval', {
                                initialValue: indicator.defaultAlertInterval ? indicator.defaultAlertInterval : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='默认报警频率：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('defaultAlertFrequency', {
                                initialValue: indicator.defaultAlertFrequency ? indicator.defaultAlertFrequency : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='默认报警形式：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('defaultAlertForm', {
                                initialValue: indicator.defaultAlertForm
                            })(
                                <Select style={{width: 200}}>
                                    {this.props.baseInfoModel.alertForms.map((line) =>
                                        <Select.Option key={line.index} value={line.index}>{line.name}</Select.Option>
                                    )}
                                </Select>
                            )}
                        </Form.Item>
                        <Form.Item label='备注：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('info', {
                                initialValue: indicator.info ? indicator.info + '' : ''
                            })(
                                <Input.TextArea style={{width: 200}} autosize={{minRows: 5, maxRows: 20}}>
                                </Input.TextArea>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ModifyIndicatorModal);


