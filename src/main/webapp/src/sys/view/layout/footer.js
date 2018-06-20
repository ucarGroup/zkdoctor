import React from "react";
import styles from "./main.less";
import config from "../../../common/config";

const Footer = () => <div className={styles.footer}>
    {config.footerText}
</div>;

export default Footer
