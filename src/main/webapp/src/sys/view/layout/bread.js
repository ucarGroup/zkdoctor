import React from "react";
import PropTypes from "prop-types";
import {Breadcrumb, Icon} from "antd";
import styles from "./main.less";
// import {menu} from '../../../utils'

// let pathSet = {};
/*
const getPathSet = function (menuArray, parentPath) {
    parentPath = parentPath || '/';

    menuArray.map(item => {
        let name = item.url ? item.url : item.name;
        pathSet[name] = {
            parent: parentPath,
            path: item.url || '',
            icon: item.icon || '',
            name: item.name
        };
        if (item.child) {
            getPathSet(item.child, name)
        }
    })
};
*/

// getPathSet(menu);

/*
function getPathArray(nodeName) {
    if (nodeName in pathSet) {
        let node = pathSet[nodeName];
        if (node.parent != '/') {
            let parentNode = getPathArray(node.parent);
            if (parentNode != null) {
                return parentNode.concat(node);
            }
        }
        return [node];
    }
    return null;
}
*/

function getPathArray(menuArray,field,value) {
    for (let item of menuArray) {
        if(field == 'url' && item.url && item.url == value){
            return [item];
        }
        if(field == 'name' && item.name == value){
            return [item];
        }
        if(item.children && item.children.length > 0){
            let subResult=getPathArray(item.children,field,value);
            if(subResult && subResult.length > 0){
                return [item].concat(subResult);
            }
        }
    }
    return [];
}

function Bread({location,menu}) {

    let pathNames = getPathArray(menu,'url',location.pathname);

    if(pathNames == null || pathNames.length ==0){
        pathNames = getPathArray(menu,'name', 'Dashbord');
    }

    const breads = pathNames.map((item,index) => {
        // if (!(item in pathSet)) {
        //     item = 'Dashboard'
        // }
        return (
            <Breadcrumb.Item key={item.key} {...((pathNames.length - 1 === index) || !item.url) ? '' : {href:'#'+ item.path}}>
                {item.extra ? <Icon type={item.extra}/> : ''}
                <span>{item.name}</span>
            </Breadcrumb.Item>
        )
    });

    return (
        <div className={styles.bread}>
            <Breadcrumb>
                <Breadcrumb.Item href='#/'><Icon type='home'/>
                    <span>主页</span>
                </Breadcrumb.Item>
                {breads}
            </Breadcrumb>
        </div>
    )
}

Bread.propTypes = {
    location: PropTypes.object
};

export default Bread
