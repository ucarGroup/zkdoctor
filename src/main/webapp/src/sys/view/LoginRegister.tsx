import React, {Component} from "react";
import LoginRegisterForm from "./login/LoginRegisterForm";
import "./layout/common.less";
import {withRouter} from "react-router";
import {SysUserModel} from "../model/SysUserModel";
import {inject} from "../../common/utils/IOC";
import config from "../../common/config.js";

const divStyle = {
    width: '100%',
    height: '1000px',
    backgroundImage: `url(${config.assertPrefix}background/background.gif)`,
    backgroundSize: '100%',
    backgroundRepeat: 'no-repeat',
    backgroundWidth: '100%'
};

interface RouterI {
    router: any
}

class LoginRegister extends Component<RouterI, any> {
    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    render() {

        let thisV = this;
        let loginProps = {
            loading: this.sysUserModel.loading,
            onOk (userName: string, password: string) {
                thisV.sysUserModel.login(userName, password, thisV.props.router);
            }
        };

        return (<div style={divStyle}>
            <LoginRegisterForm {...loginProps} />
        </div>);
    }
}

export default withRouter(LoginRegister);