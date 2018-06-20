import React, {Component} from "react";
import {withRouter} from "react-router";
import {Button, Col, Form, Input, notification, Row, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import {ServiceLineVo} from "../model/ServiceLineOpsModel";

export interface ClusterDeployFormPropsI extends FormComponentProps {
    serviceLines: Array<ServiceLineVo>;
    clusterDeploySubmit: any;
    clusterNameExist: boolean;
    isExisted: any;
}

class ClusterDeploy extends Component<ClusterDeployFormPropsI, any> {

    constructor(props) {
        super(props);
        this.state = {tipBtn: false}
    }

    onBlur = (e) => {
        this.props.isExisted(e.target.value, () => {
            notification['error']({
                message: '集群名称已经存在，请重新填写',
                description: '',
            })
        });
    };

    clusterApplyFieldConfigs = [{
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
    },{
        span: 24,
        label: 'server配置',
        fieldName: 'serverConfig',
        element: <Input.TextArea rows={5} style={{width: 500}} placeholder="每行为一台服务器配置，格式：\n server.id=host:quorumPort:electionPort:peerType"/>,
        option: {
            initialValue: '',
            rules: [
                {
                    required: true,
                    message: 'server配置未填写'
                }
            ]
        }
    }, {
        span: 24,
        label: '安装包名称',
        fieldName: 'installFileName',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: 'zookeeper-3.4.10.tar.gz',
            rules: [
                {
                    required: true,
                    message: '安装包名称未填写'
                }
            ]
        }
    }, {
        span: 24,
        label: '安装包所在服务器目录',
        fieldName: 'installFileDir',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: '/usr/local',
            rules: [
                {
                    required: true,
                    message: '安装包所在服务器目录未填写'
                }
            ]
        }
    }, {
        span: 24,
        label: '安装包下载路径',
        fieldName: 'downloadSite',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: 'http://mirrors.shuosc.org/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz',
            rules: [
                {
                    required: true,
                    message: '安装包下载路径未填写'
                }
            ]
        }
    }, {
        span: 24,
        label: 'dataDir',
        fieldName: 'dataDir',
        element: <Input style={{width: 500}}  placeholder="dataDir"/>,
        option: {
            initialValue: '/usr/local/zookeeper/data',
            rules: [
                {
                    required: true,
                    message: 'dataDir未填写'
                }
            ]
        }
    }, {
        span: 24,
        label: 'clientPort',
        fieldName: 'clientPort',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: '2181',
            rules: [
                {
                    required: true,
                    message: 'clientPort为正整数，请正确填写',
                    pattern: /^[1-9]\d*$/
                }
            ],
        }
    }, {
        span: 24,
        label: 'tickTime',
        fieldName: 'tickTime',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: '2000',
            rules: [
                {
                    required: true,
                    message: 'tickTime为正整数，请正确填写',
                    pattern: /^[1-9]\d*$/
                }
            ],
        }
    }, {
        span: 24,
        label: 'initLimit',
        fieldName: 'initLimit',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: '10',
            rules: [
                {
                    required: true,
                    message: 'initLimit为正整数，请正确填写',
                    pattern: /^[1-9]\d*$/
                }
            ],
        }
    }, {
        span: 24,
        label: 'syncLimit',
        fieldName: 'syncLimit',
        element: <Input style={{width: 500}}/>,
        option: {
            initialValue: '5',
            rules: [
                {
                    required: true,
                    message: 'syncLimit为正整数，请正确填写',
                    pattern: /^[1-9]\d*$/
                }
            ],
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
        label: '指定配置项',
        fieldName: 'extraConfig',
        element: <Input.TextArea rows={5} style={{width: 500}} placeholder="不同配置项请换行，格式：配置项=配置值"/>,
        option: {initialValue: ''}
    }];

    render() {
        let fieldConfig = this.clusterApplyFieldConfigs;
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
                        <Form.Item {...formItemLayout} label={`${config.label}`}>
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
                    this.props.clusterDeploySubmit({...values});
                }
            });
        }

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
            </div>
        );
    }
}

export default Form.create()(ClusterDeploy);
