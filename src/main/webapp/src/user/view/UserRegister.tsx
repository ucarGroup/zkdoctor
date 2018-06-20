import React, {Component} from "react";
import "../../sys/view/layout/common.less";
import {withRouter} from "react-router";
import {inject} from "../../common/utils/IOC";
import UserRegisterForm from "./UserRegisterForm";
import {SysUserModel} from "../../sys/model/SysUserModel";
import {UserVo} from "../model/UserModel";
import {notification} from "antd";

const divStyle = {
    width: '100%',
    height: '1000px',
    backgroundSize: '100%',
};

interface RouterI {
    router: any
}

class UserRegister extends Component<RouterI, any> {

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    render() {

        let thisV = this;
        let registerProps = {
            loading: this.sysUserModel.loading,
            isUserNameExisted(userName, callback) {
                thisV.sysUserModel.checkUserExists(userName, callback);
            },
            onOk (user: UserVo) {
                thisV.sysUserModel.register(thisV.props.router, user, (message) => {
                    notification['success']({
                        message: message,
                        description: '',
                    });
                })
            }
        };

        return (<div style={divStyle}>
            <UserRegisterForm {...registerProps}/>
        </div>);
    }
}

export default withRouter(UserRegister);