import React, {Component} from "react";
import {Form, Input, Modal} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";

const formItemLayout = {
    labelCol: {
        span: 8
    },
    wrapperCol: {
        span: 14
    }
};

export interface ModalProps extends FormComponentProps {
    dateTime: string;
    trafficInDetail: string;
    trafficOutDetail: string;
    onOk: any;
    onCancel: any;
}

class TrafficModal extends Component<ModalProps, any> {


    getDetail(arrayData: Array) {
        let data = '';
        for (var i = 0; i < arrayData.length; i++) {
            data += this.replaceSymbol(JSON.stringify(arrayData[i])) + " KB/s \n";
        }
        return data;
    }

    replaceSymbol(str: string) {
        return str.replace("{", "").replace("}", "").replace(/\"/g, "");
    }

    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.onOk()
        };

        const modalOpts = {
            title: '读写流量Top10，时间点：' + this.props.dateTime,
            visible: true,
            onOk: handleOk,
            onCancel: this.props.onCancel,
            wrapClassName: 'vertical-center-modal'
        };
        return (
            <div>
                <Modal {...modalOpts}>
                    <Form layout="horizontal">
                        <Form.Item label='写流量：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('trafficInDetail', {
                                initialValue: this.getDetail(this.props.trafficInDetail)
                            })(
                                <Input.TextArea style={{width: 250}} autosize={true}/>
                            )}
                        </Form.Item>
                        <Form.Item label='读流量：' hasFeedback {...formItemLayout}>
                            {this.props.form.getFieldDecorator('trafficOutDetail', {
                                initialValue: this.getDetail(this.props.trafficOutDetail)
                            })(
                                <Input.TextArea style={{width: 250}} autosize={true} />
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </div>);
    }
}

export default Form.create()(TrafficModal);


