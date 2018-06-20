import React, {Component} from "react";
import {withRouter} from "react-router";
import {Button, Col, Form, Input, Modal, notification, Row, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {BaseInfoModel} from "../../../sys/model/BaseInfoModel";
import {ServiceLineVo} from "../../../ops/model/ServiceLineOpsModel";

export interface NewClusterFormPropsI extends FormComponentProps {
    serviceLines: Array<ServiceLineVo>;
    baseInfoModel: BaseInfoModel;
    addClusterSubmit: any;
    addClusterSubmitResult: boolean;
    goListPage: any;
    resetAddClusterSubmitResult: any;
    isExisted: any;
}

class NewClusterForm extends Component<NewClusterFormPropsI, any> {

    constructor(props) {
        super(props);
        this.state = {
            tipBtn: false,
        }
    }

    onBlur = (e) => {
        this.props.isExisted(e.target.value, () => {
            notification['error']({
                message: '集群名称已经存在，请重新填写',
                description: '',
            })
        });
    };

    clusterFieldConfigs = [{
        span: 24,
        label: '集群名称',
        fieldName: 'clusterName',
        element: <Input style={{width: 500}} onBlur={this.onBlur}/>,
        option: {
            rules: [
                {
                    required: true,
                    message: '集群名称未填写'
                }
            ]
        }
    }, {
        span: 24,
        label: '负责人',
        fieldName: 'officer',
        element: <Input style={{width: 500}}/>,
        option: {initialValue: ''}
    }, {
        span: 24,
        label: '实例数',
        fieldName: 'instanceNumber',
        element: <Input style={{width: 500}}/>,
        option: {initialValue: ''}
    }, {
        span: 24,
        label: '服务器host列表',
        fieldName: 'newClusterServers',
        element: <Input.TextArea rows={6} style={{width: 500}} placeholder="服务器host列表，每行表示一台服务器，每行格式为：ip:port"/>,
        option: {
            rules: [
                {
                    required: true,
                    message: '服务器host列表未填写'
                }
            ],
        }
    }, {
        span: 24,
        label: '部署类型',
        fieldName: 'deployType',
        element: (<Select style={{width: 500}} placeholder="请选择">
            {this.props.baseInfoModel.deployTypes.map((line) =>
                <Select.Option key={line.index} value={line.index}>{line.name}</Select.Option>
            )}
        </Select>),
        option: {
            rules: [
                {
                    required: true,
                }
            ],
            initialValue: 1
        }
    }, {
        span: 24,
        label: '业务线',
        fieldName: 'serviceLine',
        element: (<Select style={{width: 500}} placeholder="请选择">
            {this.props.serviceLines.map((line) =>
                <Select.Option key={line.id} value={line.id}>{line.serviceLineDesc}</Select.Option>
            )}
        </Select>),
        option: {initialValue: ''}
    }, {
        span: 24,
        label: 'zk版本号',
        fieldName: 'version',
        element: <Input style={{width: 500}}/>,
        option: {initialValue: ''}
    }, {
        span: 24,
        label: '描述',
        fieldName: 'intro',
        element: <Input.TextArea rows={4} style={{width: 500}}/>,
        option: {initialValue: ''}
    }];

    render() {
        let fieldConfig = this.clusterFieldConfigs;
        const {getFieldDecorator} = this.props.form;
        let getFields = () => {
            const formItemLayout = {
                labelCol: {span: 7},
                wrapperCol: {span: 17},
            };
            const children = [];
            for (let i = 0; i < fieldConfig.length; i++) {
                let config = fieldConfig[i];
                children.push(
                    <Col span={config.span} key={`${config.fieldName}-col`}>
                        <Form.Item {...formItemLayout} label={`${config.label}`} key={`${config.fieldName}-col`}>
                            {getFieldDecorator(`${config.fieldName}`, config.option)(
                                config.element
                            )}
                        </Form.Item>
                    </Col>
                );
            }
            return children;
        };

        let handleSubmit = (e) => {
            e.preventDefault();
            this.props.form.validateFields((err, values) => {
                if (!err) {
                    this.props.addClusterSubmit({...values});
                }
            });
        };

        return (
            <div>
                <Form onSubmit={handleSubmit}>
                    <Row gutter={40}>
                        {getFields()}
                    </Row>
                    <Row>
                        <Col span={24} style={{textAlign: 'center'}}>
                            <Button type="primary" htmlType="submit">提交</Button>
                        </Col>
                    </Row>
                </Form>
                {this.props.addClusterSubmitResult ? <Modal
                    title="提交成功"
                    visible={true}
                    onOk={() => {
                        this.props.resetAddClusterSubmitResult();
                        this.props.goListPage();
                    }}
                    onCancel={() => {
                        this.props.resetAddClusterSubmitResult();
                        this.props.form.resetFields()
                    }}
                    okText="前往集群列表"
                    cancelText="继续提交"
                >
                    您可以选择下一步动作……
                </Modal> : null}
            </div>
        );
    }
}

export default Form.create()(NewClusterForm);
