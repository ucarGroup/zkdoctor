import {Icon, Menu} from "antd";
import styles from "./main.less";

function Header({baseInfoModel, user, logout, switchSider, siderFold,changeMessage,massageNewCount}) {
    let handleClickMenu = e => e.key === 'logout' && logout();
    return (<div className={styles.header}>
            <div className={styles.siderbutton} key="switchSider" onClick={switchSider}>
                <Icon style={{color: 'rgb(101,119,141)'}} type={siderFold ? 'menu-unfold' : 'menu-fold'}/>
            </div>
            <Menu className='header-menu' mode='horizontal' onClick={handleClickMenu}>
                <Menu.SubMenu title={< span style={{color: '#65778d'}}> <Icon type='user'/> {baseInfoModel.getUserRoleName(user.userRole)}：{user? user.chName:'请登录'} </span>}>
                    <Menu.Item key='logout'>
                        <span style={{width: 150}}><Icon type="logout" /><a>注销</a></span>
                    </Menu.Item>
                </Menu.SubMenu>
            </Menu>
        </div>)
}

export default Header;
