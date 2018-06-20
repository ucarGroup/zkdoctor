import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {MonitorTaskVo} from "../model/MonitorTaskModel";
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
    baseInfoModel: BaseInfoModel,
    task: MonitorTaskVo;
    indicator: MonitorIndicatorVo;
    onOk: any;
    onCancel: any;
}

class ModifyTaskModal extends Component<ModalProps, any> {
    render() {
        let task = this.props.task? this.props.task: new MonitorTaskVo();
        let indicator = this.props.indicator? this.props.indicator: new MonitorIndicatorVo();

        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    switchOn: task.switchOn,
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
                        <Form.Item label='监控任务id：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('id', {
                                initialValue: task.id ? task.id : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='集群名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('clusterName', {
                                initialValue: task.clusterName ? task.clusterName : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警指标：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('indicatorName', {
                                initialValue: indicator.indicatorName ? indicator.indicatorName : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警阈值：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertValue', {
                                initialValue: task.alertValue ? task.alertValue : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                            <label>{indicator.alertValueUnit}</label>
                        </Form.Item>
                        <Form.Item label='报警间隔：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertInterval', {
                                initialValue: task.alertInterval ? task.alertInterval : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警频率：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertFrequency', {
                                initialValue: task.alertFrequency ? task.alertFrequency : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警形式：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertForm', {
                                initialValue: task.alertForm
                            })(
                                <Select style={{width: 200}}>
                                    {this.props.baseInfoModel.alertForms.map((line) =>
                                        <Select.Option key={line.index} value={line.index}>{line.name}</Select.Option>
                                    )}
                                </Select>
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(ModifyTaskModal);


