import React, {Component} from "react";
import {Form, Input, Modal, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {ClusterMonitorAlarmTaskVo} from "../model/ClusterMonitorAlarmTaskModel";
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
    clusterMonitorAlarmTask: ClusterMonitorAlarmTaskVo;
    onOk: any;
    onCancel: any;
}

class ModifyMonitorAlarmTaskModal extends Component<ModalProps, any> {
    render() {
        let clusterMonitorAlarmTask = this.props.clusterMonitorAlarmTask ? this.props.clusterMonitorAlarmTask : new ClusterMonitorAlarmTaskVo();
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                const data = {
                    ...this.props.form.getFieldsValue(),
                    switchOn: clusterMonitorAlarmTask.switchOn,
                    id: clusterMonitorAlarmTask.id ? clusterMonitorAlarmTask.id : ''
                };
                this.props.onOk(data)
            })
        };

        const modalOpts = {
            title: '监控报警任务信息修改',
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
                                initialValue: clusterMonitorAlarmTask.id ? clusterMonitorAlarmTask.id : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='集群名称：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('clusterName', {
                                initialValue: clusterMonitorAlarmTask.clusterName ? clusterMonitorAlarmTask.clusterName : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警指标：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('indicatorName', {
                                initialValue: clusterMonitorAlarmTask.indicatorName ? clusterMonitorAlarmTask.indicatorName : ''
                            })(
                                <Input style={{width: 200}} placeholder='' disabled={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警阈值：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertValue', {
                                initialValue: clusterMonitorAlarmTask.alertValue ? clusterMonitorAlarmTask.alertValue : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                            <label>{clusterMonitorAlarmTask.alertValueUnit}</label>
                        </Form.Item>
                        <Form.Item label='报警间隔：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertInterval', {
                                initialValue: clusterMonitorAlarmTask.alertInterval ? clusterMonitorAlarmTask.alertInterval : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警频率：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertFrequency', {
                                initialValue: clusterMonitorAlarmTask.alertFrequency ? clusterMonitorAlarmTask.alertFrequency : ''
                            })(
                                <Input style={{width: 200}} placeholder=''/>
                            )}
                        </Form.Item>
                        <Form.Item label='报警形式：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('alertForm', {
                                initialValue: clusterMonitorAlarmTask.alertForm
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

export default Form.create()(ModifyMonitorAlarmTaskModal);


