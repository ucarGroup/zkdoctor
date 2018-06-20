import React, {Component} from "react";
import {Button, Col, Form, Input, Row, Select} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import styles from "../../../themes/Search.less";
import {BaseInfoModel} from "../../../sys/model/BaseInfoModel";
import {ServiceLineVo} from "../../../ops/model/ServiceLineOpsModel";

export interface SearchProps extends FormComponentProps {
    serviceLines: Array<ServiceLineVo>;
    baseInfoModel: BaseInfoModel;
    onSearch: any;
}

class Search extends Component<SearchProps, any> {

    constructor(props) {
        super(props);
    }

    getStatusSelect() {
        let selects = [];
        this.props.baseInfoModel.clusterStatus.forEach((status) => {
            selects.push(<Select.Option key={status.index} value={status.index}>{status.name}</Select.Option>)
        });
        return selects;
    }

    getFieldConfigs() {
        return [{
            span: 8,
            label: '集群名称',
            fieldName: 'clusterName',
            element: <Input style={{width: 150}}/>,
            option: {initialValue: ''}
        }, {
            span:7,
            label: '业务线',
            fieldName: 'serviceLine',
            element: (<Select style={{width: 150}} placeholder="请选择" key="serviceLineSelect">
                <Select.Option value="" key="全部">全部</Select.Option>
                {this.props.serviceLines.map((item) =>
                    <Select.Option value={item.id} key={item.id}>{item.serviceLineDesc}</Select.Option>
                )}
            </Select>),
            option: {initialValue: ""}
        }, {
            span:7,
            label: '状态',
            fieldName: 'status',
            element: (<Select style={{width: 150}} placeholder="请选择" key="statusSelect">
                <Select.Option value="" key="全部">全部</Select.Option>
                {this.getStatusSelect()}
            </Select>),
            option: {initialValue: ""}
        }]
    };

    render() {
        let fieldConfig = this.getFieldConfigs();
        let getFields = () => {
            const {getFieldDecorator} = this.props.form;
            const formItemLayout = {
                labelCol: {span: 9},
                wrapperCol: {span: 15},
            };
            const children = [];
            for (let i = 0; i < fieldConfig.length; i++) {
                let config = fieldConfig[i];
                children.push(
                    <Col span={config.span} key={`${config.fieldName}-col`}>
                        <Form.Item {...formItemLayout} label={`${config.label}`} key={`${config.fieldName}-item`}>
                            {getFieldDecorator(`${config.fieldName}`, config.option)(
                                config.element
                            )}
                        </Form.Item>
                    </Col>
                );
            }
            children.push(<Col span={2} key="submit">
                <Button htmlType="submit" style={{position: 'relative', top: 2, marginLeft: 40}} type="primary">查询</Button>
             </Col>);
            return children;
        };

        let handleSearch = (e) => {
            e.preventDefault();
            this.props.form.validateFields((err, values) => {
                let data = {...values};
                this.props.onSearch(data);
            });
        };
        return (<Form className={styles.antAdvancedSearchForm} onSubmit={handleSearch}>
            <Row gutter={2} >{getFields()}</Row>
        </Form>);
    }
}

export default Form.create()(Search);
