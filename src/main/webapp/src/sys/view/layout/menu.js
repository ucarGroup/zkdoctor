import React from "react";
import {Icon, Menu} from "antd";
import {Link} from "react-router";
// import {menu} from '../../../utils'
// const authorityKeys = [
//     ['dashboard', 'codes', 'roles', 'users', 'projects'],
//     ['dashboard', 'codes', 'roles', 'users'],
//     ['dashboard']
// ];
// const topMenus = menu.map(item => item.key);
/*let pathSet = {};
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

 getPathSet(menu);*/

/*function getPathArray(nodeName) {
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
 }*/

const arrayContains = function(array,needle) {
    for (let i in array) {
        if (array[i] == needle) return true;
    }
    return false;
}

const getMenus = function (topMenuKeys, menuArray, siderFold) {
    return menuArray.map(item => {
        if (item.children && item.children.length > 0) {
            return (
                <Menu.SubMenu key={item.key} title={<span>{item.extra ? <Icon
                    type={item.extra}/> : ''}{siderFold && arrayContains(topMenuKeys,item.key) > 0 ? '' : item.name}</span>}>
                    {getMenus(topMenuKeys,item.children, siderFold)}
                </Menu.SubMenu>
            )
        } else {
            return (
                <Menu.Item key={item.key} path={item.url}>
                    <Link to={item.url}>
                        {item.extra && <Icon type={item.extra} />}
                        {siderFold && arrayContains(topMenuKeys,item.key) > 0 ? '' : item.name}
                    </Link>
                    <span>
                    {/*<Link to={item.url}>
                        {item.extra ? <Icon type={item.extra}/> : ''}
                        {siderFold && arrayContains(topMenuKeys,item.key) > 0 ? '' : item.name}
                    </Link>*/}
                    </span>
                </Menu.Item>
            )
        }
    })
};


function getPathArray(menuArray, field, value) {
    for (let item of menuArray) {
        if (field == 'url' && item.url && item.url == value) {
            return [item];
        }
        if (field == 'name' && item.name == value) {
            return [item];
        }
        if (item.children && item.children.length > 0) {
            let subResult = getPathArray(item.children, field, value);
            if (subResult && subResult.length > 0) {
                return [item].concat(subResult);
            }
        }
    }
    return [];
}
function Menus({menu, siderFold, darkTheme, location, isNavbar, handleClickNavMenu}) {
    let topMenuKeys = menu.map(item => item.key);
    let menuItems = getMenus(topMenuKeys, menu, siderFold);

    let pathNames = getPathArray(menu, 'url', location.pathname);

    if (pathNames == null || pathNames.length == 0) {
        pathNames = getPathArray('name', 'Dashboard');
    }
    let selectedNode = pathNames.map(item => item.key);
    return (
        <Menu
            mode={siderFold ? 'vertical' : 'inline'}
            theme={darkTheme ? 'dark' : 'light'}
            defaultOpenKeys={isNavbar ? menuItems.map(item => item.key) : []}
            defaultSelectedKeys={selectedNode}>
            {menuItems}
        </Menu>
    )
}

export default Menus
