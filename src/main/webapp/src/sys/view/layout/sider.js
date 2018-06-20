import React from "react";
import styles from "./main.less";
import Menus from "./menu";
import config from "../../../common/config.js";

function Sider({user, menu, siderFold, darkTheme, handleClickNavMenu, location}) {
    const menusProps = {
        user,
        menu,
        siderFold,
        darkTheme,
        location,
        handleClickNavMenu
    };
    return (
        <div>
            <div className={styles.logo}>
                <img src={`${config.assertPrefix}${config.logoSrc}`}/>
            </div>
            <Menus {...menusProps} />
        </div>
    )
}

export default Sider
