import React, {Component} from "react";
import {observer} from "mobx-react";
import {inject} from "../../common/utils/IOC";
import {Button, notification} from "antd";
import {ListProps, UserList} from "./UserList";
import UserModal from "./UserModal";
import {UserModel, UserSearchVo, UserVo} from "../model/UserModel";
import UserSearch from "./UserSearch";
import {BaseInfoModel} from "../../sys/model/BaseInfoModel";

/**
 * 用户信息
 */
interface UserState {
    showModal: Boolean;
    userItem: UserVo;
    modalType: string;
}

@observer
export default class UserView extends Component<{}, UserState> {

    @inject(UserModel)
    private userModel: UserModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            modalType: 'create',
            userItem: null
        }
    }

    componentDidMount(): void {
        this.userModel.query({});
        this.setState({})
    }

    showUserModal(type: string): void {
        this.setState({
            showModal: true,
            modalType: type
        });
    };

    hideUserModal() {
        this.setState({
            showModal: false,
            userItem: null
        });
    };

    getSearchProps() {
        let thisV = this;
        return {
            user: this.state.userItem,
            onSearch(searchVo: UserSearchVo) {
                thisV.userModel.query(searchVo);
            }
        };
    };

    getListProps(): ListProps {
        let thisV = this;
        return {
            user: this.state.userItem,
            dataSource: this.userModel.users,
            loading: this.userModel.loading,
            pageConfig: this.userModel.pageConfig,
            onDelete(userId) {
                thisV.userModel.delete(userId, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                });
                thisV.setState({userItem: null});
            },
            onEdit(bean) {
                thisV.setState({userItem: bean, showModal: true, modalType: 'edit'});
            },
        };
    };

    getModalProps() {
        let thisV = this;
        return {
            userRoles: this.baseInfoModel.userRoles,
            user: this.state.userItem,
            type: this.state.modalType,
            onOk: function (user: UserVo) {
                if (thisV.state.modalType == 'create') {
                    thisV.userModel.add(user, (message) => {
                        notification['success']({
                            message: message,
                            description: '',
                        });
                    });
                } else {
                    thisV.userModel.edit(user, (message) => {
                        notification['success']({
                            message: message,
                            description: '',
                        });
                    });
                    thisV.setState({userItem: null});
                }
                thisV.hideUserModal();
            },
            onCancel() {
                thisV.hideUserModal();
            },
        };
    };

    render() {
        return (
            <div>
                <div>
                    <UserSearch {...this.getSearchProps()}/>
                </div>
                <div>
                    <Button icon="plus" type="primary" style={{marginRight: 10, marginBottom: 10}}
                            onClick={() => this.showUserModal('create')}>新增用户</Button>

                </div>
                <UserList {...this.getListProps()}/>
                {this.state.showModal ? <UserModal {...this.getModalProps()}/> : ''}
            </div>
        )
    }
}
