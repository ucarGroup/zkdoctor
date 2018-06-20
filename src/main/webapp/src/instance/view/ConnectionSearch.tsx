import React, {Component} from "react";
import {Button, DatePicker, Form, Row, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {ClientConnectionSearchVo} from "../model/InstanceModel";
import moment from "moment/moment";

export interface ConnectionSearchProps extends FormComponentProps {
    clusterId: number;
    instanceId: number;
    onSearch: any;
}

class ConnectionSearch extends Component<ConnectionSearchProps, any> {
    render() {
        let handleOk = (e) => {
            e.preventDefault();
            this.props.form.validateFields((errors) => {
                if (errors) {
                    return
                }
                let data = new ClientConnectionSearchVo();
                data.startDate = this.props.form.getFieldValue('time')[0].format('YYYY-MM-DD HH:mm');
                data.endDate = this.props.form.getFieldValue('time')[1].format('YYYY-MM-DD HH:mm');
                data.clusterId = this.props.clusterId;
                data.instanceId = this.props.instanceId;
                data.orderBy = this.props.form.getFieldValue('orderBy');
                this.props.onSearch(data);
            })
        };

        return (<Form layout="inline" onSubmit={handleOk}>
            <Row>
                <Form.Item label='时间：'>
                        <span>
                        {this.props.form.getFieldDecorator('time', {
                            initialValue: [moment().subtract(1, 'minutes'), moment()],
                        })(
                            <DatePicker.RangePicker style={{marginRight: 10, marginTop: -5}}
                                                    showTime allowClear={false} format="YYYY-MM-DD HH:mm"/>
                        )}
                        </span>
                </Form.Item>
                <Form.Item label='排序：'>
                        <span>
                        {this.props.form.getFieldDecorator('orderBy', {
                            initialValue: 'clientIp',
                        })(
                            <Select style={{width: 120}}>
                                <Select.Option value="clientIp">客户端ip</Select.Option>
                                <Select.Option value="createTime">时间</Select.Option>
                                <Select.Option value="recved">收包数</Select.Option>
                                <Select.Option value="sent">发包数</Select.Option>
                                <Select.Option value="queued">堆积请求数</Select.Option>
                                <Select.Option value="maxlat">最大延时</Select.Option>
                                <Select.Option value="avglat">平均延时</Select.Option>
                            </Select>
                        )}
                        </span>
                </Form.Item>
                <Button type='primary' htmlType='submit' style={{marginRight: 16}}>查询</Button>
            </Row>
        </Form>);
    }
}

export default Form.create()(ConnectionSearch);
