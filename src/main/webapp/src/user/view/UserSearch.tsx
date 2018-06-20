import React, {Component} from "react";
import {Button, Col, Form, Input, Row} from "antd";
import {UserVo} from "../model/UserModel";
import {FormComponentProps} from "antd/lib/form/Form";
import styles from "../../themes/Search.less";

export interface SearchProps extends FormComponentProps {
    user: UserVo;
    onSearch: any;
}

class UserSearch extends Component<SearchProps, any> {

    getFieldConfigs() {
        return [{
            span: 8,
            label: '用户名：',
            fieldName: 'userName',
            element: <Input style={{width: 150}} placeholder="用户名" />,
            option: {initialValue: ''}
        }, {
            span: 7,
            label: '中文名：',
            fieldName: 'chName',
            element: <Input style={{width: 150}} placeholder="中文名" />,
            option: {}
        }, {
            span: 7,
            label: '邮箱：',
            fieldName: 'email',
            element: <Input style={{width: 150}} placeholder="用户邮箱"/>,
            option: {}
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
            <Row gutter={2}>{getFields()}</Row>
        </Form>);
    }
}

export default Form.create()(UserSearch);
