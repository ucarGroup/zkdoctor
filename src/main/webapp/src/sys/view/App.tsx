import React, {Component} from "react";
import classnames from "classnames";
import {inject} from "../../common/utils/IOC";
import {observer} from "mobx-react";
import {withRouter} from "react-router";
import Sider from "./layout/sider";
import Header from "./layout/header";
import Footer from "./layout/footer";
import Bread from "./layout/bread";
import styles from "./layout/main.less";
import "./layout/common.less";
import {SysInfoModel} from "../model/SysInfoModel";
import {SysUserModel} from "../model/SysUserModel";
import {BaseInfoModel} from "../model/BaseInfoModel";

interface IProps extends React.Props<any> {
    location: any;
    router: any
}

@observer
class App extends Component<IProps, any> {

    @inject(SysInfoModel)
    private sysInfoModel: SysInfoModel;

    @inject(SysUserModel)
    private sysUserModel: SysUserModel;

    @inject(BaseInfoModel)
    private baseInfoModel: BaseInfoModel;

    render() {
        let thisV = this;

        let sysUser = this.sysUserModel.sysUser;
        let sysInfo = this.sysInfoModel.sysInfo;

        const headerProps = {
            baseInfoModel: this.baseInfoModel,
            user: sysUser,
            siderFold: sysInfo.siderFold,
            location: this.props.location,
            isNavbar: sysInfo.isNavbar,
            menuPopoverVisible: sysInfo.menuPopoverVisible,
            massageNewCount: 0,
            switchMenuPopover () {
                thisV.sysInfoModel.switchMenuPopver();
            },
            logout () {
                thisV.sysUserModel.logout(thisV.props.router);
            },
            switchSider () {
                thisV.sysInfoModel.switchSider();
            },
            changeMessage () {
            },
            queryLimitNewCount () {
            }
        };

        const siderProps = {
            user: sysUser,
            menu: this.sysInfoModel.menu,
            siderFold: sysInfo.siderFold,
            darkTheme: sysInfo.darkTheme,
            location: this.props.location,
            changeTheme () {
                thisV.sysInfoModel.switchTheme();
            },
            handleClickNavMenu(e){
                let path = e.item.props.path;
                if (path) {
                }
            }
        };

        const breadProps = {
            menu: this.sysInfoModel.menu,
            location: this.props.location,
        };

        return (
            <div>
                <div
                    className={classnames(styles.layout, {[styles.fold]: sysInfo.isNavbar ? false : sysInfo.siderFold}, {[styles.withnavbar]: sysInfo.isNavbar})}>
                    <aside className={classnames(styles.sider, {[styles.light]: !sysInfo.darkTheme})}>
                        <Sider {...siderProps} />
                    </aside>
                    <div className={styles.main}>
                        <Header {...headerProps} />
                        <Bread {...breadProps}/>
                        <div className={styles.container}>
                            <div className={styles.content}>
                                <div className="content-inner">
                                    {this.props.children}
                                </div>
                            </div>
                        </div>
                        <Footer />
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(App);