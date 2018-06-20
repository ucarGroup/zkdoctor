import React, {Component} from "react";
import {Button, Col, Form, Input, Row} from "antd";
import {FormComponentProps} from "antd/lib/form/Form";
import styles from "../../themes/Search.less";

export interface SearchProps extends FormComponentProps {
    onSearch: any;
}

class Search extends Component<SearchProps, any> {

    getFieldConfigs() {
        return [{
            span: 22,
            label: '触发器名称或触发组名称',
            fieldName: 'query',
            element: <Input style={{width: 150}}/>,
            option: {initialValue: ''}
        }]
    };

    render() {
        let fieldConfig = this.getFieldConfigs();
        let getFields = () => {
            const {getFieldDecorator} = this.props.form;
            const formItemLayout = {
                labelCol: {span: 13},
                wrapperCol: {span: 11},
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
                <Button htmlType="submit" style={{position: 'relative', top: 2, marginLeft: 10}}
                        type="primary">查询</Button>
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
            <Row gutter={2}>{getFields()}</Row>
        </Form>);
    }
}

export default Form.create()(Search);
